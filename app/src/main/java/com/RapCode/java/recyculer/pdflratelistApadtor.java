package com.RapCode.java.recyculer;

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

import com.RapCode.java.Fileinmodel;
import com.RapCode.java.PDFDetails;
import com.RapCode.java.Paymentactivity;
import com.RapCode.java.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;

public class pdflratelistApadtor extends RecyclerView.Adapter<pdflratelistApadtor.ViewHolder> {
    private Context context;
    private ArrayList<Uri> uris;
    private ArrayList<String> fileNames;
    private static final int UPI_PAYMENT_REQUEST_CODE = 1;
    private DatabaseReference databaseReference;
    ArrayList<PDFDetails> pdfDetails;
    private boolean orderd,delevried;
    private Activity activity;
    private boolean isPaymentActivityStarted = false;
    private int totalUploadedFiles = 0;
    private boolean isUploadingComplete = false;
    private String username;
    private ProgressDialog progressDialog;
    private boolean[] uploaded;
    private String orderid;
    String userid;
    public Float grandtotal=0.0f;
    private String notes;

    public pdflratelistApadtor(Activity activity, ArrayList<Uri> uris1, ArrayList<String> fileNames1, String orderid, ArrayList<PDFDetails> pdfDetails, String notes) {
        this.uris = uris1 != null ? uris1 : new ArrayList<>();
        this.activity = activity;
        this.uploaded = new boolean[this.uris.size()];
        this.fileNames = fileNames1 != null ? fileNames1 : new ArrayList<>();
        this.orderid = orderid;
        this.pdfDetails = pdfDetails != null ? pdfDetails : new ArrayList<>();
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
            progressDialog = new ProgressDialog(context);
            progressDialog.setTitle("Uploading");
            progressDialog.setMessage("Please wait");
            progressDialog.setCancelable(false);
            progressDialog.show();

            orderd = true;
            delevried = false;
            databaseReference = FirebaseDatabase.getInstance().getReference().child("pdfs");

            // Start the first upload
            uploadNextFile(0, progressDialog);
        }

        private void uploadNextFile(int index, ProgressDialog progressDialog) {
            if (index >= uris.size()) {
                // All files have been uploaded
                if (!isPaymentActivityStarted && activity != null && !activity.isFinishing()) {
                    isPaymentActivityStarted = true;
                    activity.runOnUiThread(() -> {
                        dismissProgressDialog();
                        Toast.makeText(context, "All files uploaded successfully", Toast.LENGTH_SHORT).show();
                        // Open PaymentActivity after all files are uploaded
                        Intent intent = new Intent(context, Paymentactivity.class);
                        intent.putExtra("orderid", orderid);
                        intent.putExtra("tt", grandtotal);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        activity.startActivity(intent);
                        activity.finish();


                    });
                }
                return;
            }

            String path = "pdfs/" + orderid + "/" + fileNames.get(index);
            String sanitizedFileName = sanitizeFileName(fileNames.get(index));
            StorageReference fileRef = FirebaseStorage.getInstance().getReference().child(path);

            UploadTask uploadTask = fileRef.putFile(uris.get(index));

            uploadTask.addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    fileRef.getDownloadUrl().addOnSuccessListener(uri -> {
                        String downloadUrl = uri.toString();
                        PDFDetails details = pdfDetails.get(index);
                        details.setGrandtotal(grandtotal.toString());
                        details.setOrderid1(orderid);
                        details.setUserid(userid);
                        details.setUri(downloadUrl);

                        Fileinmodel fileModel = new Fileinmodel();
                        fileModel.setName0(fileNames.get(index));
                        fileModel.setUri0(details.getUri());
                        fileModel.setSpiral(details.isSpiral());
                        fileModel.setOrderid0(details.getOrderid1());
                        fileModel.setColor0(details.getColor());
                        fileModel.setGrandTotal0(details.getGrandtotal());
                        fileModel.setQty0(String.valueOf(details.getCount()));
                        fileModel.setFormat0(details.getFormats());
                        fileModel.setRatio0(details.getRatios());
                        fileModel.setSheet0(details.getSheet());
                        fileModel.setDeliveyamt0("Free");
                        fileModel.setPages0(details.getPages());
                        fileModel.setPerpage0(details.getPerpage());
                        fileModel.setPerqtyamt0(details.getPerqtyamt());
                        fileModel.setOrderDate0(details.getOrderdate());
                        fileModel.setFinalamt0(details.getFinalmat());
                        fileModel.setPaid(false);
                        fileModel.setuserid0(details.getUserid());
                        fileModel.setOrderd(orderd);
                        fileModel.setDelivered(delevried);
                        fileModel.setNotes(notes != null ? notes : "");
                        fileModel.setUsername(username);
                        fileModel.setUploadTime(System.currentTimeMillis());

                        databaseReference.child(userid).child(orderid).child(sanitizedFileName).setValue(fileModel)
                                .addOnCompleteListener(task1 -> {
                                    if (task1.isSuccessful()) {
                                        // Start uploading the next file
                                        uploadNextFile(index + 1, progressDialog);
                                    } else {
                                        // Handle failure and show a message to the user
                                        handleUploadFailure(progressDialog, "Some files failed to upload");
                                    }
                                });
                    });
                } else {
                    // Handle failure and show a message to the user
                    handleUploadFailure(progressDialog, "Some files failed to upload");
                }
            });
        }

        private void handleUploadFailure(ProgressDialog progressDialog, String message) {
            if (progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
            Toast.makeText(context, message, Toast.LENGTH_LONG).show();
            if (activity != null && !activity.isFinishing()) {
                activity.finish();
            }
        }
        private void dismissProgressDialog() {
            if (progressDialog != null && progressDialog.isShowing()) {
                progressDialog.dismiss();
                progressDialog = null;
            }
        }







    }
}
