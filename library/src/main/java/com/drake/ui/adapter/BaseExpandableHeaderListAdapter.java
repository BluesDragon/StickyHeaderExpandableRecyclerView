package com.drake.ui.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;

import com.drake.ui.model.ChildItem;
import com.drake.ui.model.GroupItem;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseExpandableHeaderListAdapter<C extends ChildItem, G extends GroupItem<C>>
        extends BaseExpandableListAdapter {

    private List<G> groupItems = new ArrayList<>();

    public BaseExpandableHeaderListAdapter(List<G> groupItems) {
        setItemList(groupItems);
    }

    public void setItemList(List<G> groupItems) {
        this.groupItems.clear();
        if (groupItems != null)
            this.groupItems.addAll(groupItems);
    }

    @Override
    public int getGroupCount() {
        int groupCount = 0;
        if (groupItems != null && groupItems.size() > 0) {
            groupCount = groupItems.size();
        }
        return groupCount;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        int childrenCount = 0;
        if (groupItems != null && groupPosition >= 0 && groupPosition < groupItems.size()) {
            G g = groupItems.get(groupPosition);
            if (g != null) {
                List<C> childrenList = g.getChildrenList();
                if (childrenList != null) {
                    childrenCount = childrenList.size();
                }
            }
        }
        return childrenCount;
    }

    @Override
    public G getGroup(int groupPosition) {
        if (groupItems != null && groupPosition >= 0 && groupPosition < groupItems.size()) {
            try {
                return groupItems.get(groupPosition);
            } catch (Exception e) {
            }
        }
        return null;
    }

    @Override
    public C getChild(int groupPosition, int childPosition) {
        if (groupItems != null && groupPosition >= 0 && groupPosition < groupItems.size()) {
            try {
                G g = groupItems.get(groupPosition);
                if (g != null) {
                    List<C> childrenList = g.getChildrenList();
                    if (childrenList != null && childPosition < childrenList.size()) {
                        return childrenList.get(childPosition);
                    }
                }
            } catch (Exception e) {
            }
        }
        return null;
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    @Override
    public abstract View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent);

    @Override
    public abstract View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent);

    public int getGroupViewType(int groupPosition) {
        G groupItem = getGroup(groupPosition);
        if (groupItem != null) {
            return groupItem.getType();
        }
        return 0;
    }

    public int getChildViewType(int groupPosition, int childPosition) {
        C childItem = getChild(groupPosition, childPosition);
        if (childItem != null) {
            return childItem.getType();
        }
        return 0;
    }

    public void removeChild(int groupPosition, int childPosition) {
        G group = getGroup(groupPosition);
        if (group != null) {
            List<C> childrenList = group.getChildrenList();
            if (childrenList != null && childPosition >= 0 && childPosition < childrenList.size()) {
                childrenList.remove(childPosition);
                if (childrenList.isEmpty()) {
                    removeGroup(groupPosition);
                }
            }
        }
    }

    public void removeGroup(int groupPosition) {
        if (groupItems != null && groupPosition >= 0 && groupPosition < groupItems.size()) {
            groupItems.remove(groupPosition);
        }
    }
}
