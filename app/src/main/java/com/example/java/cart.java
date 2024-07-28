package com.example.java;

import com.example.java.recyculer.ProductDetails;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class cart {
    private static cart instance;
    private FirebaseAuth auth;
    String userid;
    private ArrayList<ProductDetails> items;
    private DatabaseReference databaseReference;

    private cart() {
        items = new ArrayList<>();
        auth=FirebaseAuth.getInstance();
        userid=auth.getCurrentUser().getUid();
        if(userid!=null){
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

    public void addItem(ProductDetails product) {
        items.add(product);
        String key = databaseReference.push().getKey();
        product.setKey(key);
        databaseReference.child(key).setValue(product);

    }

    public ArrayList<ProductDetails> getItems() {
        return items;
    }

    public void clear() {
        items.clear();
    }
}