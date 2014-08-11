
    package com.docdeal.activity;   
      
    import org.apache.poi.poifs.property.Child;

import com.docdeal.R;

import android.annotation.SuppressLint;
import android.app.ExpandableListActivity;   
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;   
import android.view.ContextMenu;   
import android.view.Gravity;   
import android.view.MenuItem;   
import android.view.View;   
import android.view.ViewGroup;   
import android.view.ContextMenu.ContextMenuInfo;   
import android.view.ViewGroup.LayoutParams;
import android.widget.AbsListView;   
import android.widget.BaseExpandableListAdapter;   
import android.widget.ExpandableListAdapter;   
import android.widget.ExpandableListView;   
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.TextView;   
import android.widget.Toast;   
import android.widget.ExpandableListView.ExpandableListContextMenuInfo;   
      
    @SuppressLint("NewApi")
	public class DocContact extends ExpandableListActivity {   
        //声明adapter   
        private ExpandableListAdapter mAdapter; 
        private SearchView sv;
        @Override  
        protected void onCreate(Bundle savedInstanceState) {   
            super.onCreate(savedInstanceState);  
            setContentView(R.layout.contact);
            //设这标题   
            setTitle("个人通讯录");   
            //实例化adapter   
            mAdapter = new MyExpandableListAdapter();  
            sv=new SearchView(this);
            //为列表设置adapter   
            setListAdapter(mAdapter);  
            //为list注册context菜单   
            registerForContextMenu(this.getExpandableListView());   
        }   
           
           
        @Override  
        public boolean onChildClick(ExpandableListView parent, View v,   
                int groupPosition, int childPosition, long id) {   
            Toast.makeText(this, mAdapter.getChildId(groupPosition, childPosition)+"", Toast.LENGTH_SHORT).show(); 
            Bundle bundle=new Bundle();
            bundle.putString("id", String.valueOf(mAdapter.getChildId(groupPosition, childPosition)));
            startActivity(new Intent(DocContact.this,
            		DocContactDetail.class).putExtras(bundle));
            return super.onChildClick(parent, v, groupPosition, childPosition, id);   
        }   
      
      
        @Override  
        public void onGroupExpand(int groupPosition) {   
            Toast.makeText(this, " 组元素索引: " + groupPosition, Toast.LENGTH_SHORT).show();   
            super.onGroupExpand(groupPosition);   
        }   
      
        @Override  
        public void onCreateContextMenu(ContextMenu menu, View v,   
                ContextMenuInfo menuInfo) {   
            menu.setHeaderTitle("上下文菜单");   
            menu.add(0, 0, 0, "单击我");   
        }   
      
        // 单击上下文菜单后的逻辑   
        @Override  
        public boolean onContextItemSelected(MenuItem item) {   
            ExpandableListContextMenuInfo info = (ExpandableListContextMenuInfo) item.getMenuInfo();   
            String title = ((TextView) info.targetView).getText().toString();   
      
            int type = ExpandableListView.getPackedPositionType(info.packedPosition);   
            if (type == ExpandableListView.PACKED_POSITION_TYPE_CHILD) {   
                int groupPos = ExpandableListView.getPackedPositionGroup(info.packedPosition);   
                int childPos = ExpandableListView.getPackedPositionChild(info.packedPosition);   
                Toast.makeText(this, title + " 组元素索引: " + groupPos + " 子元素索引: " + childPos, Toast.LENGTH_SHORT).show(); 
                return true;   
            } else if (type == ExpandableListView.PACKED_POSITION_TYPE_GROUP) {   
                int groupPos = ExpandableListView.getPackedPositionGroup(info.packedPosition);   
                Toast.makeText(this, title + " 组元素索引: " + groupPos , Toast.LENGTH_SHORT).show();   
                return true;   
            }   
            return false;   
        }   
        //自定义Adapter   
        public class MyExpandableListAdapter extends BaseExpandableListAdapter {   
            // 父列表数据   
            private String[] groups =    
            {    
                "同学",    
                "亲人",    
                "朋友",   
                "同事"    
            };   
            // 子列表数据   
            private String[][] children =    
            {   
                { "张三" },   
                { "张三", "张三","张三"},   
                { "张三", "张三", "张三" },   
                { "张三", "张三", "张三", "张三" }   
            };   
            public long[][] values =    
                {   
                    { 18602748078L },   
                    { 18602748078L, 18602748078L,18602748078L},   
                    { 18602748078L, 18602748078L, 18602748078L },   
                    { 18602733378L, 18602748078L, 18602748078L, 18602748078L }   
                }; 
            @Override  
            public Object getChild(int groupPosition, int childPosition) {   
                return children[groupPosition][childPosition];   
            }      
            @Override  
            public long getChildId(int groupPosition, int childPosition) {   
                return values[groupPosition][childPosition];   
            }   
            @Override  
            public int getChildrenCount(int groupPosition) {   
                return children[groupPosition].length;   
            }   
            // 取子列表中的某一项的 View   
            @Override  
            public View getChildView(int groupPosition, int childPosition,   
                    boolean isLastChild, View convertView, ViewGroup parent) {   
                TextView textView = getGenericView();   
                textView.setBackgroundColor(Color.WHITE);
                textView.setText(getChild(groupPosition, childPosition).toString());
                return textView;   
            }   
            @Override  
            public Object getGroup(int groupPosition) {   
                return groups[groupPosition];   
            }   
            @Override  
            public int getGroupCount() {   
                return groups.length;   
            }   
            @Override  
            public long getGroupId(int groupPosition) {   
                return groupPosition;   
            }   
            // 取父列表中的某一项的 View   
            @Override  
            public View getGroupView(int groupPosition, boolean isExpanded,   
                    View convertView, ViewGroup parent) {  
            	int w=getWindowManager().getDefaultDisplay().getWidth();
                TextView textView = getGenericView();              
                textView.setText(getGroup(groupPosition).toString());
                textView.setBackgroundResource(R.drawable.tablestyle2);
                textView.setPadding(w*1/10, 0, 0, 0);
                return textView;   
            }   
            @Override  
            public boolean hasStableIds() {   
                return true;   
            }   
            @Override  
            public boolean isChildSelectable(int groupPosition, int childPosition) {   
                return true;   
            }   
            // 获取某一项的 View 的逻辑   
            private TextView getGenericView() {   
            	int h=getWindowManager().getDefaultDisplay().getHeight();
                AbsListView.LayoutParams lp = new AbsListView.LayoutParams(   
                        ViewGroup.LayoutParams.MATCH_PARENT, h*3/40);   
                TextView textView = new TextView(DocContact.this);   
                textView.setLayoutParams(lp);   
                textView.setGravity(Gravity.CENTER_VERTICAL | Gravity.LEFT);   
                textView.setPadding(32, 0, 0, 0);  
                return textView;   
            }   
        }   
    }  
