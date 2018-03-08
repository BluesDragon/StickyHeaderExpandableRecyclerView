package com.drake.ui.viewholder;

import android.content.Context;
import android.view.View;

import com.drake.ui.model.IItem;

public abstract class ChildViewHolder extends BaseViewHolder {

    public ChildViewHolder(Context context, View itemView) {
        super(context, itemView);
    }

    @Override
    public final void bind(IItem item, int position) {
    }

    public abstract void build(Object groupItem, Object childItem, int groupPosition, int childPosition);
}
