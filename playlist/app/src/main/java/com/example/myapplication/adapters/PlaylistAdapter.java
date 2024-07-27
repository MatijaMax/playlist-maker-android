package com.example.myapplication.adapters;

import static java.security.AccessController.getContext;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.FragmentTransition;
import com.example.myapplication.R;
import com.example.myapplication.fragments.HomeFragment;
import com.example.myapplication.sqlite.model.Genre;
import com.example.myapplication.sqlite.model.Playlist;

import java.util.List;

public class PlaylistAdapter extends RecyclerView.Adapter<PlaylistAdapter.ItemViewHolder> {

    private List<Playlist> itemList;

    private EditClickListener editClickListener;
    private DeleteClickListener deleteClickListener;

    private SongsClickListener songsClickListener;

    private FragmentActivity fragmentActivity;



    public interface EditClickListener {
        void onEditClick(Playlist item);
    }

    public interface DeleteClickListener {
        void onDeleteClick(Playlist item);
    }

    public interface SongsClickListener {
        void onSongsClick(Playlist item);
    }

    public PlaylistAdapter(List<Playlist> itemList, EditClickListener editClickListener, DeleteClickListener deleteClickListener, SongsClickListener songsClickListener, FragmentActivity fragmentActivity) {
        this.itemList = itemList;
        this.editClickListener = editClickListener;
        this.deleteClickListener = deleteClickListener;
        this.songsClickListener = songsClickListener;
        this.fragmentActivity = fragmentActivity;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_playlist, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        Playlist item = itemList.get(position);
        holder.nameTextView.setText(item.getName());
        //
        Log.e("MY APP PLAYLIST", item.getName());

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

        holder.songsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (songsClickListener != null) {
                    songsClickListener.onSongsClick(item);
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

        Button songsButton;
        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.textViewNamePlaylist);
            removeButton = itemView.findViewById(R.id.buttonRemovePlaylist);
            editButton = itemView.findViewById(R.id.buttonEditPlaylist);
            songsButton= itemView.findViewById(R.id.buttonSongs);
        }
    }
}
