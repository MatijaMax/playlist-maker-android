package com.example.myapplication.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.adapters.ArtistAdapter;
import com.example.myapplication.adapters.SongAdapter;
import com.example.myapplication.databinding.FragmentArtistListBinding;
import com.example.myapplication.databinding.FragmentSongListBinding;
import com.example.myapplication.sqlite.helper.DatabaseHelper;
import com.example.myapplication.sqlite.model.Artist;
import com.example.myapplication.sqlite.model.Genre;
import com.example.myapplication.sqlite.model.Song;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class SongListFragment extends Fragment implements SongAdapter.EditClickListener, SongAdapter.DeleteClickListener   {

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private Song mSong;
    private String ownerRefId;

    private boolean isEdit;

    private long editId;
    private FragmentSongListBinding binding;

    private EditText nameEditText;

    private EditText searchGenreText;

    private EditText searchArtistText;

    private EditText searchNameText;
    private Button addButton;

    private Button searchButton;

    private Button resetButton;
    private RecyclerView recyclerView;
    private List<Song> itemList;
    private SongAdapter itemAdapter;

    private Spinner spinnerG;

    private Spinner spinnerA;

    DatabaseHelper DB;

    public SongListFragment() {
        // Required empty public constructor
    }
    public static SongListFragment newInstance(String param1, String param2) {
        SongListFragment fragment = new SongListFragment();
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
    }


    public void onEditClick(Song song) {
        nameEditText.setText(song.getName());
        this.isEdit = true;
        this.editId = song.getId();
    }

    public void onDeleteClick(Song song) {
        DB.deleteSong(song.getId());
        DB.deleteHelpersBySongId(song.getId());
    }

    public void loadItems(){
        itemList = new ArrayList<>();
        List<Song> songs = DB.getAllSongs();
        itemList.addAll(songs);
        itemAdapter = new SongAdapter(itemList, this, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(itemAdapter);
    }

    public void searchItems(String nameFilter, String artistFilter, String genreFilter){
        itemList = new ArrayList<>();
        List<Song> songs = DB.getAllSongs();
        if(!nameFilter.isEmpty()){
            songs = songs.stream()
                    .filter(song -> song.getName().equalsIgnoreCase(nameFilter))
                    .collect(Collectors.toList());
        }

        if(!artistFilter.isEmpty()){
            songs = songs.stream()
                    .filter(song -> song.getArtist().equalsIgnoreCase(artistFilter))
                    .collect(Collectors.toList());
        }

        if(!genreFilter.isEmpty()){
            songs = songs.stream()
                    .filter(song -> song.getGenre().equalsIgnoreCase(genreFilter))
                    .collect(Collectors.toList());
        }

        itemList.addAll(songs);
        itemAdapter = new SongAdapter(itemList, this, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(itemAdapter);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        fillSpinners(view);

    }

    public void fillSpinners(@NonNull View view) {
                    ArrayList<Genre> unfilteredListG  = new ArrayList<>();
                    List<Genre> genres = DB.getAllGenres();
                    unfilteredListG.addAll(genres);
                    ArrayList<String> typeGList = new ArrayList<>();
                    for(Genre genre: genres) {
                        typeGList.add(genre.getName());
                    }

                    Spinner spinnerEventTypeG = view.findViewById(R.id.spinnerSongG);
                    ArrayAdapter<String> adapterG = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, typeGList.toArray(new String[0]));
                    adapterG.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinnerEventTypeG.setAdapter(adapterG);


                    ArrayList<Artist> unfilteredListA  = new ArrayList<>();
                    List<Artist> artists = DB.getAllArtists();
                    unfilteredListA.addAll(artists);
                    ArrayList<String> typeAList = new ArrayList<>();
                    for(Artist artist: artists) {
                        typeAList.add(artist.getName());
                    }

                    Spinner spinnerEventTypeA = view.findViewById(R.id.spinnerSongA);
                    ArrayAdapter<String> adapterA = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, typeAList.toArray(new String[0]));
                    adapterA.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinnerEventTypeA.setAdapter(adapterA);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentSongListBinding.inflate(inflater, container, false);
        View view = binding.getRoot();


        nameEditText = view.findViewById(R.id.editTextNameSong);
        searchArtistText = view.findViewById(R.id.editTextNameSSArtist);
        searchGenreText = view.findViewById(R.id.editTextNameSSGenre);
        searchNameText = view.findViewById(R.id.editTextNameSNS);
        addButton = view.findViewById(R.id.buttonAddSong);
        searchButton = view.findViewById(R.id.buttonSearchSong);
        resetButton = view.findViewById(R.id.buttonResetSong);

        spinnerG = view.findViewById(R.id.spinnerSongG);
        spinnerA = view.findViewById(R.id.spinnerSongA);


        recyclerView = view.findViewById(R.id.recyclerView);
        loadItems();
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = nameEditText.getText().toString();
                String typeG = "";
                String typeA = "";
                if (spinnerG.getSelectedItem() != null) {
                    typeG = spinnerG.getSelectedItem().toString();
                }

                if (spinnerA.getSelectedItem() != null) {
                    typeA = spinnerA.getSelectedItem().toString();
                }

                if (!name.isEmpty() && !name.equalsIgnoreCase("none")) {

                    if(isEdit){

                        Song item = new Song(editId, name, typeA, typeG);
                        DB.updateSong(item);
                        loadItems();
                        nameEditText.setText("");
                        isEdit = false;
                        editId = -1;
                    }


                    Song item = new Song(name, typeA, typeG);
                    DB.createSong(item);
                    loadItems();
                    //itemList.add(item);
                    //itemAdapter.notifyDataSetChanged();
                    nameEditText.setText("");
                }
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
                String filter1 = searchNameText.getText().toString();
                String filter2 = searchArtistText.getText().toString();
                String filter3= searchGenreText.getText().toString();
                searchItems(filter1, filter2,  filter3);

            }
        });

        return view;
    }




}
