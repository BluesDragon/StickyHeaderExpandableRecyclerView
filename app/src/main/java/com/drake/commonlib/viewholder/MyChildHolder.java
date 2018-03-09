package com.drake.commonlib.viewholder;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.drake.commonlib.R;
import com.drake.commonlib.model.MyChildItem;
import com.drake.commonlib.model.MyGroupItem;
import com.drake.ui.viewholder.BaseChildViewHolder;

public class MyChildHolder extends BaseChildViewHolder<MyChildItem, MyGroupItem> {

    private TextView mTitle;

    private MyChildItem mItem;

    public MyChildHolder(Context context, View itemView) {
        super(context, itemView);
        mTitle = itemView.findViewById(R.id.id_list_item_child_tv);
    }

    @Override
    public void build(MyGroupItem groupItem, MyChildItem childItem, int groupPosition, int childPosition) {
        mItem = childItem;

        if (mItem == null)
            return;

        if (mTitle != null) {
            mTitle.setText(mItem.title);
        }
    }

}
