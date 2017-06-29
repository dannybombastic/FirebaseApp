package com.example.danny.firebaseapp.fragmentPagerAdapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 * Created by danny on 20/01/2017.
 */

public class MyAdapterViewPages extends FragmentPagerAdapter {

List<Fragment> list;
  public MyAdapterViewPages(FragmentManager fr ,List<Fragment> list){
   super(fr);
      this.list = list;
  }


    @Override
    public Fragment getItem(int position) {
        return list.get(position);
    }

    @Override
    public int getCount() {
        return list.size();
    }
}
