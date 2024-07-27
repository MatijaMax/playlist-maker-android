package com.example.myapplication.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.adapters.ArtistAdapter;
import com.example.myapplication.adapters.GenreAdapter;
import com.example.myapplication.databinding.FragmentGenreListBinding;
import com.example.myapplication.sqlite.helper.DatabaseHelper;
import com.example.myapplication.sqlite.model.Artist;
import com.example.myapplication.sqlite.model.Genre;
import com.example.myapplication.adapters.GenreAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class GenreListFragment extends Fragment implements GenreAdapter.EditClickListener, GenreAdapter.DeleteClickListener   {

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private Genre mGenre;
    private String ownerRefId;

    private boolean isEdit;

    private long editId;
    private FragmentGenreListBinding binding;

    private EditText nameEditText;
    private Button addButton;

    private Button searchButton;

    private Button resetButton;

    private EditText searchGenreText;

    private Button editButton;
    private RecyclerView recyclerView;
    private List<Genre> itemList;
    private GenreAdapter itemAdapter;

    DatabaseHelper DB;

    public GenreListFragment() {
        // Required empty public constructor
    }
    public static GenreListFragment newInstance(String param1, String param2) {
        GenreListFragment fragment = new GenreListFragment();
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


    public void onEditClick(Genre genre) {
        nameEditText.setText(genre.getName());
        this.isEdit = true;
        this.editId = genre.getId();
    }

    public void onDeleteClick(Genre genre) {
        DB.deleteGenre(genre.getId());
        DB.updateGenreToNoneForAll(genre.getName());
    }

    public void loadItems(){
        itemList = new ArrayList<>();
        List<Genre> genres = DB.getAllGenres();
        /*for (Genre genre : genres) {
            Log.d("GenreList", genre.getName());
        }*/
        itemList.addAll(genres);
        itemAdapter = new GenreAdapter(itemList, this, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(itemAdapter);

    }

    public void searchItems(String genreFilter){
        itemList = new ArrayList<>();
        List<Genre> genres = DB.getAllGenres();
        List<Genre> filteredGenres = genres.stream()
                .filter(genre -> genre.getName().equalsIgnoreCase(genreFilter))
                .collect(Collectors.toList());
        itemList.addAll(filteredGenres);
        itemAdapter = new GenreAdapter(itemList, this, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(itemAdapter);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentGenreListBinding.inflate(inflater, container, false);
        View view = binding.getRoot();


        nameEditText = view.findViewById(R.id.editTextNameGenre);
        addButton = view.findViewById(R.id.buttonAddGenre);
        searchGenreText = view.findViewById(R.id.editTextNameSNG);
        searchButton = view.findViewById(R.id.buttonSearchGenre);
        resetButton = view.findViewById(R.id.buttonResetGenre);
        recyclerView = view.findViewById(R.id.recyclerView);
        loadItems();
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = nameEditText.getText().toString();

                if (!name.isEmpty() && !name.equalsIgnoreCase("none")) {

                    if(isEdit){
                        Genre item = new Genre(editId, name);
                        Genre oldGenre = DB.getGenreById(editId);
                        DB.updateGenreNameForAll(oldGenre.getName(), name);
                        DB.updateGenre(item);
                        loadItems();
                        nameEditText.setText("");
                        isEdit = false;
                        editId = -1;
                    }


                    Genre item = new Genre(name);
                    DB.createGenre(item);
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
                String filter = searchGenreText.getText().toString();
                if (!filter.isEmpty()){
                    searchItems(filter);
                }
            }
        });

        return view;
    }




}
