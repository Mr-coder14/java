package com.example.java;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.app.ProgressDialog;
import com.github.barteksc.pdfviewer.util.FitPolicy;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.listener.OnLoadCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class preview_orderActivity extends AppCompatActivity {
    private TextView fileNameTextView,pg,amt1,finalamt,qtyno;
    private StorageReference storageRef;
    private EditText qty;
    private int count=1;
    private DatabaseReference databaseReference;
    private Button btn,preview;
    private PDFView pdfView;
    private int pgsam;
    private ImageButton plus,minus;
    private Uri pdf;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview_order);
        qtyno=findViewById(R.id.qtyno);
        fileNameTextView=findViewById(R.id.filenametxt1);
        pg=findViewById(R.id.pageno);
        amt1=findViewById(R.id.amt1);
        finalamt=findViewById(R.id.finalamt);
        qty=findViewById(R.id.qtytxt);
        preview=findViewById(R.id.preview);
        btn=findViewById(R.id.orderbtn);
        plus=findViewById(R.id.addqty);
        minus= findViewById(R.id.minusqty);
        pdfView=findViewById(R.id.pdfView);
        Uri uri=getIntent().getData();
        storageRef= FirebaseStorage.getInstance().getReference();
        databaseReference= FirebaseDatabase.getInstance().getReference("pdfs");
        String name=getIntent().getStringExtra("fileName");
        fileNameTextView.setText(name);
        qty.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String a=s.toString();
                if (!a.isEmpty()) {
                    count = Integer.parseInt(a);
                    updateamt();
                } else {
                    count = 1;
                    finalamt.setText(amt1.getText());
                }

            }
        });
        minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(count<=1){
                    count=1;
                }
                else {
                    count--;
                    qty.setText(String.valueOf(count));
                    updateamt();

                }

            }
        });

        plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                count++;
                qty.setText(String.valueOf(count));
                updateamt();

            }
        });


        if (uri != null) {
            displayPdfFromUri(uri, name);
        } else {
            Toast.makeText(this, "Failed to open PDF", Toast.LENGTH_SHORT).show();
            finish();
        }
        preview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(preview_orderActivity.this, pdfpreview_activity.class);
                intent.setData(uri);
                startActivity(intent);

            }
        });

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadpdf(uri,name);
            }
        });

    }

    private void updateamt() {
        try {
            String amt1Text = amt1.getText().toString().replaceAll("[^\\d.]", "");
            float a = Float.parseFloat(amt1Text);
            float b = a * count;
            finalamt.setText("₹ " + String.valueOf(b));
        } catch (NumberFormatException e) {
            finalamt.setText("Invalid amount");
        }
    }

    private void displayPdfFromUri(Uri uri, String name) {
        pdf = uri;
        pdfView.fromUri(uri)
                .defaultPage(0)
                .onLoad(new OnLoadCompleteListener() {
                    @Override
                    public void loadComplete(int nbPages) {
                        pg.setText(String.valueOf(nbPages));
                        pgsam = nbPages;
                        float perpage = 0.5f;
                        float a = pgsam * perpage;
                        amt1.setText("₹ " + String.valueOf(a));
                        finalamt.setText("₹ " + String.valueOf(a));
                    }
                })
                .enableAntialiasing(true)
                .spacing(0)
                .autoSpacing(false)
                .pageFitPolicy(FitPolicy.WIDTH)
                .fitEachPage(true)
                .pageSnap(true)
                .pageFling(false)
                .nightMode(false)
                .scrollHandle(null)
                .enableSwipe(false)
                .enableDoubletap(false)
                .enableAnnotationRendering(false)
                .pageSnap(false)
                .pageFling(false)
                .load();
    }


    private void uploadpdf(Uri pdfUri,String name) {
        ProgressDialog progressDialog=new ProgressDialog(this);
        progressDialog.setTitle("Uploading...");
        progressDialog.show();


        if (pdfUri != null) {
            final StorageReference pdfRef = storageRef.child("pdfs/" +name);
            pdfRef.putFile(pdfUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Task<Uri> uriTask= taskSnapshot.getStorage().getDownloadUrl();
                            while (!uriTask.isComplete());
                            Uri uri=uriTask.getResult();
                            String currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();

                            Fileinmodel fileinmodel = new Fileinmodel(name, uri.toString(),currentUserId);



                            databaseReference.child(databaseReference.push().getKey()).setValue(fileinmodel);
                            Toast.makeText(preview_orderActivity.this, "PDF Uploaded Successfully", Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                            startActivity(new Intent(preview_orderActivity.this, suceesanimation.class));


                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                            float per=(100*snapshot.getBytesTransferred())/snapshot.getTotalByteCount();
                            progressDialog.setMessage("Uploading : "+ per +"%");
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(preview_orderActivity.this, "PDF Cannot Upload", Toast.LENGTH_SHORT).show();

                        }
                    });
        }

    }
}