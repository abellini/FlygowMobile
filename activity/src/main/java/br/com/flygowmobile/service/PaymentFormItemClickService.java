package br.com.flygowmobile.service;

import android.widget.CheckBox;

import java.util.Map;

/**
 * Created by Tiago Rocha Gomes on 05/08/14.
 */
public class PaymentFormItemClickService {

    public static void onMarkItemClick(Map<Long, CheckBox> selects, CheckBox chk, boolean fromList){
        Long itemId = (Long) chk.getTag();
        if(!fromList){
            if (chk.isChecked()) {
                if(!selects.keySet().contains(itemId)) {
                    selects.put(itemId, chk);
                }
            } else {
                if(selects.keySet().contains(itemId)) {
                    selects.remove(itemId);
                }
            }
        } else {
            if(selects.keySet().contains(itemId)){
                selects.remove(itemId);
                chk.setChecked(false);
            } else {
                selects.put(itemId, chk);
                chk.setChecked(true);
            }
        }
    }
}
