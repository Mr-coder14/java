package com.example.java;
import static android.app.Activity.RESULT_OK;

import android.annotation.SuppressLint;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.OpenableColumns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
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
import java.io.File;

public class home_fragment extends Fragment {
    private static final int PICK_PDF_REQUEST = 1;
    private Context context;
    private Button btnupload,btm;
    EditText filename;
    private StorageReference storageRef;
    private DatabaseReference databaseReference;
    String diplayname=null;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.home_fragment,container,false);
        filename=view.findViewById(R.id.selectpdf);
        context=getContext();
        storageRef=FirebaseStorage.getInstance().getReference();
        databaseReference= FirebaseDatabase.getInstance().getReference("pdfs");
        filename.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openfile();
            }
        });
        return view;
    }



    private void openfile() {
        Intent intent = new Intent();
        intent.setType("application/pdf");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select File"), PICK_PDF_REQUEST);
    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_PDF_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri Uri = data.getData();
            String uri=Uri.toString();
            File myfile=new File(uri);
            String path=myfile.getAbsolutePath();

            if(uri.startsWith("content://")){
                Cursor cursor=null;
                try{

                    cursor=getContext().getContentResolver().query(Uri,null,null,null,null);
                    if(cursor!=null && cursor.moveToFirst()){
                        diplayname=cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));


                    }
                }finally {
                    cursor.close();
                }

            } else if (uri.startsWith("file://")) {
                diplayname=myfile.getName();
            }
            filename.setText(diplayname);
            openPreviewActivity(Uri,filename.getText().toString());

        }
    }

    private void openPreviewActivity(Uri uri,String fileName) {
        Intent intent = new Intent(getActivity(), preview_orderActivity.class);
        intent.setData(uri);
        intent.putExtra("fileName", fileName);
        startActivity(intent);
    }


}
