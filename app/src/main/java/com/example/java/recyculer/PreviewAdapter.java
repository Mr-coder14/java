package com.example.java.recyculer;

import static java.security.AccessController.getContext;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.java.Fileinmodel;
import com.example.java.R;

import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.listener.OnLoadCompleteListener;
import com.github.barteksc.pdfviewer.util.FitPolicy;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.storage.FirebaseStorage;

import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import de.hdodenhof.circleimageview.CircleImageView;
public class PreviewAdapter extends RecyclerView.Adapter<PreviewAdapter.ViewHolder> {

    private Uri[] uris;
    private String[] fileNames;
    private String orderid,ratios1,Color1,formats1,pgsam1,count1,delivercharge1,perpage1,amtperqty1,sheet1;
    private Context context;
    private DatabaseReference databaseReference;
    private boolean isUploading = false;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private boolean[] uploaded;

    private Activity activity;
    private UploadProgressListener uploadProgressListener;

    public interface UploadProgressListener {
        void onProgressUpdate(int progress);
        void onAllFilesUploaded();
    }

    public PreviewAdapter(Activity activity,Uri[] uris, String[] fileNames) {

        this.activity=activity;
        this.uris =  uris ;
        this.fileNames = fileNames ;
        this.orderid = generateOrderId();
        this.uploaded=new boolean[uris.length];
    }
    private String sanitizeFileName(String fileName) {

        return fileName.replaceAll("[.#$\\[\\]]", "_");
    }

    private String generateOrderId() {
        UUID uuid = UUID.randomUUID();
        String uuidString = uuid.toString().replaceAll("-", "");
        return uuidString.substring(0, Math.min(uuidString.length(), 10));
    }
    public void uploadPdf() {
        ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setTitle("Uploading");
        progressDialog.setMessage("Please wait");
        progressDialog.setCancelable(false);
        progressDialog.show();

        isUploading = true;
        databaseReference = FirebaseDatabase.getInstance().getReference().child("pdfs");

        for (int i = 0; i < uris.length; i++) {
            String path = "pdfs/" + orderid + "/" + fileNames[i];
            String sanitizedFileName = sanitizeFileName(fileNames[i]);
            StorageReference fileRef = FirebaseStorage.getInstance().getReference().child(path);

            int finalI = i;

            UploadTask uploadTask = fileRef.putFile(uris[i]);
            uploadTask.addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    fileRef.getDownloadUrl().addOnSuccessListener(uri -> {
                        String downloadUrl = uri.toString();
                        String currentUserId = mAuth.getCurrentUser().getUid();
                        String amount = "89";
                        HashMap<String, Object> timestamp = new HashMap<>();
                        timestamp.put("timestamp", ServerValue.TIMESTAMP);

                        Fileinmodel fileinmodel = new Fileinmodel(fileNames[finalI], downloadUrl, currentUserId, amount,
                                ratios1, formats1, sheet1, Color1, String.valueOf(count1), String.valueOf(pgsam1), orderid,
                                String.valueOf(amtperqty1), timestamp.get("timestamp").toString(), String.valueOf(perpage1),
                                String.valueOf(delivercharge1));

                        databaseReference.child(orderid).child(sanitizedFileName).setValue(fileinmodel)
                                .addOnCompleteListener(task1 -> {
                                    if (task1.isSuccessful()) {
                                        uploaded[finalI] = true;
                                    } else {
                                        uploaded[finalI] = false;
                                    }

                                    if (finalI == uris.length - 1) {
                                        isUploading = false;
                                        progressDialog.dismiss();

                                        boolean allSuccessful = true;
                                        for (boolean status : uploaded) {
                                            if (!status) {
                                                allSuccessful = false;
                                                break;
                                            }
                                        }

                                        if (allSuccessful) {
                                            Toast.makeText(context, "Upload successful for all files", Toast.LENGTH_SHORT).show();
                                        } else {
                                            Toast.makeText(context, "Upload successful for all files", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                    });
                } else {
                    uploaded[finalI] = false;
                    Toast.makeText(context, "Upload failed for " + fileNames[finalI], Toast.LENGTH_SHORT).show();


                    if (finalI == uris.length - 1) {
                        isUploading = false;
                        progressDialog.dismiss();




                        boolean allSuccessful = true;
                        for (boolean status : uploaded) {
                            if (!status) {
                                allSuccessful = false;
                                break;
                            }
                        }

                        if (allSuccessful) {
                            Toast.makeText(context, "Upload successful for all files", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(context, "Upload failed for some files", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            });
        }
    }


    @NonNull
    @Override
    public PreviewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context=parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.orderpreviewactivitytemplate, parent, false);
        return new ViewHolder(view,activity);

    }

    @Override
    public void onBindViewHolder(@NonNull PreviewAdapter.ViewHolder holder, int position) {
        holder.bind(uris[position], fileNames[position],position);
        holder.black.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.colortxt.setText("Black");
                holder.Color="Black";
                holder.updateamt();
            }
        });



        holder.gradient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.colortxt.setText("Gradient");
                holder.Color="Gradient";
                holder.updateamt();
            }
        });

        ArrayList<String> sheets=new ArrayList<>();
        sheets.add("A4");
        sheets.add("OHP");

        ArrayAdapter<String> adapter3=new ArrayAdapter<>(context,R.layout.spinner_item_layout,sheets);
        holder.spinner3.setAdapter(adapter3);

        holder.spinner3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                holder.sheet=parent.getItemAtPosition(position).toString();
                holder.updateamt();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        ArrayList<String> format = new ArrayList<>();
        format.add("Front & Back");
        format.add("Front Only");
        ArrayAdapter<String> Adaptor1 = new ArrayAdapter<>(
                context,
                R.layout.spinner_item_layout,
                format
        );
        Adaptor1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        holder.spinner.setAdapter(Adaptor1);
        holder.spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                holder.formats = parent.getItemAtPosition(position).toString();
                holder.updateamt();
                Toast.makeText(context, holder.formats, Toast.LENGTH_SHORT).show();
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
                context,
                R.layout.spinner_item_layout,
                ratio
        );
        Adaptor2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        holder.spinner1.setAdapter(Adaptor2);
        holder.spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                holder.ratios=parent.getItemAtPosition(position).toString();
                holder.updateamt();
                Toast.makeText(context, holder.ratios, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        holder.qty.setFilters(new InputFilter[]{new PreviewAdapter.InputFilterMinMax(1, 10000)});
        holder.qty.addTextChangedListener(new TextWatcher() {
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
                    holder.count = Integer.parseInt(a);
                    holder.qtytxt1.setText(a);
                    holder.qtyno.setText(a);
                    holder.updateamt();
                } else {

                    holder.count = 1;
                    holder.qtytxt1.setText(String.valueOf(holder.count));
                    holder.qtyno.setText(String.valueOf(holder.count));
                   // holder.finalamt.setText(holder.amt1.getText());

                }
            }
        });

        holder.minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.count <= 1) {
                    holder.count = 1;
                } else {
                    holder.count--;
                    holder.qty.setText(String.valueOf(holder.count));
                    holder.qtytxt1.setText(String.valueOf(holder.count));
                    holder.qtyno.setText(String.valueOf(holder.count));
                    holder.updateamt();
                }
            }
        });


        holder.plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.count++;
                holder.qty.setText(String.valueOf(holder.count));
                holder.qtytxt1.setText(String.valueOf(holder.count));
                holder.qtyno.setText(String.valueOf(holder.count));
                holder.updateamt();
            }
        });

        holder.preview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (uris[holder.currentPdfIndex] != null) {
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setDataAndType(uris[holder.currentPdfIndex] , "application/pdf");
                    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    activity.startActivity(intent);
                }
                else {

                    if (getContext() != null) {
                        Toast.makeText(context, "PDF URI is not valid", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });





    }

    @Override
    public int getItemCount() {
        return uris.length;

    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView fileNameTextView;
        private PDFView pdfView;
        private TextView  pg, amt1, qtyno, qtytxt1, perpageamt, deliveryamt, colortxt;

        private EditText qty;
        private int currentPdfIndex = 0;
        private Spinner spinner, spinner1, spinner3;

        private int count = 1;
        private float perpage = 0.75f;

        private CircleImageView black, gradient;

        private Button preview;
        private String formats = "Front & Back", ratios = "1:1", sheet = "A4", Color = "Black";
        private int pgsam;
        private ImageButton plus, minus;
        private Uri pdf;
        private FirebaseAuth mAuth;
        private boolean isUploading = false;
        private float currentProgress = 0, finalamount;
        private float delivercharge = 10.0f, amtperqty;
        private Activity activity;

        public ViewHolder(@NonNull View itemView, Activity activity) {
            super(itemView);
            this.activity=activity;

            fileNameTextView = itemView.findViewById(R.id.filenametxt1admin);
            pdfView = itemView.findViewById(R.id.pdfViewadmin);
            qtytxt1 = itemView.findViewById(R.id.qtytxt1admin);
            qtyno = itemView.findViewById(R.id.qtynoadmin);
            fileNameTextView = itemView.findViewById(R.id.filenametxt1admin);
            black = itemView.findViewById(R.id.blackcolor);
            gradient = itemView.findViewById(R.id.gradientcolor);
            pg = itemView.findViewById(R.id.pagenoadmin);
            spinner3 = itemView.findViewById(R.id.spinner3admin);
            spinner= itemView.findViewById(R.id.spinneradmin);
            amt1 = itemView.findViewById(R.id.amt1admin);
            spinner1 = itemView.findViewById(R.id.spinner1admin);
            //finalamt = itemView.findViewById(R.id.finalamtadmin);
            qty = itemView.findViewById(R.id.qtytxtadmin);
            preview = itemView.findViewById(R.id.preview);
            perpageamt = itemView.findViewById(R.id.perpageamtadmin);
            plus = itemView.findViewById(R.id.addqtyadmin);
            qtyno = itemView.findViewById(R.id.qtynoadmin);
            deliveryamt = itemView.findViewById(R.id.deliveryamt1admin);
            minus = itemView.findViewById(R.id.minusqtyadmin);
            pdfView = itemView.findViewById(R.id.pdfViewadmin);
            colortxt = itemView.findViewById(R.id.colorfontadmin);
            ratios1=ratios;
            formats1=formats;
            count1=String.valueOf(count);
            sheet1=sheet;
            perpage1=String.valueOf(perpage);
            pgsam1=String.valueOf(pgsam);
            Color1= Color;
            delivercharge1=String.valueOf(delivercharge);
            amtperqty1=String.valueOf(amtperqty);

        }

        private void updateamt() {
            if(sheet.equals("A4")){
                try {
                    String amt1Text = amt1.getText().toString().replaceAll("[^\\d.]", "");

                    if(Color=="Gradient"){
                        perpage=10.0f;
                    }
                    else {
                        switch (ratios) {
                            case "1:1":
                                perpage = 0.75f;
                                break;
                            case "1:2":
                            case "1:4":
                                perpage = 0.85f;
                                break;
                        }

                    }

                    amtperqty= perpage * pgsam + delivercharge;




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
            }else if (sheet.equals("OHP")) {
                String amt1Text = amt1.getText().toString().replaceAll("[^\\d.]", "");
                perpage=15.0f;
                amtperqty=perpage * pgsam + delivercharge;
                finalamount = perpage * pgsam * count;
                finalamount += delivercharge;
                setamt();


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
                            updateamt();
                            float a = pgsam * perpage+delivercharge;
                            perpageamt.setText(String.valueOf(perpage));
                            amt1.setText("₹ " + String.valueOf(a));
                            amtperqty=a;
                            //finalamt.setText("₹ " + String.valueOf(a));

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

        private void setamt() {
            deliveryamt.setText(String.format("₹ %.2f", delivercharge));
            perpageamt.setText(String.format("%.2f", perpage));
            //finalamt.setText(String.format("₹ %.2f", finalamount));
            amt1.setText(String.format("₹ %.2f", amtperqty));
        }





        public void bind(Uri uri, String fileName,int position) {
            fileNameTextView.setText(fileName);
            displayPdfFromUri(uri, fileName);
            currentPdfIndex = position;
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
}
