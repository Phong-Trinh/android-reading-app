package com.example.oneread.ui.detail.comment;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.example.oneread.R;
import com.example.oneread.data.network.model.Book;
import com.example.oneread.data.network.model.Chapter;
import com.example.oneread.data.network.model.Comment;
import com.example.oneread.ui.base.BaseFragment;
import com.example.oneread.utils.AppConstants;
import com.example.oneread.utils.CommonUtils;
import com.example.oneread.utils.MODE;
import com.nex3z.flowlayout.FlowLayout;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

import javax.inject.Inject;
import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;

@SuppressLint("NonConstantResourceId")
public class CommentFragment extends BaseFragment implements CommentContract.View, View.OnClickListener, CommentAdapter.Callback {

    private static final String TAG = "CommentFragment";

    @Inject
    CommentPresenter<CommentContract.View> presenter;

    @BindView(R.id.list_comment)
    ExpandableListView listComment;
    @BindView(R.id.btn_camera)
    ImageView btnCamera;
    @BindView(R.id.btn_image)
    ImageView btnImage;
    @BindView(R.id.btn_send)
    ImageView btnSend;
    @BindView(R.id.layout_reply)
    RelativeLayout layoutReply;
    @BindView(R.id.txt_username_reply)
    TextView txtUsernameReply;
    @BindView(R.id.btn_cancel_reply)
    ImageView btnCancelReply;
    @BindView(R.id.files_layout)
    FlowLayout filesLayout;
    @BindView(R.id.edt_comment)
    AppCompatEditText edtComment;

    private List<Comment> comments = new ArrayList<>();
    private Book book;
    private List<Uri> fileUris = new ArrayList<>();
    private Uri fileUri;
    private int positionReplyTo = -1;
    private Comment comment = new Comment();
    private CommentAdapter adapter;

    public CommentFragment(Book book) {
        this.book = book;
    }

    @Override
    public void onDestroyView() {
        presenter.onDetach();
        super.onDestroyView();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_comment, container, false);
        getActivityComponent().inject(this);
        setUnBinder(ButterKnife.bind(this, view));
        presenter.onAttach(this);
        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == AppConstants.MY_CAMERA_REQUEST_CODE){
            if(resultCode == RESULT_OK){
                try {
                    Bitmap thumbnail = MediaStore.Images.Media.getBitmap(
                            getBaseActivity().getContentResolver(), fileUri);
                    ImageView imageView = new ImageView(getBaseActivity());
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams((int) getResources().getDimension(R.dimen._70sdp),
                            (int) getResources().getDimension(R.dimen._90sdp));
                    params.setMargins(5, 10, 5, 10);
                    imageView.setLayoutParams(params);
                    imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
                    imageView.setImageBitmap(thumbnail);
                    filesLayout.addView(imageView);
                    fileUris.add(fileUri);
                    fileUri = null;
                } catch(Exception e) {
                    showMessage("[ERROR]: " + e.getMessage());
                }
            }
        }else if(requestCode == AppConstants.MY_RESULT_LOAD_IMAGE){
            if(resultCode == RESULT_OK){
                try{
                    fileUri = data.getData();
                    InputStream inputStream = getBaseActivity().getContentResolver()
                            .openInputStream(fileUri);
                    Bitmap selectedImage = BitmapFactory.decodeStream(inputStream);
                    ImageView imageView = new ImageView(getBaseActivity());
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams((int) getResources().getDimension(R.dimen._70sdp),
                            (int) getResources().getDimension(R.dimen._90sdp));
                    params.setMargins(5, 10, 5, 10);
                    imageView.setLayoutParams(params);
                    imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
                    imageView.setImageBitmap(selectedImage);
                    filesLayout.addView(imageView);
                    fileUris.add(fileUri);
                    fileUri = null;
                }catch(Exception e){
                    showMessage("[ERROR]: " + e.getMessage());
                }
            }
        }
    }


    @Override
    protected void setup(View v) {
        setupView();

        adapter = new CommentAdapter(getBaseActivity(), comments, this);
        listComment.setAdapter(adapter);
        if (comments.size() == 0) {
            presenter.loadComments(book.getEndpoint());
        } else {
            List<Comment> oldComments = new ArrayList<>();
            oldComments.addAll(comments);
            setComments(oldComments);
        }
    }

    private void setupView() {
        btnCamera.setOnClickListener(this);
        btnImage.setOnClickListener(this);
        btnCancelReply.setOnClickListener(this);
        btnSend.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_camera:
                onCaptureImageClick();
                break;
            case R.id.btn_image:
                onSelectImageClick();
                break;
            case R.id.btn_cancel_reply:
                cancelReply();
                break;
            case R.id.btn_send:
                sendComment();
                break;
            default:
                break;
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void setComments(List<Comment> comments) {
        this.comments.clear();
        this.comments.addAll(comments);
        adapter.notifyDataSetChanged();
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void setReplies(List<Comment> replies, int position) {
        comments.get(position).getReplies().clear();
        comments.get(position).getReplies().addAll(replies);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void addComment(Comment comment) {
        comments.add(0, comment);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void addReply(Comment comment, int position) {
        comments.get(position).getReplies().add(0, comment);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void loadReplies(int position) {
        presenter.loadReplies(comments.get(position).getId(), position);
    }

    @Override
    public void clearReplies(int position) {
        comments.get(position).getReplies().clear();
        adapter.notifyDataSetChanged();
    }

    @Override
    public void reply(int position) {
        positionReplyTo = position;
        txtUsernameReply.setText(comments.get(position).getUser().getUsername());
        layoutReply.setVisibility(View.VISIBLE);
        comment.setIdRoot(comments.get(position).getId());
    }

    private void cancelReply() {
        positionReplyTo = -1;
        txtUsernameReply.setText("");
        layoutReply.setVisibility(View.GONE);
        comment.setIdRoot(-1);
    }

    private void sendComment() {
        int positionReply =  -1;
        comment.setContent(edtComment.getText().toString().trim());
        edtComment.setText("");
        List<MultipartBody.Part> listPart = new ArrayList<>();
        for (int i=0; i<fileUris.size(); i++) {
            File file = new File(CommonUtils.getRealPathFromURI(getBaseActivity(), fileUris.get(i)));
            RequestBody requestFile = RequestBody.create(MediaType.parse("image/*"), file);
            MultipartBody.Part part = MultipartBody.Part.createFormData("file", file.getName(), requestFile);
            listPart.add(part);
            filesLayout.removeAllViews();
            fileUris.clear();
        }
        MultipartBody.Part content = MultipartBody.Part.createFormData("content", comment.getContent());
        listPart.add(content);
        if (comment.getIdRoot() != -1) {
            positionReply = positionReplyTo;
            MultipartBody.Part idRoot = MultipartBody.Part.createFormData("id_root", String.valueOf(comment.getIdRoot()));
            listPart.add(idRoot);
            cancelReply();
        }

        MultipartBody.Part[] mb = new MultipartBody.Part[listPart.size()];
        listPart.toArray(mb);
        if (positionReply != -1) {
            presenter.addReply(book.getEndpoint(), mb, positionReply);
        } else {
            presenter.addComment(book.getEndpoint(), mb);
        }
    }

    void onSelectImageClick(){
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, AppConstants.MY_RESULT_LOAD_IMAGE);
    }

    void onCaptureImageClick(){
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "New Picture");
        values.put(MediaStore.Images.Media.DESCRIPTION, "From your Camera");
        fileUri = getBaseActivity().getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
        startActivityForResult(intent, AppConstants.MY_CAMERA_REQUEST_CODE);
    }

    @Override
    public void showLoadingView() {

    }

    @Override
    public void hideLoadingView() {

    }

    @Override
    public void showErrorView() {

    }

    @Override
    public void hideErrorView() {

    }
}
