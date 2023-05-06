package com.example.oneread.ui.read.fragment.novel;

import android.content.Context;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.example.oneread.R;
import com.example.oneread.utils.MODE;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ChapterNovelAdapter extends RecyclerView.Adapter<ChapterNovelAdapter.ViewHolder>{

    Context context;
    List<String> list_text = new ArrayList<>();
    MODE mode;

    private static final String TAG = "ChapterNovelAdapter";

    public ChapterNovelAdapter() {
    }

    public ChapterNovelAdapter(Context context, List<String> list_text, MODE mode) {
        this.context = context;
        this.list_text = list_text;
        this.mode = mode;
    }

    @Override
    public ViewHolder onCreateViewHolder( ViewGroup parent, int viewType) {
        View row = LayoutInflater.from(context).inflate(R.layout.chapter_text_item, parent, false);
        return new ViewHolder(row);
    }

    @Override
    public void onBindViewHolder( ViewHolder holder, int position) {
        if (mode == MODE.ONLINE) {
            holder.text.setText(new StringBuilder("     ").append(list_text.get(position)));
        } else {
            String s = "/* error while read text file */";
            File file = new File(list_text.get(position));
            try {
                BufferedReader br = new BufferedReader(new FileReader(file));
                StringBuilder stringBuilder = new StringBuilder();
                String line;
                while ((line = br.readLine()) != null) {
                    stringBuilder.append("     ");
                    stringBuilder.append(line);
                    stringBuilder.append('\n');
                }
                br.close();
                s = stringBuilder.toString();
                Log.e(TAG, stringBuilder.toString());
            } catch (IOException e) {
                Log.e(TAG, e.getMessage());
            }
            holder.text.setText(s);
        }
    }

    @Override
    public int getItemCount() {
        return list_text.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView text;
        public ViewHolder( View itemView) {
            super(itemView);
            text = itemView.findViewById(R.id.text);
        }
    }
}
