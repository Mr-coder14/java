package com.example.java;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.java.databinding.ActivityBookFormApplicationBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class BookFormApplication extends AppCompatActivity {
    private ActivityBookFormApplicationBinding binding;


    // Firebase instances


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityBookFormApplicationBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();



        binding.backbtnbook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        binding.buttonSubmitg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateForm()) {
                    submitForm();
                }
            }
        });
        setContentView(view);
    }

    private boolean validateForm() {
        boolean isValid = true;

        if (binding.editTextBookName.getText().toString().trim().isEmpty()) {
            binding.editTextBookName.setError("Book name is required");
            isValid = false;
        }

        if (binding.editTextAuthorName.getText().toString().trim().isEmpty()) {
            binding.editTextAuthorName.setError("Author name is required");
            isValid = false;
        }

        if (binding.editTextLaunchedYear.getText().toString().trim().isEmpty()) {
            binding.editTextLaunchedYear.setError("Launched year is required");
            isValid = false;
        }

        return isValid;
    }

    private void submitForm() {
        String bookName = binding.editTextBookName.getText().toString().trim();
        String authorName = binding.editTextAuthorName.getText().toString().trim();
        String launchedYear = binding.editTextLaunchedYear.getText().toString().trim();
        String isbn = binding.editTextISBN.getText().toString().trim();
        String publisher = binding.editTextPublisher.getText().toString().trim();
        String price = binding.editTextPrice.getText().toString().trim();
        String description = binding.editTextDescription.getText().toString().trim();

        // Get the current date and time
        SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        SimpleDateFormat sdfTime = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
        String orderDate = sdfDate.format(new Date());
        String orderTime = sdfTime.format(new Date());

        // Create a BookModel object
        BookModel book = new BookModel(bookName, authorName, launchedYear, isbn, publisher, price, description, orderDate, orderTime);

        // Get the current user's UID
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        // Get a reference to the Firebase Realtime Database
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("books");

        // Save the book under the user's UID and book name
        databaseReference.child(uid).child(bookName).setValue(book).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(BookFormApplication.this, "Book submitted successfully!", Toast.LENGTH_LONG).show();
                    clearForm();
                    startActivity(new Intent(BookFormApplication.this,suceesanimation.class));
                    finish();
                } else {
                    Toast.makeText(BookFormApplication.this, "Failed to submit book. Try again.", Toast.LENGTH_LONG).show();
                }
            }
        });
    }


    private void clearForm() {
        binding.editTextBookName.setText("");
        binding.editTextAuthorName.setText("");
        binding.editTextLaunchedYear.setText("");
        binding.editTextISBN.setText("");
        binding.editTextPublisher.setText("");
        binding.editTextPrice.setText("");
        binding.editTextDescription.setText("");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}
