package com.example.java;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
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

public class ordersfragment_adminmain extends Fragment {

    private RecyclerView recyclerView;
    FirebaseAuth auth;
    FirebaseUser user;
    private DatabaseReference databaseReference;
    private Query query;
    private ProgressBar progressBar;
    Button button;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.oders_activity_adminadmin, container, false);
        databaseReference = FirebaseDatabase.getInstance().getReference().child("pdfs");
        recyclerView = view.findViewById(R.id.recyclerVieworders);
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        progressBar = view.findViewById(R.id.progress_baradmin);
        button=view.findViewById(R.id.btnsearchadmin);
        progressBar.setVisibility(View.VISIBLE);
        query = databaseReference.orderByChild("timestamp");

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), searchadminactivity.class));
            }
        });
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    progressBar.setVisibility(View.GONE);
                    displaypdfs();
                } else {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(getContext(), "No PDFs found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(getContext(), "Database error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


        return view;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu_admin, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id=item.getItemId();
        if (id==R.id.action_settings){
            Toast.makeText(getContext(), "clicked", Toast.LENGTH_SHORT).show();
            return true;
        }
        else {
            if (user != null) {
                auth.signOut();
                startActivity(new Intent(getContext(), loginactivity.class));
                getActivity().finish();
            }
            return true;
        }


    }


    private void displaypdfs() {
        FirebaseRecyclerOptions<Fileinmodel> options =
                new FirebaseRecyclerOptions.Builder<Fileinmodel>()
                        .setQuery(query, Fileinmodel.class)
                        .build();

        FirebaseRecyclerAdapter<Fileinmodel, RetrivepdfAdaptorhomeadmin> adapter =
                new FirebaseRecyclerAdapter<Fileinmodel, RetrivepdfAdaptorhomeadmin>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull RetrivepdfAdaptorhomeadmin holder, int position, @NonNull Fileinmodel model) {
                        progressBar.setVisibility(View.GONE);
                        //String orderids= model.getOrderid0().toString();
                        holder.orderid.setText(model.getOrderid0());


                        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("users");
                        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if (snapshot.exists()) {
                                    User user = snapshot.getValue(User.class);
                                    if (user != null) {
                                        holder.UserName1.setText(user.getName());
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                Toast.makeText(getContext(), "Failed to load user data", Toast.LENGTH_SHORT).show();
                            }
                        });

                        /*holder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                String pdfUri = model.getUri0();

                                if (pdfUri != null && !pdfUri.isEmpty()) {
                                    Intent intent=new Intent(getActivity(), OrdrerdDetailsadminactivity.class);
                                    intent.setData(Uri.parse(pdfUri));
                                    //intent.putExtra("orderid",orderids);
                                    startActivity(intent);
                                } else {
                                    Toast.makeText(getContext(), "PDF URI is not valid", Toast.LENGTH_SHORT).show();
                                }
                            }

                        });*/

                    }

                    @NonNull
                    @Override
                    public RetrivepdfAdaptorhomeadmin onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.orders_template, parent, false);
                        return new RetrivepdfAdaptorhomeadmin(view);
                    }
                };

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }
}
