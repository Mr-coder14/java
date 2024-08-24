package com.example.java;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.java.databinding.ActivityBookFormApplicationBinding;

public class BookFormApplication extends AppCompatActivity {
    private ActivityBookFormApplicationBinding binding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityBookFormApplicationBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
    }
    private void setupSubmitButton() {
        binding.buttonSubmitg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateForm()) {
                    submitForm();
                }
            }
        });
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


        String message = "Book: " + bookName + " by " + authorName + " submitted successfully!";
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();


        clearForm();
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