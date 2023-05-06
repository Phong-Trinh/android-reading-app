package com.example.oneread.data.file;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import com.example.oneread.utils.CommonUtils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

public class DownloadTextTask implements Callable<List<String>> {

    private static final String TAG = "DownloadTextTask";

    private List<String> texts;
    private String filePath;
    private Context context;

    public DownloadTextTask(Context context, List<String> texts, String filePath) {
        this.context = context;
        this.texts = texts;
        this.filePath = filePath;
    }

    @Override
    public List<String> call() {
        return downloadToAppStorage();
    }

    private List<String> downloadToPublicStorage() {
        List<String> downloadUri = new ArrayList<>();
        File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS + filePath);
        if (!path.exists()) path.mkdirs();
        for (int i=0; i<texts.size(); i++) {
            try {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    ContentResolver resolver = context.getContentResolver();
                    ContentValues contentValues = new ContentValues();
                    contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, i + ".txt");
                    contentValues.put(MediaStore.MediaColumns.MIME_TYPE, "text/plain");
                    contentValues.put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS + filePath);
                    Uri uri = resolver.insert(MediaStore.Files.getContentUri("external"), contentValues);
                    OutputStream outputStream = resolver.openOutputStream(uri);
                    byte[] bytes = texts.get(i).getBytes();
                    outputStream.write(bytes);
                    outputStream.close();
                    System.out.println(CommonUtils.getRealPathFromURI(context, uri));
                    downloadUri.add(CommonUtils.getRealPathFromURI(context, uri));
                } else {
                    File file = new File(path, i + ".txt");
                    FileWriter fw = new FileWriter(file.getAbsoluteFile());
                    BufferedWriter bw = new BufferedWriter(fw);
                    bw.write(texts.get(i));
                    bw.close();
                    downloadUri.add(file.getAbsolutePath());
                }
            } catch (Exception ignored) {
                Log.e(TAG, ignored.getMessage());
            }
        }
        return downloadUri;
    }

    private List<String> downloadToAppStorage() {
        List<String> downloadUri = new ArrayList<>();
        File path = context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS + filePath);
        if (!path.exists()) path.mkdirs();
        for (int i=0; i<texts.size(); i++) {
            try {
                File file = new File(path, i + ".txt");
                FileWriter fw = new FileWriter(file.getAbsoluteFile());
                BufferedWriter bw = new BufferedWriter(fw);
                bw.write(texts.get(i));
                bw.close();
                downloadUri.add(file.getAbsolutePath());
            } catch (Exception ignored) {
                Log.e(TAG, ignored.getMessage());
            }
        }
        return downloadUri;
    }
}
