package com.lucien.team.util.json;

import android.text.TextUtils;

import com.google.gson.Gson;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by lucien.li on 2015/10/5.
 */
public class JsonUtils {

    public static Object parseJson(String jsonStr, Class cls) {
        try {
            if (!TextUtils.isEmpty(jsonStr)) {
                Gson gson = new Gson();
                return gson.fromJson(jsonStr, cls);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new Object();
    }

    public static List<?> parseListJson(String jsonStr, Type listType) {
        try {
            if (!TextUtils.isEmpty(jsonStr)) {
                Gson gson = new Gson();
                return gson.fromJson(jsonStr, listType);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ArrayList<Object>();
    }
}
