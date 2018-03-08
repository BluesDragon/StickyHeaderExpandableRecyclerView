package com.drake.ui.model;

import java.util.List;

public abstract class GroupListItem<C extends ChildListItem> implements GroupItem {

    protected int mGroupPosition;

    public int getGroupPosition() {
        return mGroupPosition;
    }

    public void setGroupPosition(int groupPosition) {
        this.mGroupPosition = groupPosition;
    }

    public int getChildCount() {
        List childrenList = getChildrenList();
        if (childrenList != null)
            return childrenList.size();
        return 0;
    }
}
