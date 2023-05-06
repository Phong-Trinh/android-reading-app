package com.example.oneread.ui.base;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.oneread.R;
import com.example.oneread.data.network.model.Book;
import com.example.oneread.ui.detail.DetailActivity;
import com.example.oneread.utils.MODE;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class RectBookAdapter extends RecyclerView.Adapter<RectBookAdapter.ViewHolder>{
    Context context;
    List<Book> books = new ArrayList<>();
    HashMap<String, Boolean> isFollowed = new HashMap<>();
    public RectBookAdapter() {
    }

    public RectBookAdapter(Context context, List<Book> books) {
        this.context = context;
        this.books = books;
    }

    @Override
    public ViewHolder onCreateViewHolder( ViewGroup parent, int viewType) {
        View row = LayoutInflater.from(context).inflate(R.layout.book_item_rect, parent, false);
        return new ViewHolder(row);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull List<Object> payloads) {
        if (payloads.isEmpty()) super.onBindViewHolder(holder, position, payloads);
        else {
            Bundle bundle = (Bundle) payloads.get(0);
            for(String key : bundle.keySet()) {
                if (key.equals("changed")) {
                    Glide.with(context).load(books.get(position).getThumb()).placeholder(R.drawable.image_loading).error(R.drawable.image_err).into(holder.thumb);
                    holder.title.setText(books.get(position).getTitle());
                }
            }
        }
    }

    @Override
    public void onBindViewHolder( ViewHolder holder, int position) {
        Glide.with(context).load(books.get(position).getThumb()).placeholder(R.drawable.image_loading).error(R.drawable.image_err).into(holder.thumb);
        holder.title.setText(books.get(position).getTitle());
    }

    @Override
    public int getItemCount() {
        return books.size();
    }

    public List<Book> getBooks() {
        return books;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView thumb;
        TextView title;
        public ViewHolder( View itemView) {
            super(itemView);
            thumb = itemView.findViewById(R.id.thumb);
            title = itemView.findViewById(R.id.title);



            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, DetailActivity.class);
                    intent.putExtra("endpoint", books.get(getAdapterPosition()).getEndpoint());
                    intent.putExtra("mode", MODE.ONLINE);
                    context.startActivity(intent);
                }
            } );
        }
    }
}
