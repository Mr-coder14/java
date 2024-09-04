package com.example.java;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class bookorderactivityuser extends AppCompatActivity {
    private ImageButton btn;

    private TextView bookNameTextView, authorNameTextView, launchedYearTextView, isbnTextView, publisherTextView, priceTextView, descriptionTextView,time,date,name,phno;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookorderactivityuser);


        btn = findViewById(R.id.backbtnbookorders1);
        bookNameTextView = findViewById(R.id.bookname);
        authorNameTextView = findViewById(R.id.authorname);
        launchedYearTextView = findViewById(R.id.bLaunchedYear);
        isbnTextView = findViewById(R.id.ISBN);
        publisherTextView = findViewById(R.id.Publisher);
        priceTextView = findViewById(R.id.Price);
        descriptionTextView = findViewById(R.id.Description);
        time=findViewById(R.id.orderedtime);
        date=findViewById(R.id.Ordereddate);
        name=findViewById(R.id.nameuser);phno=findViewById(R.id.phnouser);




        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });



        String bookName = getIntent().getStringExtra("bookName");
        String authorName = getIntent().getStringExtra("authorName");
        String launchedYear = getIntent().getStringExtra("launchedYear");
        String isbn = getIntent().getStringExtra("isbn");
        String publisher = getIntent().getStringExtra("publisher");
        String price = getIntent().getStringExtra("price");
        String description = getIntent().getStringExtra("description");
        String orderdate =getIntent().getStringExtra("orderdate");
        String ordertime =getIntent().getStringExtra("ordertime");
        String name3=getIntent().getStringExtra("name");String phno3=getIntent().getStringExtra("phno");


        bookNameTextView.setText(bookName);
        authorNameTextView.setText(authorName);
        launchedYearTextView.setText(launchedYear);
        isbnTextView.setText(isbn);
        publisherTextView.setText(publisher);
        priceTextView.setText(price);
        descriptionTextView.setText(description);
        time.setText(ordertime);
        date.setText(orderdate);
        name.setText(name3);
        phno.setText(phno3);

    }
}
