package br.com.flygowmobile.mapper;


import java.util.ArrayList;
import java.util.List;

import br.com.flygowmobile.activity.R;
import br.com.flygowmobile.activity.navigationdrawer.PaymentFormRowItem;
import br.com.flygowmobile.entity.PaymentForm;

/**
 * Created by Tiago Rocha Gomes on 20/09/14.
 */
public class PaymentFormMapper {

    public static List<PaymentFormRowItem> paymentToRowItem(List<PaymentForm> paymentList){
        List<PaymentFormRowItem> paymentRowItems = null;
        if(paymentList != null && !paymentList.isEmpty()){
            paymentRowItems = new ArrayList<PaymentFormRowItem>();
            for(PaymentForm payment : paymentList){
                paymentRowItems.add(
                        new PaymentFormRowItem(
                            payment.getPaymentFormId(),
                            payment.getName(),
                            payment.getDescription(),
                            R.drawable.payment_bullet
                        )
                );
            }
        }
        return paymentRowItems;
    }
}
