package com.lucien.team.ui.fragment;


import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.database.Cursor;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lucien.team.R;
import com.lucien.team.task.FormGroupTask;
import com.lucien.team.task.GetDbDataTask;
import com.lucien.team.task.OnTaskFinishListener;
import com.lucien.team.task.UpdateDBTask;
import com.lucien.team.ui.adapter.DialogListAdapter;
import com.lucien.team.ui.adapter.OnItemClickListener;
import com.lucien.team.ui.adapter.UserCursorAdapter;

import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class MainFragment extends BaseFragment implements
        OnItemClickListener, View.OnClickListener, OnTaskFinishListener {

    private RecyclerView rv_members;
    private AlertDialog dialog;
    private TextView tv_dialog_title;
    private RecyclerView rv_dialog;

    private LinearLayout layout_group;

    private AnimationDrawable animDrawable;
    private OnItemClickListener itemClickListener;
    private OnTaskFinishListener finishListener;
    private Cursor cursor;
    private Boolean isFormed;
    /**
     * Odd key corresponding group 1, even key corresponding group 2
     */
    private Map<Integer, Integer> orderMap;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            tv_dialog_title.setText(context.getResources().getString(R.string.dialog_done));
            rv_dialog.setLayoutManager(new GridLayoutManager(context, 2, LinearLayoutManager.VERTICAL, false));
            DialogListAdapter listAdapter = new DialogListAdapter(context, cursor, orderMap);
            rv_dialog.setAdapter(listAdapter);
            animDrawable.stop();
        }
    };

    public static MainFragment newInstance() {
        MainFragment fragment = new MainFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }


    public MainFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_main, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        rv_members = (RecyclerView) view.findViewById(R.id.rv_members);
        itemClickListener = this;
        finishListener = this;
        new GetDbDataTask(context, this).execute();
        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDetailWindow();
                Snackbar.make(view, "Forming Group", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        layout_group = (LinearLayout) view.findViewById(R.id.layout_group);
    }

    private void showDetailWindow() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View root = inflater.inflate(R.layout.layout_form_dialog, null);
        new FormGroupTask(finishListener).execute(cursor.getCount());
        ImageButton btn_close = (ImageButton) root.findViewById(R.id.btn_close);
        ImageView iv_dialog = (ImageView) root.findViewById(R.id.iv_dialog);
        addAnimToImg(iv_dialog, R.drawable.anim_random);
        Button btn_dialog_redo = (Button) root.findViewById(R.id.btn_dialog_redo);
        Button btn_dialog_confirm = (Button) root.findViewById(R.id.btn_dialog_confirm);
        rv_dialog = (RecyclerView) root.findViewById(R.id.rv_dialog);

        tv_dialog_title = (TextView) root.findViewById(R.id.tv_dialog_title);
        btn_dialog_redo.setOnClickListener(this);
        btn_dialog_confirm.setOnClickListener(this);
        btn_close.setOnClickListener(this);
        builder.setView(root);
        dialog = builder.create();
        dialog.show();
    }

    private void addAnimToImg(ImageView view, int animId) {
        view.setImageResource(animId);
        animDrawable = (AnimationDrawable) view.getDrawable();
        animDrawable.start();
    }

    @Override
    public void onItemClick(View v, int position) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_close:
                dialog.dismiss();
                break;
            case R.id.btn_dialog_redo:
                new FormGroupTask(finishListener).execute(cursor.getCount());
                animDrawable.start();
                tv_dialog_title.setText(context.getResources().getString(R.string.dialog_processing));
                break;
            case R.id.btn_dialog_confirm:
                animDrawable.stop();
                new UpdateDBTask(context, cursor, orderMap, finishListener).execute();
                rv_members.setLayoutManager(new GridLayoutManager(context, 2, LinearLayoutManager.VERTICAL, false));
                UserCursorAdapter cursorAdapter = new UserCursorAdapter(context, cursor, orderMap, itemClickListener);
                rv_members.setAdapter(cursorAdapter);
                dialog.dismiss();
                layout_group.setVisibility(View.VISIBLE);
                break;
        }
    }

    @Override
    public void onFinish(int taskId, Object result) {
        switch (taskId) {
            case GetDbDataTask.TASK_ID:
                this.cursor = (Cursor) result;
                rv_members.setLayoutManager(new GridLayoutManager(context, 1, LinearLayoutManager.VERTICAL, false));
                UserCursorAdapter adapter = new UserCursorAdapter(context, (Cursor) result, itemClickListener);
                rv_members.setAdapter(adapter);
                break;
            case FormGroupTask.TASK_ID:
                orderMap = (Map<Integer, Integer>) result;
                handler.sendEmptyMessageDelayed(0, 1000);
                break;
        }
    }

    @Override
    public void onDBFinish(int taskId, Object result, Map orderMap) {
        cursor = (Cursor) result;
        this.orderMap = orderMap;
        rv_members.setLayoutManager(new GridLayoutManager(context, 2, LinearLayoutManager.VERTICAL, false));
        UserCursorAdapter cursorAdapter = new UserCursorAdapter(context, cursor, this.orderMap, itemClickListener);
        rv_members.setAdapter(cursorAdapter);
    }

}
