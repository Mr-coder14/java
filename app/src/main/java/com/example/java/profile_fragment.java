package com.example.java;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class profile_fragment extends Fragment {
    ImageButton btnlout;
    FirebaseAuth auth;
    TextView email,name,email1,name1,phno1;
    FirebaseUser user;
    DatabaseReference usersRef;


    @SuppressLint("MissingInflatedId")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.profile_fragment,container,false);
        btnlout=view.findViewById(R.id.imageButtonlogout);
        auth=FirebaseAuth.getInstance();
        user= auth.getCurrentUser();
        name=view.findViewById(R.id.profilename);
        phno1=view.findViewById(R.id.phno11);
        email1=view.findViewById(R.id.email11);
        name1=view.findViewById(R.id.name11);
        email=view.findViewById(R.id.emailprofile);

        usersRef = FirebaseDatabase.getInstance().getReference("users").child(user.getUid());
        usersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    User userData = dataSnapshot.getValue(User.class);
                    if (userData != null) {
                        name.setText(userData.getName());
                        name1.setText(userData.getName());
                        phno1.setText(userData.getPhno());
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
        email.setText(user.getEmail());
        email1.setText(user.getEmail());

        btnlout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(user!=null)
                {
                    auth.signOut();
                    startActivity(new Intent(getActivity(), loginactivity.class));
                    getActivity().finish();
                }
            }
        });
        return view;
    }
}
