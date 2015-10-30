package com.lucien.team.ui.adapter;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Paint;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lucien.team.R;
import com.lucien.team.db.DBHelper;
import com.lucien.team.unit.User;
import com.lucien.team.util.img.ImgUtils;

import java.util.Map;

/**
 * Created by lucien.li on 2015/10/30.
 */
public class UserCursorAdapter extends RecyclerView.Adapter {

    private Context context;
    private OnItemClickListener listener;
    private Cursor cursor;
    private Map<Integer, Integer> orderMap;

    private class ViewHolder extends RecyclerView.ViewHolder {

        private LinearLayout layout_item_container;
        private ImageView iv_user_avatar;
        private TextView tv_user_name;
        private TextView tv_user_late_count;

        public ViewHolder(View root) {
            super(root);
            layout_item_container = (LinearLayout) root.findViewById(R.id.layout_item_container);
            iv_user_avatar = (ImageView) root.findViewById(R.id.iv_user_avatar);
            tv_user_name = (TextView) root.findViewById(R.id.tv_user_name);
            tv_user_late_count = (TextView) root.findViewById(R.id.tv_user_late_count);

        }
    }


    public UserCursorAdapter(Context context, Cursor cursor, OnItemClickListener listener) {
        this.context = context;
        this.cursor = cursor;
        this.listener = listener;
    }

    public UserCursorAdapter(Context context, Cursor cursor, Map<Integer, Integer> orderMap, OnItemClickListener listener) {
        this.context = context;
        this.cursor = cursor;
        this.listener = listener;
        this.orderMap = orderMap;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        ViewHolder item = (ViewHolder) holder;
        if (orderMap == null) {
            cursor.moveToPosition(position);
        } else {
            cursor.moveToPosition(orderMap.get(position));
        }
        String avatar = cursor.getString(cursor.getColumnIndex(DBHelper.DBConstants.AVATAR));
        String name = cursor.getString(cursor.getColumnIndex(DBHelper.DBConstants.NAME));
        String lateCount = cursor.getString(cursor.getColumnIndex(DBHelper.DBConstants.LATE_COUNT));

        ImgUtils.setImage(context, avatar, item.iv_user_avatar);
        if (!TextUtils.isEmpty(name))
            item.tv_user_name.setText("Name: " + name);
        if (!TextUtils.isEmpty(lateCount))
            item.tv_user_late_count.setText("Late Count: " + lateCount);
        item.layout_item_container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onItemClick(v, position);
                }
            }
        });
        if (position % 2 == 1 && orderMap != null) {
            item.layout_item_container.setBackgroundResource(R.drawable.button2);
        }else {
            item.layout_item_container.setBackgroundResource(R.drawable.button);
        }
    }

    @Override
    public int getItemCount() {
        if (cursor != null) {
            return cursor.getCount();
        } else {
            return 0;
        }
    }

}
