package com.drake.ui.model;

import java.util.List;

public abstract class BaseGroupItem<C extends BaseChildItem> implements IGroupItem {

    protected int mGroupPosition;
    protected boolean isExpand = true;

    @Override
    public boolean isExpand() {
        return isExpand;
    }

    @Override
    public void setExpand(boolean expand) {
        this.isExpand = expand;
    }

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

    public abstract List<C> getChildrenList();
}
