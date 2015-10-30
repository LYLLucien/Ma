package com.lucien.team.task;

import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;

import com.lucien.team.db.DBDao;
import com.lucien.team.db.DBHelper;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by lucien.li on 2015/10/30.
 */
public class GetDbDataTask extends AsyncTask<Void, Void, Cursor> {

    private Context context;
    private OnTaskFinishListener listener;
    private DBDao service;
    public static final int TASK_ID = 2;
    private boolean isTeamExit = false;

    public GetDbDataTask(Context context, OnTaskFinishListener listener) {
        this.context = context;
        this.listener = listener;
        this.service = DBDao.getDBDaoInstance(context);
    }

    @Override
    protected Cursor doInBackground(Void... params) {
        Cursor cursor = service.getTeamUsers();
        if (cursor.getCount() == 0) {
            System.out.println("null");
            isTeamExit = false;
            return service.getAllUsers();
        } else {
            System.out.println("not null");
            isTeamExit = true;
            return service.getFormedUsers();
        }
    }


    @Override
    protected void onPostExecute(Cursor result) {
        super.onPostExecute(result);
        if (isTeamExit) {
            Map<Integer, Integer> orderMap = new HashMap<>();
            int j = 0, k = 0;
            for (int i = 0; i < result.getCount(); i++) {
                result.moveToPosition(i);
                int teamId = result.getInt(result.getColumnIndex(DBHelper.DBConstants.TEAM_ID));

                if (teamId == 0) {
                    orderMap.put(j * 2, i);
                    j++;
                } else {
                    orderMap.put(k * 2 + 1, i);
                    k++;
                }
            }
            if (listener != null) {
                listener.onDBFinish(TASK_ID, result, orderMap);
            }
        } else {
            if (listener != null) {
                listener.onFinish(TASK_ID, result);
            }
        }
    }
}
