package com.birthstone.widgets.tabHost;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

import java.util.ArrayList;

/**
 * 时间：2016年03月16日
 *
 * 作者：张景瑞
 *
 * 功能：界面Fragment适配器
 */
public class FragmentAdapter extends FragmentPagerAdapter {

    private FragmentManager fragmentManager;
    private ArrayList<Fragment> fragmentList;

    public FragmentAdapter(FragmentManager fragmentManager){
        super(fragmentManager);
        this.fragmentManager = fragmentManager;
    }

    public FragmentAdapter(FragmentManager fragmentManager, ArrayList<Fragment> fragments){
        super(fragmentManager);
        this.fragmentManager = fragmentManager;
        this.fragmentList = fragments;
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = fragmentList.get(position);
        Bundle bundle = new Bundle();
        bundle.putString("id","" + position);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public int getCount() {
        return fragmentList.size();
    }

    @Override
    public int getItemPosition(Object object) {
        return super.getItemPosition(object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        // 将实例化的fragment进行显示即可。
        Fragment fragment = (Fragment) super.instantiateItem(container, position);
        fragmentManager.beginTransaction().show(fragment).commit();
        return fragment;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        //            super.destroyItem(container, position, object);// 注释父类方法
        Fragment fragment = fragmentList.get(position);// 获取要销毁的fragment
        fragmentManager.beginTransaction().hide(fragment).commit();// 将其隐藏即可，并不需要真正销毁，这样fragment状态就得到了保存
    }
}
