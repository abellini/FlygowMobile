package br.com.flygowmobile.service;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import br.com.flygowmobile.activity.R;
import br.com.flygowmobile.activity.navigationdrawer.FB_Fragment;
import br.com.flygowmobile.activity.navigationdrawer.FoodFragment;
import br.com.flygowmobile.activity.navigationdrawer.RowItem;

/**
 * Created by Tiago Rocha Gomes on 21/07/14.
 */
public class ClickProductContentService {

    private Activity activity;

    public ClickProductContentService(Activity activity){
        this.activity = activity;
    }

    public void onClickProductItem(RowItem item){
            updateDisplay(item);
    }

    private void updateDisplay(RowItem item) {
        FoodFragment fragment = null;
        if(!item.isGroupHeader()){
            if(!item.isPromoItem()){
                fragment = new FoodFragment();
            } else if (item.isPromoItem()){
                //TODO: Implements fragment of promotions
            }
        }

        if (fragment != null) {
            Bundle args = new Bundle();
            args.putSerializable("item", item);
            fragment.setArguments(args);

            FragmentManager fragmentManager = activity.getFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.frame_container, fragment).commit();
            // update selected item and title, then close the drawer
            //setTitle(menutitles[position]);
            //mDrawerLayout.closeDrawer(mDrawerList);
        } else {
            // error in creating fragment
            Log.e("MainActivity", "Error in creating fragment");
        }

    }
}
