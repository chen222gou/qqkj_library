package qqkj.qqkj_library.view.banner;


import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import qqkj.qqkj_library.R;
import qqkj.qqkj_library.threadpool.ThreadPoolUtil;

/**
 * ViewPager
 * <p>
 * Created by 陈二狗 on 2017/10/12.
 */

public abstract class BannerFragment extends Fragment{

    public BannerUtil _banner = null;

    public List<Object> _list_data = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View _view = inflater.inflate(R.layout.fragment_banner_layout,null);

        _banner = (BannerUtil) _view.findViewById(R.id._base_banner);

        LoadBanner();

        return _view;
    }

    public void LoadBanner(){

        ThreadPoolUtil.execute(new Runnable() {
            @Override
            public void run() {

                _list_data = getBannerData();

                _handler.sendEmptyMessage(0);
            }
        });
    }

    private Handler _handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {

            if(_list_data.size() > 0){

                getBannerNormalPoint();

                getBannerSelectPoint();

                getBannerListData();

                getBannerAdapter();

                getBannerItemClick();
            }

            return false;
        }
    });

    private void getBannerNormalPoint(){

        _banner.setNormalPoint(getResources().getDrawable(setBannerNormalPoint()));
    }


    private void getBannerSelectPoint(){

        _banner.setSelectPoint(getResources().getDrawable(setBannerSelectPoint()));
    }


    private void getBannerListData(){

        _banner.setData(_list_data,null);
    }


    private void getBannerAdapter(){

        _banner.setmAdapter(setBannerAdapter());
    }


    private void getBannerItemClick(){

        _banner.setOnItemClickListener(setBannerItemClick());
    }


    public abstract List<Object> getBannerData();

    public abstract int setBannerNormalPoint();

    public abstract int setBannerSelectPoint();

    public abstract BannerUtil.XBannerAdapter setBannerAdapter();

    public abstract BannerUtil.OnItemClickListener setBannerItemClick();
}
