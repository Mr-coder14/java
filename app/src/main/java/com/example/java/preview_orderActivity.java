package com.example.java;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;



import com.example.java.recyculer.PreviewAdapter;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import android.text.InputFilter;
import android.text.Spanned;

import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.ArrayList;


public class preview_orderActivity extends AppCompatActivity implements PreviewAdapter.UploadProgressListener  {
    private ImageButton backbtn;
    private Button btn;

    private RecyclerView recyclerView;
    private PreviewAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview_order);
        backbtn=findViewById(R.id.backadmin);

        recyclerView = findViewById(R.id.recyclerpreviewactivity);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        btn=findViewById(R.id.orderbtnadmin);




        ArrayList<Uri> uris = getIntent().getParcelableArrayListExtra("uris");
        ArrayList<String> fileNames = getIntent().getStringArrayListExtra("fileNames");

        if (uris != null && fileNames != null) {
            Uri[] uriArray = uris.toArray(new Uri[0]);
            String[] fileNameArray = fileNames.toArray(new String[0]);
            adapter = new PreviewAdapter(this,uriArray, fileNameArray);
            recyclerView.setAdapter(adapter);
        } else {
            Toast.makeText(this, "Failed to load files.", Toast.LENGTH_SHORT).show();
        }

        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showExitConfirmationDialog();
            }
        });

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadPdfFiles();
            }
        });



    }

    private void uploadPdfFiles() {


        for (int i = 0; i < adapter.getItemCount(); i++) {
            PreviewAdapter.ViewHolder viewHolder = (PreviewAdapter.ViewHolder) recyclerView.findViewHolderForAdapterPosition(i);
            if (viewHolder != null) {
                viewHolder.uploadPdf();
            }
        }



    }

    private void showExitConfirmationDialog() {
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


    @Override
    public void onProgressUpdate(int progress) {


    }

    @Override
    public void onAllFilesUploaded() {
        //Toast.makeText(this, "All files uploaded successfully", Toast.LENGTH_SHORT).show();
    }
}
