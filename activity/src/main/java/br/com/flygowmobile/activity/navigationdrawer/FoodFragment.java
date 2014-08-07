package br.com.flygowmobile.activity.navigationdrawer;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.com.flygowmobile.Utils.FlygowAlertDialog;
import br.com.flygowmobile.activity.MainActivity;
import br.com.flygowmobile.activity.R;
import br.com.flygowmobile.database.RepositoryAccompaniment;
import br.com.flygowmobile.database.RepositoryFood;
import br.com.flygowmobile.database.RepositoryFoodAccompaniment;
import br.com.flygowmobile.entity.Accompaniment;
import br.com.flygowmobile.entity.Food;
import br.com.flygowmobile.entity.FoodAccompaniment;
import br.com.flygowmobile.enums.StaticMessages;
import br.com.flygowmobile.enums.StaticTitles;
import br.com.flygowmobile.mapper.AccompanimentMapper;
import br.com.flygowmobile.service.AccompanimentItemClickService;

/**
 * Created by Tiago Rocha Gomes on 21/07/14.
 */
public class FoodFragment extends ProductFragment {

    private RepositoryFood repositoryFood;
    private RepositoryFoodAccompaniment repositoryFoodAccompaniment;
    private AccompanimentMapper accompanimentMapper;
    private RowItem item;
    private int itemPosition;
    private boolean fromArrow;
    private Food foodItem;
    private List<Accompaniment> accompanimentList;
    private ListView mDrawerList;
    private Activity activity;

    private Map<Long, CheckBox> selects = new HashMap<Long, CheckBox>();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.product_fragment, container, false);
        activity = getActivity();

        repositoryFood = new RepositoryFood(activity);
        repositoryFoodAccompaniment = new RepositoryFoodAccompaniment(activity);

        accompanimentMapper = new AccompanimentMapper(activity);

        item = (RowItem) getArguments().get("item");
        itemPosition = getArguments().getInt("itemPosition");
        fromArrow = getArguments().getBoolean("fromArrow");
        mDrawerList = (ListView) activity.findViewById(R.id.slider_list);

        foodItem = repositoryFood.findById(item.getId());
        accompanimentList = repositoryFoodAccompaniment.findByFoodId(foodItem.getFoodId());


        defineFonts(activity, rootView);
        setFoodMedia(activity, rootView, foodItem);
        setProductPrice(rootView, item);
        setProductTitle(rootView, item);
        setProductDescription(rootView, foodItem);
        setProductNutritionalInfo(rootView);
        defineDirectionalArrows(activity, rootView, itemPosition, mDrawerList, fromArrow);
        defineOrderButton(rootView);

        if(fromArrow){
            alignProductDetailsToCenter(rootView);
        }
        return rootView;
    }


    private void setProductNutritionalInfo(View rootView){
        Button btnInfo = (Button)rootView.findViewById(R.id.btnNutritionalInfo);
        if(foodItem != null && foodItem.getNutritionalInfo() != null && !foodItem.getNutritionalInfo().isEmpty()){
            btnInfo.setVisibility(View.VISIBLE);
            btnInfo.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    FlygowAlertDialog.createInfoPopup(activity, StaticTitles.INFORMATION, foodItem.getNutritionalInfo());
                }
            });
        }else{
            btnInfo.setVisibility(View.INVISIBLE);
        }
    }

    private void defineOrderButton(View rootView){
        Button btnOrder = (Button) rootView.findViewById(R.id.btnOrder);
        TextView price = (TextView) rootView.findViewById(R.id.price);
        price.setOnClickListener(getOrderClick(rootView));
        btnOrder.setOnClickListener(getOrderClick(rootView));
    }

    private View.OnClickListener getOrderClick(final View rootView){

        return new View.OnClickListener() {
            public void onClick(View v) {
                if(accompanimentList != null && !accompanimentList.isEmpty()){
                    AlertDialog dialog = null;
                    AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                    String popupTitle = StaticTitles.ACCOMPANIMENT_POPUP.getName();
                    if(foodItem.getMaxQtdAccompaniments() > 0){
                        popupTitle += " (" + StaticMessages.MAXIMUM_OF + foodItem.getMaxQtdAccompaniments() + StaticMessages.OPTIONS + ")" ;
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
                            AccompanimentItemClickService.onMarkItemClick(selects, foodItem.getMaxQtdAccompaniments(), chk , true);
                        }
                    });
                    builder.setView(list);
                    dialog = builder.create();
                    dialog.show();
                } else {
                    //TODO: Implements dialog wihout accompaniments
                }

            }
        };
    };
}