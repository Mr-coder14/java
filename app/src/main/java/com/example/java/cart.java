package com.example.java;

import androidx.annotation.NonNull;

import com.example.java.recyculer.ProductDetails;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class cart {
    private static cart instance;
    private FirebaseAuth auth;
    private String userid;
    private ArrayList<ProductDetails> items;
    private DatabaseReference databaseReference;

    private cart() {
        items = new ArrayList<>();
        auth = FirebaseAuth.getInstance();
        userid = auth.getCurrentUser().getUid();
        if (userid != null) {
            databaseReference = FirebaseDatabase.getInstance().getReference("userscart").child(userid);
        }
    }

    public static synchronized cart getInstance() {
        if (instance == null) {
            instance = new cart();
        }
        return instance;
    }

    public void removeItem(ProductDetails itemsToRemove) {
        items.remove(itemsToRemove);
        databaseReference.child(itemsToRemove.getKey()).removeValue();
    }

    public void updateItem(ProductDetails product) {
        databaseReference.child(product.getKey()).setValue(product);
    }

    public void removeItems(ArrayList<ProductDetails> itemsToRemove) {
        for (ProductDetails item : itemsToRemove) {
            removeItem(item);
        }
    }

    public void addItem(ProductDetails product, CartAddCallback callback) {
        // Check if the item already exists in the cart
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                boolean itemExists = false;
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    ProductDetails existingProduct = dataSnapshot.getValue(ProductDetails.class);
                    if (existingProduct != null && existingProduct.getProductname().equals(product.getProductname())) {
                        itemExists = true;
                        break;
                    }
                }

                if (itemExists) {
                    callback.onItemAlreadyExists();
                } else {
                    String key = databaseReference.push().getKey();
                    product.setKey(key);
                    databaseReference.child(key).setValue(product).addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            callback.onItemAdded();
                        } else {
                            callback.onItemAddFailed();
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                callback.onItemAddFailed();
            }
        });
    }

    public ArrayList<ProductDetails> getItems() {
        return items;
    }

    public void clear() {
        items.clear();
    }

    public interface CartAddCallback {
        void onItemAlreadyExists();

        void onItemAdded();

        void onItemAddFailed();
    }
}
