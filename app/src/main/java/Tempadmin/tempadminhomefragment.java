package Tempadmin;

import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.java.Fileinmodel;
import com.example.java.R;
import com.example.java.RetrivepdfAdaptorhomeadmin;
import com.example.java.recyculer.orderadaptortadmin;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class tempadminhomefragment extends Fragment {

    private static final String TAG = "hometempadmin";

    private RecyclerView recyclerView;
    private FirebaseAuth auth;
    private FirebaseUser user;
    private String orderid;
    private boolean ordered,delivered;
    private EditText editText;
    private TextView txt;
    private DatabaseReference databaseReference;
    private HashMap<String, Fileinmodel> uniqueOrders = new HashMap<>();
    private orderadaptortadmin adapter;
    private Query query;
    private ProgressBar progressBar;
    private Handler debounceHandler = new Handler(Looper.getMainLooper());
    private Runnable searchRunnable;

    public tempadminhomefragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tempadminhomefragment, container, false);

        databaseReference = FirebaseDatabase.getInstance().getReference().child("pdfs");
        recyclerView = view.findViewById(R.id.recyclerhometadmin);
        recyclerView.setHasFixedSize(true);
        progressBar = view.findViewById(R.id.progressbarhometadmin);
        txt=view.findViewById(R.id.homefragmentempadmin123);

        editText = view.findViewById(R.id.search_edit_texttadmin);

        Drawable searchIcon = ContextCompat.getDrawable(requireContext(), R.drawable.baseline_search_24);
        if (searchIcon != null) {
            int size = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 34, getResources().getDisplayMetrics());
            searchIcon.setBounds(0, 0, size, size);
            editText.setCompoundDrawables(searchIcon, null, null, null);
        }


        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        query = databaseReference;
        progressBar.setVisibility(View.VISIBLE);
        txt.setVisibility(View.INVISIBLE);


        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                uniqueOrders.clear();
                for (DataSnapshot uniqueIdSnapshot : snapshot.getChildren()) {
                    for (DataSnapshot ui : uniqueIdSnapshot.getChildren()) {
                        for (DataSnapshot fileSnapshot : ui.getChildren()) {
                            String name = fileSnapshot.child("name0").getValue(String.class);
                            String uri = fileSnapshot.child("uri0").getValue(String.class);
                            String grandTotal = fileSnapshot.child("grandTotal0").getValue(String.class);
                            orderid = fileSnapshot.child("orderid0").getValue(String.class);
                            ordered = fileSnapshot.child("orderd").getValue(boolean.class);
                            delivered = fileSnapshot.child("delivered").getValue(boolean.class);
                            String username=fileSnapshot.child("username").getValue(String.class);

                            if (!delivered && !uniqueOrders.containsKey(orderid)) {
                                Fileinmodel pdfFile = new Fileinmodel(name, uri, grandTotal, orderid, delivered,username);
                                uniqueOrders.put(orderid, pdfFile);
                            }
                        }
                    }
                }
                updateUI();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                progressBar.setVisibility(View.GONE);
                txt.setVisibility(View.VISIBLE);
                txt.setText("Error Loading Orders");
            }
        });

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (debounceHandler != null && searchRunnable != null) {
                    debounceHandler.removeCallbacks(searchRunnable);
                }

            }

            @Override
            public void afterTextChanged(Editable s) {
                searchRunnable = () -> filterOrders(s.toString());
                debounceHandler.postDelayed(searchRunnable, 300);
            }
        });

        return view;
    }

    private void updateUI() {
        progressBar.setVisibility(View.GONE);

        if (uniqueOrders.isEmpty()) {
            txt.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        } else {
            txt.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
            displaypdfs();
        }
    }

    private void displaypdfs() {
        List<Fileinmodel> uniqueOrdersList = new ArrayList<>(uniqueOrders.values());
        adapter = new orderadaptortadmin(uniqueOrdersList, getContext());
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
    }

    private void filterOrders(String searchText) {
        List<Fileinmodel> filteredList = new ArrayList<>();
        for (Fileinmodel order : uniqueOrders.values()) {
            if (order.getOrderid0().toLowerCase().contains(searchText.toLowerCase())) {
                filteredList.add(order);
            }
        }
        if (filteredList.isEmpty()) {
            recyclerView.setVisibility(View.GONE);
            txt.setVisibility(View.VISIBLE);
        } else {
            recyclerView.setVisibility(View.VISIBLE);
            txt.setVisibility(View.GONE);
        }

        adapter.updateList(filteredList);
        adapter.updateList(filteredList);

    }
}
