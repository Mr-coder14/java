package com.example.java;

import static android.app.Activity.RESULT_OK;

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
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
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

import java.io.File;

public class home_fragment extends Fragment implements NavigationView.OnNavigationItemSelectedListener {
    private static final int PICK_PDF_REQUEST = 1;
    private Context context;
    private EditText filename;
    private DrawerLayout drawerLayout;
    private FirebaseAuth auth;
    private ProgressBar progressBarf;
    private FirebaseUser user;
    private Toolbar toolbar;
    private TextView username, email;
    private LinearLayout linearLayout;
    private DatabaseReference usersRef;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.home_fragment, container, false);
        initializeViews(view);
        setupListeners();
        return view;
    }

    private void initializeViews(View view) {
        filename = view.findViewById(R.id.selectpdf);
        drawerLayout = view.findViewById(R.id.drawer_layout);
        NavigationView navigationView = view.findViewById(R.id.nav_view);
        View headerView = navigationView.getHeaderView(0);
        progressBarf = headerView.findViewById(R.id.progressBarhfragment);
        linearLayout = headerView.findViewById(R.id.userInfoLayout);
        username = headerView.findViewById(R.id.header_username);
        email = headerView.findViewById(R.id.headeremail);
        context = getContext();
    }

    private void setupListeners() {
        filename.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openfile();
            }
        });
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        progressBarf.setVisibility(View.VISIBLE);
        linearLayout.setVisibility(View.GONE);
        setupToolbar(view);
        setupDrawer(view);
        loadUserData();
    }

    private void setupToolbar(View view) {
        toolbar = view.findViewById(R.id.toolbar);
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        if (activity != null) {
            activity.setSupportActionBar(toolbar);
            activity.getSupportActionBar().setDisplayShowTitleEnabled(false);
            LayoutInflater inflater = LayoutInflater.from(activity);
            View customTitleView = inflater.inflate(R.layout.coustom_text_tbar, toolbar, false);
            toolbar.addView(customTitleView);
        }
    }

    private void setupDrawer(View view) {
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                getActivity(), drawerLayout, toolbar, R.string.open_nav, R.string.close_nav);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = view.findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    private void loadUserData() {
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        if (user != null) {
            usersRef = FirebaseDatabase.getInstance().getReference("users").child(user.getUid());
            usersRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        User userData = dataSnapshot.getValue(User.class);
                        if (userData != null) {
                            username.setText(userData.getName());
                            email.setText(userData.getEmail());
                        }
                        progressBarf.setVisibility(View.GONE);
                        linearLayout.setVisibility(View.VISIBLE);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    progressBarf.setVisibility(View.GONE);
                    linearLayout.setVisibility(View.VISIBLE);
                }
            });
        }
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
            handleFileSelection(data.getData());
        }
    }

    private void handleFileSelection(Uri uri) {
        String displayName = getFileName(uri);
        filename.setText(displayName);
        openPreviewActivity(uri, displayName);
    }

    private String getFileName(Uri uri) {
        String displayName = null;
        if (uri.toString().startsWith("content://")) {
            try (Cursor cursor = getContext().getContentResolver().query(uri, null, null, null, null)) {
                if (cursor != null && cursor.moveToFirst()) {
                    displayName = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                }
            }
        } else if (uri.toString().startsWith("file://")) {
            displayName = new File(uri.toString()).getName();
        }
        return displayName;
    }

    private void openPreviewActivity(Uri uri, String fileName) {
        Intent intent = new Intent(getActivity(), preview_orderActivity.class);
        intent.setData(uri);
        intent.putExtra("fileName", fileName);
        startActivity(intent);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        drawerLayout.closeDrawer(GravityCompat.START);
        int itemid = item.getItemId();
        if (itemid == R.id.nav_home) {
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
        }
        return false;
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
