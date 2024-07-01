package com.example.java.recyculer;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.java.Fileinmodel;
import com.example.java.PDFDetails;
import com.example.java.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.checkerframework.checker.units.qual.A;

import java.util.ArrayList;
import java.util.HashMap;

public class pdflratelistApadtor extends RecyclerView.Adapter<pdflratelistApadtor.ViewHolder> {
    private Context context;
    private ArrayList<Uri> uris;
    private ArrayList<String> fileNames;
    private DatabaseReference databaseReference;
    ArrayList<PDFDetails> pdfDetails;
    private Activity activity;
    private boolean[] uploaded;
    private String orderid;
    public Float grandtotal=0.0f;

    public  pdflratelistApadtor(Activity activity, ArrayList<Uri> uris1, ArrayList<String> fileNames1, String orderid, ArrayList<PDFDetails> pdfDetails){
        this.uris=uris1;
        this.activity=activity;
        this.uploaded=new boolean[uris.size()];
        this.fileNames=fileNames1;
        this.orderid=orderid;
        this.pdfDetails=pdfDetails;
        calculateGrandTotal();

    }


    private void calculateGrandTotal() {
        grandtotal = 0.0f;
        for (PDFDetails details : pdfDetails) {
            grandtotal += Float.parseFloat(details.getFinalmat());
        }

    }

    public Float getGrandtotal(){
        return this.grandtotal;
    }




    @NonNull
    @Override
    public pdflratelistApadtor.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context=parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.pdfratestemplate, parent, false);
        return new pdflratelistApadtor.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull pdflratelistApadtor.ViewHolder holder, int position) {
        PDFDetails pdfDetails1=pdfDetails.get(position);
        String filename=fileNames.get(position);
        holder.pages.setText(pdfDetails1.getPages());
        holder.finalamt.setText(pdfDetails1.getFinalmat());
        holder.qty.setText(String.valueOf(pdfDetails1.getCount()));
        holder.filename.setText(filename.toString());
        holder.amtperqty.setText(pdfDetails1.getPerqtyamt());
        holder.perpage.setText(pdfDetails1.getPerpage());



    }

    @Override
    public int getItemCount() {
        return pdfDetails.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView pages,perpage,amtperqty,finalamt,filename,qty;
        private boolean isUploading = false;
        private FirebaseAuth mAuth;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            pages=itemView.findViewById(R.id.pagestxt1);
            perpage=itemView.findViewById(R.id.Perpagetxt1);
            qty=itemView.findViewById(R.id.Quantitytxt1);
            filename=itemView.findViewById(R.id.pdfnametxt);
            finalamt=itemView.findViewById(R.id.finalamt1txt1);

            amtperqty=itemView.findViewById(R.id.amtperqtytxt1);
            mAuth=FirebaseAuth.getInstance();

        }
        private String sanitizeFileName(String fileName) {

            return fileName.replaceAll("[.#$\\[\\]]", "_");
        }

        public void uploadPdf() {
            ProgressDialog progressDialog = new ProgressDialog(context);
            progressDialog.setTitle("Uploading");
            progressDialog.setMessage("Please wait");
            progressDialog.setCancelable(false);
            progressDialog.show();

            isUploading = true;
            databaseReference = FirebaseDatabase.getInstance().getReference().child("pdfs");

            for (int i = 0; i < uris.size(); i++) {
                String path = "pdfs/" + orderid + "/" + fileNames.get(i);
                String sanitizedFileName = sanitizeFileName(fileNames.get(i));
                StorageReference fileRef = FirebaseStorage.getInstance().getReference().child(path);

                int finalI = i;

                UploadTask uploadTask = fileRef.putFile(uris.get(i));
                uploadTask.addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        fileRef.getDownloadUrl().addOnSuccessListener(uri -> {
                            String downloadUrl = uri.toString();
                            String currentUserId = mAuth.getCurrentUser().getUid();
                            HashMap<String, Object> timestamp = new HashMap<>();
                            timestamp.put("timestamp", ServerValue.TIMESTAMP);

                            PDFDetails details = pdfDetails.get(finalI);

                            Fileinmodel fileModel = new Fileinmodel();

                            fileModel.setName(fileNames.get(finalI));
                            fileModel.setUri(downloadUrl);
                            fileModel.setOrderid(orderid);
                            fileModel.setTimestamp(ServerValue.TIMESTAMP.size());
                            fileModel.setColor(details.getColor());
                            fileModel.setQty(String.valueOf(details.getCount()));
                            fileModel.setFormat(details.getFormats());
                            fileModel.setRatio(details.getRatios());
                            fileModel.setSheet(details.getSheet());
                            fileModel.setDeliveyamt(details.getDeliverycharge());
                            fileModel.setPages(details.getPages());
                            fileModel.setPerpage(details.getPerpage());
                            fileModel.setPerqtyamt(details.getPerqtyamt());
                            fileModel.setOrderDate(details.getOrderdate());
                            fileModel.setFinalamt(details.getFinalmat());
                            fileModel.setUserID(details.getUerid());


                            databaseReference.child(orderid).child(sanitizedFileName).setValue(fileModel)
                                    .addOnCompleteListener(task1 -> {
                                        if (task1.isSuccessful()) {
                                            uploaded[finalI] = true;
                                        } else {
                                            uploaded[finalI] = false;
                                        }

                                        if (finalI == uris.size() - 1) {
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
                                                Toast.makeText(context, "Upload unsuccessful for some files", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                        });
                    } else {
                        uploaded[finalI] = false;
                        Toast.makeText(context, "Upload failed for " + fileNames.get(finalI), Toast.LENGTH_SHORT).show();


                        if (finalI == uris.size() - 1) {
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
    }
}
