package br.com.flygowmobile.activity.navigationdrawer;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;
import java.util.Map;

import br.com.flygowmobile.activity.R;
import br.com.flygowmobile.entity.Food;
import br.com.flygowmobile.service.AccompanimentItemClickService;

public class AccompanimentAdapter extends BaseAdapter {

    private Context context;
    private List<AccompanimentRowItem> rowItem;
    private Map<Long, CheckBox> selects;
    private Food food;

    public AccompanimentAdapter(Context context, List<AccompanimentRowItem> rowItem, Map<Long, CheckBox> selects, Food food) {
        this.context = context;
        this.rowItem = rowItem;
        this.selects = selects;
        this.food = food;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater mInflater = (LayoutInflater) context
                    .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

        if(rowItem != null) {
            AccompanimentRowItem row_pos = rowItem.get(position);
            if (row_pos != null) {
                convertView = mInflater.inflate(R.layout.accompaniment_popup_layout, null);

                ImageView imageView = (ImageView) convertView.findViewById(R.id.icon);
                TextView title = (TextView) convertView.findViewById(R.id.title);
                TextView subtitle = (TextView) convertView.findViewById(R.id.subtitle);
                TextView price = (TextView) convertView.findViewById(R.id.price);

                imageView.setImageResource(row_pos.getIcon());
                title.setText(row_pos.getTitle());
                subtitle.setText(row_pos.getSubtitle());
                price.setText(row_pos.getPrice());

                CheckBox checkBox = (CheckBox) convertView.findViewById(R.id.checkbox);
                checkBox.setTag(row_pos.getId());
                checkBox.setChecked(selects.containsKey(row_pos.getId()));
                checkBox.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AccompanimentItemClickService.onMarkItemClick(selects, food.getMaxQtdAccompaniments(), (CheckBox)v, false);
                    }
                });
            }
        }
        return convertView;
    }

    @Override
    public int getCount() {
        if(rowItem != null){
            return rowItem.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int position) {
        if(rowItem != null){
            return rowItem.get(position);
        }
        return null;
    }

    @Override
    public long getItemId(int position) {
        if(rowItem != null){
            return rowItem.indexOf(getItem(position));
        }
        return -1;
    }
}