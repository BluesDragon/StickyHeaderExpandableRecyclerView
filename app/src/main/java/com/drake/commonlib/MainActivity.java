package com.drake.commonlib;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.drake.commonlib.model.MyChildItem;
import com.drake.commonlib.model.MyGroupItem;
import com.drake.commonlib.viewholder.MyGroupHolder;
import com.drake.ui.StickyHeaderExpandableRecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private StickyHeaderExpandableRecyclerView<MyChildItem, MyGroupItem> recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.id_stick_header_expandable_recyclerView);

        recyclerView.setCallback(new StickyHeaderExpandableRecyclerView.Callback<MyGroupItem>() {

            @Override
            public RecyclerView.ViewHolder getHolder(Context context, ViewGroup parent, int viewType) {
                // TODO Add your ViewHolder like this:
                return ListFactory.getHolder(context, parent, viewType);
            }

            @Override
            public void buildList(List<MyGroupItem> list) {
                // TODO Add your Item like this:
                for (int i = 0; i < 5; i++) {
                    MyGroupItem groupItem = new MyGroupItem();
                    groupItem.callback = mGroupCallback;
                    groupItem.title = "Group" + i;
                    groupItem.mList = new ArrayList<>();
                    for (int j = 0; j < 10; j++) {
                        MyChildItem childItem = new MyChildItem();
                        childItem.title = "Child-->" + j;
                        groupItem.mList.add(childItem);
                    }
                    list.add(groupItem);
                }
            }

        });

        recyclerView.reloadData();
    }

    private MyGroupHolder.Callback mGroupCallback = new MyGroupHolder.Callback() {
        @Override
        public void onGroupClick(MyGroupHolder groupHolder, MyGroupItem groupItem) {
            groupItem.setExpand(!groupItem.isExpand());
            if (recyclerView != null) {
                recyclerView.updateListAsync();
            }
        }
    };
}
