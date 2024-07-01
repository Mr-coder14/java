package com.example.java.recyculer;

import static java.security.AccessController.getContext;

import android.annotation.SuppressLint;
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
import com.example.java.PDFDetails;
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
    private String orderid;
    private Context context;




    private Activity activity;

    private ArrayList<PDFDetails> pdfDetailsList;



    public PreviewAdapter(Activity activity,Uri[] uris, String[] fileNames) {

        this.activity=activity;
        this.uris =  uris ;
        this.fileNames = fileNames ;
        this.orderid = generateOrderId();

        this.pdfDetailsList = new ArrayList<>();


        for (int i = 0; i < uris.length; i++) {
            pdfDetailsList.add(new PDFDetails());
        }
    }




    public  String getOrderid(){
        return orderid;
    }
    public ArrayList<PDFDetails> getPdfDetailsList(){
        return new ArrayList<>(pdfDetailsList);
    }


    private String generateOrderId() {
        UUID uuid = UUID.randomUUID();
        String uuidString = uuid.toString().replaceAll("-", "");
        return uuidString.substring(0, Math.min(uuidString.length(), 10));
    }



    @NonNull
    @Override
    public PreviewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context=parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.orderpreviewactivitytemplate, parent, false);
        return new ViewHolder(view,activity);

    }

    @Override
    public void onBindViewHolder(@NonNull PreviewAdapter.ViewHolder holder,  int position) {
        int p=position;
        holder.currentPdfIndex=position;
        holder.bind(uris[p], fileNames[p],p);

        PDFDetails pdfDetails = pdfDetailsList.get(p);

        holder.black.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.colortxt.setText("Black");
                holder.Color="Black";
                pdfDetailsList.get(p).setColor("Black");
                holder.updateamt(p);
            }
        });



        holder.gradient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.colortxt.setText("Gradient");
                holder.Color="Gradient";
                pdfDetailsList.get(p).setColor("Gradient");
                holder.updateamt(p);
            }
        });

        ArrayList<String> sheets=new ArrayList<>();
        sheets.add("A4");
        sheets.add("OHP");

        ArrayAdapter<String> adapter3=new ArrayAdapter<>(context,R.layout.spinner_item_layout,sheets);
        holder.spinner3.setAdapter(adapter3);

        holder.spinner3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position1, long id) {
                holder.sheet=parent.getItemAtPosition(position1).toString();
                String a=parent.getItemAtPosition(position1).toString();
                pdfDetailsList.get(p).setSheet(a);
                holder.updateamt(p);
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
            public void onItemSelected(AdapterView<?> parent, View view, int position2, long id) {
                holder.formats = parent.getItemAtPosition(position2).toString();
                String a =parent.getItemAtPosition(position2).toString();
                pdfDetailsList.get(p).setFormats(a);
                holder.updateamt(p);
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
            public void onItemSelected(AdapterView<?> parent, View view, int position3, long id) {
                holder.ratios=parent.getItemAtPosition(position3).toString();
                String a=parent.getItemAtPosition(position3).toString();
                pdfDetailsList.get(p).setRatios(a);
                holder.updateamt(p);
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
                    pdfDetailsList.get(p).setCount(Integer.parseInt(a));
                    holder.qtytxt1.setText(a);
                    holder.qtyno.setText(a);

                    holder.updateamt(p);
                } else {

                    holder.count = 1;
                    pdfDetailsList.get(p).setCount(Integer.parseInt("1"));

                    holder.qtytxt1.setText(String.valueOf(holder.count));
                    holder.qtyno.setText(String.valueOf(holder.count));


                }
            }
        });

        holder.minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.count <= 1) {
                    holder.count = 1;
                    pdfDetailsList.get(p).setCount(Integer.parseInt("1"));

                } else {
                    holder.count--;
                    pdfDetailsList.get(p).setCount(holder.count);
                    holder.qty.setText(String.valueOf(holder.count));
                    holder.qtytxt1.setText(String.valueOf(holder.count));
                    holder.qtyno.setText(String.valueOf(holder.count));
                    holder.updateamt(p);
                }
            }
        });


        holder.plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                holder.count++;
                pdfDetailsList.get(p).setCount(holder.count);
                holder.qty.setText(String.valueOf(holder.count));
                holder.qtytxt1.setText(String.valueOf(holder.count));
                holder.qtyno.setText(String.valueOf(holder.count));
                holder.updateamt(p);
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
        private TextView  pg, amt1, qtyno, qtytxt1, perpageamt, deliveryamt, colortxt,finalamt;

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
            finalamt = itemView.findViewById(R.id.finaladmin12);
            qty = itemView.findViewById(R.id.qtytxtadmin);
            preview = itemView.findViewById(R.id.preview);
            perpageamt = itemView.findViewById(R.id.perpageamtadmin);
            plus = itemView.findViewById(R.id.addqtyadmin);
            qtyno = itemView.findViewById(R.id.qtynoadmin);
            deliveryamt = itemView.findViewById(R.id.deliveryamt1admin);
            minus = itemView.findViewById(R.id.minusqtyadmin);
            pdfView = itemView.findViewById(R.id.pdfViewadmin);
            colortxt = itemView.findViewById(R.id.colorfontadmin);



        }

        private void updateamt(int position) {
            if(pdfDetailsList.get(position).getSheet().toString().equals("A4")){
                try {
                    String amt1Text = amt1.getText().toString().replaceAll("[^\\d.]", "");

                    if(pdfDetailsList.get(position).getColor().toString().equals("Gradient")){
                        perpage=10.0f;
                    }
                    else {
                        switch (pdfDetailsList.get(position).getRatios()) {
                            case "1:1":
                                perpage = 0.75f;
                                pdfDetailsList.get(position).setPerpage(String.valueOf(0.75));
                                break;
                            case "1:2":
                            case "1:4":
                                perpage = 0.85f;
                                pdfDetailsList.get(position).setPerpage(String.valueOf(0.85));
                                break;
                        }

                    }

                    amtperqty= perpage * pgsam + delivercharge;
                    pdfDetailsList.get(position).setPerqtyamt(String.valueOf(amtperqty));
                    pdfDetailsList.get(position).setDeliverycharge(String.valueOf(delivercharge));



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
            }else if (pdfDetailsList.get(position).getSheet().toString().equals("OHP")) {
                String amt1Text = amt1.getText().toString().replaceAll("[^\\d.]", "");
                perpage=15.0f;
                pdfDetailsList.get(position).setPerpage(String.valueOf(15.0));
                amtperqty=perpage * pgsam + delivercharge;
                finalamount = perpage * pgsam * count;
                finalamount += delivercharge;
                pdfDetailsList.get(position).setPerqtyamt(String.valueOf(amtperqty));
                pdfDetailsList.get(position).setDeliverycharge(String.valueOf(delivercharge));
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
                            pdfDetailsList.get(currentPdfIndex).setPages(String.valueOf(pgsam));
                            updateamt(currentPdfIndex);
                            float a = pgsam * perpage+delivercharge;
                            perpageamt.setText(String.valueOf(perpage));
                            amt1.setText("₹ " + String.valueOf(a));
                            amtperqty=a;
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
                    .pageFling(false).load();
        }

        private void setamt() {
            deliveryamt.setText(String.format("₹ %.2f", delivercharge));
            perpageamt.setText(String.format("%.2f", perpage));
            pdfDetailsList.get(currentPdfIndex).setDeliverycharge(String.valueOf(delivercharge));
            pdfDetailsList.get(currentPdfIndex).setPerpage(String.valueOf(perpage));
            finalamt.setText(String.format("₹ %.2f", finalamount));
            amt1.setText(String.format("₹ %.2f", amtperqty));
            pdfDetailsList.get(currentPdfIndex).setPerqtyamt(String.valueOf(amtperqty));
            pdfDetailsList.get(currentPdfIndex).setFinalmat(String.valueOf(finalamount));

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

        private boolean isInRange(int a, int b, int c){
            return b > a ? c >= a && c <= b : c >= b && c <= a;
        }
    }
}
