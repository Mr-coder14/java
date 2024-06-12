package com.example.java;

import static java.security.AccessController.getContext;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.ColorFilter;
import android.graphics.LightingColorFilter;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.airbnb.lottie.LottieProperty;
import com.airbnb.lottie.model.KeyPath;
import com.airbnb.lottie.value.LottieValueCallback;
import com.github.barteksc.pdfviewer.PDFView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.StorageReference;

public class OrderdDetailsuser extends AppCompatActivity {

    private static final int PERMISSION_REQUEST_CODE = 100;

    private static final String TAG = "OrderDetailsUser";
    private TextView fileNameTextViewauser, pguser, amt1user, finalamtuser, qtynouser, qtytxt1user, perpageamtuser, deliveryamtuser, colortxtuser, formatuser, ratiouser, sheetuser;
    private String orderiduser;
    private LottieAnimationView lottieAnimationView;
    private ImageButton backbtn;
    private StorageReference storageRef;
    private DatabaseReference databaseReference;
    private ProgressDialog progressDialog;
    private Button preview;
    private PDFView pdfView;
    private ScrollView scrollView;
    private ProgressBar progressBar;
    private String filename;
    private Uri pdfuri;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orderd_detailsuser);

        fileNameTextViewauser = findViewById(R.id.filenametxt1user);
        backbtn = findViewById(R.id.backuser);
        qtytxt1user = findViewById(R.id.qtytxt1user);
        qtynouser = findViewById(R.id.qtynouser);
        perpageamtuser = findViewById(R.id.perpageuser);
        deliveryamtuser = findViewById(R.id.deliveryamtuser);
        colortxtuser = findViewById(R.id.colorfontuser);
        pguser = findViewById(R.id.pagenouser);
        amt1user = findViewById(R.id.amt1user);
        finalamtuser = findViewById(R.id.finalamtuser);
        pdfView = findViewById(R.id.pdfViewuser);
        formatuser = findViewById(R.id.formatuser);
        lottieAnimationView=findViewById(R.id.lottie);
        ratiouser = findViewById(R.id.ratiouser);
        sheetuser = findViewById(R.id.sheetuser);

        orderiduser=getIntent().getStringExtra("Orderid");

        databaseReference = FirebaseDatabase.getInstance().getReference().child("pdfs").child(orderiduser);

        lottieAnimationView.setAnimation(R.raw.lineloading);
        lottieAnimationView.setSpeed(4.0f);
        lottieAnimationView.playAnimation();
        int color = getResources().getColor(R.color.green);
        ColorFilter colorFilter = new LightingColorFilter(color, color);


        lottieAnimationView.addValueCallback(
                new KeyPath("**"),
                LottieProperty.COLOR_FILTER,
                new LottieValueCallback<ColorFilter>(colorFilter)
        );
        lottieAnimationView.loop(true);
        lottieAnimationView.setScaleType(LottieAnimationView.ScaleType.CENTER_INSIDE);

        lottieAnimationView.setScaleX(1.0f); // No horizontal flip
        lottieAnimationView.setScaleY(-1.0f);

        pdfuri=getIntent().getData();
        preview=findViewById(R.id.previewuser);

        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                colse();
            }
        });

        preview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pdfuri != null) {
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setDataAndType(pdfuri, "application/pdf");
                    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    startActivity(intent);
                }
                else {

                    if (getContext() != null) {
                        Toast.makeText(OrderdDetailsuser.this, "PDF URI is not valid", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        databaseReference.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.isSuccessful()) {
                    DataSnapshot dataSnapshot = task.getResult();
                    if (dataSnapshot.exists()) {
                        Fileinmodel fileinmodel = dataSnapshot.getValue(Fileinmodel.class);
                        fileNameTextViewauser.setText(fileinmodel.getName());
                        filename = fileNameTextViewauser.getText().toString();
                        orderiduser = fileinmodel.getOrderid();
                        pguser.setText(fileinmodel.getPages());
                        qtynouser.setText(fileinmodel.getQty());
                        qtytxt1user.setText(fileinmodel.getQty());
                        colortxtuser.setText(fileinmodel.getColor());
                        finalamtuser.setText("₹ "+fileinmodel.getAmt());
                        formatuser.setText(fileinmodel.getFormat());
                        sheetuser.setText(fileinmodel.getSheet());
                        ratiouser.setText(fileinmodel.getRatio());
                    } else {

                    }
                } else {
                    Log.e(TAG, "Error getting data", task.getException());
                }
            }
        });
    }

    private void colse() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Exit");
        builder.setMessage("Are you sure you want to quit?");
        builder.setCancelable(false);

        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });

        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();
            }
        });


        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}