package br.com.flygowmobile.service;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.conn.HttpHostConnectException;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import br.com.flygowmobile.Utils.App;
import br.com.flygowmobile.activity.CartActivity;
import br.com.flygowmobile.activity.R;

import br.com.flygowmobile.activity.navigationdrawer.AccompanimentAdapter;
import br.com.flygowmobile.activity.navigationdrawer.AccompanimentRowItem;
import br.com.flygowmobile.activity.navigationdrawer.FinalizeServiceFragment;
import br.com.flygowmobile.activity.navigationdrawer.PaymentFormAdapter;
import br.com.flygowmobile.activity.navigationdrawer.PaymentFormRowItem;
import br.com.flygowmobile.database.RepositoryPaymentForm;
import br.com.flygowmobile.database.RepositoryTablet;
import br.com.flygowmobile.entity.Food;
import br.com.flygowmobile.entity.Tablet;
import br.com.flygowmobile.enums.AlertMessageTypeEnum;
import br.com.flygowmobile.enums.ServerController;
import br.com.flygowmobile.enums.StaticMessages;
import br.com.flygowmobile.enums.StaticTitles;
import br.com.flygowmobile.mapper.PaymentFormMapper;

/**
 * Created by Tiago Rocha Gomes on 21/08/14.
 */
public class BuildMainActionBarService extends ActionBarService{
    private Activity activity;
    private OrderService orderService;
    private View actionBarView;

    public BuildMainActionBarService(Activity activity, View actionBarView){
        super(activity, actionBarView);
        this.activity = activity;
        this.orderService = new OrderService(activity);
        this.actionBarView = actionBarView;
    }

    public void buildActionBar(){
        super.buildActionBar();
        refreshActionBarPrice();
    }

    public void refreshActionBarPrice(){
        String value = orderService.getFormatedTotalValue();
        TextView mPriceTextView = (TextView) actionBarView.findViewById(R.id.txtTotalOrderPrice);
        mPriceTextView.setText(StaticTitles.YOUR_ACCOUNT.getName() + "\n" + value);
    }

    public void defineActionButtons(){
        super.defineActionButtons();
        onMyOrdersClick();
    }

    private void onMyOrdersClick() {
        ImageButton button = (ImageButton) actionBarView
                .findViewById(R.id.btnBasket);
        button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent it = new Intent(activity, CartActivity.class);
                activity.startActivity(it);
            }
        });
    }
}