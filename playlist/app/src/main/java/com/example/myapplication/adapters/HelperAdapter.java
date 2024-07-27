package com.example.myapplication.adapters;


import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.graphics.Color;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.sqlite.model.Artist;
import com.example.myapplication.sqlite.model.Genre;
import com.example.myapplication.sqlite.model.Helper;

import java.util.List;

public class HelperAdapter extends RecyclerView.Adapter<HelperAdapter.ItemViewHolder> {

    private List<Helper> itemList;

    private DeleteClickListener deleteClickListener;

    public interface DeleteClickListener {
        void onDeleteClick(Helper item);
    }

    public HelperAdapter(List<Helper> itemList, DeleteClickListener deleteClickListener) {
        this.itemList = itemList;
        this.deleteClickListener = deleteClickListener;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_helper, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        Helper item = itemList.get(position);
        holder.nameTextView.setText(item.getDisplayName());
        if(item.getDisplayName().equals("") || item.getDisplayName().equalsIgnoreCase("none")){
            holder.nameTextView.setText("none");
            holder.nameTextView.setTextColor(Color.RED);
            item.setDisplayName("none");
        }

        Log.e("MY APP HELPER", item.getDisplayName());

        holder.removeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (deleteClickListener != null) {
                    deleteClickListener.onDeleteClick(item);
                }
                int position = holder.getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    itemList.remove(position);
                    notifyItemRemoved(position);
                    notifyItemRangeChanged(position, itemList.size());
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    class ItemViewHolder extends RecyclerView.ViewHolder {
        TextView nameTextView;
        Button removeButton;
        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.textViewNameHelper);
            removeButton = itemView.findViewById(R.id.buttonRemoveHelper);

        }
    }
}

