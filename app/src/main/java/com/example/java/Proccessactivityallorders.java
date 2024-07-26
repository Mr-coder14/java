package com.example.java;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Proccessactivityallorders extends AppCompatActivity {
    private TextView gt,note;
    private String orderid,grandtotal;
    private List<Fileinmodel> fileinmodels;
    private boolean delivered;
    private DatabaseReference databaseReference;
    private ImageButton backbtn;
    private ImageView deleviredimage;
    private String notes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_proccessactivityallorders);
        backbtn=findViewById(R.id.back_btnadmin1219);
        gt=findViewById(R.id.gtt19);

        note=findViewById(R.id.notesuserdisplay9);
        fileinmodels=new ArrayList<>();
        orderid=getIntent().getStringExtra("orderid2");
        grandtotal=getIntent().getStringExtra("gt2");
        gt.setText("₹ "+grandtotal);
        deleviredimage=findViewById(R.id.handleimagedelivered19);
        databaseReference= FirebaseDatabase.getInstance().getReference().child("pdfs");

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot ui:snapshot.getChildren()){
                    for(DataSnapshot order:ui.getChildren()){
                        if(order.getKey().equals(orderid)) {
                            for (DataSnapshot su : order.getChildren()) {
                                Boolean deliveredValue = su.child("delivered").getValue(Boolean.class);
                                String notesValue = su.child("notes").getValue(String.class);

                                if (deliveredValue != null) {
                                    delivered = deliveredValue;
                                }
                                if (notesValue != null) {
                                    notes = notesValue;
                                }
                                break;
                            }
                            break;
                        }}}
                if(notes!=null){
                    note.setText(notes);
                }
                if(delivered) {
                    deleviredimage.setImageResource(R.drawable.tick);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


    }
}