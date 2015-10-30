package com.lucien.team.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.lucien.team.R;
import com.lucien.team.app.Config;
import com.lucien.team.task.InitDataTask;
import com.lucien.team.task.OnTaskFinishListener;

import java.util.Map;

public class LoadingActivity extends AppCompatActivity implements OnTaskFinishListener {

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            startActivity(new Intent(LoadingActivity.this, MainActivity.class));
            finish();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);
        new InitDataTask(this, this).execute(Config.URL_HOST + Config.URL_API + Config.URL_USER);

    }

    @Override
    public void onFinish(int taskId, Object result) {
        handler.sendEmptyMessageDelayed(0, 1000);
    }

    @Override
    public void onDBFinish(int taskId, Object result, Map orderList) {

    }
}
