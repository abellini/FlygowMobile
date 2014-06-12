package br.com.flygowmobile.service;

import android.content.Context;
import android.content.res.TypedArray;

import java.util.ArrayList;
import java.util.List;

import br.com.flygowmobile.activity.navigationdrawer.RowItem;
import br.com.flygowmobile.database.RepositoryCategory;
import br.com.flygowmobile.database.RepositoryCoin;
import br.com.flygowmobile.database.RepositoryFood;
import br.com.flygowmobile.database.RepositoryFoodPromotion;
import br.com.flygowmobile.database.RepositoryPromotion;
import br.com.flygowmobile.database.RepositoryTablet;
import br.com.flygowmobile.entity.Category;
import br.com.flygowmobile.entity.Coin;
import br.com.flygowmobile.entity.Food;
import br.com.flygowmobile.entity.Promotion;
import br.com.flygowmobile.enums.StaticMessages;
import br.com.flygowmobile.enums.StaticTitles;

/**
 * Created by Tiago Rocha Gomes on 01/06/14.
 */
public class BuildMenuItemsService {

    private final static String CHARACTER_SPACE = " ";
    private final static Integer ICON_CATEGORY_DEFAULT = 0;
    private final static Integer ICON_MENU_ITEM = 1;
    private final static Integer ICON_PROMOTION_HEADER = 2;
    private RepositoryTablet repositoryTablet;
    private RepositoryCategory repositoryCategory;
    private RepositoryCoin repositoryCoin;
    private RepositoryFood repositoryFood;
    private RepositoryPromotion repositoryPromotion;
    private RepositoryFoodPromotion repositoryFoodPromotion;
    private TypedArray menuIcons;

    public BuildMenuItemsService(Context ctx, TypedArray menuIcons){
        repositoryTablet = new RepositoryTablet(ctx);
        repositoryCategory = new RepositoryCategory(ctx);
        repositoryFood = new RepositoryFood(ctx);
        repositoryPromotion = new RepositoryPromotion(ctx);
        repositoryFoodPromotion = new RepositoryFoodPromotion(ctx);
        repositoryCoin = new RepositoryCoin(ctx);
        this.menuIcons = menuIcons;
    }

    public List<RowItem> getMenuItems(){
        List<Category> headerTitles = getCategoryHeaders();
        List<RowItem> rowItems = new ArrayList<RowItem>();
        populateMenuItemsWithPromotions(rowItems);
        for(Category header : headerTitles){

            RowItem headerItem = new RowItem(
                    header.getName(),
                    menuIcons.getResourceId(ICON_CATEGORY_DEFAULT, -1),
                    true
            );
            headerItem.setImage(header.getPhoto());
            rowItems.add(headerItem);
            List<Food> foods = getFoodsByCategory(header);
            for(Food food : foods){
                RowItem items = new RowItem(
                        food.getFoodId(),
                        food.getName(),
                        menuIcons.getResourceId(ICON_MENU_ITEM, -1),
                        false
                );
                items.setSubtitle(StaticMessages.MORE_DETAILS.getName());
                items.setPrice(formatItemValue(food.getValue()));
                rowItems.add(items);
            }
        }
        return rowItems;
    }

    private void populateMenuItemsWithPromotions(List<RowItem> rowItems) {
        List<Promotion> promotions = repositoryPromotion.listAll();
        if(promotions != null && !promotions.isEmpty()){
            RowItem promoHeaderItem = new RowItem(
                    StaticTitles.PROMOTIONS.getName(),
                    menuIcons.getResourceId(ICON_PROMOTION_HEADER, -1),
                    true
            );
            rowItems.add(promoHeaderItem);
            for(Promotion promo : promotions){
                RowItem items = new RowItem(
                        promo.getName(),
                        menuIcons.getResourceId(ICON_MENU_ITEM, -1),
                        false
                );
                items.setPromoItem(true);
                List<Food> promotionItems = repositoryFoodPromotion.listByPromotion(promo.getPromotionId());
                String promoItemStr = "";
                int i = 0;
                for(Food item : promotionItems){
                    if(i != promotionItems.size() - 1){
                        promoItemStr += item.getName() + "<br>";
                    }else{
                        promoItemStr += item.getName();
                    }
                    i++;
                }
                items.setPromoItems(promoItemStr);
                items.setSubtitle(StaticMessages.MORE_DETAILS.getName());
                items.setPrice(formatItemValue(promo.getValue()));
                rowItems.add(items);
            }
        }
    }

    private List<Category> getCategoryHeaders(){
        List<Category> categoriesModel = repositoryCategory.listAll();
        return categoriesModel;
    }

    private List<Food> getFoodsByCategory(Category category){
        List<Food> foodsByCategory = repositoryFood.listByCategoryId(category.getCategoryId());
        return foodsByCategory;
    }

    private String formatItemValue(Double value){
        Coin coin = repositoryCoin.findLast();
        return coin.getSymbol() + CHARACTER_SPACE + String.format("%.2f", value);
    }
}
