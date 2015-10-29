package com.lucien.team.api;


import com.google.gson.reflect.TypeToken;
import com.lucien.team.app.Enumeration.ApiStatus;
import com.lucien.team.unit.User;
import com.lucien.team.util.json.JsonUtils;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by lucien.li on 2015/10/5.
 */
public class ApiHelper {

    public static final String CLASSTAG = ApiHelper.class.getSimpleName();

    public static List<?> GetJsonData(Api api, String uri) {
        ApiResult result = new ApiResult();
        if (api.getJsonData(result, uri) == ApiStatus.Success) {
            Type listType = new TypeToken<ArrayList<User>>() {
            }.getType();
            return JsonUtils.parseListJson(result.getMsg(), listType);
        }
        return null;
    }

}
