package com.example.oneread.utils;

import android.app.ProgressDialog;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.widget.EditText;
import com.example.oneread.R;

public final class CommonUtils {

    private static final String TAG = "CommonUtils";

    private CommonUtils() {}

    public static ProgressDialog showLoadingDialog(Context context) {
        ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.show();
        if (progressDialog.getWindow() != null) {
            progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
        progressDialog.setContentView(R.layout.progress_dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        return progressDialog;
    }

    public static String getRealPathFromURI(Context context, Uri contentUri) {
        Cursor cursor = null;
        try {
            String[] proj = { MediaStore.Images.Media.DATA };
            cursor = context.getContentResolver().query(contentUri,  proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    public static boolean checkEmptyComponents(Object[] objects){
        for(Object o : objects){
            if(o instanceof EditText){
                if (((EditText) o).getText().toString().trim().equals("")) {
                    ((EditText) o).setError("Not Empty");
                    return false;
                }
            }
        }
        return true;
    }

    public static boolean isValidEmailAddress(String email) {
        // ảo thật đấy
        //String pattern = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$";
        String regex = "^([a-zA-Z0-9]{5,})@((([a-zA-Z]+\\.)([a-zA-Z]{2,})(\\.[a-zA-Z]{2,})?))$";
        return email.matches(regex);
    }

    public static boolean isValidEmailAddress(EditText email) {
        String regex = "^([a-zA-Z0-9]{5,})@((([a-zA-Z]+\\.)([a-zA-Z]{2,})(\\.[a-zA-Z]{2,})?))$";
        if (!email.getText().toString().matches(regex)) {
            return false;
        } else {
            return true;
        }
    }

    public static boolean isValidPassword(String password) {
        /*
        (?=x).{1,} match "xy" in "ayxy"
        (?=.x).{1,} match "yxy" in "xyxy"
        (?=.*x).{1,} match only "xyxy"
        */
        String regex = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,}$";
        return password.matches(regex);
    }

    public static boolean isValidPassword(EditText password) {
        String regex = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,}$";
        if (!password.getText().toString().matches(regex)) {
            return false;
        } else {
            return true;
        }
    }

    public static boolean isValidName(String name){
        String regex = "^[a-zA-Z\\s]+";
        return name.matches(regex);
    }

    public static boolean isValidName(EditText name){
        String regex = "^[a-zA-Z\\s]+";
        if (!name.getText().toString().matches(regex)) {
            return false;
        } else {
            return true;
        }
    }
}
