package com.lucien.team.ui.activity;

import android.app.FragmentTransaction;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.lucien.team.R;
import com.lucien.team.app.MyApplication;
import com.lucien.team.util.common.CommonUtils;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by lucien.li on 2015/10/28.
 */
public abstract class BaseActivity<Fragment> extends AppCompatActivity {

    private DrawerLayout layout_drawer;
    private RelativeLayout layout_main;
    private LinearLayout drawer_view;
    private ActionBar actionBar;
    private ActionBarDrawerToggle pulsante;
    private Toolbar toolbar;
    private TextView tv_toolbar_title;

    private MyApplication context;
    private Resources resources;

    private List<Fragment> childFragmentStack;
    private List<String> childTitleStack;

    private CharSequence title;

    private boolean isCurrentFragmentChild = false;
    private boolean multiPaneSupport = false;

    private long firstTime;

    private View.OnClickListener toolbarToggleListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (isCurrentFragmentChild) {
                System.out.println("child");
                onBackPressed();
            } else {
                System.out.println("else");
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_main);
        this.context = (MyApplication) getApplicationContext();
        this.resources = getResources();
        initView();
        initList();
        init(savedInstanceState);
    }

    public abstract void init(Bundle savedInstanceState);

    public MyApplication getContext() {
        return context;
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        if (pulsante != null) {
            pulsante.syncState();
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        if (pulsante != null) {
            pulsante.onConfigurationChanged(newConfig);
        }
    }

    private void initView() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);

        layout_drawer = (DrawerLayout) findViewById(R.id.layout_drawer);
        layout_main = (RelativeLayout) findViewById(R.id.layout_main);
        drawer_view = (LinearLayout) findViewById(R.id.drawer_view);
        DrawerLayout.LayoutParams drawerParams = (android.support.v4.widget.DrawerLayout.LayoutParams) drawer_view.getLayoutParams();
        drawerParams.width = CommonUtils.getDrawerWidth(resources);
        drawer_view.setLayoutParams(drawerParams);

        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();

        tv_toolbar_title = (TextView) findViewById(R.id.tv_toolbar_title);

        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
        pulsante = new ActionBarDrawerToggle(this, layout_drawer, toolbar, R.string.drawer_open, R.string.drawer_close) {
            @Override
            public void onDrawerClosed(View drawerView) {
                supportInvalidateOptionsMenu();
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                supportInvalidateOptionsMenu();
            }

            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                super.onDrawerSlide(drawerView, slideOffset);
                layout_main.setTranslationX(slideOffset * drawerView.getWidth());
                layout_drawer.bringChildToFront(drawerView);
                layout_drawer.requestLayout();
            }
        };
        pulsante.setDrawerIndicatorEnabled(true);
        pulsante.setToolbarNavigationClickListener(toolbarToggleListener);
        layout_drawer.setDrawerListener(pulsante);
    }

    private void initList() {
        childFragmentStack = new LinkedList<>();
        childTitleStack = new LinkedList<>();
    }

    @Override
    public void setContentView(View view) {
        throw new RuntimeException("The library have it's own content, please move all content inside section's fragments");
    }

    @Override
    public void setContentView(int layoutResID) {
        throw new RuntimeException("The library have it's own content, please move all content inside section's fragments");
    }

    @Override
    public void setContentView(View view, ViewGroup.LayoutParams params) {
        throw new RuntimeException("The library have it's own content, please move all content inside section's fragments");
    }

    public void addFragment(Fragment fragment, String title) {
        setTitle(title);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
            // before honeycomb there is not android.app.Fragment
            android.support.v4.app.FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.add(R.id.frame_container, (android.support.v4.app.Fragment) fragment).commit();
            childFragmentStack.add(fragment);
            childTitleStack.add(title);
        } else if (fragment instanceof android.app.Fragment) {
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.add(R.id.frame_container, (android.app.Fragment) fragment).commit();
            childFragmentStack.add(fragment);
            childTitleStack.add(title);
        } else if (fragment instanceof android.support.v4.app.Fragment) {
            android.support.v4.app.FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.add(R.id.frame_container, (android.support.v4.app.Fragment) fragment).commit();
            childFragmentStack.add(fragment);
            childTitleStack.add(title);
        } else
            throw new RuntimeException("Fragment must be android.app.Fragment or android.support.v4.app.Fragment");
    }

    public void setFragment(Fragment fragment, String title) {
        setFragment(fragment, title, null);

        // remove the last child from the stack
        if (!isCurrentFragmentChild) {
            childFragmentStack.remove(childFragmentStack.size() - 1);
            childTitleStack.remove(childTitleStack.size() - 1);
        } else {
            // if a section is clicked when user is into a child remove all children from stack
            for (int i = childFragmentStack.size() - 1; i >= 0; i--) {
                childFragmentStack.remove(i);
                childTitleStack.remove(i);
            }
        }

        // add to the childStack the Fragment and title
        childFragmentStack.add(fragment);
        childTitleStack.add(title);

        isCurrentFragmentChild = false;
    }

    private void setFragment(Fragment fragment, String title, Fragment oldFragment) {
        setFragment(fragment, title, oldFragment, false);
    }

    private void setFragment(Fragment fragment, String title, Fragment oldFragment, boolean hasSavedInstanceState) {
        setTitle(title);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
            // before honeycomb there is not android.app.Fragment

            if (!hasSavedInstanceState) {
                android.support.v4.app.FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                if (oldFragment != null && oldFragment != fragment) {
                    ft.remove((android.support.v4.app.Fragment) oldFragment);
                }

                ft.replace(R.id.frame_container, (android.support.v4.app.Fragment) fragment).commit();
            }
        } else if (fragment instanceof android.app.Fragment) {
            if (oldFragment instanceof android.support.v4.app.Fragment)
                throw new RuntimeException("You should use only one type of Fragment");

            if (!hasSavedInstanceState) {
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                if (oldFragment != null && fragment != oldFragment) {
                    ft.remove((android.app.Fragment) oldFragment);
                }

                ft.replace(R.id.frame_container, (android.app.Fragment) fragment).commit();
            }
        } else if (fragment instanceof android.support.v4.app.Fragment) {
            if (oldFragment instanceof android.app.Fragment)
                throw new RuntimeException("You should use only one type of Fragment");

            if (!hasSavedInstanceState) {
                android.support.v4.app.FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                if (oldFragment != null && fragment != oldFragment) {
                    ft.remove((android.support.v4.app.Fragment) oldFragment);
                }

                ft.replace(R.id.frame_container, (android.support.v4.app.Fragment) fragment).commit();
            }
        } else
            throw new RuntimeException("Fragment must be android.app.Fragment or android.support.v4.app.Fragment");
    }

    public void setFragmentChild(Fragment fragment, String title) {
        isCurrentFragmentChild = true;

        setFragment(fragment, title, childFragmentStack.get(childFragmentStack.size() - 1));

        //add to the stack the child
        childFragmentStack.add(fragment);
        childTitleStack.add(title);

        //sync the toolbar toggle state
        pulsante.setDrawerIndicatorEnabled(false);
    }

    @Override
    public void setTitle(CharSequence title) {
        this.title = title;
        tv_toolbar_title.setText(title);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (!deviceSupportMultiPane()) {
            if (pulsante.onOptionsItemSelected(item)) {
                return true;
            }
        } else {
            switch (item.getItemId()) {
                case android.R.id.home:
                    toolbarToggleListener.onClick(null);
                    return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    private boolean deviceSupportMultiPane() {
        if (multiPaneSupport &&
                resources.getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE &&
                CommonUtils.isTablet(resources))
            return true;
        else
            return false;
    }

    public String getResString(int resId) {
        return getResources().getString(resId);
    }

    @Override
    public void onBackPressed() {
        if (childFragmentStack.size() <= 1) {
            isCurrentFragmentChild = false;
            exitByDoubleClicked();
        } else {
            // reload the child before
            Fragment newFragment = childFragmentStack.get(childFragmentStack.size() - 2);
            String newTitle = childTitleStack.get(childTitleStack.size() - 2);

            // get and remove the last child
            Fragment curFragment = childFragmentStack.remove(childFragmentStack.size() - 1);
            childTitleStack.remove(childTitleStack.size() - 1);
            setFragment(newFragment, newTitle, curFragment);

            if (childFragmentStack.size() == 1) {
                // user come back to master fragment
                isCurrentFragmentChild = false;
                pulsante.setDrawerIndicatorEnabled(true);
            }
        }
    }

    private void exitByDoubleClicked() {
        if (System.currentTimeMillis() - firstTime < 3000) {
            finish();
        } else {
            firstTime = System.currentTimeMillis();
            Toast.makeText(this, getResources().getString(R.string.alert_exit), Toast.LENGTH_SHORT).show();
        }
    }
}
