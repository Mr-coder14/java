package com.example.java.recyculer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.java.R;

import java.util.ArrayList;
import java.util.List;

public class ProductSearchAdapter extends ArrayAdapter<ProductDetails> {
    private LayoutInflater inflater;
    private ArrayList<ProductDetails> productDetails=new ArrayList<>();

    public ProductSearchAdapter(Context context, List<ProductDetails> products) {
        super(context, 0, products);
        inflater = LayoutInflater.from(context);
        this.productDetails = new ArrayList<>(products);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.search_item_layout, parent, false);
        }

        ImageView imageView = convertView.findViewById(R.id.product_image);
        TextView textView = convertView.findViewById(R.id.product_name);

        ProductDetails product = getItem(position);
        if (product != null) {
            imageView.setImageResource(product.getProductimage());
            textView.setText(product.getProductname());
        }

        return convertView;
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults results = new FilterResults();
                List<ProductDetails> suggestions = new ArrayList<>();

                if (constraint != null) {
                    String filterPattern = constraint.toString().toLowerCase().trim();
                    for (ProductDetails product : getAllProducts()) {
                        if (product.getProductname().toLowerCase().contains(filterPattern)) {
                            suggestions.add(product);
                        }
                    }
                }

                results.values = suggestions;
                results.count = suggestions.size();
                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                clear();
                addAll((List<ProductDetails>) results.values);
                notifyDataSetChanged();
            }
        };
    }

    private List<ProductDetails> getAllProducts() {

        return productDetails;
    }
}
