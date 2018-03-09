package com.drake.ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.drake.ui.layoutmanager.StableLinearLayoutManager;
import com.drake.ui.model.BaseChildItem;
import com.drake.ui.model.BaseGroupItem;
import com.drake.ui.model.IItem;
import com.drake.ui.viewholder.BaseChildViewHolder;
import com.drake.ui.viewholder.BaseGroupViewHolder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 可打开/关闭组的、粘性header的RecyclerView
 */
public class StickyHeaderExpandableRecyclerView<C extends BaseChildItem, G extends BaseGroupItem<C>> extends FrameLayout {

    public static abstract class Callback<G extends BaseGroupItem> {
        public abstract RecyclerView.ViewHolder getHolder(Context context, ViewGroup parent, int viewType);

        public abstract void buildList(List<G> list);

        public void onDataSetChanged() {
        }
    }

    private static final boolean USE_HEADER = true;

    private CommonRecyclerView mCommonRecyclerView;
    private final Map<Integer, RecyclerView.ViewHolder> mHeaderHolderMap = new HashMap<>();

    private StableLinearLayoutManager mLayoutManager;
    private Callback mCallback;
    private RecyclerView.ViewHolder mHeaderHolder;

    private final List<G> mGroupList = new ArrayList<>();
    private int mCurrentGroupPosition = -1;// 记录Header的position, 避免重复刷新
    private int mCurrentTranslationY;// 记录Header的位移大小, 避免重复刷新
    private boolean useStickyHeader = USE_HEADER;// 是否固定组

    private static final int MSG_UPDATE_HEADER = 1;
    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_UPDATE_HEADER:
                    if (useStickyHeader && mHeaderHolder == null && !isEmpty()) {
                        int itemViewType = mCommonRecyclerView.getItemViewType(0);
                        mHeaderHolder = initHeader(itemViewType);
                    }

                    if (mHeaderHolder != null && mHeaderHolder.itemView != null) {
                        if (isEmpty()) {
                            mHeaderHolder.itemView.setVisibility(View.GONE);
                        } else {
                            mHeaderHolder.itemView.setVisibility(View.VISIBLE);
                        }
                        mHeaderHolder.itemView.requestLayout();
                    }
                    forceUpdateHeader = true;
                    updateHeader();
                    break;
            }
        }
    };

    public StickyHeaderExpandableRecyclerView(Context context) {
        super(context);
        init(context);
    }

    public StickyHeaderExpandableRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public StickyHeaderExpandableRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    private void init(Context context) {
        initRecyclerView(context);
        LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        mCommonRecyclerView.setLayoutParams(layoutParams);
        addView(mCommonRecyclerView);
    }

    private void initRecyclerView(Context context) {
        mLayoutManager = new StableLinearLayoutManager(context) {

            @Override
            public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
                super.onLayoutChildren(recycler, state);
                updateHeader();
            }
        };
        mCommonRecyclerView = new CommonRecyclerView(context) {
            @Override
            protected RecyclerView.LayoutManager initLayoutManager(Context context) {
                return mLayoutManager;
            }
        };
        mCommonRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                updateHeader();
            }
        });
        mCommonRecyclerView.setCallback(mCommonRecyclerViewCallback);
    }

    private void updateHeader() {
        if (!useStickyHeader)
            return;
        RecyclerView.Adapter adapter = mCommonRecyclerView.getAdapter();
        if (mLayoutManager != null && mCallback != null && adapter != null) {
            int currentFirstPosition = mLayoutManager.findFirstVisibleItemPosition();
            if (currentFirstPosition < 0) {
                return;
            }

            View firstVisibleView = mLayoutManager.findViewByPosition(currentFirstPosition);
            if (firstVisibleView == null)
                return;
            View secondVisibleView = mLayoutManager.findViewByPosition(currentFirstPosition + 1);

            RecyclerView.ViewHolder firstVisibleHolder = mCommonRecyclerView.getChildViewHolder(firstVisibleView);
            int currentGroupPosition = 0;
            if (firstVisibleHolder instanceof BaseGroupViewHolder) {
                currentGroupPosition = currentFirstPosition;
            } else {
                List<IItem> list = mCommonRecyclerView.getItemList();
                if (currentFirstPosition < list.size()) {
                    IItem iItem = list.get(currentFirstPosition);
                    if (iItem != null && iItem instanceof BaseChildItem) {
                        BaseChildItem childListItem = (BaseChildItem) iItem;
                        synchronized (mGroupList) {
                            if (childListItem.getGroupPosition() < mGroupList.size()) {
                                BaseGroupItem groupListItem = mGroupList.get(childListItem.getGroupPosition());
                                currentGroupPosition = list.indexOf(groupListItem);
                            }
                        }
                    }
                }
            }

            if (this.mCurrentGroupPosition != currentGroupPosition || forceUpdateHeader) {
                this.mCurrentGroupPosition = currentGroupPosition;
                forceUpdateHeader = false;
                adapter.onBindViewHolder(mHeaderHolder, currentGroupPosition);
            }

            if (mHeaderHolder != null) {
                RecyclerView.ViewHolder secondVisibleHolder = null;
                if (secondVisibleView != null) {
                    secondVisibleHolder = mCommonRecyclerView.getChildViewHolder(secondVisibleView);
                }
                int translationY;
                if (secondVisibleHolder != null && secondVisibleHolder instanceof BaseGroupViewHolder) {
                    int top = secondVisibleView.getTop();
                    int height = secondVisibleView.getHeight();
                    if (top < height) {
                        translationY = -(height - top);
                    } else {
                        translationY = 0;
                    }
                } else {
                    translationY = 0;
                }
                if (mCurrentTranslationY != translationY) {
                    mCurrentTranslationY = translationY;
                    mHeaderHolder.itemView.setTranslationY(translationY);
                }
            }
        }
    }

    private RecyclerView.ViewHolder initHeader(int viewType) {
        if (mCallback == null)
            return null;
        RecyclerView.ViewHolder holder = mCallback.getHolder(getContext(), this, viewType);
        if (holder.itemView.getLayoutParams() == null) {
            LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
            holder.itemView.setLayoutParams(params);
        }
        addView(holder.itemView);
        mHeaderHolderMap.put(viewType, holder);
        return holder;
    }

    private CommonRecyclerView.Callback mCommonRecyclerViewCallback = new CommonRecyclerView.Callback() {

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(Context context, ViewGroup parent, int viewType) {
            if (mCallback != null) {
                return mCallback.getHolder(context, parent, viewType);
            }
            return null;
        }

        @Override
        public boolean onBindViewHolder(RecyclerView.ViewHolder holder, IItem item, int position) {
            if (holder != null) {
                if (item != null) {
                    if (item instanceof BaseGroupItem) {
                        BaseGroupItem groupItem = (BaseGroupItem) item;
                        if (holder instanceof BaseGroupViewHolder) {
                            BaseGroupViewHolder groupViewHolder = (BaseGroupViewHolder) holder;
                            groupViewHolder.build(groupItem, groupItem.getGroupPosition());
                        }
                    } else if (item instanceof BaseChildItem) {
                        BaseChildItem childItem = (BaseChildItem) item;
                        if (holder instanceof BaseChildViewHolder) {
                            BaseChildViewHolder childViewHolder = (BaseChildViewHolder) holder;
                            synchronized (mGroupList) {
                                if (mGroupList.size() > childItem.getGroupPosition())
                                    childViewHolder.build(mGroupList.get(childItem.getGroupPosition()), childItem, childItem.getGroupPosition(), childItem.getChildPosition());
                            }
                        }
                    }
                }
            }
            return true;
        }

        @Override
        public void buildList(List<IItem> list) {
            if (mCallback != null) {
                synchronized (mGroupList) {
                    mGroupList.clear();
                    mCallback.buildList(mGroupList);
                }
            }
        }

        @Override
        public void filterList(List<IItem> list) {
            if (list == null)
                return;
            List<IItem> tempList = new ArrayList<>();
            synchronized (mGroupList) {
                for (int i = 0; i < mGroupList.size(); i++) {
                    BaseGroupItem<C> groupItem = mGroupList.get(i);
                    if (groupItem != null) {
                        groupItem.setGroupPosition(i);
                        tempList.add(groupItem);
                        List<C> childrenList = groupItem.getChildrenList();
                        if (childrenList != null) {
                            if (groupItem.isExpand()) {
                                for (int j = 0; j < childrenList.size(); j++) {
                                    BaseChildItem childItem = childrenList.get(j);
                                    if (childItem != null) {
                                        childItem.setGroupPosition(i);
                                        childItem.setChildPosition(j);
                                    }
                                }
                                tempList.addAll(childrenList);
                            }
                        }
                    }
                }
            }
            list.clear();
            list.addAll(tempList);
        }

        @Override
        public void onDataSetChanged() {
            checkUpdateHeaderDelay();
            if (mCallback != null) {
                mCallback.onDataSetChanged();
            }
        }
    };

    private void checkUpdateHeaderDelay() {
        long delay = 0;
        if (mHeaderHolder == null) {
            // 延迟处理，避免与RecyclerView同时处理布局导致header显示不完全。
            delay = 300;
        }
        if (mHandler != null) {
            mHandler.sendEmptyMessageDelayed(MSG_UPDATE_HEADER, delay);
        }
    }

    /**********************************************************************************************/

    public void reloadData() {
        if (mCommonRecyclerView != null)
            mCommonRecyclerView.reloadData();
    }

    private boolean forceUpdateHeader;

    public void updateList() {
        if (mCommonRecyclerView != null)
            mCommonRecyclerView.updateList();
        checkUpdateHeaderDelay();
    }

    public void updateListAsync() {
        forceUpdateHeader = true;
        if (mCommonRecyclerView != null)
            mCommonRecyclerView.updateListAsync();
    }

    public void setItemList(List list) {
        synchronized (mGroupList) {
            mGroupList.clear();
            mGroupList.addAll(list);
        }
        updateList();
    }

    public void setCallback(Callback callback) {
        this.mCallback = callback;
    }

    public void setUseStickyHeader(boolean useStickyHeader) {
        this.useStickyHeader = useStickyHeader;
        if (!useStickyHeader) {
            if (mHeaderHolder != null) {
                try {
                    removeView(mHeaderHolder.itemView);
                } catch (Exception e) {
                    // do nothing.
                }
            }
        }
    }

    /**
     * 根据Adapter中的位置，删除一条数据
     *
     * @param position 在Adapter中的位置
     */
    public void removeItemByAdapterPosition(int position) {
        removeItemByAdapterPosition(position, true);
    }

    /**
     * 根据Adapter中的位置，删除一条数据
     *
     * @param position  在Adapter中的位置
     * @param refreshUI 是否刷新ui
     */
    public void removeItemByAdapterPosition(int position, boolean refreshUI) {
        if (mCommonRecyclerView != null) {
            IItem item = mCommonRecyclerView.getItem(position);
            if (item instanceof BaseGroupItem) {
                BaseGroupItem groupListItem = (BaseGroupItem) item;
                removeGroupItem(groupListItem, refreshUI);
            } else if (item instanceof BaseChildItem) {
                BaseChildItem childListItem = (BaseChildItem) item;
                removeChildItem(childListItem, refreshUI);
            }
        }
    }

    /**
     * 删除一组数据
     */
    public void removeGroupItem(BaseGroupItem groupListItem) {
        removeGroupItem(groupListItem, true);
    }

    /**
     * 删除一组数据
     */
    public void removeGroupItem(int groupPosition) {
        removeGroupItem(groupPosition, true);
    }

    /**
     * 删除一组数据
     */
    public void removeGroupItem(int groupPosition, boolean refreshUI) {
        removeGroupItem(getGroupItem(groupPosition), refreshUI);
    }

    /**
     * 删除一组数据
     *
     * @param refreshUI 是否刷新ui
     */
    public void removeGroupItem(BaseGroupItem groupListItem, boolean refreshUI) {
        if (groupListItem == null)
            return;
        synchronized (mGroupList) {
            if (mGroupList.contains(groupListItem)) {
                mGroupList.remove(groupListItem);
                if (mCommonRecyclerView != null) {
                    mCommonRecyclerView.removeItemImmediately(groupListItem);
                    mCommonRecyclerView.removeItemImmediately(groupListItem.getChildrenList());
                }
                if (refreshUI) {
                    updateList();
                }
            }
        }
    }

    /**
     * 删除一条数据
     */
    public void removeChildItem(BaseChildItem childListItem) {
        removeChildItem(childListItem, true);
    }

    /**
     * 删除一条数据
     *
     * @param refreshUI 是否刷新ui
     */
    public void removeChildItem(BaseChildItem childListItem, boolean refreshUI) {
        if (childListItem == null)
            return;
        BaseGroupItem groupListItem = getGroupItem(childListItem.getGroupPosition());
        List childrenList = groupListItem.getChildrenList();
        if (childrenList != null) {
            if (childrenList.contains(childListItem)) {
                childrenList.remove(childListItem);
                if (mCommonRecyclerView != null) {
                    mCommonRecyclerView.removeItemImmediately(childListItem);
                }
            }
            if (childrenList.isEmpty()) {
                removeGroupItem(groupListItem, false);
            }
        }
        if (refreshUI) {
            updateList();
        }
    }

    public void removeChildItem(int groupPosition, int childPosition) {
        removeChildItem(groupPosition, childPosition, true);
    }

    public void removeChildItem(int groupPosition, int childPosition, boolean refreshUI) {
        BaseChildItem childItem = getChildItem(groupPosition, childPosition);
        if (childItem != null) {
            removeChildItem(childItem, refreshUI);
        }
    }

    public void clearList() {
        synchronized (mGroupList) {
            mGroupList.clear();
        }
        if (mCommonRecyclerView != null) {
            mCommonRecyclerView.clearList();
        }
    }

    public int getGroupItemCount() {
        synchronized (mGroupList) {
            return mGroupList.size();
        }
    }

    public int getChildItemCount() {
        int size = 0;
        synchronized (mGroupList) {
            if (mGroupList != null) {
                for (BaseGroupItem groupListItem : mGroupList) {
                    try {
                        if (groupListItem != null) {
                            size += groupListItem.getChildCount();
                        }
                    } catch (Exception ex) {
                    }
                }
            }
        }

        return size;
    }

    public boolean isEmpty() {
        synchronized (mGroupList) {
            return mGroupList.isEmpty();
        }
    }

    public CommonRecyclerView getRecyclerView() {
        return mCommonRecyclerView;
    }

    public G getGroupItem(int groupPosition) {
        synchronized (mGroupList) {
            if (groupPosition >= 0 && groupPosition < mGroupList.size())
                return mGroupList.get(groupPosition);
        }
        return null;
    }

    public C getChildItem(int groupPosition, int childPosition) {
        BaseGroupItem<C> groupItem = getGroupItem(groupPosition);
        if (groupItem != null) {
            List<C> childrenList = groupItem.getChildrenList();
            if (childrenList != null && childPosition >= 0 && childPosition < childrenList.size()) {
                return childrenList.get(childPosition);
            }
        }
        return null;
    }

    public IItem getChildItemByAdapterPosition(int position) {
        if (mCommonRecyclerView != null) {
            IItem item = mCommonRecyclerView.getItem(position);
            if (item != null && item instanceof BaseChildItem) {
                return item;
            }
        }
        return null;
    }

    /**********************************************************************************************/


    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (mHandler != null) {
            mHandler.removeCallbacksAndMessages(null);
        }
    }
}
