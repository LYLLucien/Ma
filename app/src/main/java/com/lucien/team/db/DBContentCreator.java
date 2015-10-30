package com.lucien.team.db;

import android.content.ContentValues;

import com.lucien.team.db.DBHelper.DBConstants;
import com.lucien.team.unit.Team;
import com.lucien.team.unit.User;


/**
 * Created by lucien.li on 2015/10/6.
 */
public class DBContentCreator {

    public static ContentValues UserCreator(User user) {
        ContentValues values = new ContentValues();
        values.put(DBConstants.NAME, user.getName());
        values.put(DBConstants.AVATAR, user.getAvatar());
        values.put(DBConstants.AVATAR_THUMBNAIL_URL, user.getAvatar_thumbnail_url());
        values.put(DBConstants.LATE_COUNT, user.getLate_count());
        StringBuilder stringBuilder = new StringBuilder("[");
        for (int i = 0; i < user.getTeams().length; i++) {
            stringBuilder.append(user.getTeams()[i]);
            if (i < user.getTeams().length - 1)
                stringBuilder.append(", ");
            else
                stringBuilder.append("]");
        }
        values.put(DBConstants.TEAM, stringBuilder.toString());
        return values;
    }

    public static ContentValues LateCreator(String name, String lateTime) {
        ContentValues values = new ContentValues();
        values.put(DBConstants.NAME, name);
        values.put(DBConstants.LATE_TIME, lateTime);
        return values;
    }

    public static ContentValues TeamIdUpdateCreator(String name, int teamId) {
        ContentValues values = new ContentValues();
        values.put(DBConstants.NAME, name);
        values.put(DBConstants.TEAM_ID, teamId);
        return values;
    }
}
