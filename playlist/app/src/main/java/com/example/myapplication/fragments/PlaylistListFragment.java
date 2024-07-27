package com.example.myapplication.fragments;

import static android.content.ContentValues.TAG;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.content.SharedPreferences;


import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.FragmentTransition;
import com.example.myapplication.R;
import com.example.myapplication.adapters.GenreAdapter;
import com.example.myapplication.adapters.PlaylistAdapter;
import com.example.myapplication.databinding.FragmentGenreListBinding;
import com.example.myapplication.databinding.FragmentPlaylistListBinding;
import com.example.myapplication.sqlite.helper.DatabaseHelper;
import com.example.myapplication.sqlite.model.Genre;
import com.example.myapplication.sqlite.model.Playlist;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class PlaylistListFragment extends Fragment implements PlaylistAdapter.EditClickListener, PlaylistAdapter.DeleteClickListener, PlaylistAdapter.SongsClickListener   {

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private Genre mPlaylist;
    private String ownerRefId;

    private boolean isEdit;

    private long editId;
    private FragmentPlaylistListBinding binding;

    private EditText nameEditText;
    private Button addButton;

    private Button searchButton;

    private Button resetButton;

    private EditText searchPlaylistText;

    private Button editButton;
    private RecyclerView recyclerView;
    private List<Playlist> itemList;
    private PlaylistAdapter itemAdapter;

    private String username;

    DatabaseHelper DB;


    public PlaylistListFragment() {
        // Required empty public constructor
    }
    public static PlaylistListFragment newInstance(String param1, String param2) {
        PlaylistListFragment fragment = new PlaylistListFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DB = new DatabaseHelper(getContext());
        this.isEdit = false;
        this.editId = -1;
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("MyAppPreferences", getContext().MODE_PRIVATE);
        this.username = sharedPreferences.getString("username", null);
        Log.i(TAG, "USER LOGGED IN PLAYLISTS :" + this.username);
    }


    public void onEditClick(Playlist p) {
        nameEditText.setText(p.getName());
        this.isEdit = true;
        this.editId = p.getId();
    }

    public void onDeleteClick(Playlist p) {
        DB.deletePlaylist(p.getId());
        DB.deleteHelpersByPlaylistId(p.getId());
    }

    public void onSongsClick(Playlist p) {
        FragmentTransition.to(HelperListFragment.newInstance(p.getName(),""), getActivity(),
                true, R.id.scroll_items_list, "helperPage");
    }

    public void loadItems(){
        itemList = new ArrayList<>();
        List<Playlist> playlists = DB.getAllPlaylists(this.username);
        itemList.addAll(playlists);
        itemAdapter = new PlaylistAdapter(itemList, this, this, this, getActivity());
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(itemAdapter);

    }

    public void searchItems(String plFilter){
        itemList = new ArrayList<>();
        List<Playlist> pls = DB.getAllPlaylists(this.username);
        List<Playlist> filteredPls = pls.stream()
                .filter(pl -> pl.getName().equalsIgnoreCase(plFilter))
                .collect(Collectors.toList());
        itemList.addAll(filteredPls);
        itemAdapter = new PlaylistAdapter(itemList, this, this, this, getActivity());
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(itemAdapter);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentPlaylistListBinding.inflate(inflater, container, false);
        View view = binding.getRoot();


        nameEditText = view.findViewById(R.id.editTextNamePlaylist);
        addButton = view.findViewById(R.id.buttonAddPlaylist);
        searchPlaylistText = view.findViewById(R.id.editTextNameSPL);
        searchButton = view.findViewById(R.id.buttonSearchPlaylist);
        resetButton = view.findViewById(R.id.buttonResetPlaylist);
        recyclerView = view.findViewById(R.id.recyclerView);
        loadItems();
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = nameEditText.getText().toString();



                    if(isEdit){
                        Playlist item = new Playlist(editId, name, username);
                        Playlist oldPlaylist = DB.getPlaylistById(editId);
                        DB.updatePlaylist(item);
                        loadItems();
                        nameEditText.setText("");
                        isEdit = false;
                        editId = -1;
                    }


                    Playlist item = new Playlist(name, username);
                    DB.createPlaylist(item);
                    loadItems();
                    //itemList.add(item);
                    //itemAdapter.notifyDataSetChanged();
                    nameEditText.setText("");
                }

        });

        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadItems();
            }
        });

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String filter = searchPlaylistText.getText().toString();
                if (!filter.isEmpty()){
                    searchItems(filter);
                }
            }
        });

        return view;
    }




}
