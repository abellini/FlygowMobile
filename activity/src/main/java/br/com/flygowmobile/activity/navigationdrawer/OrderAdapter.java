package br.com.flygowmobile.activity.navigationdrawer;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;
import java.util.Map;

import br.com.flygowmobile.activity.R;
import br.com.flygowmobile.service.OrderService;

public class OrderAdapter extends BaseAdapter {

    private static final String ORDER_ADAPTER = "OrderAdapter";

    private Context context;
    private List<OrderRowItem> rowItem;
    private Map<Long, CheckBox> selects;
    private OrderService orderService;

    public OrderAdapter(Context context, List<OrderRowItem> rowItem, Map<Long, CheckBox> selects) {
        this.context = context;
        this.rowItem = rowItem;
        this.selects = selects;
        this.orderService = new OrderService(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

        if (rowItem != null) {
            OrderRowItem row_pos = rowItem.get(position);
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
                quantity.setText(row_pos.getQuantity());
                priceUnit.setText(orderService.getFormatedValue(row_pos.getPriceUnit()));
                priceTotal.setText(orderService.getFormatedValue(row_pos.getPriceTotal()));

                CheckBox checkBox = (CheckBox) convertView.findViewById(R.id.checkboxOrder);
                checkBox.setTag(row_pos.getId());
                checkBox.setChecked(selects.containsKey(row_pos.getId()));
                checkBox.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast toast = Toast.makeText(context, "Action!", Toast.LENGTH_LONG);
                        toast.show();
                    }
                });

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