package br.com.flygowmobile.service;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.CheckBox;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.conn.HttpHostConnectException;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import br.com.flygowmobile.Utils.App;
import br.com.flygowmobile.Utils.FlygowAlertDialog;
import br.com.flygowmobile.activity.ByeByeActivity;
import br.com.flygowmobile.activity.MainActivity;
import br.com.flygowmobile.activity.R;
import br.com.flygowmobile.database.RepositoryOrder;
import br.com.flygowmobile.database.RepositoryOrderItem;
import br.com.flygowmobile.database.RepositoryOrderItemAccompaniment;
import br.com.flygowmobile.database.RepositoryTablet;
import br.com.flygowmobile.entity.Order;
import br.com.flygowmobile.entity.OrderItem;
import br.com.flygowmobile.entity.Tablet;
import br.com.flygowmobile.enums.AlertMessageTypeEnum;
import br.com.flygowmobile.enums.OrderItemStatusEnum;
import br.com.flygowmobile.enums.OrderStatusTypeEnum;
import br.com.flygowmobile.enums.ServerController;
import br.com.flygowmobile.enums.StaticMessages;
import br.com.flygowmobile.enums.StaticTitles;

/**
 * Created by Tiago Rocha Gomes on 20/09/14.
 */
public class EndOrderService {
    private Activity activity;
    private RepositoryOrderItemAccompaniment repositoryOrderItemAccompaniment;
    private RepositoryOrderItem repositoryOrderItem;
    private RepositoryOrder repositoryOrder;
    private RepositoryTablet repositoryTablet;

    private ProgressDialog progressCloseOrderDialog;

    private CloseAttendanceTask mCloseAttendanceTask;

    private static final String MAIN_ACTIVITY = "MainActivity";

    public EndOrderService(Activity activity){
        this.activity = activity;
        this.repositoryOrderItemAccompaniment = new RepositoryOrderItemAccompaniment(activity);
        this.repositoryOrderItem = new RepositoryOrderItem(activity);
        this.repositoryOrder = new RepositoryOrder(activity);
        this.repositoryTablet = new RepositoryTablet(activity);
    }

    public void endOrder(final boolean onlyCloseOrder, final Map<Long, CheckBox> paymentFormSelects){
        Order order = repositoryOrder.getByStatusType(OrderStatusTypeEnum.OPENED.getId());
        if(order != null){
            List<OrderItem> items =
                    repositoryOrderItem.listAllByOrderAndStatus(order.getOrderId(), OrderItemStatusEnum.OPENED.getId());
            if(!onlyCloseOrder && items != null && !items.isEmpty()){
                FlygowAlertDialog.createInfoPopup(activity, StaticTitles.INFORMATION, StaticMessages.ORDER_ITEMS_NOT_SENDED);
            }else{
                AlertDialog dialog = null;
                AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                String popupTitle = StaticTitles.CLOSING_ORDER.getName();
                builder.setTitle(popupTitle);
                builder.setMessage(StaticMessages.CONFIRM_CLOSING_ATTENDANCE.getName());
                builder.setPositiveButton(StaticTitles.YES.getName(), new
                        DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                if(onlyCloseOrder){
                                    final AlertDialog alertDialog = new AlertDialog.Builder(activity).create();
                                    alertDialog.setTitle(StaticTitles.INFORMATION.getName());
                                    alertDialog.setMessage(StaticMessages.ONLY_CLOSE_ORDER.getName());
                                    alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, StaticTitles.OK.getName(), new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            resetTabletOrder(true);
                                            alertDialog.dismiss();
                                        }
                                    });
                                    // Set the Icon for the Dialog
                                    alertDialog.setIcon(R.drawable.ic_dialog_info);
                                    alertDialog.show();
                                }else{
                                    progressCloseOrderDialog = ProgressDialog.show(activity, StaticTitles.SENDING.getName(),
                                            StaticMessages.SEND_CLOSE_ORDER.getName(), true);
                                    mCloseAttendanceTask = new CloseAttendanceTask(paymentFormSelects);
                                    mCloseAttendanceTask.execute((Void) null);
                                }
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
            }
        }else{
            FlygowAlertDialog.createInfoPopup(activity, StaticTitles.INFORMATION, StaticMessages.WITHOUT_ORDER);
        }
    }

    public void resetTabletOrder(boolean toMainActivity){
        repositoryOrderItemAccompaniment.removeAll();
        repositoryOrderItem.removeAll();
        repositoryOrder.removeAll();
        if(toMainActivity){
            Intent it = new Intent(activity, MainActivity.class);
            activity.startActivity(it);
            activity.finish();
        }
    }

    private class CloseAttendanceTask extends AsyncTask<Void, Void, String> {

        private App serverAddressObj = (App) activity.getApplication();
        private String url = serverAddressObj.getServerUrl(ServerController.CLOSE_ATTENDANCE);
        private Map<Long, CheckBox> paymentFormSelects;



        public CloseAttendanceTask(Map<Long, CheckBox> paymentFormSelects) {
            this.paymentFormSelects = paymentFormSelects;
        }

        @Override
        protected String doInBackground(Void... params) {
            if (paymentFormSelects != null && !paymentFormSelects.isEmpty()) {
                try {
                    Tablet tablet = repositoryTablet.findLast();
                    String listOfChoosedPaymentForms = "";
                    int i = 0;
                    for (Long paymentForm : paymentFormSelects.keySet()) {
                        if (i != paymentFormSelects.keySet().size() - 1) {
                            listOfChoosedPaymentForms += paymentForm + ",";
                        } else {
                            listOfChoosedPaymentForms += paymentForm;
                        }
                        i++;
                    }
                    String closeAttendanceJson =
                            "{" +
                                "tabletNumber: " + tablet.getNumber() + ", " +
                                "paymentFormIds: \'" + listOfChoosedPaymentForms + "\', " +
                                "alertType: " + AlertMessageTypeEnum.TO_PAYMENT.getId() +
                            "}";
                    NameValuePair valuePair = new BasicNameValuePair("closeAttendanceJson", closeAttendanceJson);
                    return ServiceHandler.makeServiceCall(url, ServiceHandler.POST, Arrays.asList(valuePair));
                }catch(HttpHostConnectException ex){
                    progressCloseOrderDialog.dismiss();
                    Log.i(MAIN_ACTIVITY, StaticMessages.TIMEOUT.getName());
                }catch(Exception e){
                    progressCloseOrderDialog.dismiss();
                    Log.i(MAIN_ACTIVITY, StaticMessages.NOT_SERVICE.getName());
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(final String response) {
            Log.i(MAIN_ACTIVITY, "Service: " + response);
            if(response == null){
                progressCloseOrderDialog.dismiss();
                FlygowAlertDialog.createWarningPopup(activity, StaticTitles.WARNING, StaticMessages.SELECT_ONE_PAYMENT_FORM);
            }else{
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    Boolean success = jsonObject.getBoolean("success");
                    if (success) {
                        progressCloseOrderDialog.dismiss();
                        Intent it = new Intent(activity, ByeByeActivity.class);
                        activity.startActivity(it);
                        activity.finish();
                    } else {
                        //FINISH LOADING...
                        progressCloseOrderDialog.dismiss();
                        Toast.makeText(activity, jsonObject.getString("message"), Toast.LENGTH_LONG).show();
                    }
                } catch (Exception e) {
                    //FINISH LOADING...
                    progressCloseOrderDialog.dismiss();
                    Log.i(MAIN_ACTIVITY, StaticMessages.EXCEPTION.getName(), e);
                    Toast.makeText(activity, StaticMessages.EXCEPTION.getName(), Toast.LENGTH_LONG).show();
                }
            }
        }

        @Override
        protected void onCancelled() {
            progressCloseOrderDialog.dismiss();
        }
    }
}
