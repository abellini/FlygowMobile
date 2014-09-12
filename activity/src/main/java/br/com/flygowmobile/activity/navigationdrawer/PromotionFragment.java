package br.com.flygowmobile.activity.navigationdrawer;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

import br.com.flygowmobile.Utils.FlygowAlertDialog;
import br.com.flygowmobile.activity.R;
import br.com.flygowmobile.database.RepositoryFoodPromotion;
import br.com.flygowmobile.database.RepositoryPromotion;
import br.com.flygowmobile.entity.Food;
import br.com.flygowmobile.entity.Promotion;
import br.com.flygowmobile.enums.StaticTitles;

/**
 * Created by Tiago Rocha Gomes on 21/07/14.
 */
public class PromotionFragment extends ProductFragment {

    private RepositoryPromotion repositoryPromotion;
    private RepositoryFoodPromotion repositoryFoodPromotion;
    private RowItem item;
    private int itemPosition;
    private boolean fromArrow;
    private Promotion promotionItem;
    private ListView mDrawerList;
    private Activity activity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View rootView = inflater.inflate(R.layout.product_fragment, container, false);
        activity = getActivity();

        repositoryPromotion = new RepositoryPromotion(activity);
        repositoryFoodPromotion = new RepositoryFoodPromotion(activity);

        item = (RowItem) getArguments().get("item");
        itemPosition = getArguments().getInt("itemPosition");
        fromArrow = getArguments().getBoolean("fromArrow");
        mDrawerList = (ListView) activity.findViewById(R.id.slider_list);

        promotionItem = repositoryPromotion.findById(item.getId());

        defineTexts(rootView);
        defineFonts(rootView);
        setFoodMedia(rootView, promotionItem);
        setProductPrice(rootView, item);
        setProductTitle(rootView, item);
        setProductDescription(rootView, promotionItem);
        setInfo(rootView);
        defineDirectionalArrows(rootView, itemPosition, mDrawerList, fromArrow);
        defineSwipe(rootView, itemPosition, mDrawerList);
        defineOrderButton(rootView);
        if (fromArrow) {
            alignProductDetailsToCenter(rootView);
        }
        return rootView;
    }

    private void setInfo(View rootView) {
        Button btnInfo = (Button) rootView.findViewById(R.id.btnNutritionalInfo);
        List<Food> foods = repositoryFoodPromotion.listByPromotion(promotionItem.getPromotionId());
        if (foods != null && !foods.isEmpty()) {
            btnInfo.setVisibility(View.VISIBLE);
            String info = "";
            for (Food food : foods) {
                info += " - " + food.getName() + "<br>";
            }
            final String finalInfo = info;
            btnInfo.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    FlygowAlertDialog.createInfoPopup(activity, StaticTitles.PROMOTION_INFO, finalInfo);
                }
            });
        } else {
            btnInfo.setVisibility(View.INVISIBLE);
        }
    }

    private void defineOrderButton(View rootView) {
        Button btnOrder = (Button) rootView.findViewById(R.id.btnOrder);
        TextView price = (TextView) rootView.findViewById(R.id.price);
        price.setOnClickListener(getOrderClick(null, promotionItem, null));
        btnOrder.setOnClickListener(getOrderClick(null, promotionItem, null));
    }
}