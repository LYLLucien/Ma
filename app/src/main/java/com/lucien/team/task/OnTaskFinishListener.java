package com.lucien.team.task;

import java.util.Map;

/**
 * Created by lucien.li on 2015/10/30.
 */
public interface OnTaskFinishListener<T> {
    void onFinish(int taskId, T result);

    void onDBFinish(int taskId, T result, Map<Integer, Integer> orderList);

}
