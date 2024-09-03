package com.example.java.recyculer;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.java.BookModel;
import com.example.java.R;

import java.util.List;

public class BookOrderAdapter extends RecyclerView.Adapter<BookOrderAdapter.BookOrderViewHolder> {

    private List<BookModel> bookList;

    public BookOrderAdapter(List<BookModel> bookList) {
        this.bookList = bookList;
    }

    @NonNull
    @Override
    public BookOrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_book_order, parent, false);
        return new BookOrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BookOrderViewHolder holder, int position) {
        BookModel book = bookList.get(position);
        holder.textViewBookName.setText(book.getBookName());
        holder.textViewAuthorName.setText(book.getAuthorName());
        holder.textViewOrderDate.setText(book.getOrderDate());

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

