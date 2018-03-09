package com.drake.ui.model;

import java.util.List;

public interface IGroupItem<C extends IChildItem> extends IItem {

    boolean isExpand();

    void setExpand(boolean expand);

    List<C> getChildrenList();

}
