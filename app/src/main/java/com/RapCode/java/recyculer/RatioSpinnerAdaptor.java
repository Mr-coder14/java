package com.RapCode.java.recyculer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

import com.RapCode.java.R;

public class RatioSpinnerAdaptor extends ArrayAdapter<String> {
    private Context context;
    private String[] ratios;
    private int[] previewImages = {
            R.drawable.onebyone,
            R.drawable.onebytwo
    };

    public RatioSpinnerAdaptor(Context context, String[] ratios) {
        super(context, R.layout.ratio_spinner_item, ratios);
        this.context = context;
        this.ratios = ratios;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position, convertView, parent, false);
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position, convertView, parent, true);
    }

    private View getCustomView(int position, View convertView, ViewGroup parent, boolean isDropDown) {
        ViewHolder holder;

        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.ratio_spinner_item, parent, false);

            holder = new ViewHolder();
            holder.textView = convertView.findViewById(R.id.ratioText);
            holder.imageView = convertView.findViewById(R.id.ratioIcon);
            holder.previewBtn = convertView.findViewById(R.id.ratioPreviewBtn);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.textView.setText(ratios[position]);
        holder.imageView.setImageResource(previewImages[position]);

        // Only show preview button in dropdown
        if (holder.previewBtn != null) {
            holder.previewBtn.setVisibility(isDropDown ? View.VISIBLE : View.GONE);
        }

        // Set up preview button click listener
        if (isDropDown && holder.previewBtn != null) {
            holder.previewBtn.setOnClickListener(v -> {
                v.requestFocus(); // Prevent spinner selection
                showPreviewDialog(position);
            });
        }

        return convertView;
    }


    private void showPreviewDialog(int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View dialogView = LayoutInflater.from(context).inflate(R.layout.imgpreview, null);

        ImageView previewImage = dialogView.findViewById(R.id.ratioPreviewImage);
        TextView titleText = dialogView.findViewById(R.id.ratioPreviewTitle);

        previewImage.setImageResource(previewImages[position]);
        titleText.setText(ratios[position] + " Ratio Preview");

        builder.setView(dialogView);
        builder.setPositiveButton("Close", (dialog, which) -> dialog.dismiss());

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private static class ViewHolder {
        TextView textView;
        ImageView imageView;
        ImageButton previewBtn;
    }
}
