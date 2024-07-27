package com.example.myapplication.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.myapplication.FragmentTransition;
import com.example.myapplication.R;
import com.example.myapplication.databinding.PlaylistPageFragmentBinding;

import java.util.ArrayList;

public class PlaylistPageFragment extends Fragment {


    private PlaylistPageFragmentBinding binding;
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = PlaylistPageFragmentBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        FragmentTransition.to(PlaylistListFragment.newInstance("", ""), getActivity(),
                true, R.id.scroll_items_list, "playlistPage");

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
