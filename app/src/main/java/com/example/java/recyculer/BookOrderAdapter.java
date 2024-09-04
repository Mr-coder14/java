package com.example.java.recyculer;


import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.java.BookModel;
import com.example.java.R;
import com.example.java.bookorderactivityuser;

import java.util.List;

public class BookOrderAdapter extends RecyclerView.Adapter<BookOrderAdapter.BookOrderViewHolder> {

    private List<BookModel> bookList;
    private Context context;

    public BookOrderAdapter(List<BookModel> bookList) {
        this.bookList = bookList;
    }

    @NonNull
    @Override
    public BookOrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context=parent.getContext();
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_book_order, parent, false);
        return new BookOrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BookOrderViewHolder holder, int position) {
        BookModel book = bookList.get(position);
        holder.textViewBookName.setText(book.getBookName());
        holder.textViewAuthorName.setText(book.getAuthorName());
        holder.textViewOrderDate.setText(book.getOrderDate());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create an Intent to start the BookOrderActivityUser
                Intent intent = new Intent(context, bookorderactivityuser.class);
                // Put book details in the Intent
                intent.putExtra("bookName", book.getBookName());
                intent.putExtra("authorName", book.getAuthorName());
                intent.putExtra("launchedYear", book.getLaunchedYear());
                intent.putExtra("isbn", book.getIsbn());
                intent.putExtra("publisher", book.getPublisher());
                intent.putExtra("price", book.getPrice());
                intent.putExtra("description", book.getDescription());
                intent.putExtra("orderdate",book.getOrderDate());
                intent.putExtra("ordertime",book.getOrderTime());
                intent.putExtra("name",book.getUsername());
                intent.putExtra("phno",book.getPhno());// Start the activity// Start the activity
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return bookList.size();
    }

    public static class BookOrderViewHolder extends RecyclerView.ViewHolder {
        TextView textViewBookName, textViewAuthorName, textViewOrderDate;

        public BookOrderViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewBookName = itemView.findViewById(R.id.textViewBookName);
            textViewAuthorName = itemView.findViewById(R.id.textViewAuthorName);
            textViewOrderDate = itemView.findViewById(R.id.textViewOrderDate);

        }
    }
}

