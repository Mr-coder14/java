package com.example.java;
import static java.security.AccessController.getContext;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;

import com.example.java.recyculer.PreviewAdapter;
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
import com.google.firebase.database.ServerValue;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import de.hdodenhof.circleimageview.CircleImageView;

public class preview_orderActivity extends AppCompatActivity {
    /*private TextView fileNameTextView, pg, amt1, finalamt, qtyno, qtytxt1, perpageamt, deliveryamt, colortxt;
    private StorageReference storageRef;
    private EditText qty;
    private List<Uri> pdfUris;
    private List<String> pdfNames;
    private int currentPdfIndex = 0;
    private String orderid;
    private Spinner spinner, spinner1, spinner3;
    private ImageButton backbtn;
    private int count = 1;
    private float perpage = 0.75f;
    private DatabaseReference databaseReference;
    private CircleImageView black, gradient;
    private ProgressDialog progressDialog;
    private Button btn, preview;
    private PDFView pdfView;
    private String formats = "Front & Back", ratios = "1:1", sheet = "A4", Color = "Black";
    private int pgsam;
    private ImageButton plus, minus;
    private Uri pdf;
    private boolean isUploading = false;
    private float currentProgress = 0, finalamount;
    private float delivercharge = 10.0f, amtperqty;*/

    private RecyclerView recyclerView;
    private PreviewAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview_order);
        /*spinner = findViewById(R.id.spinneradmin);
        backbtn = findViewById(R.id.backadmin);
        qtytxt1 = findViewById(R.id.qtytxt1admin);
        qtyno = findViewById(R.id.qtynoadmin);
        fileNameTextView = findViewById(R.id.filenametxt1admin);
        black = findViewById(R.id.blackcolor);
        gradient = findViewById(R.id.gradientcolor);

        pg = findViewById(R.id.pagenoadmin);
        spinner3 = findViewById(R.id.spinner3admin);
        amt1 = findViewById(R.id.amt1admin);
        spinner1 = findViewById(R.id.spinner1admin);
        finalamt = findViewById(R.id.finalamtadmin);
        qty = findViewById(R.id.qtytxtadmin);
        preview = findViewById(R.id.preview);
        btn = findViewById(R.id.orderbtnadmin);
        perpageamt = findViewById(R.id.perpageamtadmin);
        plus = findViewById(R.id.addqtyadmin);
        qtyno = findViewById(R.id.qtynoadmin);
        deliveryamt = findViewById(R.id.deliveryamt1admin);
        minus = findViewById(R.id.minusqtyadmin);
        pdfView = findViewById(R.id.pdfViewadmin);
        qtyno.setText(String.valueOf(count));
        colortxt = findViewById(R.id.colorfontadmin);*/

        recyclerView = findViewById(R.id.recyclerpreviewactivity);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        ArrayList<Uri> uris = getIntent().getParcelableArrayListExtra("uris");
        ArrayList<String> fileNames = getIntent().getStringArrayListExtra("fileNames");

        if (uris != null && fileNames != null) {
            Uri[] uriArray = uris.toArray(new Uri[0]);
            String[] fileNameArray = fileNames.toArray(new String[0]);
            adapter = new PreviewAdapter(uriArray, fileNameArray);
            recyclerView.setAdapter(adapter);
        } else {
            Toast.makeText(this, "Failed to load files.", Toast.LENGTH_SHORT).show();
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
