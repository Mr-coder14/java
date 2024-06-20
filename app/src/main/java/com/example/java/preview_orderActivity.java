package com.example.java;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;



import com.example.java.recyculer.PreviewAdapter;

import android.net.Uri;
import android.os.Bundle;

import android.text.InputFilter;
import android.text.Spanned;

import android.view.View;
import android.widget.ImageButton;

import android.widget.Toast;

import java.util.ArrayList;


public class preview_orderActivity extends AppCompatActivity {
    private ImageButton backbtn;

    private RecyclerView recyclerView;
    private PreviewAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview_order);
        backbtn=findViewById(R.id.backadmin);

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

        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showExitConfirmationDialog();
            }
        });



    }

    private void showExitConfirmationDialog() {

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
