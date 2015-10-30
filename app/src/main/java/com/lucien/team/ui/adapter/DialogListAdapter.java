package com.lucien.team.ui.adapter;

import android.content.Context;
import android.database.Cursor;
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
import com.lucien.team.util.img.ImgUtils;

import java.util.Map;

/**
 * Created by lucien.li on 2015/10/30.
 */
public class DialogListAdapter extends RecyclerView.Adapter {

    private Context context;
    private Cursor cursor;
    private Map<Integer, Integer> orderMap;
    private int itemHeight;

    private class ViewHolder extends RecyclerView.ViewHolder {

        private FrameLayout layout_item_container;
        private TextView tv_user_name;

        public ViewHolder(View root) {
            super(root);
            layout_item_container = (FrameLayout) root.findViewById(R.id.layout_item_container);
            tv_user_name = (TextView) root.findViewById(R.id.tv_user_name);
        }
    }


    public DialogListAdapter(Context context, Cursor cursor, Map<Integer, Integer> orderMap) {
        this.context = context;
        this.cursor = cursor;
        this.orderMap = orderMap;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        itemHeight = parent.getHeight() / (orderMap.size() / 2) - context.getResources().getDimensionPixelSize(R.dimen.dimen_7dp);
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_dialog_list, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        ViewHolder item = (ViewHolder) holder;
        cursor.moveToPosition(orderMap.get(position));
        String name = cursor.getString(cursor.getColumnIndex(DBHelper.DBConstants.NAME));
        item.tv_user_name.setText(name);
        ViewGroup.LayoutParams lp = item.layout_item_container.getLayoutParams();
        lp.height = itemHeight;
        item.layout_item_container.setLayoutParams(lp);
        if (position % 2 == 1) {
            item.tv_user_name.setTextColor(context.getResources().getColor(R.color.txt_group_2));
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
