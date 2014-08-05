package br.com.flygowmobile.activity.navigationdrawer;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import br.com.flygowmobile.activity.R;

public class AccompanimentAdapter extends BaseAdapter {

    Context context;
    List<AccompanimentRowItem> rowItem;

    public AccompanimentAdapter(Context context, List<AccompanimentRowItem> rowItem) {
        this.context = context;
        this.rowItem = rowItem;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater mInflater = (LayoutInflater) context
                    .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

        if(rowItem != null) {
            AccompanimentRowItem row_pos = rowItem.get(position);
            if (row_pos != null) {
                convertView = mInflater.inflate(R.layout.accompaniment_fragment_layout, null);

                TextView title = (TextView) convertView.findViewById(R.id.title);
                TextView subtitle = (TextView) convertView.findViewById(R.id.subtitle);
                TextView price = (TextView) convertView.findViewById(R.id.price);

                title.setText(row_pos.getTitle());
                subtitle.setText(row_pos.getSubtitle());
                price.setText(row_pos.getPrice());
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