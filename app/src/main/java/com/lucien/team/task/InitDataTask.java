package com.lucien.team.task;

import android.content.Context;
import android.os.AsyncTask;

import com.lucien.team.api.Api;
import com.lucien.team.api.ApiHelper;
import com.lucien.team.db.DBDao;
import com.lucien.team.db.DBService;

/**
 * Created by lucien.li on 2015/10/30.
 */
public class InitDataTask extends AsyncTask<String, Void, Void> {

    private Context context;
    private DBService service;
    private OnTaskFinishListener listener;
    public static final int TASK_ID = 1;

    public InitDataTask(Context context, OnTaskFinishListener listener) {
        this.context = context;
        this.listener = listener;
        this.service = DBDao.getDBDaoInstance(context);
    }

    @Override
    protected Void doInBackground(String... params) {
        ApiHelper.GetJsonData(Api.getApiInstance(context), params[0], service);
        return null;
    }

    @Override
    protected void onPostExecute(Void result) {
        super.onPostExecute(result);
        if (listener != null) {
            listener.onFinish(TASK_ID, result);
        }
    }
}
