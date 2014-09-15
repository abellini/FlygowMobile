package br.com.flygowmobile.activity.navigationdrawer;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import br.com.flygowmobile.activity.R;
import br.com.flygowmobile.enums.StaticTitles;
import br.com.flygowmobile.service.BuildAccompanimentInfoService;
import br.com.flygowmobile.service.CartItemClickService;
import br.com.flygowmobile.service.OrderService;

public class OrderAdapter extends BaseAdapter {

    private static final String ORDER_ADAPTER = "OrderAdapter";

    private Activity activity;
    private List<OrderRowItem> rowItem;
    private Map<Long, CheckBox> selects;
    private OrderService orderService;
    private BuildAccompanimentInfoService buildAccompanimentInfoService;

    public OrderAdapter(Activity activity, List<OrderRowItem> rowItem, Map<Long, CheckBox> selects) {
        this.activity = activity;
        this.rowItem = rowItem;
        this.selects = selects;
        this.orderService = new OrderService(activity);
        this.buildAccompanimentInfoService = new BuildAccompanimentInfoService(activity);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater mInflater = (LayoutInflater) activity.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

        if (rowItem != null) {
            final OrderRowItem row_pos = rowItem.get(position);
            if (row_pos != null) {
                convertView = mInflater.inflate(R.layout.basket_fragment, null);

                TextView title = (TextView) convertView.findViewById(R.id.title);
                TextView observations = (TextView) convertView.findViewById(R.id.observations);
                TextView titleAcc = (TextView) convertView.findViewById(R.id.accTitle);
                TextView subTitleAcc = (TextView) convertView.findViewById(R.id.accSubtitle);
                ImageView accIcon = (ImageView)convertView.findViewById(R.id.accIcon);
                TextView quantity = (TextView) convertView.findViewById(R.id.quantity);
                TextView priceUnit = (TextView) convertView.findViewById(R.id.priceUnit);
                TextView priceTotal = (TextView) convertView.findViewById(R.id.priceTotal);

                title.setText(row_pos.getTitle());
                observations.setText(row_pos.getObservations());
                titleAcc.setText(StaticTitles.CART_ROW_ACC_TITLE.getName());
                subTitleAcc.setText(StaticTitles.CART_ROW_ACC_SUBTITLE.getName());
                quantity.setText(row_pos.getQuantity());
                priceUnit.setText(orderService.getFormatedValue(row_pos.getPriceUnit()));
                priceTotal.setText(orderService.getFormatedValue(row_pos.getPriceTotal()));

                CheckBox checkBox = (CheckBox) convertView.findViewById(R.id.checkboxOrder);
                checkBox.setTag(row_pos.getOrderItemId());
                checkBox.setChecked(selects.containsKey(row_pos.getOrderItemId()));
                checkBox.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        CartItemClickService.onMarkItemClick(selects, (CheckBox) v);
                    }
                });

                List<View> accompanimentInfoElements = new ArrayList<View>();
                accompanimentInfoElements.add(accIcon);
                accompanimentInfoElements.add(titleAcc);
                accompanimentInfoElements.add(subTitleAcc);

                for(View view : accompanimentInfoElements){
                    view.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            buildAccompanimentInfoService.onAccompanimentInfoClick(row_pos.getOrderItemId());
                        }
                    });
                }
            }
        }
        return convertView;
    }

    @Override
    public int getCount() {
        if (rowItem != null) {
            return rowItem.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int position) {
        if (rowItem != null) {
            return rowItem.get(position);
        }
        return null;
    }

    @Override
    public long getItemId(int position) {
        if (rowItem != null) {
            return rowItem.indexOf(getItem(position));
        }
        return -1;
    }
}