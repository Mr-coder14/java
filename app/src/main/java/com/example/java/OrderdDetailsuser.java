package com.example.java;

import static java.security.AccessController.getContext;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.java.recyculer.Orderpreviewuseradaptor;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.listener.OnLoadCompleteListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class OrderdDetailsuser extends AppCompatActivity {

    private static final int PERMISSION_REQUEST_CODE = 100;

    private static final String TAG = "OrderDetailsUser";
    private RecyclerView recyclerView;
    private ImageButton backbtn;
    private String orderid,userid;
    private StorageReference storageRef;
    private DatabaseReference databaseReference,pdfsRef;
    private ProgressDialog progressDialog;
    private String name,uri,amtperqty,delverycharge,ratio,sheet,format,perpgae,pages,color,qty,finalmat;
    private String grandtotal;
    private Button dbtn;
    private ProgressBar progressBar;
    private ArrayList<Fileinmodel>fileinmodels;
    private  TextView gt;
    private FirebaseAuth mAuth;
    private Query query;

    private BroadcastReceiver downloadReceiver;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orderd_detailsuser);
        backbtn = findViewById(R.id.backuser);
        dbtn=findViewById(R.id.downloadbtnuser);
        mAuth=FirebaseAuth.getInstance();
        userid=mAuth.getCurrentUser().getUid();
        recyclerView=findViewById(R.id.recyculerviewuser);
        progressBar=findViewById(R.id.progressadmin11);
        progressBar.setVisibility(View.VISIBLE);
        gt=findViewById(R.id.gtuser);
        recyclerView.setVisibility(View.GONE);
        fileinmodels=new ArrayList<>();

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        orderid=getIntent().getStringExtra("orderid");
        grandtotal=getIntent().getStringExtra("gt");
        gt.setText("₹ "+grandtotal);

        databaseReference=FirebaseDatabase.getInstance().getReference().child("pdfs").child(userid).child(orderid);

        query=databaseReference;



        pdfsRef = FirebaseDatabase.getInstance().getReference().child("pdfs").child(userid).child(orderid);
        pdfsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                    for (DataSnapshot fileSnapshot : snapshot.getChildren()) {
                        name = fileSnapshot.child("name0").getValue(String.class);
                        uri = fileSnapshot.child("uri0").getValue(String.class);
                        ratio=fileSnapshot.child("ratio0").getValue(String.class);
                        sheet=fileSnapshot.child("sheet0").getValue(String.class);
                        pages=fileSnapshot.child("pages0").getValue(String.class);
                        format=fileSnapshot.child("format0").getValue(String.class);
                        finalmat=fileSnapshot.child("finalamt0").getValue(String.class);
                        perpgae=fileSnapshot.child("perpage0").getValue(String.class);
                        qty=fileSnapshot.child("qty0").getValue(String.class);
                        amtperqty=fileSnapshot.child("perqtyamt0").getValue(String.class);
                        delverycharge=fileSnapshot.child("deliveyamt0").getValue(String.class);
                        color=fileSnapshot.child("color0").getValue(String.class);

                        Fileinmodel pdfFile = new Fileinmodel(name,uri,userid,finalmat,ratio,format,sheet,color,qty,pages,orderid,amtperqty,perpgae,delverycharge);
                        fileinmodels.add(pdfFile);
                    }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

                Toast.makeText(OrderdDetailsuser.this, "no files", Toast.LENGTH_SHORT).show();
            }
        });



        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    displaypdfs();
                    progressBar.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(OrderdDetailsuser.this, "No pdfs", Toast.LENGTH_SHORT).show();

            }
        });







        /*dbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                  downloadPdf(pdfuri.toString());
            }
        });*/

        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                colse();
            }
        });




    }

    private void displaypdfs() {
        FirebaseRecyclerOptions<Fileinmodel>options=new FirebaseRecyclerOptions.Builder<Fileinmodel>().setQuery(query,Fileinmodel.class).build();
        FirebaseRecyclerAdapter<Fileinmodel, Orderpreviewuseradaptor> adapter=new FirebaseRecyclerAdapter<Fileinmodel, Orderpreviewuseradaptor>(options) {
            @Override
            protected void onBindViewHolder(@NonNull Orderpreviewuseradaptor holder, int position, @NonNull Fileinmodel model) {
                Fileinmodel fileinmodel=fileinmodels.get(position);
                holder.fileNameTextViewauser.setText(fileinmodel.getName0());
                holder.colortxtuser.setText(fileinmodel.getColor0());
                holder.sheetuser.setText(fileinmodel.getSheet0());
                holder.formatuser.setText(fileinmodel.getFormat0());
                holder.perpageamtuser.setText(fileinmodel.getPerpage0());
                holder.perqtyamtuser.setText(fileinmodel.getPerqtyamt0());
                holder.ratiouser.setText(fileinmodel.getRatio0());
                holder.finalamtuser.setText(fileinmodel.getFinalamt0());
                holder.deliveryamtuser.setText(fileinmodel.getDeliveyamt0());
                holder.qtynouser.setText(fileinmodel.getQty0());
                holder.pguser.setText(fileinmodel.getPages0());

            }

            @NonNull
            @Override
            public Orderpreviewuseradaptor onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.orderpreviewtemplateuser,parent,false);
                return new Orderpreviewuseradaptor(view);
            }
        };

        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }

    /*private void loadPdfFromUri(String toString) {
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
    }*/

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

    /*private void downloadPdf(String uri) {
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
    }*/

   /* @Override
    protected void onDestroy() {
        super.onDestroy();
        if (downloadReceiver != null) {
            unregisterReceiver(downloadReceiver);
        }
    }*/

    /*@Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                downloadPdf(pdfuri.toString());
            } else {
                Toast.makeText(this, "Permission denied to write to storage", Toast.LENGTH_SHORT).show();
            }
        }
    }*/
}