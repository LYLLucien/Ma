package com.lucien.team.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

import com.lucien.team.util.common.CommonLog;


/**
 * Created by lucien.li on 2015/10/6.
 */
public class DBHelper extends SQLiteOpenHelper {


    private static final String CLASSTAG = DBHelper.class.getSimpleName();
    private Context context;

    public DBHelper(Context context) {
        super(context, DBConstants.DB_NAME, null, DBConstants.VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql_user =
                "CREATE TABLE " + DBConstants.TABLE_USER + " (" +
                        DBConstants._ID + " " + DBConstants.INTEGER + " PRIMARY KEY AUTOINCREMENT," +
                        DBConstants.NAME + " " + DBConstants.VARCHAR(32) + " UNIQUE ON CONFLICT REPLACE, " +
                        DBConstants.AVATAR + " " + DBConstants.VARCHAR(64) + "," +
                        DBConstants.AVATAR_THUMBNAIL_URL + " " + DBConstants.VARCHAR(64) + "," +
                        DBConstants.LATE_COUNT + " " + DBConstants.INTEGER + "," +
                        DBConstants.TEAM + " " + DBConstants.TEXT + ")";
        String sql_late =
                "CREATE TABLE " + DBConstants.TABLE_LATES + " (" +
                        DBConstants._ID + " " + DBConstants.INTEGER + " PRIMARY KEY AUTOINCREMENT," +
                        DBConstants.NAME + " " + DBConstants.VARCHAR(32) + " NOT NULL, " +
                        DBConstants.LATE_TIME + " " + DBConstants.VARCHAR(64) + "NOT NULL UNIQUE)";
        String sql_team =
                "CREATE TABLE " + DBConstants.TABLE_TEAM + " (" +
                        DBConstants._ID + " " + DBConstants.INTEGER + " PRIMARY KEY AUTOINCREMENT," +
                        DBConstants.NAME + " " + DBConstants.VARCHAR(32) + " NOT NULL UNIQUE ON CONFLICT REPLACE, " +
                        DBConstants.TEAM_ID + " " + DBConstants.INTEGER + " NOT NULL)";
        db.execSQL(sql_user);
        db.execSQL(sql_late);
        db.execSQL(sql_team);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        CommonLog.i(CLASSTAG, "onUpgrade: from " + oldVersion + " to " + newVersion);
        switch (oldVersion) {
            default:
                db.execSQL("DROP TABLE IF EXISTS " + DBConstants.TABLE_USER);
                db.execSQL("DROP TABLE IF EXISTS " + DBConstants.TABLE_LATES);
                db.execSQL("DROP TABLE IF EXISTS " + DBConstants.TABLE_TEAM);
                onCreate(db);
        }
    }

    public static final class DBConstants implements BaseColumns {
        public static final String DB_NAME = "ma.db";
        public static final int VERSION = 1;

        public static final String TEXT = "text";
        public static final String INTEGER = "INTEGER";

        public static String VARCHAR(int num) {
            return "varchar(" + num + ")";
        }

        public static final String TABLE_USER = "table_user";
        public static final String TABLE_LATES = "table_lates";
        public static final String TABLE_TEAM = "table_team";

        public static final String _ID = "_id";
        public static final String NAME = "name";
        public static final String TEAM_ID = "team_id";
        public static final String AVATAR = "avatar";
        public static final String AVATAR_THUMBNAIL_URL = "avatar_thumbnail_url";
        public static final String LATE_COUNT = "late_count";
        public static final String LATE_TIME = "lates";
        public static final String TEAM = "team";

    }
}
