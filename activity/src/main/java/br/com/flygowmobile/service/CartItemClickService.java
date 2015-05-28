package br.com.flygowmobile.service;

import android.widget.CheckBox;

import java.util.Map;

/**
 * Created by Tiago Rocha Gomes on 05/08/14.
 */
public class CartItemClickService {

    public static void onMarkItemClick(Map<Long, CheckBox> selects, CheckBox chk){
        Long itemId = (Long) chk.getTag();
        if (chk.isChecked()) {
            if(!selects.keySet().contains(itemId)) {
                selects.put(itemId, chk);
            }
        } else {
            if(selects.keySet().contains(itemId)) {
                selects.remove(itemId);
            }
        }
    }
}
