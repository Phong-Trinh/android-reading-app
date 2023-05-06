package com.example.oneread.data.file;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import com.example.oneread.utils.CommonUtils;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

public class DownloadImageTask implements Callable<List<String>> {

    private static final String TAG = "DownloadImageTask";

    private List<String> images;
    private String filePath;
    private Context context;

    public DownloadImageTask(Context context, List<String> images, String filePath) {
        this.context = context;
        this.images = images;
        this.filePath = filePath;
    }

    @Override
    public List<String> call() {
        return downloadToAppStorage();
    }

    private List<String> downloadToPublicStorage() {
        List<String> downloadUri = new ArrayList<>();
        InputStream inputStream = null;
        OutputStream outputStream = null;
        HttpURLConnection httpURLConnection = null;
        File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS + filePath);
        if (!path.exists()) path.mkdirs();
        for (int i=0; i<images.size(); i++) {
            try {
                URL url = new URL(images.get(i));
                httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.connect();
                if (httpURLConnection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                    return null;
                }
                File file = new File(path, i + ".jpg");
                if (file.exists())
                    file.delete();

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    inputStream = httpURLConnection.getInputStream();
                    BufferedInputStream buffer = new BufferedInputStream(inputStream);
                    Bitmap bitmap = BitmapFactory.decodeStream(buffer);
                    ContentResolver resolver = context.getContentResolver();
                    ContentValues contentValues = new ContentValues();
                    contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, i + ".jpg");
                    contentValues.put(MediaStore.MediaColumns.MIME_TYPE, "image/jpg");
                    contentValues.put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS + filePath);
                    Uri uri = resolver.insert(MediaStore.Files.getContentUri("external"), contentValues);
                    outputStream = resolver.openOutputStream(uri);
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
                    System.out.println(CommonUtils.getRealPathFromURI(context, uri));
                    downloadUri.add(CommonUtils.getRealPathFromURI(context, uri));
                } else {
                    inputStream = httpURLConnection.getInputStream();
                    outputStream = new FileOutputStream(file);

                    byte[] data = new byte[4096];
                    int count;
                    while ((count = inputStream.read(data)) != -1) {
                        outputStream.write(data, 0, count);
                    }
                    downloadUri.add(file.getAbsolutePath());
                }

                outputStream.flush();
                outputStream.close();
                inputStream.close();
                httpURLConnection.disconnect();
            } catch (Exception ignored) {
                Log.e(TAG, ignored.getMessage());
            }
        }
        return downloadUri;
    }

    private List<String> downloadToAppStorage() {
        List<String> downloadUri = new ArrayList<>();
        InputStream inputStream = null;
        OutputStream outputStream = null;
        HttpURLConnection httpURLConnection = null;
        File path = context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS + filePath);
        if (!path.exists()) path.mkdirs();
        for (int i=0; i<images.size(); i++) {
            try {
                URL url = new URL(images.get(i));
                httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.connect();
                if (httpURLConnection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                    return null;
                }
                File file = new File(path, i + ".jpg");
                if (file.exists())
                    file.delete();

                inputStream = httpURLConnection.getInputStream();
                outputStream = new FileOutputStream(file);

                byte[] data = new byte[4096];
                int count;
                while ((count = inputStream.read(data)) != -1) {
                    outputStream.write(data, 0, count);
                }
                downloadUri.add(file.getAbsolutePath());

                outputStream.flush();
                outputStream.close();
                inputStream.close();
                httpURLConnection.disconnect();
            } catch (Exception ignored) {
                Log.e(TAG, ignored.getMessage());
            }
        }
        return downloadUri;
    }


}
