package com.RapCode.java;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.concurrent.TimeUnit;

public class CleanupWorker extends Worker {
    private DatabaseReference databaseReference,hj;
    private StorageReference storageReference;

    public CleanupWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        databaseReference = FirebaseDatabase.getInstance().getReference().child("pdfs");
        hj=FirebaseDatabase.getInstance().getReference().child("uploadscreenshots");
        storageReference = FirebaseStorage.getInstance().getReference();
    }

    @NonNull
    @Override
    public Result doWork() {
        final long currentTime = System.currentTimeMillis();
        final long threeDaysInMillis = TimeUnit.DAYS.toMillis(3);

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                    for (DataSnapshot orderSnapshot : userSnapshot.getChildren()) {
                        for (DataSnapshot fileSnapshot : orderSnapshot.getChildren()) {
                            Fileinmodel fileModel = fileSnapshot.getValue(Fileinmodel.class);

                            if (fileModel != null) {
                                long uploadTime = fileModel.getUploadTime();


                                if (currentTime - uploadTime >= threeDaysInMillis) {

                                    String filePath = "pdfs/" + fileModel.getOrderid0() + "/" + fileModel.getName0();
                                    String uplaodpath= "paymentscreenshots/" + fileModel.getOrderid0() + "/" + "payment_screenshot.jpg";
                                    storageReference.child(filePath).delete().addOnCompleteListener(task -> {
                                        if (task.isSuccessful()) {

                                            fileSnapshot.getRef().removeValue();
                                        }
                                    });
                                    storageReference.child(uplaodpath).delete();
                                    hj.child(fileModel.getOrderid0()).removeValue();
                                }
                            }
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        return Result.success();
    }
}
