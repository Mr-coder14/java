package com.example.java;

import static android.content.ContentValues.TAG;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import de.hdodenhof.circleimageview.CircleImageView;

public class editdetails extends AppCompatActivity {
    private static final int PICK_IMAGE_REQUEST = 1;
    private EditText editTextName, editTextPhno;
    private CircleImageView circleImageView;
    private CircleImageView profileimage;
    private Button buttonSave;
    private Uri imageUri;
    private FirebaseAuth auth;
    private StorageReference storageRef;
    private DatabaseReference usersRef;
    private FirebaseUser currentUser;
    private ConstraintLayout constraintLayout;
    private ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_detailsactivity);
        editTextName=findViewById(R.id.editdetailsname);
        profileimage=findViewById(R.id.profileImageView);
        editTextPhno=findViewById(R.id.editdetailsphno);
        buttonSave=findViewById(R.id.savechanges);
        progressBar=findViewById(R.id.progressedit);
        constraintLayout=findViewById(R.id.constraintedit);
        circleImageView=findViewById(R.id.profileImageView);

        progressBar.setVisibility(View.VISIBLE);
        constraintLayout.setVisibility(View.GONE);


        auth = FirebaseAuth.getInstance();
        currentUser = auth.getCurrentUser();
        usersRef = FirebaseDatabase.getInstance().getReference("users").child(currentUser.getUid());
        storageRef = FirebaseStorage.getInstance().getReference();

        profileimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFileChooser();

            }
        });

        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveProfile();
            }
        });

        LOADdata();

        progressBar.setVisibility(View.GONE);
        constraintLayout.setVisibility(View.VISIBLE);
    }

    private void LOADdata() {
        usersRef.child("name").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if(task.isSuccessful()){
                    String name=task.getResult().getValue(String.class);
                    editTextName.setText(name);
                }
            }
        });
        usersRef.child("profileImageUrl").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.isSuccessful()){
                    String uri=task.getResult().getValue(String.class);
                    if(uri!=null){
                        Glide.with(editdetails.this)
                                .load(uri)
                                .into(circleImageView);
                    }else {
                        Glide.with(editdetails.this)
                                .load(R.drawable.person3)
                                .into(circleImageView);
                    }
                }
            }
        });
        usersRef.child("phno").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.isSuccessful()) {
                    String phno = task.getResult().getValue(String.class);
                    editTextPhno.setText(phno);
                }
            }
        });
    }

    private void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                profileimage.setImageBitmap(bitmap);
                // Convert to circular image
                Bitmap circularBitmap = getCircularBitmap(bitmap);
                profileimage.setImageBitmap(circularBitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void saveProfile() {
        String name = editTextName.getText().toString().trim();
        String phno = editTextPhno.getText().toString().trim();

        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(phno)) {
            Toast.makeText(this, "All fields are required", Toast.LENGTH_SHORT).show();
            return;
        }

        if (imageUri != null) {
            uploadImageToFirebase();
        }


        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setDisplayName(name)
                .build();




        usersRef.child("name").setValue(name);
        usersRef.child("phno").setValue(phno);
        Toast.makeText(editdetails.this, "Profile updated successfully", Toast.LENGTH_SHORT).show();
        finish();
    }

    private void uploadImageToFirebase() {
        final ProgressDialog progressDialog = new ProgressDialog(editdetails.this);
        progressDialog.setTitle("Uploading");
        progressDialog.setMessage("Please wait...");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        StorageReference fileReference = storageRef.child("profileimages/" + currentUser.getUid());

        profileimage.setDrawingCacheEnabled(true);
        profileimage.buildDrawingCache();
        Bitmap bitmap = ((BitmapDrawable) profileimage.getDrawable()).getBitmap();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();

        UploadTask uploadTask = fileReference.putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                progressDialog.dismiss();
                Toast.makeText(editdetails.this, "Failed to upload image", Toast.LENGTH_SHORT).show();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                fileReference.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        progressDialog.dismiss();
                        if (task.isSuccessful()) {
                            Uri downloadUri = task.getResult();
                            usersRef.child("profileImageUrl").setValue(downloadUri.toString());
                            Toast.makeText(editdetails.this, "Image uploaded successfully", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                double progress = (100.0 * snapshot.getBytesTransferred() / snapshot.getTotalByteCount());
                progressDialog.setMessage("Uploaded " + (int) progress + "%");
            }
        });
    }
    private Bitmap getCircularBitmap(Bitmap bitmap) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawOval(rectF, paint);

        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);

        bitmap.recycle();

        return output;
    }


}
