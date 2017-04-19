package azsecuer.zhuoxin.com.recyclerviewdemo;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Collections;
import java.util.List;

import azsecuer.zhuoxin.com.recyclerviewdemo.helper.ItemTouchHelperAdapter;

/**
 * Created by Karl on 2016/9/8.
 */
public class DragAdapter extends RecyclerView.Adapter<DragAdapter.DragViewHolder> implements ItemTouchHelperAdapter{

    private Context context;
    private List<String> mList;
    private LayoutInflater inflater;
    private int num=0;
    private ItemClicListener listener;
    private int mposition,time=1;
    private RecyclerView recyclerView;
    private DragViewHolder holder;

    public void setListener(ItemClicListener listener) {
        this.listener = listener;
    }

    public DragAdapter(Context context, List<String> mList,RecyclerView recyclerView) {
        this.context = context;
        this.mList = mList;
        this.recyclerView=recyclerView;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public DragViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.layout_item,parent,false);
        DragViewHolder  holder = new DragViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final DragViewHolder holder, final int position) {
        this.holder=holder;
        holder.textView.setText(mList.get(position));
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mList.remove(mList.get(position));
//                ishua=true;
                //自带动画删除某一项
                notifyItemRemoved(position);
                holder.scrollView.setScrollX(0);//防止加载数据的时候刷出来的item是滑开的状态
                //重新绑定positon-1项到mList.size()-1项之间的item
                notifyItemRangeChanged(position-2,mList.size()-1);
            }
        });
        holder.bianji.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, "编辑", Toast.LENGTH_SHORT).show();
            }
        });

        //滑动监听
        holder.scrollView.setmListener(new MyScrollview.ScrollListener() {
            @Override
            public void scrollOritention(int l, int t, int oldl, int oldt) {
                Log.i("scrollOritention","现在的x="+l+",现在的t");
            }
            @Override
            public void onScrollStateChanged(MyScrollview view, int scrollState) {
                if(scrollState== MyScrollview.ScrollListener.SCROLL_STATE_left){
                    if(time==1){
                        //第一次滑动item
                        holder.scrollView.setScrollX(holder.scrollView.getWidth());
                        mposition=position;//得到这次滑动的position
                        time=2;
                    }else if(time==2){
                        //第二次滑动item
                        holder.scrollView.setScrollX(holder.scrollView.getWidth());
                        //得到上一次滑动的holder，判断是否归位
                        if(getHolser(mposition).scrollView.getScrollX()!=0){
                            getHolser(mposition).scrollView.setScrollX(0);
                            time=1;
                            mposition=position;
                            Log.i("onScrollStateChanged","之前的position="+mposition);
                        }
                    }
                         Log.i("onScrollStateChanged","现在的position="+position);
                }else if(scrollState== MyScrollview.ScrollListener.SCROLL_STATE_right){
                    holder.scrollView.setScrollX(0);
                    Log.i("11111","111111");
                }
                mposition=position;
            }
        });
        //判断当前点击项是否需要归位，传入上一次滑动的holder，判断是否需要归位
        holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(holder.scrollView.getScrollX()!=0){
                    holder.scrollView.setScrollX(0);
                }else {
                    if (listener != null) {
                        listener.OnClick(position,getHolser(mposition));
                    }
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    @Override
    public boolean onItemMove(int fromPosition, int toPosition) {
        /**
         * 拖拽后，切换位置，数据排序
         */
        Collections.swap(mList,fromPosition,toPosition);
        notifyItemMoved(fromPosition,toPosition);

        return true;

    }

    @Override
    public void onItemDismiss(int position) {
        /**
         * 移除之前的数据
         */
        mList.remove(position);
        notifyItemRemoved(position);

    }

    public static class DragViewHolder extends RecyclerView.ViewHolder{
        TextView textView,delete,bianji;
        RelativeLayout relativeLayout;
        MyScrollview scrollView;

        public DragViewHolder(View itemView) {
            super(itemView);
            delete= (TextView) itemView.findViewById(R.id.tv_delete);
            textView = (TextView) itemView.findViewById(R.id.text);
            bianji = (TextView) itemView.findViewById(R.id.tv_bianji);
            relativeLayout= (RelativeLayout) itemView.findViewById(R.id.layout_content);
            scrollView= (MyScrollview) itemView.findViewById(R.id.scroll);
        }
    }
    public void adddata(){
        num+=1;
        for (int i = 0; i <10 ; i++) {
            mList.add("刷新出来的第"+num+"次数据"+i);
        }
//        notifyItemInserted(1);//有问题待解决
        notifyItemRangeChanged(mList.size()-10,mList.size());//刷新加载出来的数据
    }
    interface ItemClicListener{
        void OnClick(int position,DragViewHolder holder);
    }
    //通过position得到点击的holder
    private DragViewHolder getHolser(int position){
        DragViewHolder holder=(DragViewHolder)recyclerView.getChildViewHolder(recyclerView.getChildAt(position));
        return holder;
    }
}
