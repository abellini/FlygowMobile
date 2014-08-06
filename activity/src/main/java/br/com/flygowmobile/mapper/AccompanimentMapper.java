package br.com.flygowmobile.mapper;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

import br.com.flygowmobile.activity.R;
import br.com.flygowmobile.activity.navigationdrawer.AccompanimentRowItem;
import br.com.flygowmobile.entity.Accompaniment;
import br.com.flygowmobile.enums.StaticMessages;
import br.com.flygowmobile.service.BuildMenuItemsService;

/**
 * Created by Tiago Rocha Gomes on 05/08/14.
 */
public class AccompanimentMapper {

    private BuildMenuItemsService buildMenuItemsService;

    public AccompanimentMapper (Context ctx){
        buildMenuItemsService = new BuildMenuItemsService(ctx);
    }

    public List<AccompanimentRowItem> accompanimentToRowItem(List<Accompaniment> accompanimentList){
        List<AccompanimentRowItem> accompanimentRowItems = null;
        if(accompanimentList != null && !accompanimentList.isEmpty()){
            accompanimentRowItems = new ArrayList<AccompanimentRowItem>();
            for(Accompaniment accompaniment : accompanimentList){
                accompanimentRowItems.add(
                        new AccompanimentRowItem(
                                accompaniment.getAccompanimentId(),
                                accompaniment.getName(),
                                accompaniment.getDescription(),
                                accompaniment.getValue() > 0 ?
                                        buildMenuItemsService.formatItemValue(accompaniment.getValue()) :
                                        StaticMessages.FREE.getName(),
                                R.drawable.plus
                        )
                );
            }
        }
        return accompanimentRowItems;
    }
}
