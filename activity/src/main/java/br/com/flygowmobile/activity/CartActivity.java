package br.com.flygowmobile.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.conn.HttpHostConnectException;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.com.flygowmobile.Utils.FlygowServerUrl;
import br.com.flygowmobile.activity.navigationdrawer.OrderAdapter;
import br.com.flygowmobile.activity.navigationdrawer.OrderRowItem;
import br.com.flygowmobile.entity.OrderItem;
import br.com.flygowmobile.enums.ServerController;
import br.com.flygowmobile.enums.StaticMessages;
import br.com.flygowmobile.enums.StaticTitles;
import br.com.flygowmobile.service.OrderService;
import br.com.flygowmobile.service.ServiceHandler;

/**
 * Created by Tiago Rocha Gomes on 30/08/14.
 */
public class CartActivity extends Activity {

    private static final String CART_ACTIVITY = "CartActivity";

    private ViewGroup header;
    private ViewGroup footer;
    private View cartView;
    private OrderService orderService;
    private Map<Long, CheckBox> selects = new HashMap<Long, CheckBox>();
    private RegisterOrderTask mRegisterTask = null;

    private ProgressDialog progressSaveDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater inflater = LayoutInflater.from(this);

        this.cartView = inflater.inflate(R.layout.cart_list_orders, null);
        setContentView(cartView);

        this.header = (ViewGroup) inflater.inflate(R.layout.header_order, null, false);
        this.footer = (ViewGroup) inflater.inflate(R.layout.footer_order, null, false);
        this.orderService = new OrderService(this);

        defineFonts();
        fillCartOrders();
    }

    protected void defineFonts() {
        // Font path
        String fontChillerPath = "fonts/CHILLER.TTF";
        Typeface chiller = Typeface.createFromAsset(getAssets(), fontChillerPath);

        String fontErasPath = "fonts/ERASMD.TTF";
        Typeface eras = Typeface.createFromAsset(getAssets(), fontErasPath);

        //Title
        TextView cartTitle = (TextView) this.cartView.findViewById(R.id.cartTitle);
        TextView cartSubTitle = (TextView) this.cartView.findViewById(R.id.cartSubTitle);
        cartTitle.setTypeface(chiller);
        cartSubTitle.setTypeface(eras);

        //Header
        TextView txtDescription = (TextView) this.header.findViewById(R.id.lblDescriptionOrder);
        TextView txtQtde = (TextView) this.header.findViewById(R.id.lblQtdTitle);
        TextView txtAccompaniments = (TextView) this.header.findViewById(R.id.lblAccompaniments);
        TextView txtPriceUnit = (TextView) this.header.findViewById(R.id.lblPriceUnit);
        TextView txtPriceTotal = (TextView) this.header.findViewById(R.id.lblPriceTotal);
        txtDescription.setTypeface(chiller);
        txtQtde.setTypeface(chiller);
        txtAccompaniments.setTypeface(chiller);
        txtPriceUnit.setTypeface(chiller);
        txtPriceTotal.setTypeface(chiller);

        // Footer
        TextView txtTotal = (TextView) this.footer.findViewById(R.id.vlTotal);
        TextView txtVlTotal = (TextView) this.footer.findViewById(R.id.lbTotalPedido);
        txtTotal.setTypeface(chiller);
        txtVlTotal.setTypeface(chiller);

    }

    private void fillCartOrders() {
        ListView listView = (ListView) cartView.findViewById(R.id.cartList);

        listView.addHeaderView(this.header);
        listView.addFooterView(this.footer);

        List<OrderRowItem> orderItemRow = this.orderService.populateOrderItemList();
        listView.setAdapter(new OrderAdapter(this, orderItemRow, selects));

        TextView txtTotal = (TextView) footer.findViewById(R.id.vlTotal);
        txtTotal.setText(this.orderService.getFormatedTotalValue());

        Button mRegisterButton = (Button) cartView.findViewById(R.id.btnEnviarPedido);
        mRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                progressSaveDialog = ProgressDialog.show(CartActivity.this, StaticTitles.LOAD.getName(),
                        StaticMessages.SAVE_ORDER_FROM_SERVER.getName(), true);

                mRegisterTask = new RegisterOrderTask();
                mRegisterTask.execute((Void) null);
            }
        });
    }


    /**
     * Represents an asynchronous registration task used to salve Order
     */
    public class RegisterOrderTask extends AsyncTask<Void, Void, String> {

        FlygowServerUrl serverAddressObj = (FlygowServerUrl) getApplication();
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
                Toast.makeText(CartActivity.this, StaticMessages.TIMEOUT.getName(), Toast.LENGTH_LONG).show();
            } catch (Exception e) {
                Log.i(CART_ACTIVITY, StaticMessages.NOT_SERVICE.getName());
                Toast.makeText(CartActivity.this, StaticMessages.NOT_SERVICE.getName(), Toast.LENGTH_LONG).show();
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
                    Toast.makeText(CartActivity.this, StaticMessages.SUCCESS_SAVE_IN_SERVER.getName(), Toast.LENGTH_LONG).show();

                } else {
                    //FINISH LOADING...
                    progressSaveDialog.dismiss();
                    Toast.makeText(CartActivity.this, jsonObject.getString("message"), Toast.LENGTH_LONG).show();
                }
            } catch (Exception e) {
                //FINISH LOADING...
                progressSaveDialog.dismiss();
                Log.i(CART_ACTIVITY, StaticMessages.EXCEPTION.getName());
                Toast.makeText(CartActivity.this, StaticMessages.EXCEPTION.getName(), Toast.LENGTH_LONG).show();
            }
        }

        @Override
        protected void onCancelled() {
            //FINISH LOADING...
        }
    }

}
