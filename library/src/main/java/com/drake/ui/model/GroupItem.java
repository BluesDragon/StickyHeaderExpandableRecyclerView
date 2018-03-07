package com.drake.ui.model;

import java.util.List;

public interface GroupItem<C extends ChildItem> extends IItem {

    boolean isExpand();

    void setExpand(boolean closed);

    List<C> getChildrenList();

}
