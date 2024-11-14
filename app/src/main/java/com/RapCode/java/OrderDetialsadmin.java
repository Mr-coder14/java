package com.RapCode.java;

import android.Manifest;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.RapCode.java.recyculer.orederpreviewadaptor;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

import Tempadmin.Processorderactivity;

public class OrderDetialsadmin extends AppCompatActivity {

    private static final int PERMISSION_REQUEST_CODE = 100;

    private ProgressDialog progressDialog;
    private int totalPdfs;
    private int downloadedPdfs;
    private static final String TAG = "OrderDetailsUser";
    private RecyclerView recyclerView;
    private ImageButton backbtn,orderinfo;
    private String orderid,userid;
    private StorageReference storageRef;
    private DatabaseReference databaseReference,pdfsRef;

    private String grandtotal;
    private Button dbtn;
    private ProgressBar progressBar;
    private ArrayList<Fileinmodel>fileinmodels;
    private  TextView gt;
    private DownloadManager downloadManager;

    private FirebaseAuth mAuth;
    private Query query;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orderd_detailsadmin);
        backbtn = findViewById(R.id.backuser);
        dbtn=findViewById(R.id.downloadbtnuser);
        mAuth=FirebaseAuth.getInstance();
        userid=mAuth.getCurrentUser().getUid();
        recyclerView=findViewById(R.id.recyculerviewuser);
        progressBar=findViewById(R.id.progressadmin11);
        progressBar.setVisibility(View.VISIBLE);
        gt=findViewById(R.id.gtuser);
        orderinfo=findViewById(R.id.orderinfouser);
        fileinmodels=new ArrayList<>();

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        orderid=getIntent().getStringExtra("orderid");
        grandtotal=getIntent().getStringExtra("gt");
        gt.setText("₹ "+grandtotal);

        databaseReference=FirebaseDatabase.getInstance().getReference().child("pdfs");

        query=databaseReference;

        orderinfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(OrderDetialsadmin.this, Processorderactivity.class);
                intent.putExtra("orderid2",orderid);
                intent.putExtra("gt2",grandtotal);

                startActivity(intent);
            }
        });



       loadpfds();
        downloadManager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);




        dbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkPermission()) {
                    downloadAllPdfs();
                } else {
                    requestPermission();
                }
            }
        });

        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });




    }

    private void loadpfds() {
        pdfsRef = FirebaseDatabase.getInstance().getReference().child("pdfs");
        pdfsRef.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                    for (DataSnapshot orderSnapshot : userSnapshot.getChildren()) {
                        if (orderSnapshot.getKey().equals(orderid)) {
                            for (DataSnapshot fileSnapshot : orderSnapshot.getChildren()) {
                                Fileinmodel pdfFile = fileSnapshot.getValue(Fileinmodel.class);

                                if (pdfFile != null) {
                                    fileinmodels.add(pdfFile);
                                }
                            }
                            break;
                        }}
                }
                displaypdfs();

                progressBar.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

                Toast.makeText(OrderDetialsadmin.this, "No files", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void displaypdfs() {
        if(fileinmodels==null){
            Toast.makeText(this, "No files", Toast.LENGTH_SHORT).show();

        }else {
            orederpreviewadaptor adapter = new orederpreviewadaptor(fileinmodels,this);
            recyclerView.setAdapter(adapter);
            progressBar.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }

    }




    private boolean checkPermission() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.R) {
            return Environment.isExternalStorageManager();
        } else {
            int result = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
            return result == PackageManager.PERMISSION_GRANTED;
        }
    }

    private void requestPermission() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.R) {
            try {
                Intent intent = new Intent(android.provider.Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
                intent.setData(Uri.parse("package:" + getPackageName()));
                startActivityForResult(intent, PERMISSION_REQUEST_CODE);
            } catch (Exception e) {
                Intent intent = new Intent(android.provider.Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION);
                startActivityForResult(intent, PERMISSION_REQUEST_CODE);
            }
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                downloadAllPdfs();
            } else {
                Toast.makeText(this, "Permission denied to write to storage", Toast.LENGTH_SHORT).show();
            }
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.R) {
                if (Environment.isExternalStorageManager()) {
                    downloadAllPdfs();
                } else {
                    Toast.makeText(this, "Permission denied to access storage", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }


    private void downloadAllPdfs() {
        totalPdfs = fileinmodels.size();
        downloadedPdfs = 0;

        // Set up the progress dialog
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Downloading PDFs");
        progressDialog.setMessage("Downloaded 0 of " + totalPdfs);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setMax(totalPdfs);
        progressDialog.setProgress(0);
        progressDialog.setCancelable(false);
        progressDialog.show();

        // Register the BroadcastReceiver for download completion
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
            registerReceiver(downloadReceiver, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE), Context.RECEIVER_NOT_EXPORTED);
        } else {
            registerReceiver(downloadReceiver, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
        }

        // Start downloads for each file and keep track of download IDs
        ArrayList<Long> downloadIds = new ArrayList<>();
        for (Fileinmodel pdfFile : fileinmodels) {
            Uri pdfUri = Uri.parse(pdfFile.getUri0());
            String fileName = pdfFile.getName0();

            DownloadManager.Request request = new DownloadManager.Request(pdfUri);
            request.setTitle("Downloading " + fileName);
            request.setDescription("Downloading PDF file");
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
            request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, fileName);

            long downloadId = downloadManager.enqueue(request);
            downloadIds.add(downloadId);
        }

        // Check progress periodically
        new Thread(() -> {
            boolean downloading = true;
            while (downloading) {
                downloading = false;
                for (Long downloadId : downloadIds) {
                    DownloadManager.Query query = new DownloadManager.Query();
                    query.setFilterById(downloadId);
                    try (Cursor cursor = downloadManager.query(query)) {
                        if (cursor != null && cursor.moveToFirst()) {
                            int status = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS));
                            if (status == DownloadManager.STATUS_RUNNING) {
                                downloading = true;
                                final int bytesDownloaded = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR));
                                final int bytesTotal = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_TOTAL_SIZE_BYTES));
                                if (bytesTotal > 0) {
                                    final int progress = (int) ((bytesDownloaded * 100L) / bytesTotal);
                                    runOnUiThread(() -> progressDialog.setProgress(progress));
                                }
                            }
                        }
                    }
                }
                // Sleep to avoid high CPU usage
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private final BroadcastReceiver downloadReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            downloadedPdfs++;
            progressDialog.setProgress(downloadedPdfs);
            progressDialog.setMessage("Downloaded " + downloadedPdfs + " of " + totalPdfs);

            if (downloadedPdfs == totalPdfs) {
                progressDialog.dismiss();
                Toast.makeText(OrderDetialsadmin.this, "All PDFs downloaded successfully", Toast.LENGTH_SHORT).show();
                unregisterReceiver(this);  // Unregister the receiver after all files are downloaded
            }
        }
    };

    private void updateDownloadProgress() {
        downloadedPdfs++;
        progressDialog.setProgress(downloadedPdfs);
        progressDialog.setMessage("Downloaded " + downloadedPdfs + " of " + totalPdfs);

        if (downloadedPdfs == totalPdfs) {
            progressDialog.dismiss();
            Toast.makeText(this, "All PDFs downloaded successfully", Toast.LENGTH_SHORT).show();
        }
    }



}