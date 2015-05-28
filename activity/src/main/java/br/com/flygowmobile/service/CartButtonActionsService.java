package br.com.flygowmobile.service;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
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

import br.com.flygowmobile.Utils.App;
import br.com.flygowmobile.Utils.FlygowAlertDialog;
import br.com.flygowmobile.activity.CartActivity;
import br.com.flygowmobile.activity.R;
import br.com.flygowmobile.database.RepositoryFood;
import br.com.flygowmobile.database.RepositoryOrder;
import br.com.flygowmobile.database.RepositoryOrderItem;
import br.com.flygowmobile.database.RepositoryOrderItemAccompaniment;
import br.com.flygowmobile.database.RepositoryPromotion;
import br.com.flygowmobile.entity.Accompaniment;
import br.com.flygowmobile.entity.Food;
import br.com.flygowmobile.entity.Order;
import br.com.flygowmobile.entity.OrderItem;
import br.com.flygowmobile.entity.Promotion;
import br.com.flygowmobile.enums.OrderItemStatusEnum;
import br.com.flygowmobile.enums.ProductTypeEnum;
import br.com.flygowmobile.enums.ServerController;
import br.com.flygowmobile.enums.StaticMessages;
import br.com.flygowmobile.enums.StaticTitles;

/**
 * Created by Tiago Rocha Gomes on 11/09/14.
 */
public class CartButtonActionsService {
    private static final String CART_ACTIVITY = "CartActivity";
    private Activity activity;
    private View view;
    private OrderService orderService;
    private BuildMainActionBarService actionBarService;
    private ProgressDialog progressSaveDialog;
    private RegisterOrderTask mRegisterTask = null;
    private RepositoryFood repositoryFood;
    private RepositoryPromotion repositoryPromotion;
    private RepositoryOrder repositoryOrder;
    private RepositoryOrderItem repositoryOrderItem;
    private RepositoryOrderItemAccompaniment repositoryOrderItemAccompaniment;

    public CartButtonActionsService(Activity activity, View view){
        this.activity = activity;
        this.view = view;
        this.orderService = new OrderService(activity);

        this.repositoryFood = new RepositoryFood(activity);
        this.repositoryPromotion = new RepositoryPromotion(activity);
        this.repositoryOrder = new RepositoryOrder(activity);
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
                final List<OrderItem> orderItemsList = orderService.getOrderListToServer();
                if(orderItemsList != null && !orderItemsList.isEmpty()) {
                    String messageItemsToServer = getProductNamesToServer(orderItemsList);
                    AlertDialog dialog = null;
                    AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                    String popupTitle = StaticTitles.SEND_ORDER.getName();
                    builder.setTitle(popupTitle);
                    builder.setMessage(StaticMessages.CONFIRM_SEND_ORDER.getName() + messageItemsToServer);
                    builder.setPositiveButton(StaticTitles.YES.getName(), new
                            DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    progressSaveDialog = ProgressDialog.show(activity, StaticTitles.LOAD.getName(),
                                            StaticMessages.SAVE_ORDER_FROM_SERVER.getName(), true);

                                    mRegisterTask = new RegisterOrderTask(orderItemsList);
                                    mRegisterTask.execute((Void) null);
                                    dialog.dismiss();
                                }
                            });
                    builder.setNegativeButton(StaticTitles.NO.getName(), new
                            DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                    dialog = builder.create();
                    dialog.setIcon(R.drawable.question_title);
                    dialog.show();
                } else {
                    FlygowAlertDialog.createInfoPopup(activity, StaticTitles.INFORMATION, StaticMessages.ALL_ITEMS_SENDED);
                }
            }
        });
    }

    private String getProductNamesToServer(List<OrderItem> orderItemsList){
        String itemsToSend = "";
        for(OrderItem orderItem : orderItemsList){
            if(ProductTypeEnum.FOOD.equals(ProductTypeEnum.fromName(orderItem.getProductType()))){
                Food food = repositoryFood.findById(orderItem.getFoodId());
                itemsToSend +=  "\n" + " - " + food.getName() + " (" + orderItem.getQuantity()  + "un.)";
            }else if (ProductTypeEnum.PROMOTION.equals(ProductTypeEnum.fromName(orderItem.getProductType()))){
                Promotion promotion = repositoryPromotion.findById(orderItem.getFoodId());
                itemsToSend += "\n" + " - " + promotion.getName() + " (" + orderItem.getQuantity()  + "un.)";
            }
        }
        return itemsToSend;
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
                final Map<Long, CheckBox> selecteds = ((CartActivity)activity).getCheckedItems();
                if(!selecteds.isEmpty()) {
                    AlertDialog dialog = null;
                    AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                    String popupTitle = StaticTitles.CART_CANCEL_ITEM.getName();
                    builder.setTitle(popupTitle);
                    builder.setMessage(StaticMessages.CANCEL_ITEM.getName());
                    builder.setPositiveButton(StaticTitles.YES.getName(), new
                        DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
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
                                }
                                selecteds.clear();
                                activity.finish();
                                activity.startActivity(activity.getIntent());
                            }
                        });
                    builder.setNegativeButton(StaticTitles.NO.getName(), new
                        DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                    dialog = builder.create();
                    dialog.setIcon(R.drawable.btn_cancel);
                    dialog.show();
                }else{
                    FlygowAlertDialog.createWarningPopup(activity, StaticTitles.WARNING, StaticMessages.SELECT_ONE);
                }
            }
        });
    }

    /**
     * Represents an asynchronous registration task used to save Order
     */
    private class RegisterOrderTask extends AsyncTask<Void, Void, String> {

        private App serverAddressObj = (App) activity.getApplication();
        private String url = serverAddressObj.getServerUrl(ServerController.REGISTER_ORDER);
        private List<OrderItem> orderItemsList;

        public RegisterOrderTask(List<OrderItem> orderItemsList) {
            this.orderItemsList = orderItemsList;
        }

        @Override
        protected String doInBackground(Void... params) {
            try {

                Order order = orderService.getCurrentOrder();
                order.setTotalValue(orderService.getTotalOrderValue());
                if(orderItemsList != null && !orderItemsList.isEmpty()){
                    JSONObject jsonOrderObject = new JSONObject();
                    JSONArray jsonArray = new JSONArray();

                    for (OrderItem orderItem : orderItemsList) {
                        List<Accompaniment> oia = repositoryOrderItemAccompaniment.findByOrderItemId(orderItem.getOrderItemId());
                        if(oia != null && !oia.isEmpty()){
                            orderItem.setAccompanimentList(oia);
                        }
                        jsonArray.put(orderItem.toJSONObject());
                    }
                    try {
                        jsonOrderObject.put("order", order.toJSONObject());
                        jsonOrderObject.put("orderItens", jsonArray);
                    } catch (JSONException e) {
                        Log.i(CART_ACTIVITY, "Erro" + e);
                    }
                    NameValuePair orderJsonPair = new BasicNameValuePair("orderJson", jsonOrderObject.toString());
                    Log.i(CART_ACTIVITY, "URL -->>>>>>>>> " + url);
                    return ServiceHandler.makeServiceCall(url, ServiceHandler.POST, Arrays.asList(orderJsonPair));
                }else{
                    return null;
                }

            } catch (HttpHostConnectException ex) {
                Log.i(CART_ACTIVITY, StaticMessages.TIMEOUT.getName(), ex);
                Toast.makeText(activity, StaticMessages.TIMEOUT.getName(), Toast.LENGTH_LONG).show();
            } catch (Exception e) {
                Log.i(CART_ACTIVITY, StaticMessages.NOT_SERVICE.getName(), e);
                Toast.makeText(activity, StaticMessages.NOT_SERVICE.getName(), Toast.LENGTH_LONG).show();
            }
            return "";
        }

        @Override
        protected void onPostExecute(final String response) {
            if (response == null){
                progressSaveDialog.dismiss();
                FlygowAlertDialog.createInfoPopup(activity, StaticTitles.INFORMATION, StaticMessages.ALL_ITEMS_SENDED);
            }else{
                Log.i(CART_ACTIVITY, "Service: " + response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    Boolean success = jsonObject.getBoolean("success");
                    if (success) {
                        //BEGIN of from/to of ids of orders and order items from the server to the tablet.
                        JSONObject serverIds = jsonObject.getJSONObject("serverIds");
                        Order tabletOrder = repositoryOrder.findById(serverIds.getLong("tabletOrderId"));
                        if(tabletOrder.getOrderServerId() == 0){
                            tabletOrder.setOrderServerId(serverIds.getLong("serverOrderId"));
                            repositoryOrder.save(tabletOrder);
                        }
                        JSONArray serverOrderItemIds = serverIds.getJSONArray("orderItemIds");
                        if(serverOrderItemIds != null && serverOrderItemIds.length() > 0){
                            for(int i = 0; i < serverOrderItemIds.length(); i++){
                                JSONObject orderItemIdsCompare = serverOrderItemIds.getJSONObject(i);
                                OrderItem tabletOrderItem=
                                        repositoryOrderItem.findById(orderItemIdsCompare.getLong("tabletOrderItemId"));
                                if(tabletOrderItem.getOrderItemServerId() == 0){
                                    tabletOrderItem.setOrderItemServerId(orderItemIdsCompare.getLong("serverOrderItemId"));
                                    tabletOrderItem.setStatus(OrderItemStatusEnum.SENDED.getId());
                                    repositoryOrderItem.save(tabletOrderItem);
                                }
                            }
                        }
                        //END of from/to of ids of orders and order items from the server to the tablet.
                        progressSaveDialog.dismiss();

                        Intent it = new Intent(activity, CartActivity.class);
                        it.putExtra("saved", true);
                        activity.startActivity(it);
                        activity.finish();
                    } else {
                        //FINISH LOADING...
                        progressSaveDialog.dismiss();
                        Toast.makeText(activity, jsonObject.getString("message"), Toast.LENGTH_LONG).show();
                    }
                } catch (Exception e) {
                    //FINISH LOADING...
                    progressSaveDialog.dismiss();
                    Log.i(CART_ACTIVITY, StaticMessages.EXCEPTION.getName(), e);
                    Toast.makeText(activity, StaticMessages.EXCEPTION.getName(), Toast.LENGTH_LONG).show();
                }
            }
        }

        @Override
        protected void onCancelled() {
            //FINISH LOADING...
        }
    }

}
