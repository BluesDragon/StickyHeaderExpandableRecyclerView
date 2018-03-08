package com.drake.ui.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import com.drake.ui.model.ChildItem;
import com.drake.ui.model.GroupItem;
import com.drake.ui.viewholder.ChildViewHolder;
import com.drake.ui.viewholder.GroupViewHolder;

import java.util.List;

/**
 * 默认的可扩展Adapter：仿照RecyclerView的ViewHolder形式加载。
 */
public abstract class DefaultExpandableListAdapter<C extends ChildItem, G extends GroupItem<C>> extends BaseExpandableHeaderListAdapter {

    protected Context mContext;

    public DefaultExpandableListAdapter(Context context, List groupItems) {
        super(groupItems);
        this.mContext = context;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        GroupViewHolder holder;
        int viewType = getGroupViewType(groupPosition);
        if (convertView == null) {
            holder = onCreateGroupViewHolder(parent.getContext(), viewType);
            if (holder != null) {
                convertView = holder.itemView;
                if (convertView != null) {
                    convertView.setTag(holder);
                }
            }
        } else {
            Object tag = convertView.getTag();
            if (tag != null && tag instanceof GroupViewHolder) {
                holder = (GroupViewHolder) tag;
            } else {
                holder = onCreateGroupViewHolder(parent.getContext(), viewType);
                if (holder != null) {
                    convertView = holder.itemView;
                    if (convertView != null) {
                        convertView.setTag(holder);
                    }
                }
            }
        }
        onBindGroupViewHolder(holder, groupPosition);
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        ChildViewHolder holder;
        int viewType = getChildViewType(groupPosition, childPosition);
        if (convertView == null) {
            holder = onCreateChildViewHolder(parent.getContext(), viewType);
            if (holder != null) {
                convertView = holder.itemView;
                if (convertView != null) {
                    convertView.setTag(holder);
                }
            }
        } else {
            Object tag = convertView.getTag();
            if (tag != null && tag instanceof ChildViewHolder) {
                holder = (ChildViewHolder) tag;
            } else {
                holder = onCreateChildViewHolder(parent.getContext(), viewType);
                if (holder != null) {
                    convertView = holder.itemView;
                    if (convertView != null) {
                        convertView.setTag(holder);
                    }
                }
            }
        }
        onBindChildViewHolder(holder, groupPosition, childPosition);
        return convertView;
    }

    public void onBindGroupViewHolder(GroupViewHolder holder, int groupPosition) {
        if (holder != null) {
            holder.build(getGroup(groupPosition), groupPosition);
        }
    }

    public void onBindChildViewHolder(ChildViewHolder holder, int groupPosition, int childPosition) {
        if (holder != null) {
            holder.build(getGroup(groupPosition), getChild(groupPosition, childPosition), groupPosition, childPosition);
        }
    }

    public abstract GroupViewHolder onCreateGroupViewHolder(Context context, int groupViewType);

    public abstract ChildViewHolder onCreateChildViewHolder(Context context, int childViewType);

}
