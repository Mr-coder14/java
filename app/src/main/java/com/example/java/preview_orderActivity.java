package com.example.java;
import static java.security.AccessController.getContext;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.app.ProgressDialog;
import com.github.barteksc.pdfviewer.util.FitPolicy;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.listener.OnLoadCompleteListener;
import com.google.android.gms.tasks.OnCompleteListener;
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
import java.util.ArrayList;
import java.util.UUID;

import de.hdodenhof.circleimageview.CircleImageView;

public class preview_orderActivity extends AppCompatActivity {
    private TextView fileNameTextView, pg, amt1, finalamt, qtyno, qtytxt1,perpageamt,deliveryamt,colortxt;
    private StorageReference storageRef;
    private EditText qty;
    private String orderid;
    private Spinner spinner, spinner1,spinner3;
    private ImageButton backbtn;
    private int count = 1;
    private float perpage=0.75f;
    private DatabaseReference databaseReference;
    private CircleImageView black,gradient;
    private ProgressDialog progressDialog;
    private Button btn, preview;
    private PDFView pdfView;
    private String formats="Front & Back",ratios="1:1",sheet="A4",Color="Black";
    private int pgsam;
    private ImageButton plus, minus;
    private Uri pdf;
    private boolean isUploading = false;
    private float currentProgress = 0,finalamount;
    private float delivercharge=10.0f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview_order);
        spinner = findViewById(R.id.spinneradmin);
        backbtn=findViewById(R.id.backadmin);
        qtytxt1 = findViewById(R.id.qtytxt1admin);
        qtyno = findViewById(R.id.qtynoadmin);
        fileNameTextView = findViewById(R.id.filenametxt1admin);
        black=findViewById(R.id.blackcolor);
        gradient=findViewById(R.id.gradientcolor);

        pg = findViewById(R.id.pagenoadmin);
        spinner3=findViewById(R.id.spinner3admin);
        amt1 = findViewById(R.id.amt1admin);
        spinner1 = findViewById(R.id.spinner1admin);
        finalamt = findViewById(R.id.finalamtadmin);
        qty = findViewById(R.id.qtytxtadmin);
        preview = findViewById(R.id.preview);
        btn = findViewById(R.id.orderbtnadmin);
        perpageamt=findViewById(R.id.perpageamtadmin);
        plus = findViewById(R.id.addqtyadmin);
        qtyno=findViewById(R.id.qtynoadmin);
        deliveryamt=findViewById(R.id.deliveryamt1admin);
        minus = findViewById(R.id.minusqtyadmin);
        pdfView = findViewById(R.id.pdfViewadmin);
        qtyno.setText(String.valueOf(count));
        colortxt=findViewById(R.id.colorfontadmin);

        Uri uri = getIntent().getData();
        storageRef = FirebaseStorage.getInstance().getReference();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("pdfs");
        String name = getIntent().getStringExtra("fileName");
        fileNameTextView.setText(name);

        black.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                colortxt.setText("Black");
                Color="Black";
            }
        });

        gradient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                colortxt.setText("Gradient");
                Color="Gradient";
            }
        });

        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               showExitConfirmationDialog();
            }
        });


        ArrayList<String> sheets=new ArrayList<>();
        sheets.add("A4");
        sheets.add("OHP");

        ArrayAdapter<String> adapter3=new ArrayAdapter<>(this,R.layout.spinner_item_layout,sheets);
        spinner3.setAdapter(adapter3);

        spinner3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                sheet=parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        ArrayList<String> format = new ArrayList<>();
        format.add("Front & Back");
        format.add("Front Only");
        ArrayAdapter<String> Adaptor1 = new ArrayAdapter<>(
                this,
                R.layout.spinner_item_layout,
                format
        );
        Adaptor1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(Adaptor1);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                formats = parent.getItemAtPosition(position).toString();
                updateamt();
                Toast.makeText(preview_orderActivity.this, formats, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        ArrayList<String> ratio = new ArrayList<>();
        ratio.add("1:1");
        ratio.add("1:2");
        ratio.add("1:4");
        ArrayAdapter<String> Adaptor2 = new ArrayAdapter<>(
                this,
                R.layout.spinner_item_layout,
                ratio
        );
        Adaptor2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner1.setAdapter(Adaptor2);
        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ratios=parent.getItemAtPosition(position).toString();
                updateamt();
                Toast.makeText(preview_orderActivity.this, ratios, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        qty.setFilters(new InputFilter[]{new InputFilterMinMax(1, 10000)});
        qty.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                String a = s.toString();
                if (!a.isEmpty()) {
                    count = Integer.parseInt(a);
                    qtytxt1.setText(a);
                    qtyno.setText(a);
                    updateamt();
                } else {
                    count = 1;
                    qtytxt1.setText(String.valueOf(count));
                    qtyno.setText(String.valueOf(count));
                    finalamt.setText(amt1.getText());
                }
            }
        });

        minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (count <= 1) {
                    count = 1;
                } else {
                    count--;
                    qty.setText(String.valueOf(count));
                    qtytxt1.setText(String.valueOf(count));
                    qtyno.setText(String.valueOf(count));
                    updateamt();
                }
            }
        });

        plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                count++;
                qty.setText(String.valueOf(count));
                qtytxt1.setText(String.valueOf(count));
                qtyno.setText(String.valueOf(count));
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
                if (uri != null) {
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setDataAndType(uri, "application/pdf");
                    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    startActivity(intent);
                }
                else {

                    if (getContext() != null) {
                        Toast.makeText(preview_orderActivity.this, "PDF URI is not valid", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(finalamount>=50){
                    uploadpdf(uri, name);
                }else{
                    Toast.makeText(preview_orderActivity.this, "The Minimum Order is 50 rupees", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    private void showExitConfirmationDialog() {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Exit");
            builder.setMessage("Are you sure you want to quit?");
            builder.setCancelable(false);

            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    startActivity(new Intent(preview_orderActivity.this, MainActivity.class));
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

    private void updateamt() {
        try {
            String amt1Text = amt1.getText().toString().replaceAll("[^\\d.]", "");

            switch (ratios) {
                case "1:1":
                    perpage = 0.75f;
                    break;
                case "1:2":
                case "1:4":
                    perpage = 0.85f;
                    break;
            }

            finalamount = perpage * pgsam * count;

            if (ratios.equals("1:1") && formats.equals("Front Only") && pgsam > 50) {
                float discount = 0.05f * finalamount;
                finalamount -= discount;
            }

            if((ratios.equals("1:2") || ratios.equals("1:4")) && formats.equals("Front & Back") && pgsam>200){
                float discount = 0.05f * finalamount;
                finalamount -= discount;
            }

            finalamount += delivercharge;
            setamt();

        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
    }
    private void setamt() {
        deliveryamt.setText(String.format("₹ %.2f", delivercharge));
        perpageamt.setText(String.format("%.2f", perpage));
        finalamt.setText(String.format("₹ %.2f", finalamount));
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
                        float a = pgsam * perpage+delivercharge;
                        perpageamt.setText(String.valueOf(perpage));
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
    public String generateOrderId() {

        UUID uuid = UUID.randomUUID();

        String uuidString = uuid.toString().replaceAll("-", "");

        String id = uuidString.substring(0, Math.min(uuidString.length(), 10));
        return id;


    }

    private void uploadpdf(Uri pdfUri, String name) {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading...");
            progressDialog.setCancelable(false);
        }
        progressDialog.show();
        isUploading = true;

        if (pdfUri != null) {
            final StorageReference pdfRef = storageRef.child("pdfs/" + name);
            pdfRef.putFile(pdfUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                            uriTask.addOnCompleteListener(new OnCompleteListener<Uri>() {
                                @Override
                                public void onComplete(@NonNull Task<Uri> task) {
                                    if (task.isSuccessful()) {
                                        Uri uri = task.getResult();
                                        String currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                                        String amount = finalamt.getText().toString().replace("₹ ", "");
                                        orderid = generateOrderId();


                                        Fileinmodel fileinmodel = new Fileinmodel(name, uri.toString(), currentUserId, amount,ratios,formats,sheet,Color,String.valueOf(count),String.valueOf(pgsam),orderid);
                                        databaseReference.child(orderid).setValue(fileinmodel);

                                        Toast.makeText(preview_orderActivity.this, "PDF Uploaded Successfully", Toast.LENGTH_SHORT).show();
                                        progressDialog.dismiss();
                                        isUploading = false;
                                        startActivity(new Intent(preview_orderActivity.this, suceesanimation.class));
                                        finish();
                                    } else {
                                        progressDialog.dismiss();
                                        isUploading = false;
                                        Toast.makeText(preview_orderActivity.this, "Failed to get download URL", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                            currentProgress = (int)(100 * snapshot.getBytesTransferred()) / snapshot.getTotalByteCount();
                            progressDialog.setMessage("Uploading: " + currentProgress + "%");
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            isUploading = false;
                            Toast.makeText(preview_orderActivity.this, "PDF Cannot Upload", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }
    @Override
    protected void onPause() {
        super.onPause();
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

    @Override
    public void onBackPressed() {
        showExitConfirmationDialog();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isUploading) {
            if (progressDialog == null) {
                progressDialog = new ProgressDialog(this);
                progressDialog.setTitle("Uploading...");
                progressDialog.setCancelable(false);
            }
            progressDialog.show();
            progressDialog.setMessage("Uploading: " + currentProgress + "%");
        }
    }
}


class InputFilterMinMax implements InputFilter {
    private int min, max;

    public InputFilterMinMax(int min, int max) {
        this.min = min;
        this.max = max;
    }

    @Override
    public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
        try {
            int input = Integer.parseInt(dest.toString() + source.toString());
            if (isInRange(min, max, input))
                return null;
        } catch (NumberFormatException nfe) {
        }
        return "";
    }

    private boolean isInRange(int a, int b, int c) {
        return b > a ? c >= a && c <= b : c >= b && c <= a;
    }
}
