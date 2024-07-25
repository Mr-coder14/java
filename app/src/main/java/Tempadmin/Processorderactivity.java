package Tempadmin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.java.Fileinmodel;
import com.example.java.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Processorderactivity extends AppCompatActivity {
    
    private TextView gt;
    private String orderid,grandtotal;
    private List<Fileinmodel> fileinmodels;
    private Button btn;
    private boolean delivered;
    private ImageButton backbtn;
    DatabaseReference databaseReference,newchild;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_processorderactivity);
        
        btn=findViewById(R.id.OrderConfirmedbtn);
        backbtn=findViewById(R.id.back_btnadmin12);
        gt=findViewById(R.id.gtt);
        fileinmodels=new ArrayList<>();
        orderid=getIntent().getStringExtra("orderid2");
        databaseReference = FirebaseDatabase.getInstance().getReference().child("pdfs");
        newchild = FirebaseDatabase.getInstance().getReference().child("orderstempadmin");
        grandtotal=getIntent().getStringExtra("gt2");
        gt.setText("₹ "+grandtotal);

        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                delivered=true;
                databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {

                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                            for (DataSnapshot orderSnapshot : userSnapshot.getChildren()) {
                                if (orderSnapshot.getKey().equals(orderid)) {
                                    for (DataSnapshot fileSnapshot : orderSnapshot.getChildren()) {

                                        databaseReference.child(userSnapshot.getKey())
                                                .child(orderid)
                                                .child(fileSnapshot.getKey())
                                                .child("delivered")
                                                .setValue(true)
                                                .addOnSuccessListener(aVoid -> {
                                                    Toast.makeText(Processorderactivity.this, "yes", Toast.LENGTH_SHORT).show();
                                                })
                                                .addOnFailureListener(e -> {
                                                    Toast.makeText(Processorderactivity.this, "no", Toast.LENGTH_SHORT).show();
                                                });

                                        Fileinmodel pdfFile = fileSnapshot.getValue(Fileinmodel.class);
                                        if (pdfFile != null) {
                                            fileinmodels.add(pdfFile);

                                            newchild.child(orderid).child(fileSnapshot.getKey()).setValue(pdfFile)
                                                    .addOnSuccessListener(aVoid -> {
                                                        //Toast.makeText(Processorderactivity.this, "Added Successfully", Toast.LENGTH_SHORT).show();
                                                        newchild.child(orderid).child(fileSnapshot.getKey()).child("delivered").setValue(true);
                                                    })
                                                    .addOnFailureListener(e -> {
                                                       // Toast.makeText(Processorderactivity.this, "Added Failed", Toast.LENGTH_SHORT).show();
                                                    });
                                        }
                                    }
                                    break;
                                }}
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                        Toast.makeText(Processorderactivity.this, "No files", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
}