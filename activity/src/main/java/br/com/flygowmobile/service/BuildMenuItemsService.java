package br.com.flygowmobile.service;

import android.content.Context;
import android.content.res.TypedArray;

import java.util.ArrayList;
import java.util.List;

import br.com.flygowmobile.activity.navigationdrawer.RowItem;
import br.com.flygowmobile.database.RepositoryCategory;
import br.com.flygowmobile.database.RepositoryCoin;
import br.com.flygowmobile.database.RepositoryFood;
import br.com.flygowmobile.database.RepositoryTablet;
import br.com.flygowmobile.entity.Category;
import br.com.flygowmobile.entity.Coin;
import br.com.flygowmobile.entity.Food;
import br.com.flygowmobile.entity.Tablet;
import br.com.flygowmobile.enums.StaticMessages;

/**
 * Created by Tiago Rocha Gomes on 01/06/14.
 */
public class BuildMenuItemsService {

    private RepositoryTablet repositoryTablet;
    private RepositoryCategory repositoryCategory;
    private RepositoryCoin repositoryCoin;
    private RepositoryFood repositoryFood;
    private TypedArray menuIcons;

    private final static String CHARACTER_SPACE = " ";

    public BuildMenuItemsService(Context ctx, TypedArray menuIcons){
        repositoryTablet = new RepositoryTablet(ctx);
        repositoryCategory = new RepositoryCategory(ctx);
        repositoryFood = new RepositoryFood(ctx);
        repositoryCoin = new RepositoryCoin(ctx);
        this.menuIcons = menuIcons;
    }

    public List<RowItem> getMenuItems(){
        List<Category> headerTitles = getCategoryHeaders();
        List<RowItem> rowItems = new ArrayList<RowItem>();
        for(Category header : headerTitles){
            RowItem headerItem = new RowItem(header.getName(), menuIcons.getResourceId(1, -1), true);
            rowItems.add(headerItem);
            List<Food> foods = getFoodsByCategory(header);
            for(Food food : foods){
                RowItem items = new RowItem(food.getName(), menuIcons.getResourceId(
                        0, -1), false);
                items.setSubtitle(StaticMessages.MORE_DETAILS.getName());
                items.setPrice(formatFoodValue(food.getValue()));
                rowItems.add(items);
            }
        }
        return rowItems;
    }

    private List<Category> getCategoryHeaders(){
        List<Category> categoriesModel = repositoryCategory.listAll();
        return categoriesModel;
    }

    private List<Food> getFoodsByCategory(Category category){
        List<Food> foodsByCategory = repositoryFood.listByCategoryId(category.getCategoryId());
        return foodsByCategory;
    }

    private String formatFoodValue(Double value){
        Coin coin = repositoryCoin.findLast();
        return coin.getSymbol() + CHARACTER_SPACE + String.format("%.2f", value);
    }
}
