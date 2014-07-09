package br.com.flygowmobile.Utils;

import android.content.Context;
import android.util.Log;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.io.BufferedInputStream;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import br.com.flygowmobile.enums.MediaTypeEnum;

/**
 * Created by Tiago Rocha Gomes on 19/06/14.
 */
public class VideoUtils {

    private static final String EXTENSION = ".mp4";
    private static final Integer MAX_VIDEO_SIZE = 10024; //10 MB

    public static void downloadVideoByEntityId(Context ctx, String serverUrl, Integer entityId, String videoName) throws IOException {
        URL u = new URL(serverUrl);
        HttpURLConnection c = (HttpURLConnection) u.openConnection();
        c.setRequestMethod("POST");
        c.setDoOutput(true);

        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("entityId", entityId + ""));
        params.add(new BasicNameValuePair("mediaType", MediaTypeEnum.VIDEO.getId() + ""));

        OutputStream os = c.getOutputStream();
        BufferedWriter writer = new BufferedWriter(
                new OutputStreamWriter(os, "UTF-8"));
        writer.write(getQuery(params));
        writer.flush();
        writer.close();
        os.close();

        c.connect();
        FileOutputStream f = new FileOutputStream(new File(ctx.getFilesDir() + "/" + videoName + EXTENSION));

        InputStream in = c.getInputStream();

        byte[] buffer = new byte[MAX_VIDEO_SIZE];
        int len1 = 0;
        while ( (len1 = in.read(buffer)) > 0 ) {
            f.write(buffer, 0, len1);
        }
        f.close();
    }

    private static String getQuery(List<NameValuePair> params) throws UnsupportedEncodingException {
        StringBuilder result = new StringBuilder();
        boolean first = true;
        for (NameValuePair pair : params){
            if (first)
                first = false;
            else
                result.append("&");

            result.append(URLEncoder.encode(pair.getName(), "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(pair.getValue(), "UTF-8"));
        }
        return result.toString();
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
