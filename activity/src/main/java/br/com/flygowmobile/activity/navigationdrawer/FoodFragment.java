package br.com.flygowmobile.activity.navigationdrawer;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import br.com.flygowmobile.Utils.FlygowAlertDialog;
import br.com.flygowmobile.activity.R;
import br.com.flygowmobile.database.RepositoryFood;
import br.com.flygowmobile.entity.Food;
import br.com.flygowmobile.enums.StaticTitles;

/**
 * Created by Tiago Rocha Gomes on 21/07/14.
 */
public class FoodFragment extends ProductFragment {

    private RepositoryFood repositoryFood;
    private RowItem item;
    private int itemPosition;
    private boolean fromArrow;
    private Food foodItem;
    private ListView mDrawerList;
    private Activity activity;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.product_fragment, container, false);
        activity = getActivity();

        repositoryFood = new RepositoryFood(activity);
        item = (RowItem) getArguments().get("item");
        itemPosition = getArguments().getInt("itemPosition");
        fromArrow = getArguments().getBoolean("fromArrow");
        mDrawerList = (ListView) activity.findViewById(R.id.slider_list);

        foodItem = repositoryFood.findById(item.getId());

        defineFonts(activity, rootView);
        setFoodMedia(activity, rootView, foodItem);
        setProductPrice(rootView, item);
        setProductTitle(rootView, item);
        setProductDescription(rootView, foodItem);
        setProductNutritionalInfo(rootView);
        defineDirectionalArrows(activity, rootView, itemPosition, mDrawerList);
        defineOrderButton(activity, rootView);

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
}