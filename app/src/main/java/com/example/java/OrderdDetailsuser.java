package com.example.java;

import static java.security.AccessController.getContext;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DownloadManager;
import android.app.ProgressDialog;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
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
import com.github.barteksc.pdfviewer.listener.OnLoadCompleteListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class OrderdDetailsuser extends AppCompatActivity {

    private static final int PERMISSION_REQUEST_CODE = 100;

    private static final String TAG = "OrderDetailsUser";
    private TextView fileNameTextViewauser, pguser, amt1user, finalamtuser, qtynouser, qtytxt1user, perpageamtuser, deliveryamtuser, colortxtuser, formatuser, ratiouser, sheetuser,perqtyamtuser;
    private String orderiduser;

    private ImageButton backbtn;
    private StorageReference storageRef;
    private DatabaseReference databaseReference;
    private ProgressDialog progressDialog;
    private Button preview,dbtn;
    private PDFView pdfView;
    private ScrollView scrollView;
    private ProgressBar progressBar;
    private String filename;
    private BroadcastReceiver downloadReceiver;
    private Uri pdfuri;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orderd_detailsuser);

        fileNameTextViewauser = findViewById(R.id.filenametxt1user);
        backbtn = findViewById(R.id.backuser);
        qtytxt1user = findViewById(R.id.qtytxt1user);
        dbtn=findViewById(R.id.downloadbtnuser);
        qtynouser = findViewById(R.id.qtynouser);
        perpageamtuser = findViewById(R.id.perpageuser);
        deliveryamtuser = findViewById(R.id.deliveryamtuser);
        colortxtuser = findViewById(R.id.colorfontuser);
        pguser = findViewById(R.id.pagenouser);
        amt1user = findViewById(R.id.amt1user);
        finalamtuser = findViewById(R.id.finalamtuser);
        pdfView = findViewById(R.id.pdfViewuser);
        formatuser = findViewById(R.id.formatuser);


        ratiouser = findViewById(R.id.ratiouser);
        sheetuser = findViewById(R.id.sheetuser);

        orderiduser=getIntent().getStringExtra("Orderid");

        databaseReference = FirebaseDatabase.getInstance().getReference().child("pdfs").child(orderiduser);



        pdfuri=getIntent().getData();
        preview=findViewById(R.id.previewuser);

        if (pdfuri == null) {
            Toast.makeText(this, "No PDF URI provided", Toast.LENGTH_SHORT).show();
        } else {
            Log.d(TAG, "PDF URI: " + pdfuri.toString());
            loadPdfFromUri(pdfuri.toString());
        }

        dbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                  downloadPdf(pdfuri.toString());
            }
        });

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
                        perpageamtuser.setText("₹ "+fileinmodel.getPerpage());
                        qtynouser.setText(fileinmodel.getQty());
                        deliveryamtuser.setText("₹ "+fileinmodel.getDeliveyamt());
                        qtytxt1user.setText(fileinmodel.getQty());
                        amt1user.setText("₹ "+fileinmodel.getPerqtyamt());
                        colortxtuser.setText(fileinmodel.getColor());
                        finalamtuser.setText("₹ "+fileinmodel.getFinalamt());
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

    private void loadPdfFromUri(String toString) {
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Loading PDF");
        progressDialog.setMessage("Please wait...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        new Thread(() -> {
            try {
                URL url = new URL(toString);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setDoInput(true);
                connection.connect();
                InputStream inputStream = connection.getInputStream();
                runOnUiThread(() -> {
                    pdfView.fromStream(inputStream)
                            .enableAnnotationRendering(true)
                            .spacing(10)
                            .onLoad(new OnLoadCompleteListener() {
                                @Override
                                public void loadComplete(int nbPages) {
                                    progressDialog.dismiss();
                                }
                            })
                            .load();
                });
            } catch (Exception e) {
                Log.e(TAG, "Error loading PDF", e);
                runOnUiThread(() -> {
                    progressDialog.dismiss();
                    Toast.makeText(this, "Failed to load PDF", Toast.LENGTH_SHORT).show();
                });
            }
        }).start();
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

    private void downloadPdf(String uri) {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReferenceFromUrl(uri);

        final ProgressDialog progressDialog = new ProgressDialog(OrderdDetailsuser.this);
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