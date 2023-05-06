package com.example.oneread.ui.profile;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.oneread.R;
import com.example.oneread.data.network.model.User;
import com.example.oneread.ui.base.BaseActivity;
import com.example.oneread.utils.AppConstants;
import com.example.oneread.utils.CommonUtils;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindInt;
import butterknife.BindView;
import butterknife.ButterKnife;
import eightbitlab.com.blurview.BlurView;
import eightbitlab.com.blurview.RenderScriptBlur;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

@SuppressLint("NonConstantResourceId")
public class ProfileActivity extends BaseActivity implements ProfileContract.View {

    @Inject
    ProfilePresenter<ProfileContract.View> presenter;

    @BindView(R.id.btn_back)
    LinearLayout btnBack;
    @BindView(R.id.btn_avatar)
    LinearLayout btnAvatar;
    @BindView(R.id.btn_save)
    AppCompatButton btnSave;
    @BindView(R.id.username)
    EditText edtUsername;
    @BindView(R.id.email)
    EditText edtEmail;
    @BindView(R.id.password)
    EditText edtPassword;
    @BindView(R.id.retype_password)
    EditText edtRetypePassword;
    @BindView(R.id.avatar)
    ImageView avatar;
    @BindView(R.id.avatar_back)
    ImageView avatarBack;
    @BindView(R.id.layout_update_password)
    LinearLayout layoutUpdatePassword;
    @BindView(R.id.layout_password)
    LinearLayout layoutPassword;
    @BindView(R.id.layout_verify_email)
    LinearLayout layoutVerifyEmail;
    @BindView(R.id.txt_account_status)
    TextView txtAccountStatus;
    @BindView(R.id.root)
    RelativeLayout root;
    @BindView(R.id.content_background)
    BlurView contentBackground;

    private Uri uriAvatar;
    private boolean isUpdatePassword;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        getActivityComponent().inject(this);
        setUnBinder(ButterKnife.bind(this));
        presenter.onAttach(this);
        setup();
    }

    @Override
    protected void onDestroy() {
        presenter.onDetach();
        super.onDestroy();
    }

    @Override
    protected void setup() {
        setupView();

        presenter.getUser();
        contentBackground.setupWith(root)
                .setFrameClearDrawable(getWindow().getDecorView().getBackground())
                .setBlurAlgorithm(new RenderScriptBlur(this))
                .setBlurRadius(25f).setBlurAutoUpdate(true);
    }

    private void setupView() {
        btnBack.setOnClickListener(v -> finish());
        btnAvatar.setOnClickListener(v -> onSelectImageClick());
        btnSave.setOnClickListener(v -> onBtnSaveClick());
        layoutUpdatePassword.setOnClickListener(v -> {
            isUpdatePassword = true;
            layoutPassword.setVisibility(View.VISIBLE);
            layoutUpdatePassword.setVisibility(View.GONE);
        });
        layoutVerifyEmail.setOnClickListener(v -> presenter.verifyEmail());
    }

    void onBtnSaveClick() {
        if (isUpdatePassword) {
            if (edtPassword.getText().toString().trim().equals(edtRetypePassword.getText().toString().trim())) {
                String password = edtPassword.getText().toString().trim();
                if (isUpdatePassword && password.isEmpty()) {
                    onError(R.string.empty_password);
                    return;
                }
                if (isUpdatePassword && !CommonUtils.isValidPassword(password)) {
                    onError(R.string.invalid_password);
                    return;
                }
                presenter.updatePassword(edtPassword.getText().toString().trim());
            } else {
                showMessage("Mật khẩu không trùng khớp");
                return;
            }
        }

        String email = edtEmail.getText().toString().trim();
        if (email == null || email.isEmpty()) {
            onError(R.string.empty_email);
            return;
        }
        if (!CommonUtils.isValidEmailAddress(email)) {
            onError(R.string.invalid_email);
            return;
        }
        List<MultipartBody.Part> listPart = new ArrayList<>();
        if (uriAvatar != null) {
            File file = new File(CommonUtils.getRealPathFromURI(this, uriAvatar));
            RequestBody requestFile = RequestBody.create(MediaType.parse("image/*"), file);
            MultipartBody.Part part = MultipartBody.Part.createFormData("avatar", file.getName(), requestFile);
            listPart.add(part);
        }
        listPart.add(MultipartBody.Part.createFormData("email", edtEmail.getText().toString().trim()));

        MultipartBody.Part[] mb = new MultipartBody.Part[listPart.size()];
        listPart.toArray(mb);
        presenter.updateUserProfile(mb);
    }

    void onSelectImageClick(){
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, AppConstants.MY_RESULT_LOAD_IMAGE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == AppConstants.MY_RESULT_LOAD_IMAGE){
            if(resultCode == RESULT_OK){
                try{
                    uriAvatar = data.getData();
                    InputStream inputStream = this.getContentResolver()
                            .openInputStream(uriAvatar);
                    Bitmap selectedImage = BitmapFactory.decodeStream(inputStream);
                    avatar.setImageBitmap(selectedImage);
                    avatarBack.setImageBitmap(selectedImage);
                }catch(Exception e){
                    showMessage("[ERROR]: " + e.getMessage());
                }
            }
        }
    }

    @Override
    public void loadUser(User user) {
        Glide.with(this).load(user.getAvatar())
                .placeholder(R.drawable.background)
                .error(R.drawable.image_err)
                .into(avatar);
        Glide.with(this).load(user.getAvatar())
                .placeholder(R.drawable.background)
                .error(R.drawable.image_err)
                .into(avatarBack);
        edtUsername.setText(user.getUsername());
        edtEmail.setText(user.getEmail());
        edtPassword.setText(user.getPassword());
        if(user.getStatus() != 1) {
            txtAccountStatus.setText("Tài khoản chưa được xác minh");
            layoutVerifyEmail.setVisibility(View.VISIBLE);
        } else {
            txtAccountStatus.setText("Tài khoản đã được xác minh");
            layoutVerifyEmail.setVisibility(View.GONE);
        }
        layoutPassword.setVisibility(View.GONE);
        layoutUpdatePassword.setVisibility(View.VISIBLE);
    }
}