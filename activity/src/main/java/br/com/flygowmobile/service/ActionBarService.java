package br.com.flygowmobile.service;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import java.util.HashMap;
import java.util.Map;

import br.com.flygowmobile.Utils.FontUtils;
import br.com.flygowmobile.activity.R;
import br.com.flygowmobile.activity.navigationdrawer.PaymentFormAdapter;
import br.com.flygowmobile.activity.navigationdrawer.PaymentFormRowItem;
import br.com.flygowmobile.database.RepositoryPaymentForm;
import br.com.flygowmobile.enums.FontTypeEnum;
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
    private CallAttendantService callAttendantService;
    private View actionBarView;
    private Map<Long, CheckBox> paymentFormSelects;
    private RepositoryPaymentForm repositoryPaymentForm;
    private FontUtils fontUtils;

    private static final String MAIN_ACTIVITY = "MainActivity";

    public ActionBarService(Activity activity, View actionBarView){
        this.activity = activity;
        this.actionBarView = actionBarView;
        this.orderService = new OrderService(activity);
        this.callAttendantService = new CallAttendantService(activity);
        this.endOrderService = new EndOrderService(activity);
        this.fontUtils = new FontUtils(activity);
        repositoryPaymentForm = new RepositoryPaymentForm(activity);
        paymentFormSelects = new HashMap<Long, CheckBox>();
    }

    public void buildActionBar(boolean enableHomeBtn){
        ActionBar actionBar = activity.getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(enableHomeBtn);
        actionBar.setHomeButtonEnabled(enableHomeBtn);

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
        fontUtils.applyFont(FontTypeEnum.LOGOTYPE, mTitleTextView);
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
                            callAttendantService.callAttendant();

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
}