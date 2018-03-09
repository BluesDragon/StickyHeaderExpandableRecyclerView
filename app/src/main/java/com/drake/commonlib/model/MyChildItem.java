package com.drake.commonlib.model;

import com.drake.commonlib.ListFactory;
import com.drake.ui.model.BaseChildItem;

public class MyChildItem extends BaseChildItem {

    public String title;

    @Override
    public int getType() {
        return ListFactory.VIEW_TYPE_CHILD;
    }
}
