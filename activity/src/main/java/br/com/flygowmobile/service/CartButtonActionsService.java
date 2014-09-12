package br.com.flygowmobile.service;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.conn.HttpHostConnectException;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import br.com.flygowmobile.Utils.FlygowAlertDialog;
import br.com.flygowmobile.Utils.FlygowServerUrl;
import br.com.flygowmobile.activity.CartActivity;
import br.com.flygowmobile.activity.MainActivity;
import br.com.flygowmobile.activity.R;
import br.com.flygowmobile.database.RepositoryOrderItem;
import br.com.flygowmobile.database.RepositoryOrderItemAccompaniment;
import br.com.flygowmobile.entity.OrderItem;
import br.com.flygowmobile.enums.ServerController;
import br.com.flygowmobile.enums.StaticMessages;
import br.com.flygowmobile.enums.StaticTitles;

/**
 * Created by Tiago Rocha Gomes on 11/09/14.
 */
public class CartButtonActionsService {
    private Activity activity;
    private View view;
    private OrderService orderService;
    private BuildMainActionBarService actionBarService;
    private ProgressDialog progressSaveDialog;
    private RegisterOrderTask mRegisterTask = null;

    private RepositoryOrderItem repositoryOrderItem;
    private RepositoryOrderItemAccompaniment repositoryOrderItemAccompaniment;

    private static final String CART_ACTIVITY = "CartActivity";

    public CartButtonActionsService(Activity activity, View view){
        this.activity = activity;
        this.view = view;
        this.orderService = new OrderService(activity);

        this.repositoryOrderItem = new RepositoryOrderItem(activity);
        this.repositoryOrderItemAccompaniment = new RepositoryOrderItemAccompaniment(activity);
    }

    public void defineCartButtons(){
        onSendOrderClick();
        onContinueClick();
        onCancelItemClick();
    }

    private void onSendOrderClick(){
        ImageButton mRegisterButton = (ImageButton) view.findViewById(R.id.btnSendOrder);
        mRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                progressSaveDialog = ProgressDialog.show(activity, StaticTitles.LOAD.getName(),
                        StaticMessages.SAVE_ORDER_FROM_SERVER.getName(), true);

                mRegisterTask = new RegisterOrderTask();
                mRegisterTask.execute((Void) null);
            }
        });
    }

    private void onContinueClick(){
        ImageButton mContinueButton = (ImageButton) view.findViewById(R.id.btnContinue);
        mContinueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activity.finish();
            }
        });
    }

    private void onCancelItemClick(){
        ImageButton mCancelButton = (ImageButton) view.findViewById(R.id.btnCancelItem);
        mCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Map<Long, CheckBox> selecteds = ((CartActivity)activity).getCheckedItems();
                if(!selecteds.isEmpty()){
                    for(Long orderItemId : selecteds.keySet()){
                        try{
                            repositoryOrderItemAccompaniment.deleteByOrderItemId(orderItemId);
                        }catch (Exception e){
                            Log.w(CART_ACTIVITY, e);
                        }
                        try{
                            repositoryOrderItem.delete(orderItemId);
                        }catch (Exception e){
                            Log.e(CART_ACTIVITY, e.getMessage(), e);
                        }
                        selecteds.remove(orderItemId);
                    }
                    activity.finish();
                    activity.startActivity(activity.getIntent());
                }else{
                    FlygowAlertDialog.createWarningPopup(activity, StaticTitles.WARNING, StaticMessages.SELECT_ONE);
                }
            }
        });
    }

    /**
     * Represents an asynchronous registration task used to salve Order
     */
    private class RegisterOrderTask extends AsyncTask<Void, Void, String> {

        FlygowServerUrl serverAddressObj = (FlygowServerUrl) activity.getApplication();
        String url = serverAddressObj.getServerUrl(ServerController.REGISTER_ORDER);

        public RegisterOrderTask() {

        }

        @Override
        protected String doInBackground(Void... params) {
            try {
                List<OrderItem> orderList = orderService.getOrderListToServer();
                JSONArray jsonArray = new JSONArray();
                for (OrderItem orderItem : orderList) {
                    jsonArray.put(orderItem.toJSONObject());
                }
                JSONObject orderTabletObj = new JSONObject();
                Integer numberTablet = orderService.getNumberTablet();
                try {
                    orderTabletObj.put("numberTablet", numberTablet);
                    orderTabletObj.put("orderItens", jsonArray);
                } catch (JSONException e) {
                    Log.i(CART_ACTIVITY, "Erro" + e);
                }
                NameValuePair orderJsonPair = new BasicNameValuePair("orderJson", orderTabletObj.toString());
                Log.i(CART_ACTIVITY, "URL -->>>>>>>> " + url);
                return ServiceHandler.makeServiceCall(url, ServiceHandler.POST, Arrays.asList(orderJsonPair));
            } catch (HttpHostConnectException ex) {
                Log.i(CART_ACTIVITY, StaticMessages.TIMEOUT.getName());
                Toast.makeText(activity, StaticMessages.TIMEOUT.getName(), Toast.LENGTH_LONG).show();
            } catch (Exception e) {
                Log.i(CART_ACTIVITY, StaticMessages.NOT_SERVICE.getName());
                Toast.makeText(activity, StaticMessages.NOT_SERVICE.getName(), Toast.LENGTH_LONG).show();
            }
            return "";
        }

        @Override
        protected void onPostExecute(final String response) {

            Log.i(CART_ACTIVITY, "Service: " + response);
            try {
                JSONObject jsonObject = new JSONObject(response);
                Boolean success = jsonObject.getBoolean("success");
                if (success) {
                    progressSaveDialog.dismiss();
                    Toast.makeText(activity, StaticMessages.SUCCESS_SAVE_IN_SERVER.getName(), Toast.LENGTH_LONG).show();

                } else {
                    //FINISH LOADING...
                    progressSaveDialog.dismiss();
                    Toast.makeText(activity, jsonObject.getString("message"), Toast.LENGTH_LONG).show();
                }
            } catch (Exception e) {
                //FINISH LOADING...
                progressSaveDialog.dismiss();
                Log.i(CART_ACTIVITY, StaticMessages.EXCEPTION.getName());
                Toast.makeText(activity, StaticMessages.EXCEPTION.getName(), Toast.LENGTH_LONG).show();
            }
        }

        @Override
        protected void onCancelled() {
            //FINISH LOADING...
        }
    }

}
