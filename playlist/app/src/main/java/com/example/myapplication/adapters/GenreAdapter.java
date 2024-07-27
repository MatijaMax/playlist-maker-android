package com.example.myapplication.adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.sqlite.model.Genre;

import java.util.List;

public class GenreAdapter extends RecyclerView.Adapter<GenreAdapter.ItemViewHolder> {

    private List<Genre> itemList;

    private EditClickListener editClickListener;
    private DeleteClickListener deleteClickListener;



    public interface EditClickListener {
        void onEditClick(Genre item);
    }

    public interface DeleteClickListener {
        void onDeleteClick(Genre item);
    }

    public GenreAdapter(List<Genre> itemList, EditClickListener editClickListener, DeleteClickListener deleteClickListener) {
        this.itemList = itemList;
        this.editClickListener = editClickListener;
        this.deleteClickListener = deleteClickListener;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_genre, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        Genre item = itemList.get(position);
        holder.nameTextView.setText(item.getName());
        //
        Log.e("MY APP GENRE", item.getName());

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

        holder.editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editClickListener != null) {
                    editClickListener.onEditClick(item);
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
        Button editButton;
        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.textViewNameGenre);
            removeButton = itemView.findViewById(R.id.buttonRemoveGenre);
            editButton = itemView.findViewById(R.id.buttonEditGenre);
        }
    }
}
