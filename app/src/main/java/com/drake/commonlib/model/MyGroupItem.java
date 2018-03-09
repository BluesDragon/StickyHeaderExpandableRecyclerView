package com.drake.commonlib.model;

import com.drake.commonlib.ListFactory;
import com.drake.commonlib.viewholder.MyGroupHolder;
import com.drake.ui.model.BaseGroupItem;

import java.util.List;

public class MyGroupItem extends BaseGroupItem<MyChildItem> {

    public String title;
    public List<MyChildItem> mList;
    public MyGroupHolder.Callback callback;

    @Override
    public List<MyChildItem> getChildrenList() {
        return mList;
    }

    @Override
    public int getType() {
        return ListFactory.VIEW_TYPE_GROUP;
    }
}
