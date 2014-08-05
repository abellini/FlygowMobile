package br.com.flygowmobile.service;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import br.com.flygowmobile.activity.R;
import br.com.flygowmobile.activity.navigationdrawer.FB_Fragment;
import br.com.flygowmobile.activity.navigationdrawer.FoodFragment;
import br.com.flygowmobile.activity.navigationdrawer.PromotionFragment;
import br.com.flygowmobile.activity.navigationdrawer.RowItem;

/**
 * Created by Tiago Rocha Gomes on 21/07/14.
 */
public class ClickProductContentService {

    private Activity activity;
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private int actualPositionItem;

    public ClickProductContentService(Activity activity, DrawerLayout mDrawerLayout, ListView mDrawerList){
        this.activity = activity;
        this.mDrawerLayout = mDrawerLayout;
        this.mDrawerList = mDrawerList;
    }

    public ClickProductContentService(Activity activity, int actualPositionItem){
        this.activity = activity;
        this.actualPositionItem = actualPositionItem;
    }

    public void onClickProductItem(final RowItem item, int position){
        this.actualPositionItem = position;
        boolean fromArrow = false;
        updateDisplay(item, fromArrow);
        /*Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                //Close de drawer list after 0.5 sec
                if(!item.isGroupHeader())
                    mDrawerLayout.closeDrawer(mDrawerList);
            }
        }, 500);*/
    }

    public void updateDisplay(RowItem item, Boolean fromArrow) {
        Fragment fragment = null;
        if(!item.isGroupHeader()){
            if(item.isPromoItem()){
                fragment = new PromotionFragment();
            }else{
                fragment = new FoodFragment();
            }
        }

        if (fragment != null) {
            Bundle args = new Bundle();
            args.putSerializable("item", item);
            args.putSerializable("itemPosition", actualPositionItem);
            args.putSerializable("fromArrow", fromArrow);
            fragment.setArguments(args);

            FragmentManager fragmentManager = activity.getFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.frame_container, fragment).commit();
            // update selected item and title, then close the drawer
            //setTitle(menutitles[position]);
        } else {
            // error in creating fragment
            Log.e("MainActivity", "Error in creating fragment");
        }
    }

}
