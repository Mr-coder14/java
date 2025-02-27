package com.RapCode.java;

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
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextUtils;
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
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;

public class editdetails extends AppCompatActivity {
    private static final int PICK_IMAGE_REQUEST = 1;


    private EditText editTextName, editTextPhno;
    private CircleImageView circleImageView;
    private CircleImageView profileimage;
    private Button buttonSave;
    private Uri imageUri;
    private FirebaseAuth auth;
    private  boolean ok=false;
    private StorageReference storageRef;
    private DatabaseReference usersRef;
    private FirebaseUser currentUser;
    private ConstraintLayout constraintLayout;
    private ProgressBar progressBar;
    private String currentPhotoPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_detailsactivity);
        editTextName = findViewById(R.id.editdetailsname);
        profileimage = findViewById(R.id.profileImageView);
        editTextPhno = findViewById(R.id.editdetailsphno);
        editTextPhno.setFilters(new InputFilter[]{phoneNumberFilter()});
        buttonSave = findViewById(R.id.savechanges);
        progressBar = findViewById(R.id.progressedit);
        constraintLayout = findViewById(R.id.constraintedit);
        circleImageView = findViewById(R.id.profileImageView);

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





    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(null);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
        return image;
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

        final ProgressDialog progressDialog = new ProgressDialog(editdetails.this);
        progressDialog.setTitle("Updating Profile");
        progressDialog.setMessage("Please wait...");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setCancelable(false);
        progressDialog.show();

        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setDisplayName(name)
                .build();

        currentUser.updateProfile(profileUpdates).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    usersRef.child("name").setValue(name);
                    usersRef.child("phno").setValue(phno).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                ok = true;
                                if (imageUri != null) {
                                    uploadImageToFirebase();
                                } else {
                                    progressDialog.dismiss();
                                    checkAndCloseActivity();
                                }
                            } else {
                                progressDialog.dismiss();
                                Toast.makeText(editdetails.this, "Failed to update profile", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                } else {
                    progressDialog.dismiss();
                    Toast.makeText(editdetails.this, "Failed to update profile", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    private void checkAndCloseActivity() {
        if (ok) {
            Toast.makeText(editdetails.this, "Profile updated successfully", Toast.LENGTH_SHORT).show();
            finish(); // This will close the activity
        }
    }

    private void uploadImageToFirebase() {
        final ProgressDialog progressDialog = new ProgressDialog(editdetails.this);
        progressDialog.setTitle("Uploading");
        progressDialog.setMessage("Please wait...");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setCancelable(false);
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
                checkAndCloseActivity(); // Add this line
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
                            ok = true;
                            Toast.makeText(editdetails.this, "Image uploaded successfully", Toast.LENGTH_SHORT).show();
                            checkAndCloseActivity(); // Replace 'finish()' with this line
                        } else {
                            checkAndCloseActivity(); // Add this line
                        }
                    }
                });
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

    private InputFilter phoneNumberFilter() {
        return new InputFilter() {
            @Override
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                StringBuilder builder = new StringBuilder(dest);
                builder.replace(dstart, dend, source.subSequence(start, end).toString());
                if (!builder.toString().matches("^\\d{0,10}$")) {
                    if (source.length() == 0) {
                        return dest.subSequence(dstart, dend);
                    }
                    return "";
                }
                return null;
            }
        };
    }
}
