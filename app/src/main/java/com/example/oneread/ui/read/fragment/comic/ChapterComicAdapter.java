package com.example.oneread.ui.read.fragment.comic;

import android.content.Context;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.oneread.R;
import com.example.oneread.utils.MODE;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ChapterComicAdapter extends RecyclerView.Adapter<ChapterComicAdapter.ViewHolder>{

    Context context;
    List<String> list_image = new ArrayList<>();
    MODE mode;

    public ChapterComicAdapter() {
    }

    public ChapterComicAdapter(Context context, List<String> list_image, MODE mode) {
        this.context = context;
        this.list_image = list_image;
        this.mode = mode;
    }

    @Override
    public ViewHolder onCreateViewHolder( ViewGroup parent, int viewType) {
        View row = LayoutInflater.from(context).inflate(R.layout.chapter_image_item, parent, false);
        return new ViewHolder(row);
    }

    @Override
    public void onBindViewHolder( ViewHolder holder, int position) {
        if (mode == MODE.ONLINE) {
            Glide.with(context).load(list_image.get(position)).placeholder(R.drawable.image_loading).error(R.drawable.image_err).into(holder.chapter_image);
        } else {
            File file = new File(list_image.get(position));
            Glide.with(context).load(file).placeholder(R.drawable.image_loading).error(R.drawable.image_err).into(holder.chapter_image);
        }
    }

    @Override
    public int getItemCount() {
        return list_image.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
       ImageView chapter_image;
        public ViewHolder( View itemView) {
            super(itemView);
            chapter_image = itemView.findViewById(R.id.chapter_image);
        }
    }
}
