package com.example.myapplication.adapters;

import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.sqlite.model.Artist;
import com.example.myapplication.sqlite.model.Song;

import java.util.List;

public class SongAdapter extends RecyclerView.Adapter<SongAdapter.ItemViewHolder> {

    private List<Song> itemList;

    private EditClickListener editClickListener;
    private DeleteClickListener deleteClickListener;

    public interface EditClickListener {
        void onEditClick(Song item);
    }

    public interface DeleteClickListener {
        void onDeleteClick(Song item);
    }

    public SongAdapter(List<Song> itemList, EditClickListener editClickListener, DeleteClickListener deleteClickListener) {
        this.itemList = itemList;
        this.editClickListener = editClickListener;
        this.deleteClickListener = deleteClickListener;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_song, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        Song item = itemList.get(position);
        holder.nameTextView.setText(item.getName());
        holder.nameTextViewArtist.setText(item.getArtist());
        holder.nameTextViewGenre.setText(item.getGenre());
        if (item.getGenre().equalsIgnoreCase("none") || item.getGenre().isEmpty()) {
            holder.nameTextViewGenre.setTextColor(Color.RED);
        } else {
            holder.nameTextViewGenre.setTextColor(Color.BLACK);
        }

        if (item.getArtist().equalsIgnoreCase("none") || item.getGenre().isEmpty()) {
            holder.nameTextViewArtist.setTextColor(Color.RED);
        } else {
            holder.nameTextViewArtist.setTextColor(Color.BLACK);
        }

        Log.e("MY APP SONG", item.getName());

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

        TextView nameTextViewGenre;

        TextView nameTextViewArtist;
        Button removeButton;
        Button editButton;
        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.textViewNameSong);
            nameTextViewGenre = itemView.findViewById(R.id.textViewNameSongGenre);
            nameTextViewArtist = itemView.findViewById(R.id.textViewNameSongArtist);
            removeButton = itemView.findViewById(R.id.buttonRemoveSong);
            editButton = itemView.findViewById(R.id.buttonEditSong);
        }
    }
}
