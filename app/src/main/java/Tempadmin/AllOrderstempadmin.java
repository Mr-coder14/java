package Tempadmin;

import static androidx.core.content.ContentProviderCompat.requireContext;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.java.Fileinmodel;
import com.example.java.R;
import com.example.java.RetrivepdfAdaptorhomeadmin;
import com.example.java.orderdetailsuser;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class AllOrderstempadmin extends AppCompatActivity {
    private RecyclerView recyclerView;
    private DatabaseReference databaseReference;
    private DatabaseReference pdfsRef;
    private FirebaseAuth auth;
    private Query query;
    private ProgressBar progressBar;
    private FirebaseUser user;
    private List<Fileinmodel> fl;
    private String orderid;
    private boolean delevireid;
    private String userid;
    private EditText searchEditText;
    private FirebaseRecyclerAdapter<Fileinmodel, RetrivepdfAdaptorhomeadmin> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_orderstempadmin);
        databaseReference = FirebaseDatabase.getInstance().getReference().child("pdfs");
        recyclerView = findViewById(R.id.recyclerhometadminhis);
        auth = FirebaseAuth.getInstance();

        user = auth.getCurrentUser();
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        progressBar = findViewById(R.id.progressbarhometadminhis);
        progressBar.setVisibility(View.VISIBLE);
        searchEditText=findViewById(R.id.search_edit_texttadminhish);
        fl = new ArrayList<>();
        userid = user.getUid();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("pdfs");
        query = databaseReference;

        Drawable searchIcon = ContextCompat.getDrawable(this, R.drawable.baseline_search_24);
        if (searchIcon != null) {
            int size = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 34, getResources().getDisplayMetrics());
            searchIcon.setBounds(0, 0, size, size);
            searchEditText.setCompoundDrawables(searchIcon, null, null, null);
        }


        pdfsRef = FirebaseDatabase.getInstance().getReference().child("pdfs");
        pdfsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot uniqueIdSnapshot : snapshot.getChildren()) {
                    for(DataSnapshot ui:uniqueIdSnapshot.getChildren()){
                        for (DataSnapshot fileSnapshot : ui.getChildren()) {
                            String name = fileSnapshot.child("name0").getValue(String.class);
                            String uri = fileSnapshot.child("uri0").getValue(String.class);
                            String grandTotal = fileSnapshot.child("grandTotal0").getValue(String.class);
                            orderid = fileSnapshot.child("orderid0").getValue(String.class);
                            delevireid=fileSnapshot.child("delivered").getValue(boolean.class);
                            String username=fileSnapshot.child("username").getValue(String.class);
                            Fileinmodel pdfFile = new Fileinmodel(name, uri, grandTotal, orderid,delevireid,username);
                            fl.add(pdfFile);
                        }
                    }}



            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

                Toast.makeText(AllOrderstempadmin.this, "Database error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    setupAdapter();
                    progressBar.setVisibility(View.GONE);
                }
                else {
                    Toast.makeText(AllOrderstempadmin.this, "No pdf found", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



    }
    private void setupAdapter() {
        FirebaseRecyclerOptions<Fileinmodel> options = new FirebaseRecyclerOptions.Builder<Fileinmodel>()
                .setQuery(query, Fileinmodel.class)
                .build();

        adapter = new FirebaseRecyclerAdapter<Fileinmodel, RetrivepdfAdaptorhomeadmin>(options) {
            @Override
            protected void onBindViewHolder(@NonNull RetrivepdfAdaptorhomeadmin holder, int position, @NonNull Fileinmodel model) {

                Fileinmodel fg=fl.get(position);
                holder.orderid.setText(fg.getOrderid0());
                holder.Grandtotal.setText("₹ "+fg.getGrandTotal0());

                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent=new Intent(AllOrderstempadmin.this, orderdetailsuser.class);
                        intent.putExtra("orderid",fg.getOrderid0());
                        intent.putExtra("gt",fg.getGrandTotal0());
                        startActivity(intent);
                    }
                });


            }


            @NonNull
            @Override
            public RetrivepdfAdaptorhomeadmin onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.orders_template, parent, false);
                return new RetrivepdfAdaptorhomeadmin(view);
            }
        };

        recyclerView.setLayoutManager(new LinearLayoutManager(AllOrderstempadmin.this, LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }

}