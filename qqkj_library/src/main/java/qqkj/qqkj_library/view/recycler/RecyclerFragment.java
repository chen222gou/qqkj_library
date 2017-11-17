package qqkj.qqkj_library.view.recycler;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import qqkj.qqkj_library.R;

/**
 * 这个类是用来做甚的
 * <p>
 * Created by 陈二狗 on 2017/11/17.
 */

public abstract class RecyclerFragment extends Fragment {

    public SmartRefreshLayout _smart_layout = null;

    public RecyclerView _recycler_view = null;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View _view = inflater.inflate(R.layout.recycler_layout, container, false);

        _init_layout(_view);

        _get_data();

        return _view;
    }


    private void _init_layout(View _view){

        _recycler_view = (RecyclerView) _view.findViewById(R.id._qqkj_recycler_view);

        _recycler_view.setLayoutManager(new LinearLayoutManager(getContext()));

        _smart_layout = (SmartRefreshLayout) _view.findViewById(R.id._qqkj_smart_view);

        //设置显示是否加载更多

        if(_get_load_time() > 0){

            _smart_layout.setEnableLoadmore(true);

            _smart_layout.setOnLoadmoreListener(new OnLoadmoreListener() {

                @Override
                public void onLoadmore(RefreshLayout refreshlayout) {

                    _get_load();

                    _smart_layout.finishLoadmore(_get_load_time());
                }
            });
        }else{

            _smart_layout.setEnableLoadmore(false);
        }


        //设置显示是否下拉刷新
        if(_get_refresh_time() > 0){

            _smart_layout.setEnableRefresh(true);

            _smart_layout.setOnRefreshListener(new OnRefreshListener() {
                @Override
                public void onRefresh(RefreshLayout refreshlayout) {

                    _get_refresh();

                    _smart_layout.finishRefresh(_get_refresh_time());
                }
            });
        }else{

            _smart_layout.setEnableRefresh(false);
        }

    }


    public void _set_recycler_background(int _res){

        if(null != _smart_layout){

            _smart_layout.setBackgroundResource(_res);
        }
    }


    protected abstract int _get_load_time();

    protected abstract int _get_refresh_time();

    protected abstract void _get_load();

    protected abstract void _get_refresh();

    protected abstract void _get_data();

}
