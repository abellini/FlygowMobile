package br.com.flygowmobile.service;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
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
import br.com.flygowmobile.activity.navigationdrawer.PaymentFormAdapter;
import br.com.flygowmobile.activity.navigationdrawer.PaymentFormRowItem;
import br.com.flygowmobile.database.RepositoryPaymentForm;
import br.com.flygowmobile.database.RepositoryTablet;
import br.com.flygowmobile.entity.Tablet;
import br.com.flygowmobile.enums.AlertMessageTypeEnum;
import br.com.flygowmobile.enums.ServerController;
import br.com.flygowmobile.enums.StaticMessages;
import br.com.flygowmobile.enums.StaticTitles;
import br.com.flygowmobile.mapper.PaymentFormMapper;

/**
 * Created by Tiago Rocha Gomes on 21/08/14.
 */
public class ActionBarService {
    private Activity activity;
    private OrderService orderService;
    private EndOrderService endOrderService;
    private View actionBarView;

    private Map<Long, CheckBox> paymentFormSelects;

    private RepositoryTablet repositoryTablet;
    private RepositoryPaymentForm repositoryPaymentForm;

    private ProgressDialog callAttendantDialog;

    private static final String MAIN_ACTIVITY = "MainActivity";

    public ActionBarService(Activity activity, View actionBarView){
        this.activity = activity;
        this.actionBarView = actionBarView;
        this.orderService = new OrderService(activity);
        this.endOrderService = new EndOrderService(activity);

        repositoryTablet = new RepositoryTablet(activity);
        repositoryPaymentForm = new RepositoryPaymentForm(activity);

        paymentFormSelects = new HashMap<Long, CheckBox>();
    }

    public void buildActionBar(){
        ActionBar actionBar = activity.getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);

        defineActionTitle();
        defineActionButtons();

        actionBar.setCustomView(actionBarView);
        actionBar.setDisplayShowCustomEnabled(true);
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

    public void defineActionButtons(){
        onEndOrderClick();
        onCallAttendantClick();
    }

    private void onEndOrderClick() {
        ImageButton button = (ImageButton) actionBarView
                .findViewById(R.id.btnEndOrder);
        button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Double totalOrder = orderService.getTotalOrderValue();
                if(totalOrder > 0.0) {
                    AlertDialog dialog = null;
                    AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                    String popupTitle = StaticTitles.PAYMENT_FORM_POPUP.getName();
                    builder.setTitle(popupTitle);
                    builder.setPositiveButton(StaticTitles.CONTINUE.getName(), new
                            DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    endOrderService.endOrder(false, paymentFormSelects);
                                    dialog.dismiss();
                                }
                            });
                    builder.setNegativeButton(StaticTitles.CANCEL.getName(), new
                            DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                    if (paymentFormSelects != null && !paymentFormSelects.isEmpty()) {
                                        paymentFormSelects.clear();
                                    }
                                }
                            });
                    final ListView list = new ListView(activity);
                    list.setAdapter(
                            new PaymentFormAdapter(
                                    activity,
                                    PaymentFormMapper.paymentToRowItem(repositoryPaymentForm.listAll()),
                                    paymentFormSelects
                            )
                    );
                    list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> arg0, View view, int position, long arg3) {
                            CheckBox chk = (CheckBox) view.findViewById(R.id.checkbox);
                            chk.setTag(((PaymentFormRowItem) list.getItemAtPosition(position)).getId());
                            PaymentFormItemClickService.onMarkItemClick(paymentFormSelects, chk, true);
                        }
                    });
                    builder.setView(list);
                    dialog = builder.create();
                    dialog.setIcon(R.drawable.icon_title_payment);
                    dialog.show();
                }else{
                    endOrderService.endOrder(true, null);
                }
            }
        });
    }


    private void onCallAttendantClick() {
        ImageButton button = (ImageButton) actionBarView
                .findViewById(R.id.btnCallAttendant);
        button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                AlertDialog dialog = null;
                AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                String popupTitle = StaticTitles.CALL_ATTENDANT.getName();
                builder.setTitle(popupTitle);
                builder.setMessage(StaticMessages.CONFIRM_CALL_ATTENDANT.getName());
                builder.setPositiveButton(StaticTitles.YES.getName(), new
                    DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            callAttendantDialog = ProgressDialog.show(activity, StaticTitles.LOAD.getName(),
                                    StaticMessages.CALLING_ATTENDANT.getName(), true);
                            CallAttendantTask callAttendantTask = new CallAttendantTask();
                            callAttendantTask.execute((Void) null);
                        }
                    });
                builder.setNegativeButton(StaticTitles.NO.getName(), new
                    DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                dialog = builder.create();
                dialog.setIcon(R.drawable.attendant);
                dialog.show();
            }
        });
    }

    public class CallAttendantTask extends AsyncTask<Void, Void, String> {

        App serverAddressObj = (App) activity.getApplication();
        String url = serverAddressObj.getServerUrl(ServerController.CALL_ATTENDANT);

        @Override
        protected String doInBackground(Void... params) {
            try {
                Tablet tablet = repositoryTablet.findLast();
                Integer tabletNumber = tablet.getNumber();
                Long attendantId = tablet.getAttendantId();
                Long alertTypeId = AlertMessageTypeEnum.TO_ATTENDANCE.getId();

                NameValuePair tabletIdPair = new BasicNameValuePair("tabletNumber", String.valueOf(tabletNumber));
                NameValuePair attendantIdPair = new BasicNameValuePair("attendantId", String.valueOf(attendantId));
                NameValuePair alertTypeIdPair = new BasicNameValuePair("alertTypeId", String.valueOf(alertTypeId));

                return ServiceHandler.makeServiceCall(url, ServiceHandler.POST,
                        Arrays.asList(tabletIdPair, attendantIdPair, alertTypeIdPair));
            } catch (HttpHostConnectException ex) {
                callAttendantDialog.dismiss();
                Log.i(MAIN_ACTIVITY, StaticMessages.TIMEOUT.getName());
            } catch (Exception e) {
                callAttendantDialog.dismiss();
                Log.i(MAIN_ACTIVITY, StaticMessages.NOT_SERVICE.getName());
            }
            return "";
        }

        @Override
        protected void onPostExecute(final String response) {

            Log.i(MAIN_ACTIVITY, "Call Attendant Service: " + response);
            try {
                JSONObject jsonObject = new JSONObject(response);
                Boolean success = jsonObject.getBoolean("success");
                if (success) {
                    callAttendantDialog.dismiss();
                    Toast.makeText(activity, StaticMessages.CALL_ATTENDANT.getName(), Toast.LENGTH_LONG).show();
                } else {
                    callAttendantDialog.dismiss();
                    Toast.makeText(activity, StaticMessages.CALL_ATTENDANT_ERROR.getName(), Toast.LENGTH_LONG).show();
                }
            } catch (Exception e) {
                callAttendantDialog.dismiss();
                Log.i(MAIN_ACTIVITY, StaticMessages.EXCEPTION.getName(), e);
                Toast.makeText(activity, StaticMessages.EXCEPTION.getName(), Toast.LENGTH_LONG).show();
            }
        }

        @Override
        protected void onCancelled() {
            callAttendantDialog.dismiss();
        }
    }
}