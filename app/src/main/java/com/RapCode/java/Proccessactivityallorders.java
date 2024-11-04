package com.RapCode.java;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class Proccessactivityallorders extends AppCompatActivity {
    private TextView gt,note,timee;
    private String orderid,grandtotal;
    private List<Fileinmodel> fileinmodels;
    private boolean delivered;
    private DatabaseReference databaseReference;
    private ImageButton backbtn;
    private ImageView deleviredimage;
    private String notes;
    private Long time;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_proccessactivityallorders);
        backbtn=findViewById(R.id.back_btnadmin1219);
        gt=findViewById(R.id.gtt19);
        timee=findViewById(R.id.uploadtime);
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
                               time=su.child("uploadTime").getValue(Long.class);

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
                if(time!=null){
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTimeInMillis(time);

                    // Add one day
                    calendar.add(Calendar.DAY_OF_MONTH, 1);

                    // Format the date and display
                    SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy", Locale.getDefault());
                    String newDate = dateFormat.format(calendar.getTime());
                    timee.setText(newDate);
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