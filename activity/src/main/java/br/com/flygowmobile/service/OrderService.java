package br.com.flygowmobile.service;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.List;
import java.util.Map;

import br.com.flygowmobile.Utils.FunctionUtils;
import br.com.flygowmobile.activity.MainActivity;
import br.com.flygowmobile.activity.R;
import br.com.flygowmobile.activity.navigationdrawer.AccompanimentAdapter;
import br.com.flygowmobile.activity.navigationdrawer.AccompanimentRowItem;
import br.com.flygowmobile.database.RepositoryCoin;
import br.com.flygowmobile.database.RepositoryOrder;
import br.com.flygowmobile.database.RepositoryOrderItem;
import br.com.flygowmobile.database.RepositoryOrderItemAccompaniment;
import br.com.flygowmobile.database.RepositoryTablet;
import br.com.flygowmobile.entity.Accompaniment;
import br.com.flygowmobile.entity.Coin;
import br.com.flygowmobile.entity.Food;
import br.com.flygowmobile.entity.Order;
import br.com.flygowmobile.entity.OrderItem;
import br.com.flygowmobile.entity.OrderItemAccompaniment;
import br.com.flygowmobile.entity.Product;
import br.com.flygowmobile.entity.Promotion;
import br.com.flygowmobile.entity.Tablet;
import br.com.flygowmobile.enums.OrderStatusTypeEnum;
import br.com.flygowmobile.enums.ProductTypeEnum;
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

    private View qtdObservationsPopup;

    private RepositoryTablet repositoryTablet;
    private RepositoryCoin repositoryCoin;
    private RepositoryOrder repositoryOrder;
    private RepositoryOrderItem repositoryOrderItem;
    private RepositoryOrderItemAccompaniment repositoryOrderItemAccompaniment;

    private static final int MAX_QUANTITY = 99;
    private static final int MIN_QUANTITY = 1;

    public OrderService(Context ctx){
        accompanimentMapper = new AccompanimentMapper(ctx);
        repositoryTablet = new RepositoryTablet(ctx);
        repositoryCoin = new RepositoryCoin(ctx);
        repositoryOrder = new RepositoryOrder(ctx);
        repositoryOrderItem = new RepositoryOrderItem(ctx);
        repositoryOrderItemAccompaniment = new RepositoryOrderItemAccompaniment(ctx);
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

    public Double getTotalOrderValue(){
        double totalValue = 0.0;
        List<OrderItem> orderItems = repositoryOrderItem.listAll();
        for(OrderItem orderItem : orderItems){
            totalValue += (orderItem.getQuantity() * orderItem.getValue());
            List<Accompaniment> accompaniments =
                    repositoryOrderItemAccompaniment.findByOrderItemId(orderItem.getOrderItemId());
            for(Accompaniment accompaniment : accompaniments){
                totalValue += accompaniment.getValue();
            }
        }
        return totalValue;
    }

    public String getFormatedTotalValue(){
        Coin coin = repositoryCoin.findById(repositoryTablet.findLast().getCoinId());
        return FunctionUtils.getMonetaryString(coin, getTotalOrderValue());
    }

    private void foodOrderAction(){
        Food foodItem = (Food) productItem;
        if(accompanimentList != null && !accompanimentList.isEmpty()){
            createAccompanimentPopup(foodItem);
        } else {
            createQtdObservationsPopup(foodItem);
        }
    }

    private void createAccompanimentPopup(final Food foodItem){

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
                        dialog.dismiss();
                        createQtdObservationsPopup(foodItem);
                    }
                });
        builder.setNegativeButton(StaticTitles.CANCEL.getName(), new
                DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        if(selects != null && !selects.isEmpty()){
                            selects.clear();
                        }
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

    private void createQtdObservationsPopup(final Product item){
        AlertDialog dialog = null;
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle(StaticTitles.MAKE_ORDER.getName());
        builder.setPositiveButton(StaticTitles.CONTINUE.getName(), new
                DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        saveOrderItem(item);
                        dialog.dismiss();
                    }
                });
        builder.setNegativeButton(StaticTitles.CANCEL.getName(), new
                DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        if(selects != null && !selects.isEmpty()){
                            selects.clear();
                        }
                    }
                });

        builder.setView(buildQtdObservationsPopup());
        dialog = builder.create();
        dialog.show();
    }

    private View buildQtdObservationsPopup(){
        LayoutInflater mInflater = (LayoutInflater) activity
                .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        qtdObservationsPopup = mInflater.inflate(R.layout.qtd_observations_popup_layout, null);

        String fontChillerPath = "fonts/CHILLER.TTF";
        String fontErasMediumPath = "fonts/ERASMD.TTF";

        Typeface chiller = Typeface.createFromAsset(activity.getAssets(), fontChillerPath );
        Typeface erasMedium = Typeface.createFromAsset(activity.getAssets(), fontErasMediumPath);

        TextView qtdTitleView = (TextView)qtdObservationsPopup.findViewById(R.id.qtdTitle);
        TextView qtdObsView = (TextView)qtdObservationsPopup.findViewById(R.id.obsTitle);
        final TextView quantityView = (TextView)qtdObservationsPopup.findViewById(R.id.qtdNumber);
        TextView qtdSubTitleView = (TextView)qtdObservationsPopup.findViewById(R.id.qtdSubTitle);
        TextView obsSubTitleView = (TextView)qtdObservationsPopup.findViewById(R.id.obsSubTitle);
        EditText obsDescView = (EditText)qtdObservationsPopup.findViewById(R.id.obsDesc);

        qtdTitleView.setTypeface(chiller);
        qtdObsView.setTypeface(chiller);
        quantityView.setTypeface(erasMedium);
        obsDescView.setTypeface(erasMedium);

        qtdTitleView.setText(StaticTitles.QUANTITY_CHOICES.getName());
        qtdObsView.setText(StaticTitles.ENTER_OBSERVATIONS.getName());
        qtdSubTitleView.setText(StaticMessages.QUANTITY_SUBTILE.getName());
        obsSubTitleView.setText(StaticMessages.OBSERVATIONS_SUBTITLE.getName());
        obsDescView.setHint(StaticMessages.OBSERVATIONS_EMPTY_TEXT.getName());
        quantityView.setText(StaticMessages.DEFAULT_QTD_ORDER.getName());

        Button qtdPlus = (Button)qtdObservationsPopup.findViewById(R.id.btn_plus);
        Button qtdMinus = (Button)qtdObservationsPopup.findViewById(R.id.btn_minus);

        qtdPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                plusItem(quantityView);
            }
        });

        qtdMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                minusItem(quantityView);
            }
        });
        return qtdObservationsPopup;
    }

    private void plusItem(TextView view){
        int actualQtd = Integer.parseInt(view.getText() + "");
        if(actualQtd < MAX_QUANTITY){
            actualQtd++;
            view.setText(FunctionUtils.addZero(actualQtd));
        }
    }

    private void minusItem(TextView view){
        int actualQtd = Integer.parseInt(view.getText()+"");
        if(actualQtd > MIN_QUANTITY){
            actualQtd--;
            view.setText(FunctionUtils.addZero(actualQtd));
        }
    }

    private void promotionOrderAction(){
        Promotion promoItem = (Promotion) productItem;
        createQtdObservationsPopup(promoItem);
    }

    private void saveOrderItem(Product item){
        ProgressDialog progressOrderItemDialog = ProgressDialog.show(activity, StaticTitles.LOAD.getName(),
                StaticMessages.LOAD_ORDER_ITEM.getName(), true);;
        try{
            Order order = getCurrentOrder();
            final TextView quantityView = (TextView)qtdObservationsPopup.findViewById(R.id.qtdNumber);
            EditText obsDescView = (EditText)qtdObservationsPopup.findViewById(R.id.obsDesc);
            OrderItem orderItem = new OrderItem();
            orderItem.setOrderId(order.getOrderId());
            if(item instanceof Food){
                orderItem.setFoodId(((Food)item).getFoodId());
                orderItem.setProductType(ProductTypeEnum.FOOD.getName());
            } else if (item instanceof Promotion){
                orderItem.setFoodId(((Promotion)item).getPromotionId());
                orderItem.setProductType(ProductTypeEnum.PROMOTION.getName());
            }
            orderItem.setQuantity(Integer.parseInt(quantityView.getText().toString()));
            orderItem.setValue(item.getValue());
            orderItem.setObservations(obsDescView.getText().toString());
            orderItem.setOrderItemId(repositoryOrderItem.save(orderItem));
            if(orderItem.getOrderItemId() != -1 && selects != null && !selects.isEmpty()){
                for(Long accId : selects.keySet()){
                    OrderItemAccompaniment orderItemAccompaniment = new OrderItemAccompaniment();
                    orderItemAccompaniment.setAccompanimentId(accId);
                    orderItemAccompaniment.setOrderItemId(orderItem.getOrderItemId());
                    repositoryOrderItemAccompaniment.save(orderItemAccompaniment);
                }
            }
            progressOrderItemDialog.dismiss();
            //Refresh the actionBar total order
            ((MainActivity)activity).mainActionBarService.refreshActionBarPrice();

            //TODO: GO TO THE CART ACTIVITY
        }catch (Exception e){
            Log.e("OrderService", e.getMessage());
            progressOrderItemDialog.dismiss();
        }
    }

    private Order getCurrentOrder(){
        Order order = null;
        try{
            order = repositoryOrder.getByStatusType(OrderStatusTypeEnum.OPENED.getId());
            if(order == null){
                Tablet thisTablet = repositoryTablet.findLast();
                order = new Order();
                order.setTabletId(thisTablet.getTabletId());
                order.setAttendantId(thisTablet.getAttendantId());
                //order.setClientId(); TODO: future, implements the client in the order
                order.setHour(new Date(new java.util.Date().getTime()));
                order.setStatusType(OrderStatusTypeEnum.OPENED.getId());
                order.setTotalValue(BigDecimal.ZERO.doubleValue());
                order.setOrderId(repositoryOrder.save(order));
            }
        }catch (Exception e){
            Log.e("OrderService", e.getMessage());
        }
        return order;
    }
}
