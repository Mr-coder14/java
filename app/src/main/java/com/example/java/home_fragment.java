package com.example.java;

import static android.app.Activity.RESULT_OK;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;

public class home_fragment extends Fragment implements NavigationView.OnNavigationItemSelectedListener {
    private static final int PICK_PDF_REQUEST = 1;
    private Context context;
    private EditText filename;
    private DrawerLayout drawerLayout;
    private FirebaseAuth auth;
    private FirebaseUser user;
    private Fragment fragment;
    private Toolbar toolbar;
    private TextView username, email;
    DatabaseReference usersRef;
    private String diplayname = null;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.home_fragment, container, false);
        filename = view.findViewById(R.id.selectpdf);
        context = getContext();

        filename.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openfile();
            }
        });
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        drawerLayout = view.findViewById(R.id.drawer_layout);
        toolbar = view.findViewById(R.id.toolbar);
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        // Get the NavigationView and its header view
        NavigationView navigationView = view.findViewById(R.id.nav_view);
        View headerView = navigationView.getHeaderView(0);
        username = headerView.findViewById(R.id.header_username);
        email = headerView.findViewById(R.id.headeremail);

        AppCompatActivity activity = (AppCompatActivity) getActivity();
        if (activity != null) {
            activity.setSupportActionBar(toolbar);
            activity.getSupportActionBar().setDisplayShowTitleEnabled(false);

            LayoutInflater inflater = LayoutInflater.from(activity);
            View customTitleView = inflater.inflate(R.layout.coustom_text_tbar, toolbar, false);
            toolbar.addView(customTitleView);
        }

        // Initialize Firebase Database reference to the user
        usersRef = FirebaseDatabase.getInstance().getReference("users").child(user.getUid());
        usersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    User userData = dataSnapshot.getValue(User.class);
                    if (userData != null) {
                        username.setText(userData.getName());
                        email.setText(userData.getEmail());
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle possible errors
            }
        });

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                getActivity(), drawerLayout, toolbar, R.string.open_nav, R.string.close_nav);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
    }

    private void openfile() {
        Intent intent = new Intent();
        intent.setType("application/pdf");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select File"), PICK_PDF_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_PDF_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri Uri = data.getData();
            String uri = Uri.toString();
            File myfile = new File(uri);
            String path = myfile.getAbsolutePath();

            if (uri.startsWith("content://")) {
                Cursor cursor = null;
                try {
                    cursor = getContext().getContentResolver().query(Uri, null, null, null, null);
                    if (cursor != null && cursor.moveToFirst()) {
                        diplayname = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                    }
                } finally {
                    cursor.close();
                }
            } else if (uri.startsWith("file://")) {
                diplayname = myfile.getName();
            }
            filename.setText(diplayname);
            openPreviewActivity(Uri, filename.getText().toString());
        }
    }

    private void openPreviewActivity(Uri uri, String fileName) {
        Intent intent = new Intent(getActivity(), preview_orderActivity.class);
        intent.setData(uri);
        intent.putExtra("fileName", fileName);
        startActivity(intent);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int itemid = item.getItemId();
        if (itemid == R.id.nav_home) {
            drawerLayout.closeDrawer(GravityCompat.START);
            return true;
        } else if (itemid == R.id.nav_profile) {

            ((MainActivity) getActivity()).selectBottomNavItem(R.id.profilebottom);
            return true;
        } else if (itemid == R.id.nav_logout) {
            logout();
            return true;
        } else if (itemid == R.id.nav_history) {

            ((MainActivity) getActivity()).selectBottomNavItem(R.id.Historybottom);
            return true;
        } else {
            Toast.makeText(context, "About Clicked", Toast.LENGTH_SHORT).show();
            return true;
        }

    }

    private void logout() {
        if (user != null) {
            auth.signOut();
            Intent intent = new Intent(getActivity(), loginactivity.class);
            startActivity(intent);
            getActivity().finish();
        }
    }
}
