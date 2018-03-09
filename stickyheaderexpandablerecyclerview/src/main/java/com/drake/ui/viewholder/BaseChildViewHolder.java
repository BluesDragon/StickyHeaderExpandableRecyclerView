package com.drake.ui.viewholder;

import android.content.Context;
import android.view.View;

import com.drake.ui.model.BaseChildItem;
import com.drake.ui.model.BaseGroupItem;
import com.drake.ui.model.IItem;

public abstract class BaseChildViewHolder<C extends BaseChildItem, G extends BaseGroupItem<C>> extends BaseViewHolder {

    public BaseChildViewHolder(Context context, View itemView) {
        super(context, itemView);
    }

    @Override
    public final void bind(IItem item, int position) {
    }

    public abstract void build(G groupItem, C childItem, int groupPosition, int childPosition);
}
