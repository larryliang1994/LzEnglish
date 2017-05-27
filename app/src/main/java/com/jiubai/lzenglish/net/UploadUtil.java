package com.jiubai.lzenglish.net;

import android.content.Context;
import android.util.Log;

import com.jiubai.lzenglish.config.Config;
import com.jiubai.lzenglish.config.Urls;

import net.gotev.uploadservice.MultipartUploadRequest;
import net.gotev.uploadservice.UploadStatusDelegate;

import java.util.Map;

/**
 * Created by Larry Liang on 25/05/2017.
 */

public class UploadUtil {

    public static void upload(final Context context, final Map<String, String> params,
                                       String filePath, UploadStatusDelegate listener) {
        String url = Urls.SERVER_URL + "?";
        for (String key : params.keySet()) {
            url += key + "=" + params.get(key) + "&";
        }

        try {
            new MultipartUploadRequest(context, url)
                    .addFileToUpload(filePath, "Filedata")
                    .addParameter("third_session", Config.ThirdSession)
                    .setMaxRetries(2)
                    .setDelegate(listener)
                    .startUpload();
        } catch (Exception exc) {
            Log.e("AndroidUploadService", exc.getMessage(), exc);
        }
    }
}
