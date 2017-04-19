package azsecuer.zhuoxin.com.recyclerviewdemo;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.HorizontalScrollView;

/**
 * Created by Administrator on 2017/4/14.
 */

public class MyScrollview extends HorizontalScrollView {
    private boolean isTouch =false;
    private boolean isfirst=true;
    private int lastx=0;
    public MyScrollview(Context context) {
        super(context,null);
    }

    public MyScrollview(Context context, AttributeSet attrs) {
        super(context, attrs,0);
    }

    public MyScrollview(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
    private ScrollListener mListener;

    public static interface ScrollListener {//声明接口，用于传递数据
        public static int SCROLL_STATE_left = 0;
        public static int SCROLL_STATE_right = 1;
        void scrollOritention(int l, int t, int oldl, int oldt);
        void onScrollStateChanged(MyScrollview view,int scrollState);
    }

    public void setmListener(ScrollListener mListener) {
        this.mListener = mListener;
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        if(mListener!=null){
            if(isTouch){
                if(l!=oldl){
                 // 有手指触摸，并且位置有滚动
                    if(l-oldl>20){
                        mListener.onScrollStateChanged(this,ScrollListener.SCROLL_STATE_left);
                    }else {
                        mListener.onScrollStateChanged(this,ScrollListener.SCROLL_STATE_right);
                    }

                }
            }else {
                if(l!=oldl){
                     // 没有手指触摸，并且位置有滚动
                    mListener.onScrollStateChanged(this,ScrollListener.SCROLL_STATE_right);
                    lastx=l;
                }
            }
            mListener.scrollOritention(l,t,oldl,oldt);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getAction()){
            case MotionEvent.ACTION_DOWN:
                break;
            case MotionEvent.ACTION_MOVE:
                isTouch=true;
                isfirst=false;
                Log.i("MotionEvent","MotionEvent");
                break;
            case MotionEvent.ACTION_UP:
                isTouch=false;
                isfirst=true;
                lastx=getScrollX();
                break;
        }
        return super.onTouchEvent(ev);
    }
}
