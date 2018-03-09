package com.drake.ui.viewholder;

import android.content.Context;
import android.view.View;

import com.drake.ui.model.BaseGroupItem;
import com.drake.ui.model.IItem;

public abstract class BaseGroupViewHolder<G extends BaseGroupItem> extends BaseViewHolder {

    public BaseGroupViewHolder(Context context, View itemView) {
        super(context, itemView);
    }

    @Override
    public final void bind(IItem item, int position) {
    }

    public abstract void build(G groupItem, int groupPosition);
}
