package com.example.oneread.ui.detail.comment;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.bumptech.glide.Glide;
import com.example.oneread.R;
import com.example.oneread.data.network.model.Comment;
import com.nex3z.flowlayout.FlowLayout;

import java.util.ArrayList;
import java.util.List;

public class CommentAdapter extends BaseExpandableListAdapter {

    Context context;
    List<Comment> comments = new ArrayList<>();

    private Callback callback;

    interface Callback {
        void loadReplies(int position);

        void clearReplies(int position);

        void reply(int position);
    }

    public CommentAdapter() {
    }

    public CommentAdapter(Context context, List<Comment> comments, Callback callback) {
        this.context = context;
        this.comments = comments;
        this.callback = callback;
    }

    @Override
    public int getGroupCount() {
        return comments.size();
    }

    @Override
    public int getChildrenCount(int i) {
        return comments.get(i).getReplies().size();
    }

    @Override
    public Comment getGroup(int i) {
        return comments.get(i);
    }

    @Override
    public Comment getChild(int i, int i1) {
        return comments.get(i).getReplies().get(i1);
    }

    @Override
    public long getGroupId(int i) {
        return i;
    }

    @Override
    public long getChildId(int i, int i1) {
        return i1;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getGroupView(int i, boolean b, View view, ViewGroup viewGroup) {
        view = LayoutInflater.from(context).inflate(R.layout.item_comment_root, null);
        ImageView avatar;
        TextView txtUsername, txtContent, txtTime;
        LinearLayout btnReply;
        FlowLayout filesLayout;
        LinearLayout layoutShowReply, layoutHideReply;

        avatar = view.findViewById(R.id.avatar);
        txtUsername = view.findViewById(R.id.txt_username);
        txtContent = view.findViewById(R.id.txt_content);
        layoutShowReply = view.findViewById(R.id.show_reply);
        layoutHideReply = view.findViewById(R.id.hide_reply);
        txtTime = view.findViewById(R.id.txt_time);
        btnReply = view.findViewById(R.id.btn_reply);
        filesLayout = view.findViewById(R.id.files_layout);


        Glide.with(context).load(comments.get(i).getUser().getAvatar()).placeholder(R.drawable.image_loading).error(R.drawable.image_err).into(avatar);
        txtUsername.setText(comments.get(i).getUser().getUsername());
        txtContent.setText(comments.get(i).getContent());
        txtTime.setText(comments.get(i).getTime());
        filesLayout.removeAllViews();
        for (String link : comments.get(i).getFiles()) {
            ImageView imageView = new ImageView(context);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams((int) context.getResources().getDimension(R.dimen._70sdp),
                    (int) context.getResources().getDimension(R.dimen._90sdp));
            params.setMargins(5, 10, 5, 10);
            imageView.setId(i);
            imageView.setLayoutParams(params);
            imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
            Glide.with(context).asBitmap().load(link)
                    .override((int) context.getResources().getDimension(R.dimen._70sdp),
                            (int) context.getResources().getDimension(R.dimen._90sdp))
                    .placeholder(R.drawable.image_loading)
                    .error(R.drawable.image_err)
                    .into(imageView);
//            Picasso.get().load(link).placeholder(R.drawable.image_loading).error(R.drawable.image_err).into(imageView);
            filesLayout.addView(imageView);
        }

        ((ExpandableListView) viewGroup).expandGroup(i);

        layoutShowReply.setVisibility(comments.get(i).getReplies().size() > 0 ? View.GONE : View.VISIBLE);
        layoutHideReply.setVisibility(comments.get(i).getReplies().size() > 0 ? View.VISIBLE : View.GONE);

        layoutShowReply.setOnClickListener(v -> {
            callback.loadReplies(i);
        });
        layoutHideReply.setOnClickListener(v -> {
            callback.clearReplies(i);
        });

        btnReply.setOnClickListener(v -> {
            callback.reply(i);
        });
        return view;
    }

    @Override
    public View getChildView(int i, int i1, boolean b, View view, ViewGroup viewGroup) {
        view = LayoutInflater.from(context).inflate(R.layout.item_comment_reply, null);

        ImageView avatar;
        TextView txtUsername, txtContent, txtTime;
        FlowLayout filesLayout;

        avatar = view.findViewById(R.id.avatar);
        txtUsername = view.findViewById(R.id.txt_username);
        txtContent = view.findViewById(R.id.txt_content);
        txtTime = view.findViewById(R.id.txt_time);
        filesLayout = view.findViewById(R.id.files_layout);

        Glide.with(context).load(comments.get(i).getReplies().get(i1).getUser().getAvatar()).placeholder(R.drawable.image_loading).error(R.drawable.image_err).into(avatar);
        txtUsername.setText(comments.get(i).getReplies().get(i1).getUser().getUsername());
        txtContent.setText(comments.get(i).getReplies().get(i1).getContent());
        txtTime.setText(comments.get(i).getReplies().get(i1).getTime());
        filesLayout.removeAllViews();
        for (String link : comments.get(i).getReplies().get(i1).getFiles()) {
            ImageView imageView = new ImageView(context);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams((int) context.getResources().getDimension(R.dimen._70sdp),
                    (int) context.getResources().getDimension(R.dimen._90sdp));
            params.setMargins(5, 10, 5, 10);
            imageView.setId(i1);
            imageView.setLayoutParams(params);
            imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
            Glide.with(context).asBitmap().load(link)
                    .override((int) context.getResources().getDimension(R.dimen._70sdp),
                            (int) context.getResources().getDimension(R.dimen._90sdp))
                    .placeholder(R.drawable.image_loading)
                    .error(R.drawable.image_err)
                    .into(imageView);
//            Picasso.get().load(link).placeholder(R.drawable.image_loading).error(R.drawable.image_err).into(imageView);
            filesLayout.addView(imageView);
        }

        return view;
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return true;
    }
}
