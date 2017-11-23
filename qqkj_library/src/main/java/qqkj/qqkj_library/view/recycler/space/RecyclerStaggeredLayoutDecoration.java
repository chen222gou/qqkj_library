package qqkj.qqkj_library.view.recycler.space;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * RecyclerView 间距
 * <p>
 * Created by 陈二狗 on 2017/11/23.
 */

public class RecyclerStaggeredLayoutDecoration extends RecyclerView.ItemDecoration{

    private int _span_count;
    private int _space;
    private boolean _include_edge;

    public RecyclerStaggeredLayoutDecoration(int _span_count, int _space, boolean _include_edge) {

        this._span_count = _span_count;

        this._space = _space;

        this._include_edge = _include_edge;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {

        int _position = parent.getChildAdapterPosition(view);

        int _column = _position % _span_count;

        if (_include_edge) {

            outRect.left = _space - _column * _space / _span_count;

            outRect.right = (_column + 1) * _space / _span_count;

            if (_position < _span_count) {

                outRect.top = _space;
            }

            outRect.bottom = _space;
        } else {

            outRect.left = _column * _space / _span_count;

            outRect.right = _space - (_column + 1) * _space / _span_count;

            if (_position >= _span_count) {

                outRect.top = _space;
            }
        }
    }
}
