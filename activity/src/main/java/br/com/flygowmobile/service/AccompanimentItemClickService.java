package br.com.flygowmobile.service;

import android.widget.CheckBox;

import java.util.Map;

/**
 * Created by Tiago Rocha Gomes on 05/08/14.
 */
public class AccompanimentItemClickService {

    public static void onMarkItemClick(Map<Long, CheckBox> selects, Integer maxQtdSelects, CheckBox chk, boolean fromList){
        Long itemId = (Long) chk.getTag();
        if(!fromList){
            if (chk.isChecked()) {
                if(!selects.keySet().contains(itemId)) {
                    if(selects.size() < maxQtdSelects){
                        selects.put(itemId, chk);
                    } else if(maxQtdSelects == 0 || maxQtdSelects == null){
                        selects.put(itemId, chk);
                    } else {
                        chk.setChecked(false);
                    }
                }
            } else {
                if(selects.keySet().contains(itemId))
                    selects.remove(itemId);
            }
        } else {
            if(selects.keySet().contains(itemId)){
                selects.remove(itemId);
                chk.setChecked(false);
            } else {
                if(selects.size() < maxQtdSelects) {
                    selects.put(itemId, chk);
                    chk.setChecked(true);
                } else if(maxQtdSelects == 0 || maxQtdSelects == null){
                    selects.put(itemId, chk);
                    chk.setChecked(true);
                }
            }
        }
    }
}
