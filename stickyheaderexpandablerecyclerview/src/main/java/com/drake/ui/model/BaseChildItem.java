package com.drake.ui.model;

public abstract class BaseChildItem implements IChildItem {

    protected int mGroupPosition;
    protected int mChildPosition;

    public int getChildPosition() {
        return mChildPosition;
    }

    public void setChildPosition(int position) {
        this.mChildPosition = position;
    }

    public int getGroupPosition() {
        return mGroupPosition;
    }

    public void setGroupPosition(int position) {
        this.mGroupPosition = position;
    }

}
