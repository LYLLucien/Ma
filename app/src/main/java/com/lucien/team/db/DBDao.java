package com.lucien.team.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.lucien.hkmdemo.utils.common.CommonLog;

import java.util.List;

/**
 * Created by lucien.li on 2015/10/6.
 */
public class DBDao implements DBService {

    private final static String CLASSTAG = DBDao.class.getSimpleName();
    private static DBHelper helper;
    private static DBDao dao;
    private SQLiteDatabase database = null;
    private Cursor cursor = null;
    private Context context;


    public static DBDao getDBDaoInstance(Context context) {
        if (dao == null) {
            dao = new DBDao(context);
            CommonLog.i("db is null!");
        } else {
            CommonLog.i("db is not null!");
        }
        return dao;
    }

    private DBDao(Context context) {
        helper = new DBHelper(context);
        this.context = context;
        openDB();
    }

    @Override
    public boolean openDB() {
        CommonLog.i("open DB!");
        try {
            database = helper.getWritableDatabase();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    public boolean openReadOnlyDB() {
        try {
            database = helper.getReadableDatabase();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    public void closeDB() {
        CommonLog.i("close DB!");
        if (database != null) {
            database.close();
        } else {
            CommonLog.i(CLASSTAG, "Database already close or not open!");
        }
        if (cursor != null) {
            cursor.close();
        } else {
            CommonLog.i(CLASSTAG, "Cursor already close or not open!");
        }
    }

    @Override
    public boolean addItem(String tableName, ContentValues values) {
        boolean flag = false;
        long id;
        try {
            if (database == null) {
                database = helper.getWritableDatabase();
            }
            id = database.replace(tableName, null, values);
            flag = (id != -1 ? true : false);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return flag;
    }

    @Override
    public boolean deleteItem(String tableName, String whereClause, String[] whereArgs) {
        boolean flag = false;
        int count = 0;

        try {
            if (database == null) {
                database = helper.getWritableDatabase();
            }
            count = database.delete(tableName, whereClause, whereArgs);
            flag = (count > 0 ? true : false);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return flag;
    }

    @Override
    public boolean updateItem(String tableName, ContentValues values, String whereClause, String[] whereArgs) {
        boolean flag = false;
        int count = 0;
        try {
            if (database == null) {
                database = helper.getWritableDatabase();
            }
            count = database.update(tableName, values, whereClause, whereArgs);
            flag = (count > 0 ? true : false);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return flag;
    }

    @Override
    public void bulkInsert(String tableName, List<ContentValues> contentValuesList) {
        long id;
        try {
            if (database == null) {
                database = helper.getWritableDatabase();
            }
            database.beginTransaction();
            for (ContentValues values : contentValuesList) {
                id = database.replace(tableName, null, values);
                if (id == -1) {
                    CommonLog.e(CLASSTAG, "bulk insert err, value: " + values.toString());
                }
            }
            database.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (database != null) {
                database.endTransaction();
            }
        }
    }

    @Override
    public void bulkUpdate(String tableName, List<ContentValues> contentValuesList, String key, List<Integer> deleteList) {
        long id;
        try {
            if (database == null) {
                database = helper.getWritableDatabase();
            }
            for (ContentValues values : contentValuesList) {
                id = database.replace(tableName, null, values);
                if (id == -1) {
                    CommonLog.e(CLASSTAG, "bulk update err, value: " + values.toString());
                }
            }

            for (int values : deleteList) {
                database.execSQL("DELETE FROM " + tableName + " WHERE " + key + " = " + values);
            }
            database.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (database != null) {
                database.endTransaction();
            }
        }
    }

    @Override
    public Cursor execRawQuery(String sql, String[] whereClause) {
        try {
            if (database == null) {
                database = helper.getWritableDatabase();
            }
            cursor = database.rawQuery(sql, whereClause);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return cursor;
    }

    public Cursor getMoviesByType(int type) {
        String sql = "SELECT "
                + DBHelper.DBConstants._ID + ", "
                + DBHelper.DBConstants.NAME + ", "
                + DBHelper.DBConstants.THUMBNAIL_URL + ", "
                + DBHelper.DBConstants.TOTAL_REVENUE + ", "
                + DBHelper.DBConstants.OPEN_DATE + " "
                + "FROM " + DBHelper.DBConstants.TABLE_USER;

        switch (type) {
            case 0:
                sql = sql + " ORDER BY " + DBHelper.DBConstants.TOTAL_REVENUE + " DESC";
                break;
            case 1:
                sql = sql + " ORDER BY " + DBHelper.DBConstants.TOTAL_REVENUE + " ASC";
                break;
            default:
                sql = "SELECT * FROM " + DBHelper.DBConstants.TABLE_USER;
                break;
        }
        return execRawQuery(sql, null);
    }
}
