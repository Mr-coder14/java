package com.example.java.recyculer;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.util.Pair;
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

import com.example.java.R;
import com.github.barteksc.pdfviewer.PDFView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class PreviewAdapter extends RecyclerView.Adapter<PreviewAdapter.ViewHolder> {

    private Uri[] uris;
    private String[] fileNames;
    private Context context;

    public PreviewAdapter(Uri[] uris, String[] fileNames) {
        this.uris =  uris ;
        this.fileNames = fileNames ;
    }


    @NonNull
    @Override
    public PreviewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.orderpreviewactivitytemplate, parent, false);
        return new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull PreviewAdapter.ViewHolder holder, int position) {
        holder.bind(uris[position], fileNames[position]);


    }

    @Override
    public int getItemCount() {
        return uris.length;

    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView fileNameTextView;
        private PDFView pdfView;
        private TextView  pg, amt1, finalamt, qtyno, qtytxt1, perpageamt, deliveryamt, colortxt;
        private StorageReference storageRef;
        private EditText qty;
        private int currentPdfIndex = 0;
        private String orderid;
        private Spinner spinner, spinner1, spinner3;

        private int count = 1;
        private float perpage = 0.75f;
        private DatabaseReference databaseReference;
        private CircleImageView black, gradient;
        private ProgressDialog progressDialog;
        private Button btn, preview;
        private String formats = "Front & Back", ratios = "1:1", sheet = "A4", Color = "Black";
        private int pgsam;
        private ImageButton plus, minus;
        private Uri pdf;
        private boolean isUploading = false;
        private float currentProgress = 0, finalamount;
        private float delivercharge = 10.0f, amtperqty;




        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            fileNameTextView = itemView.findViewById(R.id.filenametxt1admin);
            pdfView = itemView.findViewById(R.id.pdfViewadmin);
            qtytxt1 = itemView.findViewById(R.id.qtytxt1admin);
            qtyno = itemView.findViewById(R.id.qtynoadmin);
            fileNameTextView = itemView.findViewById(R.id.filenametxt1admin);
            black = itemView.findViewById(R.id.blackcolor);
            gradient = itemView.findViewById(R.id.gradientcolor);
            pg = itemView.findViewById(R.id.pagenoadmin);
            spinner3 = itemView.findViewById(R.id.spinner3admin);
            amt1 = itemView.findViewById(R.id.amt1admin);
            spinner1 = itemView.findViewById(R.id.spinner1admin);
            finalamt = itemView.findViewById(R.id.finalamtadmin);
            qty = itemView.findViewById(R.id.qtytxtadmin);
            preview = itemView.findViewById(R.id.preview);
            btn = itemView.findViewById(R.id.orderbtnadmin);
            perpageamt = itemView.findViewById(R.id.perpageamtadmin);
            plus = itemView.findViewById(R.id.addqtyadmin);
            qtyno = itemView.findViewById(R.id.qtynoadmin);
            deliveryamt = itemView.findViewById(R.id.deliveryamt1admin);
            minus = itemView.findViewById(R.id.minusqtyadmin);
            pdfView = itemView.findViewById(R.id.pdfViewadmin);
            colortxt = itemView.findViewById(R.id.colorfontadmin);

            black.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    colortxt.setText("Black");
                    Color="Black";
                    updateamt();
                }
            });

            gradient.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    colortxt.setText("Gradient");
                    Color="Gradient";
                    updateamt();
                }
            });

            ArrayList<String> sheets=new ArrayList<>();
            sheets.add("A4");
            sheets.add("OHP");

            ArrayAdapter<String> adapter3=new ArrayAdapter<>(context,R.layout.spinner_item_layout,sheets);
            spinner3.setAdapter(adapter3);

            spinner3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    sheet=parent.getItemAtPosition(position).toString();
                    updateamt();
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
            spinner.setAdapter(Adaptor1);
            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    formats = parent.getItemAtPosition(position).toString();
                    updateamt();
                    Toast.makeText(context, formats, Toast.LENGTH_SHORT).show();
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
            spinner1.setAdapter(Adaptor2);
            spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    ratios=parent.getItemAtPosition(position).toString();
                    updateamt();
                    Toast.makeText(context, ratios, Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }

        private void updateamt() {

        }

        public void bind(Uri uri, String fileName) {
            fileNameTextView.setText(fileName);
             pdfView.fromUri(uri).defaultPage(0).load();
        }

    }
}
