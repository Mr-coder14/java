package com.RapCode.java.recyculer;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.RapCode.java.ComboDrafter;
import com.RapCode.java.ComboOfferpen;
import com.RapCode.java.Combopencil;
import com.RapCode.java.Mycart;
import com.RapCode.java.Productpreviewa;
import com.RapCode.java.R;
import com.RapCode.java.cart;

import java.util.ArrayList;

public class Itemlistadaptormycart extends RecyclerView.Adapter<Itemlistadaptormycart.ViewHolder> {
    private ArrayList<ProductDetails> productDetails;
    private Activity activity;
    public ArrayList<ProductDetails> selectedItems = new ArrayList<>();

    public boolean isSelectionMode = false;

    public Itemlistadaptormycart(ArrayList<ProductDetails> productDetails, Activity activity) {
        this.productDetails = productDetails;
        this.activity = activity;
    }

    @NonNull
    @Override
    public Itemlistadaptormycart.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.itemlistmycart, parent, false);
        return new Itemlistadaptormycart.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Itemlistadaptormycart.ViewHolder holder, int position) {
        ProductDetails p = productDetails.get(position);

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (!isSelectionMode) {
                    isSelectionMode = true;
                    toggleSelection(holder, p);
                }
                return true;
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isSelectionMode) {
                    toggleSelection(holder, p);
                } else {
                    if (p.getProductimage()==R.drawable.pencombo) {
                        Intent intent = new Intent(activity, ComboOfferpen.class);
                        intent.putExtra("comboProduct", p);
                        activity.startActivity(intent);
                    } else if (p.getProductimage()==R.drawable.pencilcombo) {
                        Intent intent = new Intent(activity, Combopencil.class);
                        intent.putExtra("comboProduct", p);
                        activity.startActivity(intent);

                    } else if (p.getProductimage()==R.drawable.draftercombo) {
                        Intent intent = new Intent(activity, ComboDrafter.class);
                        intent.putExtra("comboProduct", p);
                        activity.startActivity(intent);
                    } else {
                        Intent intent=new Intent(activity, Productpreviewa.class);
                        intent.putExtra("product",p);
                        activity.startActivity(intent);
                    }

                }
            }
        });



        holder.plusbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int count = p.getQty() + 1;
                p.setQty(count);
                holder.qty.setText(String.valueOf(count));
                holder.qty1.setText(String.valueOf(count));
                cart.getInstance().updateItem(p);
                ((Mycart) activity).calculateAndDisplayTotals();
            }
        });

        holder.minusbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int count = p.getQty();
                if (count > 1) {
                    count--;
                    p.setQty(count);
                    holder.qty.setText(String.valueOf(count));
                    holder.qty1.setText(String.valueOf(count));
                    cart.getInstance().updateItem(p);
                    ((Mycart) activity).calculateAndDisplayTotals();
                }
            }
        });

        holder.bind(p);

        if (selectedItems.contains(p)) {
            holder.itemView.setBackgroundResource(R.drawable.graybackround);
        } else {
            holder.itemView.setBackgroundResource(R.drawable.whitebg_profile);
        }
    }

    private void toggleSelection(ViewHolder holder, ProductDetails item) {
        if (selectedItems.contains(item)) {
            selectedItems.remove(item);
        } else {
            selectedItems.add(item);
        }
        notifyItemChanged(holder.getAdapterPosition());

        if (selectedItems.isEmpty()) {
            isSelectionMode = false;
        }


        if (activity instanceof Mycart) {
            ((Mycart) activity).onSelectionChanged(selectedItems.size());
        }
    }

    public void deleteSelectedItems() {
        ArrayList<ProductDetails> itemsToRemove = new ArrayList<>(selectedItems);

        for (ProductDetails item : itemsToRemove) {
            int index = productDetails.indexOf(item);
            if (index != -1) {
                productDetails.remove(index);
                notifyItemRemoved(index);
            }
        }

        cart.getInstance().removeItems(itemsToRemove);
        selectedItems.clear();
        isSelectionMode = false;
        ((Mycart) activity).calculateAndDisplayTotals();
    }


    @Override
    public int getItemCount() {
        return productDetails.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView name, qty, qty1, amt, minusbtn, plusbtn;
        private ImageView img;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.productNamemycart);
            qty = itemView.findViewById(R.id.quantityValuemycart);
            qty1 = itemView.findViewById(R.id.quantityValuemycart1);
            amt = itemView.findViewById(R.id.productPricemycart);
            img = itemView.findViewById(R.id.productImagemycart);
            minusbtn = itemView.findViewById(R.id.quantityMinusmycart);
            plusbtn = itemView.findViewById(R.id.quantityPlusmycart);
        }

        public void bind(ProductDetails p) {
            name.setText(p.getProductname());
            amt.setText("₹ " + p.getProductamt());
            img.setImageResource(p.getProductimage());
            qty.setText(String.valueOf(p.getQty()));
            qty1.setText(String.valueOf(p.getQty()));
        }
    }
}
