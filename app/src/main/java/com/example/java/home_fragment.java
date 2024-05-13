package com.example.java;
import static android.app.Activity.RESULT_OK;
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
        btnupload=view.findViewById(R.id.pdfupload);btm=view.findViewById(R.id.buttondownload);
        filename=view.findViewById(R.id.selectpdf);
        context=getContext();
        storageRef=FirebaseStorage.getInstance().getReference();
        databaseReference= FirebaseDatabase.getInstance().getReference("pdfs");
        btnupload.setEnabled(false);
        filename.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openfile();
            }
        });
        btm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                downloadpdf();
            }
        });

        return view;
    }

    private void downloadpdf() {


        StorageReference pdfRef = storageRef.child("pdfs/"+diplayname);

        pdfRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                String url = uri.toString();
                DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
                request.setTitle("PDF File");
                request.setDescription("Downloading");
                request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS,diplayname);
                DownloadManager manager = (DownloadManager) getActivity().getSystemService(Context.DOWNLOAD_SERVICE);
                manager.enqueue(request);
                Toast.makeText(getActivity(), "PDF Download started", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(context, "Failed to download PDF", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void openfile() {
        Intent intent = new Intent();
        intent.setType("application/pdf");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select PDF"), PICK_PDF_REQUEST);
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
            btnupload.setEnabled(true);
            filename.setText(diplayname);
            btnupload.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    uploadPdfToFirebase(data.getData());
                }
            });
        }
    }

    private void uploadPdfToFirebase(Uri pdfUri) {
        ProgressDialog progressDialog=new ProgressDialog(getContext());
        progressDialog.setTitle("Uploading...");
        progressDialog.show();


        if (pdfUri != null) {
            final StorageReference pdfRef = storageRef.child("pdfs/" +diplayname);
            pdfRef.putFile(pdfUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Task<Uri> uriTask= taskSnapshot.getStorage().getDownloadUrl();
                            while (!uriTask.isComplete());
                            Uri uri=uriTask.getResult();
                            String currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();

                            Fileinmodel fileinmodel = new Fileinmodel(filename.getText().toString(), uri.toString(),currentUserId);



                            databaseReference.child(databaseReference.push().getKey()).setValue(fileinmodel);
                            Toast.makeText(getActivity(), "PDF Uploaded Successfully", Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();


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
                            Toast.makeText(getActivity(), "PDF Cannot Upload", Toast.LENGTH_SHORT).show();

                        }
                    });
        }
    }
}
