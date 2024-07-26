package com.example.java.recyculer;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
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
import com.example.java.suceesanimation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.HashMap;

public class pdflratelistApadtor extends RecyclerView.Adapter<pdflratelistApadtor.ViewHolder> {
    private Context context;
    private ArrayList<Uri> uris;
    private ArrayList<String> fileNames;
    private DatabaseReference databaseReference;
    ArrayList<PDFDetails> pdfDetails;
    private boolean orderd,delevried;
    private Activity activity;
    private String username;
    private boolean[] uploaded;
    private String orderid;
    String userid;
    public Float grandtotal=0.0f;
    private String notes;

    public  pdflratelistApadtor(Activity activity, ArrayList<Uri> uris1, ArrayList<String> fileNames1, String orderid, ArrayList<PDFDetails> pdfDetails,String notes){
        this.uris=uris1;
        this.activity=activity;
        this.uploaded=new boolean[uris.size()];
        this.fileNames=fileNames1;
        this.orderid=orderid;
        this.pdfDetails=pdfDetails;
        this.notes = notes != null ? notes : "";
        calculateGrandTotal();

    }
    public void updateNotes(String newNotes) {
        this.notes = newNotes;
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
        holder.finalamt.setText("₹ "+pdfDetails1.getFinalmat());
        holder.qty.setText(String.valueOf(pdfDetails1.getCount()));
        holder.filename.setText(filename.toString());
        holder.amtperqty.setText("₹ "+pdfDetails1.getPerqtyamt());
        holder.perpage.setText("₹ "+pdfDetails1.getPerpage());



    }

    @Override
    public int getItemCount() {
        return pdfDetails.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView pages,perpage,amtperqty,finalamt,filename,qty;
        private boolean isUploading = false;
        private FirebaseAuth mAuth;
        private DatabaseReference df;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            pages=itemView.findViewById(R.id.pagestxt1);
            perpage=itemView.findViewById(R.id.Perpagetxt1);
            qty=itemView.findViewById(R.id.Quantitytxt1);
            filename=itemView.findViewById(R.id.pdfnametxt);
            finalamt=itemView.findViewById(R.id.finalamt1txt1);

            amtperqty=itemView.findViewById(R.id.amtperqtytxt1);
            mAuth=FirebaseAuth.getInstance();
            userid=mAuth.getCurrentUser().getUid();
            df=FirebaseDatabase.getInstance().getReference().child("users").child(userid);

            df.child("name").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DataSnapshot> task) {
                    if(task.isSuccessful()){
                        username=task.getResult().getValue(String.class);
                    }
                }
            });




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
            orderd = true;
            delevried = false;
            databaseReference = FirebaseDatabase.getInstance().getReference().child("pdfs");

            int totalFiles = uris.size();
            int[] uploadedCount = {0};

            for (int i = 0; i < totalFiles; i++) {
                String path = "pdfs/" + orderid + "/" + fileNames.get(i);
                String sanitizedFileName = sanitizeFileName(fileNames.get(i));
                StorageReference fileRef = FirebaseStorage.getInstance().getReference().child(path);

                int finalI = i;

                UploadTask uploadTask = fileRef.putFile(uris.get(i));
                uploadTask.addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        fileRef.getDownloadUrl().addOnSuccessListener(uri -> {
                            String downloadUrl = uri.toString();
                            HashMap<String, Object> timestamp = new HashMap<>();
                            timestamp.put("timestamp", ServerValue.TIMESTAMP);

                            PDFDetails details = pdfDetails.get(finalI);
                            details.setGrandtotal(grandtotal.toString());
                            details.setOrderid1(orderid);
                            details.setUserid(userid);
                            details.setUri(downloadUrl);

                            Fileinmodel fileModel = new Fileinmodel();
                            fileModel.setName0(fileNames.get(finalI));
                            fileModel.setUri0(details.getUri());
                            fileModel.setOrderid0(details.getOrderid1());
                            fileModel.setColor0(details.getColor());
                            fileModel.setGrandTotal0(details.getGrandtotal());
                            fileModel.setQty0(String.valueOf(details.getCount()));
                            fileModel.setFormat0(details.getFormats());
                            fileModel.setRatio0(details.getRatios());
                            fileModel.setSheet0(details.getSheet());
                            fileModel.setDeliveyamt0(details.getDeliverycharge());
                            fileModel.setPages0(details.getPages());
                            fileModel.setPerpage0(details.getPerpage());
                            fileModel.setPerqtyamt0(details.getPerqtyamt());
                            fileModel.setOrderDate0(details.getOrderdate());
                            fileModel.setFinalamt0(details.getFinalmat());
                            fileModel.setuserid0(details.getUserid());
                            fileModel.setOrderd(orderd);
                            fileModel.setDelivered(delevried);
                            fileModel.setNotes(notes != null ? notes : "");
                            Toast.makeText(activity, fileModel.getNotes(), Toast.LENGTH_SHORT).show();
                            fileModel.setUsername(username);
                            databaseReference.child(userid).child(orderid).child(sanitizedFileName).setValue(fileModel)
                                    .addOnCompleteListener(task1 -> {
                                        if (task1.isSuccessful()) {
                                            uploaded[finalI] = true;
                                        } else {
                                            uploaded[finalI] = false;
                                        }

                                        uploadedCount[0]++;

                                        if (uploadedCount[0] == totalFiles) {
                                            isUploading = false;

                                            if (activity != null && !activity.isFinishing()) {
                                                activity.runOnUiThread(() -> {
                                                    try {
                                                        if (progressDialog != null && progressDialog.isShowing()) {
                                                            progressDialog.dismiss();
                                                        }

                                                        boolean allSuccessful = true;
                                                        for (boolean status : uploaded) {
                                                            if (!status) {
                                                                allSuccessful = false;
                                                                break;
                                                            }
                                                        }

                                                        if (allSuccessful) {
                                                            Toast.makeText(context, "Upload successful for all files", Toast.LENGTH_SHORT).show();
                                                            activity.startActivity(new Intent(activity, suceesanimation.class));
                                                            activity.finish();
                                                        } else {
                                                            Toast.makeText(context, "Upload unsuccessful for some files", Toast.LENGTH_SHORT).show();
                                                        }
                                                    } catch (Exception e) {
                                                        e.printStackTrace();
                                                    }
                                                });
                                            }
                                        } else {
                                            // Update progress
                                            if (activity != null && !activity.isFinishing()) {
                                                activity.runOnUiThread(() -> {
                                                    try {
                                                        if (progressDialog != null && progressDialog.isShowing()) {
                                                            progressDialog.setMessage("Uploading file " + (uploadedCount[0] + 1) + " of " + totalFiles);
                                                        }
                                                    } catch (Exception e) {
                                                        e.printStackTrace();
                                                    }
                                                });
                                            }
                                        }
                                    });
                        });
                    } else {
                        uploaded[finalI] = false;
                        uploadedCount[0]++;

                        if (activity != null && !activity.isFinishing()) {
                            activity.runOnUiThread(() -> {
                                try {
                                    Toast.makeText(context, "Upload failed for " + fileNames.get(finalI), Toast.LENGTH_SHORT).show();

                                    if (uploadedCount[0] == totalFiles) {
                                        isUploading = false;
                                        if (progressDialog != null && progressDialog.isShowing()) {
                                            progressDialog.dismiss();
                                        }
                                        Toast.makeText(context, "Upload failed for some files", Toast.LENGTH_SHORT).show();
                                    } else {

                                        if (progressDialog != null && progressDialog.isShowing()) {
                                            progressDialog.setMessage("Uploading file " + (uploadedCount[0] + 1) + " of " + totalFiles);
                                        }
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            });
                        }
                    }
                });
            }
        }

    }
}
