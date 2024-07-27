package com.example.myapplication.fragments;

import android.os.Bundle;
import android.util.Log;
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
import com.example.myapplication.adapters.GenreAdapter;
import com.example.myapplication.databinding.FragmentArtistListBinding;
import com.example.myapplication.databinding.FragmentGenreListBinding;
import com.example.myapplication.sqlite.helper.DatabaseHelper;
import com.example.myapplication.sqlite.model.Artist;
import com.example.myapplication.sqlite.model.Genre;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ArtistListFragment extends Fragment implements ArtistAdapter.EditClickListener, ArtistAdapter.DeleteClickListener   {

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private Artist mArtist;
    private String ownerRefId;

    private boolean isEdit;

    private long editId;
    private FragmentArtistListBinding binding;

    private EditText nameEditText;

    private EditText searchGenreText;

    private EditText searchNameText;
    private Button addButton;

    private Button searchButton;

    private Button resetButton;
    private RecyclerView recyclerView;
    private List<Artist> itemList;
    private ArtistAdapter itemAdapter;

    private Spinner spinnerG;

    DatabaseHelper DB;

    public ArtistListFragment() {
        // Required empty public constructor
    }
    public static ArtistListFragment newInstance(String param1, String param2) {
        ArtistListFragment fragment = new ArtistListFragment();
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


    public void onEditClick(Artist artist) {
        nameEditText.setText(artist.getName());
        this.isEdit = true;
        this.editId = artist.getId();
    }

    public void onDeleteClick(Artist artist) {
        DB.deleteArtist(artist.getId());
        DB.updateArtistToNoneForAll(artist.getName());
    }

    public void loadItems(){
        itemList = new ArrayList<>();
        List<Artist> artists = DB.getAllArtists();
        itemList.addAll(artists);
        itemAdapter = new ArtistAdapter(itemList, this, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(itemAdapter);
    }

    public void searchItems(String nameFilter, String genreFilter){
        itemList = new ArrayList<>();
        List<Artist> artists = DB.getAllArtists();
        if(!nameFilter.isEmpty()){
            artists = artists.stream()
                    .filter(artist -> artist.getName().equalsIgnoreCase(nameFilter))
                    .collect(Collectors.toList());
        }
        if(!genreFilter.isEmpty()){
            artists = artists.stream()
                    .filter(artist -> artist.getGenre().equalsIgnoreCase(genreFilter))
                    .collect(Collectors.toList());
        }

        itemList.addAll(artists);
        itemAdapter = new ArtistAdapter(itemList, this, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(itemAdapter);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        fillSpinner(view);

    }

    public void fillSpinner(@NonNull View view) {
                    ArrayList<Genre> unfilteredList  = new ArrayList<>();
                    List<Genre> genres = DB.getAllGenres();
                    unfilteredList.addAll(genres);
                    ArrayList<String> typeGList = new ArrayList<>();
                    for(Genre genre: genres) {
                        typeGList.add(genre.getName());
                    }

                    Spinner spinnerEventType = view.findViewById(R.id.spinnerArtistG);
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, typeGList.toArray(new String[0]));
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinnerEventType.setAdapter(adapter);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentArtistListBinding.inflate(inflater, container, false);
        View view = binding.getRoot();


        nameEditText = view.findViewById(R.id.editTextNameArtist);
        searchGenreText = view.findViewById(R.id.editTextNameASBGenre);
        searchNameText = view.findViewById(R.id.editTextNameSNA);
        addButton = view.findViewById(R.id.buttonAddArtist);
        searchButton = view.findViewById(R.id.buttonSearchArtist);
        resetButton = view.findViewById(R.id.buttonResetArtist);

        spinnerG = view.findViewById(R.id.spinnerArtistG);


        recyclerView = view.findViewById(R.id.recyclerView);
        loadItems();
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = nameEditText.getText().toString();
                String typeG = "";
                if (spinnerG.getSelectedItem() != null) {
                    typeG = spinnerG.getSelectedItem().toString();
                }

                if (!name.isEmpty() && !name.equalsIgnoreCase("none")) {

                    if(isEdit){

                        Artist item = new Artist(editId,name,  typeG);
                        Artist oldArtist = DB.getArtistById(editId);
                        DB.updateArtistNameForAll(oldArtist.getName(), name);
                        DB.updateArtist(item);
                        loadItems();
                        nameEditText.setText("");
                        isEdit = false;
                        editId = -1;
                    }


                    Artist item = new Artist(name, typeG);
                    DB.createArtist(item);
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
                String filter2 = searchGenreText.getText().toString();
                searchItems(filter1, filter2);

            }
        });

        return view;
    }




}
