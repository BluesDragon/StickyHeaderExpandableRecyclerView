package com.drake.commonlib;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.drake.commonlib.viewholder.MyChildHolder;
import com.drake.commonlib.viewholder.MyGroupHolder;

public class ListFactory {

    public static final int VIEW_TYPE_GROUP = 1;

    public static final int VIEW_TYPE_CHILD = 2;

    public static View getView(Context context, ViewGroup parent, int viewType) {
        switch (viewType) {
            case VIEW_TYPE_GROUP:
                return LayoutInflater.from(context).inflate(R.layout.layout_list_group, parent, false);
            case VIEW_TYPE_CHILD:
                return LayoutInflater.from(context).inflate(R.layout.layout_list_child, parent, false);
        }
        return null;
    }

    public static RecyclerView.ViewHolder getHolder(Context context, ViewGroup parent, int viewType) {
        View view = getView(context, parent, viewType);
        switch (viewType) {
            case VIEW_TYPE_GROUP:
                return new MyGroupHolder(context, view);
            case VIEW_TYPE_CHILD:
                return new MyChildHolder(context, view);

        }
        return null;
    }
}
