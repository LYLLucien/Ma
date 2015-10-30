package com.lucien.team.task;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;

import com.lucien.team.db.DBContentCreator;
import com.lucien.team.db.DBDao;
import com.lucien.team.db.DBHelper;
import com.lucien.team.db.DBService;
import com.lucien.team.unit.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by lucien.li on 2015/10/30.
 */
public class UpdateDBTask extends AsyncTask<Void, Void, Void> {

    public static final int TASK_ID = 4;
    private Context context;
    /**
     * Odd key corresponding group 1, even key corresponding group 2
     */
    private Map<Integer, Integer> orderMap;
    private DBService service;
    private OnTaskFinishListener listener;
    private Cursor cursor;
    private List<ContentValues> userUpdateList;

    public UpdateDBTask(Context context, Cursor cursor, Map<Integer, Integer> orderMap, OnTaskFinishListener listener) {
        this.context = context;
        this.cursor = cursor;
        this.orderMap = orderMap;
        this.listener = listener;
        service = DBDao.getDBDaoInstance(context);
        userUpdateList = new ArrayList<>();
    }

    @Override
    protected Void doInBackground(Void... params) {
        for (int i = 0; i < orderMap.size(); i++) {
            cursor.moveToPosition(orderMap.get(i));
            String name = cursor.getString(cursor.getColumnIndex(DBHelper.DBConstants.NAME));
            int teamId = i % 2;
            ContentValues values = DBContentCreator.TeamIdUpdateCreator(name, teamId);
            userUpdateList.add(values);
        }
        service.bulkInsert(DBHelper.DBConstants.TABLE_TEAM, userUpdateList);
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        if (listener != null) {
            listener.onFinish(TASK_ID, null);
        }
    }
}
