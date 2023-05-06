package com.example.oneread.ui.main.notify;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.*;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.example.oneread.R;
import com.example.oneread.data.network.model.Notify;
import com.example.oneread.ui.base.BaseDialog;
import com.example.oneread.ui.detail.DetailActivity;
import com.example.oneread.ui.detail.rating.RateDialog;
import com.example.oneread.ui.main.adapter.HistoryReadAdapter;
import com.example.oneread.utils.MODE;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@SuppressLint("NonConstantResourceId")
public class NotificationDialog extends BaseDialog implements NotificationContract.View, NotificationAdapter.Callback {

    private static final String TAG = "NotificationDialog";

    @Inject
    NotificationPresenter<NotificationContract.View> presenter;

    @BindView(R.id.txt_delete_read_notification)
    TextView txtDeleteReadNotification;
    @BindView(R.id.list_notify)
    RecyclerView listNotify;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.notification_dialog, container, false);

        getActivityComponent().inject(this);
        setUnBinder(ButterKnife.bind(this, view));
        presenter.onAttach(this);

        return view;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.setCanceledOnTouchOutside(true);
        dialog.getWindow().setDimAmount(0.0f);
        dialog.getWindow().getAttributes().windowAnimations = R.style.NotificationDialogAnimation;


        Window window = dialog.getWindow();
        WindowManager.LayoutParams params = window.getAttributes();
        params.gravity = Gravity.TOP | Gravity.RIGHT;
        params.y = getBaseActivity().getResources().getDimensionPixelSize(R.dimen._55sdp);
        params.x = getBaseActivity().getResources().getDimensionPixelSize(R.dimen._10sdp);
        params.width = getBaseActivity().getResources().getDisplayMetrics().widthPixels * 2 / 3;
        params.height = getBaseActivity().getResources().getDisplayMetrics().heightPixels / 2;
        dialog.getWindow().setAttributes(params);
        return dialog;
    }

    @Override
    public void onDestroy() {
        presenter.onDetach();
        super.onDestroy();
    }

    @Override
    protected void setUp(View view) {
        listNotify.setLayoutManager(new LinearLayoutManager(getBaseActivity(), LinearLayoutManager.VERTICAL, false));
        listNotify.setAdapter(new NotificationAdapter(getBaseActivity(), new ArrayList<>(), this));
        presenter.getAllNotifications();
        txtDeleteReadNotification.setOnClickListener(v -> {
            presenter.deleteReadNotification();
        });
    }

    public void show(FragmentManager fragmentManager) {
        super.show(fragmentManager, TAG);
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

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void setNotifications(List<Notify> notifications) {
        ((NotificationAdapter) Objects.requireNonNull(listNotify.getAdapter())).getNotifications().clear();
        ((NotificationAdapter) listNotify.getAdapter()).getNotifications().addAll(notifications);
        ((NotificationAdapter) listNotify.getAdapter()).notifyDataSetChanged();
    }

    @Override
    public void removeReadNotification(List<Notify> notifications) {
        presenter.getAllNotifications();
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void updateNotification(Notify notify) {
         List<Notify> notifications =  ((NotificationAdapter) Objects.requireNonNull(listNotify.getAdapter())).getNotifications();
         for (Notify n : notifications) {
             if (n.getEndpoint().equals(notify.getEndpoint())) {
                 notifications.remove(n);
                 notifications.add(notify);
                 break;
             }
         }
        ((NotificationAdapter) listNotify.getAdapter()).notifyDataSetChanged();
        Log.e(TAG, notify.getEndpoint().split("[+]")[1] + notify.getEndpoint());
        if (notify.getEndpoint().split("[+]")[1].equals("book") ||
                notify.getEndpoint().split("[+]")[1].equals("chapter")) {
            Intent intent = new Intent(getBaseActivity(), DetailActivity.class);
            intent.putExtra("endpoint", notify.getEndpoint().split("[+]")[2]);
            intent.putExtra("mode", MODE.ONLINE);
            startActivity(intent);
            dismissDialog(TAG);
        }
    }

    @Override
    public void onNotificationClick(int position) {
        presenter.readNotification(((NotificationAdapter) Objects.requireNonNull(listNotify.getAdapter()))
                .getNotifications().get(position).getEndpoint());
    }
}
