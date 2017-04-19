package azsecuer.zhuoxin.com.recyclerviewdemo.helper;

/**
 * Created by Karl on 2016/9/8.
 */
public interface ItemTouchHelperAdapter {
    boolean onItemMove(int fromPosition, int toPosition);
    void onItemDismiss(int position);
}
