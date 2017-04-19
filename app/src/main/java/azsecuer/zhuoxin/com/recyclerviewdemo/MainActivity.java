package azsecuer.zhuoxin.com.recyclerviewdemo;

import android.os.Handler;
import android.os.Message;
import android.support.v4.media.MediaBrowserCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import azsecuer.zhuoxin.com.recyclerviewdemo.helper.ItemTouchHelperCallback;
import q.rorbin.qrefreshlayout.QRefreshLayout;
import q.rorbin.qrefreshlayout.RefreshHandler;

public class MainActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private DragAdapter dragAdapter;
    private ItemTouchHelper helper;
    private List<String> dragList = new ArrayList<>();
    private QRefreshLayout refreshLayout;
    private LinearLayout linearLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        linearLayout= (LinearLayout) findViewById(R.id.activity_main);
        mRecyclerView = (RecyclerView) findViewById(R.id.nested_scroll_recycler);
        mRecyclerView.setFocusable(false);//失去焦点
        refreshLayout= (QRefreshLayout) findViewById(R.id.refresh);
        //加载数据
        initData();
        //自定义的manager
        FullyLinearLayoutManager manager = new FullyLinearLayoutManager(this);
        mRecyclerView.setLayoutManager(manager);
        dragAdapter=new DragAdapter(this,dragList,mRecyclerView);
        mRecyclerView.setAdapter(dragAdapter);
        ItemTouchHelper.Callback callback = new ItemTouchHelperCallback(dragAdapter);
        helper = new ItemTouchHelper(callback);
        helper.attachToRecyclerView(mRecyclerView);

        //点击item的回调监听
        dragAdapter.setListener(new DragAdapter.ItemClicListener() {
            @Override
            public void OnClick(int position, DragAdapter.DragViewHolder holder) {
                Log.i("OnClick","OnClick");
                //判断上一次滑动是否归位，然后才能点击事件
                if(holder.scrollView.getScrollX()!=0){
                    holder.scrollView.setScrollX(0);
                    Toast.makeText(MainActivity.this, "item归位", Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(MainActivity.this, "点击了第"+position+"项", Toast.LENGTH_SHORT).show();
                }
            }

        });


        //上啦加载  下拉刷新
        refreshLayout.setLoadMoreEnable(true);
        refreshLayout.setRefreshHandler(new RefreshHandler() {
            @Override
            public void onRefresh(QRefreshLayout refresh) {
                dragAdapter.notifyDataSetChanged();
                refreshLayout.refreshComplete();
            }

            @Override
            public void onLoadMore(QRefreshLayout refresh) {
                dragAdapter.adddata();
                refreshLayout.LoadMoreComplete();
            }
        });
    }
    private void initData(){
        for (int i = 0; i < 10; i++) {
            dragList.add("可拖拽#"+i);
        }
    }
}
