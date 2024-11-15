package com.RapCode.java;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class ContactusActivity extends AppCompatActivity {
    private ImageButton img;
    private TextView whatsappTextView,instagramTextView;
    private LinearLayout emailTextView;
    private LinearLayout layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contactus);

        img=findViewById(R.id.backbtncontact);
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        emailTextView = findViewById(R.id.emailid);
        whatsappTextView = findViewById(R.id.whatsapp);
        layout=findViewById(R.id.rapcde1);
        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://rapcodetech.netlify.app"));
                startActivity(browserIntent);
            }
        });
        emailTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openEmailClient("Jasaessential@gmail.com");
            }
        });
        whatsappTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Open WhatsApp group link
                String whatsappGroupLink = "https://whatsapp.com/channel/0029VaIvRoG4yltTHASfHK3T";
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(whatsappGroupLink));
                startActivity(intent);
            }
        });

        // Initialize the Instagram TextView
        instagramTextView = findViewById(R.id.instaid);
        instagramTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Open Instagram profile link
                String instagramProfileLink = "https://www.instagram.com/jasa_essential?igsh=MWVpaXJiZGhzeDZ4Ng==";
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(instagramProfileLink));
                startActivity(intent);
            }
        });

    }

    private void openEmailClient(String emailAddress) {
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:" + emailAddress));
        intent.putExtra(Intent.EXTRA_SUBJECT, "Subject of Email");
        intent.putExtra(Intent.EXTRA_TEXT, "Body of Email");
        startActivity(intent);
    }
}