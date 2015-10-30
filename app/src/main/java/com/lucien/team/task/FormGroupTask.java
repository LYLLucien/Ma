package com.lucien.team.task;

import android.os.AsyncTask;

import com.lucien.team.util.common.CommonLog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * Created by lucien.li on 2015/10/30.
 */
public class FormGroupTask extends AsyncTask<Integer, Void, Map<Integer, Integer>> {

    public static final int TASK_ID = 3;
    private OnTaskFinishListener listener;
    private List<Integer> orderList;
    private Map<Integer, Integer> orderMap;

    public FormGroupTask(OnTaskFinishListener listener) {
        this.listener = listener;
        this.orderList = new ArrayList<>();
        this.orderMap = new HashMap<>();
    }

    @Override
    protected Map<Integer, Integer> doInBackground(Integer... params) {
        for (int i = 0; i < params[0]; i++) {
            orderList.add(i);
        }
        for (int i = 0; i < params[0] / 2; i++) {
            Random random = new Random();
            int selectOrder = random.nextInt(orderList.size());
            orderMap.put(i * 2, orderList.get(selectOrder));
            CommonLog.d("group 1: " + orderList.get(selectOrder));
            orderList.remove(selectOrder);
        }
        for (int i = 0; i < params[0] / 2; i++) {
            orderMap.put(i * 2 + 1, orderList.get(i));
        }
        return orderMap;
    }

    @Override
    protected void onPostExecute(Map<Integer, Integer> result) {
        super.onPostExecute(result);
//        for (int i = 0; i < orderMap.size(); i++) {
//            System.out.println("map position " + i + ": " + orderMap.get(i));
//        }
        if (listener != null) {
            listener.onFinish(TASK_ID, result);
        }
    }
}
