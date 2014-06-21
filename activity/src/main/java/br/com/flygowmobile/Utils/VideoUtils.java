package br.com.flygowmobile.Utils;

import android.content.Context;
import android.util.Log;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by Tiago Rocha Gomes on 19/06/14.
 */
public class VideoUtils {

    private static final String EXTENSION = ".mp4";

    public static void saveVideo(Context ctx, String fileName, byte[] video) throws IOException {
        FileOutputStream fos = ctx.openFileOutput(fileName.trim() + EXTENSION, Context.MODE_PRIVATE);
        fos.write(video);
        fos.close();
    }

    public static String getVideo(Context ctx, String fileName) throws IOException {
        File file = new File(ctx.getFilesDir() + "/" + fileName);
        return file.getAbsolutePath() + EXTENSION;
    }

    public static void removeAllPhisicalVideos(Context ctx) throws IOException {
        String name = ctx.getFilesDir() + "/";
        Log.e("", "path : " + name);
        File myDir = new File(name);
        File[] videosToDelete = myDir.listFiles();
        if(videosToDelete != null){
            for (int i = 0; i < videosToDelete.length; i++) {
                File file = videosToDelete[i];
                if(file.getCanonicalPath().endsWith(EXTENSION)){
                    file.delete();
                }
            }
        }
    }
}
