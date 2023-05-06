package com.example.oneread.ui.read.fragment.comic;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.oneread.R;
import com.example.oneread.utils.MODE;

import java.util.ArrayList;
import java.util.List;

public class ChapterComicFragment extends Fragment {

    private static final String TAG = "ChapterContentFragment";

    private RecyclerView recyclerView;
    private List<String> images;
    private static final String arg_images = "arg_images";
    private static final String arg_mode = "arg_mode";
    private MODE mode;

    public static ChapterComicFragment newInstance(List<String> images, MODE mode) {
        ChapterComicFragment fragment = new ChapterComicFragment();
        Bundle args = new Bundle();
        args.putStringArrayList(arg_images, (ArrayList<String>) images);
        args.putSerializable(arg_mode, mode);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            images = getArguments().getStringArrayList(arg_images);
            mode = (MODE) getArguments().getSerializable(arg_mode);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chapter_content, container, false);
        recyclerView = view.findViewById(R.id.recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
        recyclerView.setAdapter(new ChapterComicAdapter(getContext(), images, mode));
        return view;
    }


    @Override
    public void onDestroy() {
        System.out.println("DESTROY");
        super.onDestroy();
    }

    @Override
    public void onAttach(Context context) {
        System.out.println("ATTACH");
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        System.out.println("DETACH");
        super.onDetach();
    }
}
