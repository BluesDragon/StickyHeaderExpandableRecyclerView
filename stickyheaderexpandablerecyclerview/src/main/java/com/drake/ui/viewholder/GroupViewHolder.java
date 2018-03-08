package com.drake.ui.viewholder;

import android.content.Context;
import android.view.View;
import com.drake.ui.model.GroupItem;
import com.drake.ui.model.IItem;

public abstract class GroupViewHolder extends BaseViewHolder {

    public GroupViewHolder(Context context, View itemView) {
        super(context, itemView);
    }

    @Override
    public final void bind(IItem item, int position) {
    }

    public abstract void build(GroupItem groupItem, int groupPosition);
}
