package com.example.java;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;

import android.net.Uri;

import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.barteksc.pdfviewer.PDFView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;



public class OrdrerdDetailsadminactivity extends AppCompatActivity {
    private static final int PERMISSION_REQUEST_CODE = 100;
    private BroadcastReceiver downloadReceiver;

    private static final String TAG = "OrderDetailsAdmin";
    private TextView fileNameTextViewadmin, pgadmin, amt1admin, finalamtadmin, qtynoadmin, qtytxt1admin, perpageamtadmin, deliveryamtadmin, colortxtadmin, formatadmin, ratioadmin, sheetadmin;
    private String orderidadmin;
    private ImageButton backbtn;
    private StorageReference storageRef;
    private DatabaseReference databaseReference;
    private ProgressDialog progressDialog;
    private Button preview, downloadbtn;
    private PDFView pdfView;
    private ScrollView scrollView;
    private ProgressBar progressBar;
    private String filename;
    private Uri pdfuri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ordrerd_detailsadminactivity);

        fileNameTextViewadmin = findViewById(R.id.filenametxt1admin1);
        backbtn = findViewById(R.id.backadmin1);
        qtytxt1admin = findViewById(R.id.qtytxt1admin1);
        qtynoadmin = findViewById(R.id.qtynoadmin1);
        progressBar = findViewById(R.id.progressadmin11);
        downloadbtn = findViewById(R.id.downloadbtnadmin);
        scrollView = findViewById(R.id.scrolladmin);
        perpageamtadmin = findViewById(R.id.perpageamtadmin1);
        deliveryamtadmin = findViewById(R.id.deliveryamt1admin1);
        colortxtadmin = findViewById(R.id.colorfontadmin1);
        pgadmin = findViewById(R.id.pagenoadmin1);
        amt1admin = findViewById(R.id.amt1admin1);
        finalamtadmin = findViewById(R.id.finalamtadmin1);
        preview = findViewById(R.id.preview1);
        pdfView = findViewById(R.id.pdfViewadmin1);
        formatadmin = findViewById(R.id.formatadmin1);
        ratioadmin = findViewById(R.id.ratioadmin1);
        sheetadmin = findViewById(R.id.sheetadmin1);
        orderidadmin=getIntent().getStringExtra("orderid");



        progressBar.setVisibility(View.VISIBLE);
        scrollView.setVisibility(View.GONE);

        databaseReference = FirebaseDatabase.getInstance().getReference().child("pdfs").child(orderidadmin);
        pdfuri = getIntent().getData();



        if (pdfuri == null) {
            Toast.makeText(this, "No PDF URI provided", Toast.LENGTH_SHORT).show();
        } else {
            Log.d(TAG, "PDF URI: " + pdfuri.toString());
        }

        databaseReference.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.isSuccessful()) {
                    DataSnapshot dataSnapshot = task.getResult();
                    if (dataSnapshot.exists()) {
                        Fileinmodel fileinmodel = dataSnapshot.getValue(Fileinmodel.class);
                        fileNameTextViewadmin.setText(fileinmodel.getName());
                        filename = fileNameTextViewadmin.getText().toString();
                        orderidadmin = fileinmodel.getOrderid();
                        pgadmin.setText(fileinmodel.getPages());
                        qtynoadmin.setText(fileinmodel.getQty());
                        qtytxt1admin.setText(fileinmodel.getQty());
                        colortxtadmin.setText(fileinmodel.getColor());
                        finalamtadmin.setText("₹ "+fileinmodel.getAmt());
                        formatadmin.setText(fileinmodel.getFormat());
                        sheetadmin.setText(fileinmodel.getSheet());
                        ratioadmin.setText(fileinmodel.getRatio());
                        progressBar.setVisibility(View.GONE);
                        scrollView.setVisibility(View.VISIBLE);
                    } else {
                        progressBar.setVisibility(View.GONE);
                        scrollView.setVisibility(View.VISIBLE);
                    }
                } else {
                    Log.e(TAG, "Error getting data", task.getException());
                    progressBar.setVisibility(View.GONE);
                    scrollView.setVisibility(View.VISIBLE);
                }
            }
        });


        downloadbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                downloadPdf(pdfuri.toString());

            }
        });
    }



    private void downloadPdf(String uri) {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReferenceFromUrl(uri);

        final ProgressDialog progressDialog = new ProgressDialog(OrdrerdDetailsadminactivity.this);
        progressDialog.setTitle("Downloading PDF");
        progressDialog.setMessage("Please wait...");
        progressDialog.setIndeterminate(true);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setCancelable(false);
        progressDialog.show();

        DownloadManager downloadManager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(uri));
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, filename);
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE | DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);

        final long downloadId = downloadManager.enqueue(request);

        downloadReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1) == downloadId) {
                    progressDialog.dismiss();
                }
            }
        };

        registerReceiver(downloadReceiver, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (downloadReceiver != null) {
            unregisterReceiver(downloadReceiver);
        }
    }




    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                downloadPdf(pdfuri.toString());
            } else {
                Toast.makeText(this, "Permission denied to write to storage", Toast.LENGTH_SHORT).show();
            }
        }
    }


}