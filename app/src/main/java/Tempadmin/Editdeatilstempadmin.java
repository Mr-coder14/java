package Tempadmin;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
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
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import com.bumptech.glide.Glide;
import com.example.java.R;
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

public class Editdeatilstempadmin extends AppCompatActivity {
    private static final int PICK_IMAGE_REQUEST = 1;
    private static final int CAMERA_REQUEST = 2;
    private static final int REQUEST_CAMERA_PERMISSION = 3;

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
    private String userid;
    private String currentPhotoPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editdeatilstempadmin);
        editTextName = findViewById(R.id.editdetailsnametad);
        profileimage = findViewById(R.id.profileImageViewtad);
        editTextPhno = findViewById(R.id.editdetailsphnotad);
        editTextPhno.setFilters(new InputFilter[]{phoneNumberFilter()});
        buttonSave = findViewById(R.id.savechangestad);
        progressBar = findViewById(R.id.progressedittad);
        constraintLayout = findViewById(R.id.constraintedittad);
        circleImageView = findViewById(R.id.profileImageViewtad);

        progressBar.setVisibility(View.VISIBLE);
        constraintLayout.setVisibility(View.GONE);

        auth = FirebaseAuth.getInstance();
        currentUser = auth.getCurrentUser();
        userid=currentUser.getUid();
        usersRef = FirebaseDatabase.getInstance().getReference("tempadmin1").child(currentUser.getUid());
        storageRef = FirebaseStorage.getInstance().getReference();

        profileimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showImageOptions();
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
                        Glide.with(Editdeatilstempadmin.this)
                                .load(uri)
                                .into(circleImageView);
                    }else {
                        Glide.with(Editdeatilstempadmin.this)
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
    private void showImageOptions() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Choose an option");
        builder.setItems(new String[]{"Camera", "Gallery"}, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                        if (ContextCompat.checkSelfPermission(Editdeatilstempadmin.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                            ActivityCompat.requestPermissions(Editdeatilstempadmin.this, new String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSION);
                        } else {
                            openCamera();
                        }
                        break;
                    case 1:

                        openFileChooser();
                        break;
                }
            }
        });
        builder.show();
    }

    private void openCamera() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {

                ex.printStackTrace();
            }
            if (photoFile != null) {
                imageUri = FileProvider.getUriForFile(this, "com.example.java.fileprovider", photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                startActivityForResult(takePictureIntent, CAMERA_REQUEST);
            }
        }
    }

    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(null);
        File image = File.createTempFile(
                imageFileName,
                ".jpg",
                storageDir
        );
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
                Bitmap circularBitmap = getCircularBitmap(bitmap);
                profileimage.setImageBitmap(circularBitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                profileimage.setImageBitmap(bitmap);
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

        final ProgressDialog progressDialog = new ProgressDialog(Editdeatilstempadmin.this);
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
                                Toast.makeText(Editdeatilstempadmin.this, "Failed to update profile", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                } else {
                    progressDialog.dismiss();
                    Toast.makeText(Editdeatilstempadmin.this, "Failed to update profile", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    private void checkAndCloseActivity() {
        if (ok) {
            Toast.makeText(Editdeatilstempadmin.this, "Profile updated successfully", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void uploadImageToFirebase() {
        final ProgressDialog progressDialog = new ProgressDialog(Editdeatilstempadmin.this);
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
                Toast.makeText(Editdeatilstempadmin.this, "Failed to upload image", Toast.LENGTH_SHORT).show();
                checkAndCloseActivity();
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
                            Toast.makeText(Editdeatilstempadmin.this, "Image uploaded successfully", Toast.LENGTH_SHORT).show();
                            checkAndCloseActivity();
                        } else {
                            checkAndCloseActivity();
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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CAMERA_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openCamera();
            } else {
                Toast.makeText(this, "Camera permission is required to take a photo", Toast.LENGTH_SHORT).show();
            }
        }
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