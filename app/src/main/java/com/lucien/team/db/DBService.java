package com.lucien.team.db;

import android.content.ContentValues;
import android.database.Cursor;

import java.util.List;

/**
 * Created by lucien.li on 2015/10/6.
 */
public interface DBService {

    boolean openDB();

    boolean openReadOnlyDB();

    void closeDB();

    boolean addItem(String tableName, ContentValues values);

    boolean deleteItem(String tableName, String whereClause, String[] whereArgs);

    boolean updateItem(String tableName, ContentValues values,
                       String whereClause, String[] whereArgs);

    void bulkInsert(String tableName, List<ContentValues> contentValues);

    void bulkUpdate(String tableName, List<ContentValues> contentValuesList, String key, List<Integer> deleteList);

    Cursor execRawQuery(String sql, String[] whereClause);
}
