# StickyHeaderExpandbleRecyclerView

## Maven:
    compile 'com.drake.ui:stickyheaderexpandablerecyclerview:1.0.1'

### This library is based on [CommonRecyclerView](https://github.com/BluesDragon/CommonRecyclerView).

### Init StickyHeaderExpandbleRecyclerView
    StickyHeaderExpandableRecyclerView<MyChildItem, MyGroupItem> recyclerView = findViewById(R.id.id_stick_header_expandable_recyclerView);
    
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
    
    // Load Async
    recyclerView.reloadData();

#### ListFactory
    public class ListFactory {
    
        public static final int VIEW_TYPE_GROUP = 1;
    
        public static final int VIEW_TYPE_CHILD = 2;
    
        public static View getView(Context context, ViewGroup parent, int viewType) {
            switch (viewType) {
                case VIEW_TYPE_GROUP:
                    return LayoutInflater.from(context).inflate(R.layout.layout_list_group, parent, false);
                case VIEW_TYPE_CHILD:
                    return LayoutInflater.from(context).inflate(R.layout.layout_list_child, parent, false);
            }
            return null;
        }
    
        public static RecyclerView.ViewHolder getHolder(Context context, ViewGroup parent, int viewType) {
            View view = getView(context, parent, viewType);
            switch (viewType) {
                case VIEW_TYPE_GROUP:
                    return new MyGroupHolder(context, view);
                case VIEW_TYPE_CHILD:
                    return new MyChildHolder(context, view);
    
            }
            return null;
        }
    }

#### MyGroupHolder
    public class MyGroupHolder extends BaseGroupViewHolder<MyGroupItem> {
    
        private TextView mTitle;
    
        private MyGroupItem mItem;
    
        public MyGroupHolder(Context context, View itemView) {
            super(context, itemView);
            mTitle = itemView.findViewById(R.id.id_list_item_group_tv);
        }
    
        @Override
        public void build(MyGroupItem groupItem, int groupPosition) {
            mItem = groupItem;

            if (mItem == null)
                return;
    
            if (mTitle != null) {
                mTitle.setText(mItem.title);
            }
        }
    
    }
    
#### MyChildHolder
    public class MyChildHolder extends BaseChildViewHolder<MyChildItem, MyGroupItem> {
    
        private TextView mTitle;
    
        private MyChildItem mItem;
    
        public MyChildHolder(Context context, View itemView) {
            super(context, itemView);
            mTitle = itemView.findViewById(R.id.id_list_item_child_tv);
        }
    
        @Override
        public void build(MyGroupItem groupItem, MyChildItem childItem, int groupPosition, int childPosition) {
            mItem = childItem;
    
            if (mItem == null)
                return;
    
            if (mTitle != null) {
                mTitle.setText(mItem.title);
            }
        }
    
    }

#### MyGroupItem
    public class MyGroupItem extends BaseGroupItem<MyChildItem> {

        public String title;
        public List<MyChildItem> mList;

        @Override
        public List<MyChildItem> getChildrenList() {
            return mList;
        }

        @Override
        public int getType() {
            return ListFactory.VIEW_TYPE_GROUP;
        }
    }

#### MyChildItem
    public class MyChildItem extends BaseChildItem {
    
        public String title;
    
        @Override
        public int getType() {
            return ListFactory.VIEW_TYPE_CHILD;
        }
    }

### Load Data
    // Load Async
    recyclerView.reloadData();

### Refresh List Async
    recyclerView.updateListAsync();

### Refresh List Sync (Same to notifyDataSetChange)
    recyclerView.updateList();

### Set your Item list
    recyclerView.setItemList(mList);
    // refresh
    recyclerView.updateList();

### Get CommonRecyclerView
    CommonRecyclerView getRecyclerView();
