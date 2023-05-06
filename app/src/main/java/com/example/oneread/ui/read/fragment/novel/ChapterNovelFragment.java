package com.example.oneread.ui.read.fragment.novel;

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

public class ChapterNovelFragment extends Fragment {

    private static final String TAG = "ChapterContentFragment";

    private RecyclerView recyclerView;
    private List<String> texts;
    private static final String arg_texts = "arg_texts";
    private static final String arg_mode = "arg_mode";
    private MODE mode;

    public static ChapterNovelFragment newInstance(List<String> texts, MODE mode) {
        ChapterNovelFragment fragment = new ChapterNovelFragment();
        Bundle args = new Bundle();
        args.putStringArrayList(arg_texts, (ArrayList<String>) texts);
        args.putSerializable(arg_mode, mode);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            texts = getArguments().getStringArrayList(arg_texts);
            mode = (MODE) getArguments().getSerializable(arg_mode);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chapter_content, container, false);
        recyclerView = view.findViewById(R.id.recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
        recyclerView.setAdapter(new ChapterNovelAdapter(getContext(), texts, mode));
        return view;
    }

}
