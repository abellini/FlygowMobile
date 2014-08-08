package br.com.flygowmobile.service;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.Toast;

import java.util.List;
import java.util.Map;

import br.com.flygowmobile.activity.R;
import br.com.flygowmobile.activity.navigationdrawer.AccompanimentAdapter;
import br.com.flygowmobile.activity.navigationdrawer.AccompanimentRowItem;
import br.com.flygowmobile.entity.Accompaniment;
import br.com.flygowmobile.entity.Food;
import br.com.flygowmobile.entity.Product;
import br.com.flygowmobile.entity.Promotion;
import br.com.flygowmobile.enums.StaticMessages;
import br.com.flygowmobile.enums.StaticTitles;
import br.com.flygowmobile.mapper.AccompanimentMapper;

/**
 * Created by Tiago Rocha Gomes on 07/08/14.
 */
public class OrderService {

    private AccompanimentMapper accompanimentMapper;
    private Activity activity;
    private Product productItem;
    private List<Accompaniment> accompanimentList;
    private Map<Long, CheckBox> selects;


    public OrderService(Context ctx){
        accompanimentMapper = new AccompanimentMapper(ctx);
    }

    public void orderAction(
            final Activity activity,
            final Product productItem,
            List<Accompaniment> accompanimentList,
            final Map<Long, CheckBox> selects){

        this.activity = activity;
        this.productItem = productItem;
        this.accompanimentList = accompanimentList;
        this.selects = selects;

        if(this.productItem instanceof Food){
            foodOrderAction();
        } else if (this.productItem instanceof Promotion) {
            promotionOrderAction();
        }
    }

    private void foodOrderAction(){
        final Food foodItem = (Food) productItem;
        if(accompanimentList != null && !accompanimentList.isEmpty()){
            AlertDialog dialog = null;
            AlertDialog.Builder builder = new AlertDialog.Builder(activity);
            String popupTitle = StaticTitles.ACCOMPANIMENT_POPUP.getName();
            if(foodItem.getMaxQtdAccompaniments() > 0){
                popupTitle += " (" + StaticMessages.MAXIMUM_OF.getName() + " " + foodItem.getMaxQtdAccompaniments() + " "  + StaticMessages.OPTIONS.getName() + ")" ;
            }
            builder.setTitle(popupTitle);
            builder.setPositiveButton(StaticTitles.CONTINUE.getName(), new
                    DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            //TODO: Implements continue buttom
                        }
                    });
            builder.setNegativeButton(StaticTitles.CANCEL.getName(), new
                    DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            //TODO: Implements cancel buttom
                        }
                    });
            final ListView list = new ListView(activity);
            list.setAdapter(
                    new AccompanimentAdapter(
                            activity,
                            accompanimentMapper.accompanimentToRowItem(accompanimentList),
                            selects,
                            foodItem
                    )
            );
            list.setOnItemClickListener(new AdapterView.OnItemClickListener(){
                @Override
                public void onItemClick(AdapterView<?> arg0, View view, int position, long arg3){
                    CheckBox chk = (CheckBox) view.findViewById(R.id.checkbox);
                    chk.setTag(((AccompanimentRowItem)list.getItemAtPosition(position)).getId());
                    AccompanimentItemClickService.onMarkItemClick(selects, foodItem.getMaxQtdAccompaniments(), chk, true);
                }
            });
            builder.setView(list);
            dialog = builder.create();
            dialog.show();
        }
    }

    private void promotionOrderAction(){
        //TODO: Implements promotion order action
    }
}
