package com.lucien.team.ui.activity;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.lucien.team.R;
import com.lucien.team.api.Api;
import com.lucien.team.api.ApiHelper;
import com.lucien.team.app.Config;
import com.lucien.team.db.DBDao;
import com.lucien.team.db.DBService;
import com.lucien.team.ui.fragment.MainFragment;

import java.util.List;

public class MainActivity extends BaseActivity {

    @Override
    public void init(Bundle savedInstanceState) {
        addFragment(MainFragment.newInstance(), getResString(R.string.main_fragment));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
