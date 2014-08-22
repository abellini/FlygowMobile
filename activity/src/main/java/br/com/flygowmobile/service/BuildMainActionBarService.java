package br.com.flygowmobile.service;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import br.com.flygowmobile.activity.R;
import br.com.flygowmobile.activity.navigationdrawer.BasketFragment;
import br.com.flygowmobile.activity.navigationdrawer.CallAtendantFragment;
import br.com.flygowmobile.activity.navigationdrawer.FinalizeServiceFragment;
import br.com.flygowmobile.enums.StaticTitles;

/**
 * Created by Tiago Rocha Gomes on 21/08/14.
 */
public class BuildMainActionBarService {
    private Activity activity;
    private OrderService orderService;
    private View actionBarView;
    public BuildMainActionBarService(Activity activity, View actionBarView){
        this.activity = activity;
        this.actionBarView = actionBarView;
        this.orderService = new OrderService(activity);
    }

    public void buildActionBar(){
        ActionBar actionBar = activity.getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);

        defineActionTitle();
        defineActionButtons();
        refreshActionBarPrice();

        actionBar.setCustomView(actionBarView);
        actionBar.setDisplayShowCustomEnabled(true);
    }

    public void refreshActionBarPrice(){
        String value = orderService.getFormatedTotalValue();
        TextView mPriceTextView = (TextView) actionBarView.findViewById(R.id.txtTotalOrderPrice);
        mPriceTextView.setText(StaticTitles.YOUR_ACCOUNT.getName() + "\n" + value);
    }

    private void defineActionTitle(){
        TextView mTitleTextView = (TextView) actionBarView.findViewById(R.id.title_text);
        mTitleTextView.setText(StaticTitles.MAIN_APP_TITLE.getName());
        defineTitleFont(mTitleTextView);
    }

    private void defineTitleFont(TextView mTitleTextView){
        String fontChillerPath = "fonts/CHILLER.TTF";
        Typeface chiller = Typeface.createFromAsset(activity.getAssets(), fontChillerPath );
        mTitleTextView.setTypeface(chiller);
    }

    private void defineActionButtons(){
        onEndOrderClick();
        onMyOrdersClick();
        onCallAttendantClick();
    }

    private void onEndOrderClick() {
        ImageButton button = (ImageButton) actionBarView
                .findViewById(R.id.btnEndOrder);
        button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Fragment fragment = new FinalizeServiceFragment();
                FragmentManager fragmentManager = activity.getFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.frame_container, fragment).commit();
            }
        });
    }

    private void onMyOrdersClick() {
        ImageButton button = (ImageButton) actionBarView
                .findViewById(R.id.btnBasket);
        button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Fragment fragment = new BasketFragment();
                FragmentManager fragmentManager = activity.getFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.frame_container, fragment).commit();
            }
        });
    }

    private void onCallAttendantClick() {
        ImageButton button = (ImageButton) actionBarView
                .findViewById(R.id.btnCallAttendant);
        button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Fragment fragment = new CallAtendantFragment();
                FragmentManager fragmentManager = activity.getFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.frame_container, fragment).commit();
            }
        });
    }
}
