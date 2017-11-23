package qqkj.qqkj_library.view.recycler.space;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * RecyclerView 间距
 * <p>
 * Created by 陈二狗 on 2017/11/23.
 */

public class RecyclerLinearLayoutDecoration extends RecyclerView.ItemDecoration{

    int _item_space = 0;

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {

        super.getItemOffsets(outRect, view, parent, state);

        outRect.left = _item_space;

        outRect.right = _item_space;

        outRect.bottom = _item_space;

        if (parent.getChildAdapterPosition(view) == 0) {

            outRect.top = _item_space;
        }

    }

    public RecyclerLinearLayoutDecoration(int space) {

        this._item_space = space;
    }
}
