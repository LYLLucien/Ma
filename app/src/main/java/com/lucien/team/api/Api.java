package com.lucien.team.api;

import android.content.Context;
import android.provider.BaseColumns;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;


import com.lucien.team.app.Enumeration.ApiStatus;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

/**
 * Created by lucien.li on 2015/10/5.
 */
public class Api {

    private final String CLASSTAG = Api.class.getSimpleName();
    private final String METHOD_GET = "GET";
    private static Api api;

    private Context context;
    private String androidID;


    public static Api getApiInstance(Context context) {
        if (api == null) {
            api = new Api(context);
        }
        return api;
    }

    private Api(Context context) {
        this.context = context;
        androidID = Settings.Secure.getString(context.getContentResolver(),
                Settings.Secure.ANDROID_ID);
        Log.v(CLASSTAG, "Api constructing");
    }

    public ApiStatus getJsonData(ApiResult result, String uri) {
        URL url;
        HttpURLConnection urlConnection;
        try {
            url = new URL(uri);
            urlConnection = (HttpURLConnection) url
                    .openConnection();
            urlConnection.setRequestMethod(METHOD_GET);
            urlConnection.setRequestProperty("Accept", "application/json");
            InputStream in = urlConnection.getInputStream();
            SimpleParser parser = new SimpleParser(result);
            return parser.doParse(in);
        } catch (Exception e) {
            e.printStackTrace();
            return ApiStatus.General_Error;
        }
    }


    public static final class ApiConstants implements BaseColumns {
    }
}
