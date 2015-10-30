package com.lucien.team.api;


import android.content.ContentValues;

import com.google.gson.reflect.TypeToken;
import com.lucien.team.app.Enumeration.ApiStatus;
import com.lucien.team.db.DBContentCreator;
import com.lucien.team.db.DBHelper;
import com.lucien.team.db.DBService;
import com.lucien.team.unit.User;
import com.lucien.team.util.common.CommonLog;
import com.lucien.team.util.json.JsonUtils;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by lucien.li on 2015/10/5.
 */
public class ApiHelper {

    public static final String CLASSTAG = ApiHelper.class.getSimpleName();

    public static void GetJsonData(Api api, String uri, DBService service) {
        ApiResult result = new ApiResult();
        if (api.getJsonData(result, uri) == ApiStatus.Success) {
            Type listType = new TypeToken<ArrayList<User>>() {
            }.getType();
            List<User> userList = (List<User>) JsonUtils.parseListJson(result.getMsg(), listType);
            List<ContentValues> userValuesList = new ArrayList<>();
            List<ContentValues> lateValuesList = new ArrayList<>();

            for (User user : userList) {
                ContentValues values = DBContentCreator.UserCreator(user);
                userValuesList.add(values);
                for (String lateTime : user.getLates()) {
                    ContentValues lateValue = DBContentCreator.LateCreator(user.getName(), lateTime);
                    lateValuesList.add(lateValue);
                }
            }
            service.bulkInsert(DBHelper.DBConstants.TABLE_USER, userValuesList);
            service.bulkInsert(DBHelper.DBConstants.TABLE_LATES, lateValuesList);

        } else {
            CommonLog.e("get json failed!");
        }
    }

}
