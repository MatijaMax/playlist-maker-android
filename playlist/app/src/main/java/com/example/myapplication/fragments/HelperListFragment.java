package com.example.myapplication.fragments;

import static android.content.ContentValues.TAG;

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
import com.example.myapplication.adapters.HelperAdapter;
import com.example.myapplication.databinding.FragmentArtistListBinding;
import com.example.myapplication.databinding.FragmentHelperListBinding;
import com.example.myapplication.sqlite.helper.DatabaseHelper;
import com.example.myapplication.sqlite.model.Artist;
import com.example.myapplication.sqlite.model.Genre;
import com.example.myapplication.sqlite.model.Helper;
import com.example.myapplication.sqlite.model.Playlist;
import com.example.myapplication.sqlite.model.Song;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class HelperListFragment extends Fragment implements HelperAdapter.DeleteClickListener   {

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private boolean isEdit;

    private long editId;
    private FragmentHelperListBinding binding;

    private EditText nameEditText;

    private long playlistId;

    private long songId;


    private Button addButton;

    private RecyclerView recyclerView;
    private List<Helper> itemList;
    private HelperAdapter itemAdapter;
    private Spinner spinnerG;
    DatabaseHelper DB;

    public HelperListFragment() {
        // Required empty public constructor
    }
    public static HelperListFragment newInstance(String param1, String param2) {
        HelperListFragment fragment = new HelperListFragment();
        Bundle args = new Bundle();
        Log.e(TAG, "ARG PARAM 1 :" + param1);
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

    public void onDeleteClick(Helper helper) {
        DB.deleteHelper(helper.getId());
    }

    public void loadItems(){
        itemList = new ArrayList<>();
        Playlist playlist = DB.getPlaylistByName(getArguments().getString(ARG_PARAM1));
        List<Helper> helpers = DB.getHelpersByPlaylistId(playlist.getId());
        this.playlistId = playlist.getId();
        itemList.addAll(helpers);
        itemAdapter = new HelperAdapter(itemList, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(itemAdapter);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        fillSpinner(view);

    }

    public void fillSpinner(@NonNull View view) {
                    ArrayList<Song> unfilteredList  = new ArrayList<>();
                    List<Song> songs = DB.getAllSongs();
                    unfilteredList.addAll(songs);
                    ArrayList<String> typeGList = new ArrayList<>();
                    for(Song song: songs) {
                        typeGList.add(song.getName());
                    }

                    Spinner spinnerEventType = view.findViewById(R.id.spinnerHelper);
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, typeGList.toArray(new String[0]));
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinnerEventType.setAdapter(adapter);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentHelperListBinding.inflate(inflater, container, false);
        View view = binding.getRoot();


        addButton = view.findViewById(R.id.buttonAddHelper);
        spinnerG = view.findViewById(R.id.spinnerHelper);
        recyclerView = view.findViewById(R.id.recyclerView);
        loadItems();
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String typeG = "";
                if (spinnerG.getSelectedItem() != null) {
                    typeG = spinnerG.getSelectedItem().toString();
                }

                if (!typeG.isEmpty()) {
                    Song song = DB.getSongByName(typeG);
                    if(isEdit){

                        Helper item = new Helper(editId, playlistId, song.getId(), typeG);
                        Helper oldHelper = DB.getHelperById(editId);
                        DB.updateHelper(item);
                        loadItems();
                        isEdit = false;
                        editId = -1;
                    }


                    Helper item = new Helper(playlistId, song.getId(), typeG);
                    DB.createHelper(item);
                    loadItems();
                    //itemList.add(item);
                    //itemAdapter.notifyDataSetChanged();
                }
            }
        });

        return view;
    }




}
