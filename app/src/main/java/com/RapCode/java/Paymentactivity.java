package com.RapCode.java;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class Paymentactivity extends AppCompatActivity {
    private ImageView qrCodeImageView,uplaodscrrenshotimageview;
    private TextView textView,address,notes;
    private RadioGroup radioGroup;
    private Uri imageUri;
    private Button btn;
    private ImageButton btnw;
    private Orderconfirmuseractivity orderconfirmuseractivity;
    private boolean isFullPayment = false;
    private float balanceAmount = 0.0f;
    private ProgressDialog progressDialog;
    private float totalAmount = 0.0f;
    private String paymentScreenshotUrl;
    private DatabaseReference usersRef,uploadscreenshotref,databaseReference;
    private Button qrDownloadBtn, uploadScreenshotBtn;
    private StorageReference storageRef;
    private String orderid;
    private RadioButton fullPaymentRadio, offPaymentRadio,Adress;
    private static final int REQUEST_WRITE_PERMISSION = 100;
    private final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paymentactivity);
        radioGroup = findViewById(R.id.radioGroup);
        address=findViewById(R.id.address);
        fullPaymentRadio = findViewById(R.id.radioButton1full);
        offPaymentRadio = findViewById(R.id.radioButton2off);
        notes=findViewById(R.id.noteupalod);
        orderid=getIntent().getStringExtra("orderid");
        databaseReference = FirebaseDatabase.getInstance().getReference().child("pdfs").child(orderid);

        textView=findViewById(R.id.grandamty);
        Adress=findViewById(R.id.ratiobtnaddress);
        btnw=findViewById(R.id.backbtnpayment);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Uploading, please wait...");
        progressDialog.setCancelable(false);
        uplaodscrrenshotimageview=findViewById(R.id.uploadedimgaes);
        fullPaymentRadio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPaymentConfirmationDialog();
            }
        });
        offPaymentRadio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPaymentConfirmationDialog();
            }
        });

        Float t=getIntent().getExtras().getFloat("tt");
        totalAmount=t;
        orderconfirmuseractivity=new Orderconfirmuseractivity();
        btn=findViewById(R.id.orderbtnuser124);
        textView.setText("₹ "+String.valueOf(t));
        btnw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(Paymentactivity.this);
                builder.setTitle("Are you sure?");
                builder.setMessage("Do you want to go back to the main screen?");

                // Set up "Yes" button
                builder.setPositiveButton("Yes", (dialog, which) -> {

                    Intent intent = new Intent(Paymentactivity.this, UsermainActivity.class);
                    startActivity(intent);
                    finish();
                });

                // Set up "No" button
                builder.setNegativeButton("No", (dialog, which) -> dialog.dismiss());

                // Show the dialog
                builder.create().show();

            }
        });
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateInputs()) {
                    uploadScreenshotAndSaveOrder();

                }

            }
        });

        qrCodeImageView = findViewById(R.id.qrCodeImageView);
        qrDownloadBtn = findViewById(R.id.qrdownlaodbtn);
        uploadScreenshotBtn = findViewById(R.id.uploadscreenshotbtn);
        storageRef = FirebaseStorage.getInstance().getReference();


        qrCodeImageView.setImageResource(R.drawable.qrcodesalem);
        address.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                updateAddressInDatabase(String.valueOf(s));

            }
        });
        usersRef = FirebaseDatabase.getInstance().getReference("users").child(user.getUid());
        uploadscreenshotref= FirebaseDatabase.getInstance().getReference().child("uploadscreenshots");
        usersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    User userData = dataSnapshot.getValue(User.class);
                    if(userData.getAddress()!=null){
                        address.setText(userData.getAddress());
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        qrDownloadBtn.setOnClickListener(v -> requestWritePermission());


        uploadScreenshotBtn.setOnClickListener(view -> showScreenshotUploadAlert());
    }

    private void downloadImage() {
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.qrcodesalem);
        String fileName = "qrcodesalem.jpg";

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {

            ContentValues values = new ContentValues();
            values.put(MediaStore.Images.Media.DISPLAY_NAME, fileName);
            values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
            values.put(MediaStore.Images.Media.RELATIVE_PATH, Environment.DIRECTORY_PICTURES + "/YourAppName");

            Uri uri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

            try (FileOutputStream out = (FileOutputStream) getContentResolver().openOutputStream(uri)) {
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
                Toast.makeText(this, "Image saved to Pictures/YourAppName", Toast.LENGTH_LONG).show();
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(this, "Error saving image: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }

        } else {

            File directory = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "YourAppName");
            if (!directory.exists()) {
                directory.mkdirs();
            }

            File file = new File(directory, fileName);

            try (FileOutputStream out = new FileOutputStream(file)) {
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
                Toast.makeText(this, "Image saved to Pictures/YourAppName", Toast.LENGTH_LONG).show();
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(this, "Error saving image: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }



    private final ActivityResultLauncher<Intent> pickImageLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    imageUri = result.getData().getData();
                    uplaodscrrenshotimageview.setImageURI(imageUri);

                }
            }
    );

    private void openImagePicker() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        pickImageLauncher.launch(intent);
    }



    private void requestWritePermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M &&
                ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_WRITE_PERMISSION);
        } else {
            downloadImage();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_WRITE_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                downloadImage();
            } else {
                Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }
    private void uploadScreenshotAndSaveOrder() {
        if (imageUri != null && user != null) {
            // Show the progress dialog
            progressDialog.show();

            StorageReference userImageRef = storageRef
                    .child("paymentscreenshots")
                    .child(orderid)
                    .child("payment_screenshot.jpg");

            userImageRef.putFile(imageUri)
                    .addOnSuccessListener(taskSnapshot -> {
                        userImageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                            paymentScreenshotUrl = uri.toString();
                            saveOrderDetails();
                        }).addOnFailureListener(e -> {
                            progressDialog.dismiss();
                            Toast.makeText(Paymentactivity.this, "Error retrieving screenshot URL: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        });
                    })
                    .addOnFailureListener(e -> {
                        progressDialog.dismiss();
                        Toast.makeText(Paymentactivity.this, "Failed to upload screenshot: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    });
        } else {
            Toast.makeText(this, "Please select an image before uploading", Toast.LENGTH_SHORT).show();
        }
    }


    private void saveOrderDetails() {
        try {
            isFullPayment = fullPaymentRadio.isChecked();

            if (totalAmount <= 0) {
                Toast.makeText(this, "Invalid total amount", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
                return;
            }

            float paidAmount = isFullPayment ? totalAmount : totalAmount / 2;
            balanceAmount = isFullPayment ? 0 : totalAmount / 2;

            OrderScreenshotDetails orderDetails = new OrderScreenshotDetails(
                    orderid,
                    user.getUid(),
                    address.getText().toString(),
                    isFullPayment,
                    totalAmount,
                    paidAmount,
                    balanceAmount,
                    paymentScreenshotUrl,
                    System.currentTimeMillis()
            );

            uploadscreenshotref.child(orderid).setValue(orderDetails)
                    .addOnSuccessListener(aVoid -> {
                        updatePaidStatusForAllFiles();
                        Toast.makeText(Paymentactivity.this, "Order placed successfully", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                        startActivity(new Intent(Paymentactivity.this,suceesanimation.class));
                        finish();
                    })
                    .addOnFailureListener(e -> {
                        progressDialog.dismiss();
                        Toast.makeText(Paymentactivity.this, "Failed to save order details: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    });
        } catch (Exception e) {
            progressDialog.dismiss();
            e.printStackTrace();
            Toast.makeText(this, "Error in saving order details: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void updatePaidStatusForAllFiles() {
        DatabaseReference filesRef = FirebaseDatabase.getInstance().getReference().child("pdfs").child(user.getUid()).child(orderid);

        filesRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot fileSnapshot : dataSnapshot.getChildren()) {
                        fileSnapshot.getRef().child("paid").setValue(true)
                                .addOnSuccessListener(aVoid -> {
                                    // Success message for each file (optional logging)
                                })
                                .addOnFailureListener(e -> {
                                    // Failure message for each file
                                });
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(Paymentactivity.this, "Failed to update payment status", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private AlertDialog showLoadingDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        return builder.create();
    }
    private boolean validateInputs() {

        if (address.getText().toString().isEmpty()) {
            Toast.makeText(this, "Address cannot be empty", Toast.LENGTH_SHORT).show();
            address.setError("Address cannot be empty");
            address.requestFocus();
            return false;
        }

        if (!Adress.isChecked()) {
            Toast.makeText(this, "Please select the address", Toast.LENGTH_SHORT).show();
            Adress.setError("Please select the address");
            Adress.requestFocus();
            return false;
        }


        int selectedPaymentOptionId = radioGroup.getCheckedRadioButtonId();
        if (selectedPaymentOptionId == -1) {
            Toast.makeText(this, "Please select a payment option", Toast.LENGTH_SHORT).show();
            return false;
        }


        if (imageUri == null) {
            Toast.makeText(this, "Please upload a screenshot of the payment", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }


    private void updateAddressInDatabase(String newAddress) {
        if (newAddress.isEmpty()) {
            address.setError("Address cannot be empty");
            address.requestFocus();
            return;
        }


        usersRef.child("address").setValue(newAddress).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {

            } else {

            }
        });
    }
    private void showPaymentConfirmationDialog() {
        Float t = getIntent().getExtras().getFloat("tt");
        String paymentAmount;
        Float balancemaount=0.0f;

        if (fullPaymentRadio.isChecked()) {
            paymentAmount = String.format("₹ %.2f", t);
            textView.setText(String.format("₹ %.2f", t));
        } else {
            paymentAmount = String.format("₹ %.2f", t / 2);
            textView.setText(String.format("₹ %.2f", t/2));
            balancemaount=t/2;
        }

        new AlertDialog.Builder(this)
                .setTitle("Confirm Payment")
                .setMessage("You have selected the " + (fullPaymentRadio.isChecked() ? "Full Payment option.\nYou need to pay " + paymentAmount : "50% Payment option.\nYou need to pay " + paymentAmount + " Now.\nThe balance amount of ₹ " + String.format("%.2f",balancemaount) + " will be paid upon delivery."))
                .setPositiveButton("Confirm", (dialog, which) -> {

                })
                .setNegativeButton("Cancel", null)
                .show();
    }
    private void showScreenshotUploadAlert() {
        new AlertDialog.Builder(this)
                .setTitle("Upload Screenshot")
                .setMessage("Please upload a screenshot of the paid amount to continue. The screenshot should match the payment amount mentioned in the confirmation dialog.")
                .setPositiveButton("Upload", (dialog, which) -> {
                    openImagePicker();
                })
                .setNegativeButton("Cancel", null)
                .setIcon(R.drawable.baseline_info_24)
                .show();
    }
    @Override
    public void onBackPressed() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Are you sure?");
        builder.setMessage("Do you want to go back(The Order Will be Cancelled)?");

        // Set up "Yes" button
        builder.setPositiveButton("Yes", (dialog, which) -> {

            Intent intent = new Intent(Paymentactivity.this, UsermainActivity.class);
            startActivity(intent);
            finish();
        });

        // Set up "No" button
        builder.setNegativeButton("No", (dialog, which) -> dialog.dismiss());

        // Show the dialog
        builder.create().show();
    }
    public static class OrderScreenshotDetails {
        private String orderId;
        private String userId;
        private String address;
        private boolean isFullPayment;
        private float totalAmount;
        private float paidAmount;
        private float balanceAmount;
        private String screenshotUrl;
        private long timestamp;

        public OrderScreenshotDetails() {
            // Required empty constructor for Firebase
        }

        public OrderScreenshotDetails(String orderId, String userId, String address,
                                      boolean isFullPayment, float totalAmount,
                                      float paidAmount, float balanceAmount,
                                      String screenshotUrl, long timestamp) {
            this.orderId = orderId;
            this.userId = userId;
            this.address = address;
            this.isFullPayment = isFullPayment;
            this.totalAmount = totalAmount;
            this.paidAmount = paidAmount;
            this.balanceAmount = balanceAmount;
            this.screenshotUrl = screenshotUrl;
            this.timestamp = timestamp;
        }

        // Getters and setters
        public String getOrderId() { return orderId; }
        public void setOrderId(String orderId) { this.orderId = orderId; }
        public String getUserId() { return userId; }
        public void setUserId(String userId) { this.userId = userId; }
        public String getAddress() { return address; }
        public void setAddress(String address) { this.address = address; }
        public boolean isFullPayment() { return isFullPayment; }
        public void setFullPayment(boolean fullPayment) { isFullPayment = fullPayment; }
        public float getTotalAmount() { return totalAmount; }
        public void setTotalAmount(float totalAmount) { this.totalAmount = totalAmount; }
        public float getPaidAmount() { return paidAmount; }
        public void setPaidAmount(float paidAmount) { this.paidAmount = paidAmount; }
        public float getBalanceAmount() { return balanceAmount; }
        public void setBalanceAmount(float balanceAmount) { this.balanceAmount = balanceAmount; }
        public String getScreenshotUrl() { return screenshotUrl; }
        public void setScreenshotUrl(String screenshotUrl) { this.screenshotUrl = screenshotUrl; }
        public long getTimestamp() { return timestamp; }
        public void setTimestamp(long timestamp) { this.timestamp = timestamp; }
    }
}
