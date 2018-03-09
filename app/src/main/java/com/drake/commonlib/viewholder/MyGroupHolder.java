package com.drake.commonlib.viewholder;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.drake.commonlib.R;
import com.drake.commonlib.model.MyGroupItem;
import com.drake.ui.viewholder.BaseGroupViewHolder;

public class MyGroupHolder extends BaseGroupViewHolder<MyGroupItem> implements View.OnClickListener {

    public interface Callback {
        void onGroupClick(MyGroupHolder groupHolder, MyGroupItem groupItem);
    }

    private TextView mTitle;

    private MyGroupItem mItem;

    public MyGroupHolder(Context context, View itemView) {
        super(context, itemView);
        mTitle = itemView.findViewById(R.id.id_list_item_group_tv);

        itemView.setOnClickListener(this);
    }

    @Override
    public void build(MyGroupItem groupItem, int groupPosition) {
        mItem = groupItem;

        if (mItem == null)
            return;

        if (mTitle != null) {
            mTitle.setText(mItem.title);
        }
    }

    @Override
    public void onClick(View v) {
        if (mItem != null && mItem.callback != null) {
            mItem.callback.onGroupClick(this, mItem);
        }
    }
}
