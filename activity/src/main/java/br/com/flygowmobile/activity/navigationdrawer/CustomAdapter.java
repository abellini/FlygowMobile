package br.com.flygowmobile.activity.navigationdrawer;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import br.com.flygowmobile.activity.R;

public class CustomAdapter extends BaseAdapter {

    Context context;
    List<RowItem> rowItem;

    public CustomAdapter(Context context, List<RowItem> rowItem) {
        this.context = context;
        this.rowItem = rowItem;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater mInflater = (LayoutInflater) context
                    .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);


        RowItem row_pos = rowItem.get(position);
        if(!row_pos.isGroupHeader()){
            convertView = mInflater.inflate(R.layout.drawer_fragment_layout, null);
            ImageView imgIcon = (ImageView) convertView.findViewById(R.id.itemicon);
            TextView txtTitle = (TextView) convertView.findViewById(R.id.title);
            TextView txtSubTitle = (TextView) convertView.findViewById(R.id.subtitle);
            TextView txtPrice = (TextView) convertView.findViewById(R.id.price);

            txtTitle.setText(row_pos.getTitle());
            txtSubTitle.setText(row_pos.getSubtitle());
            txtPrice.setText(row_pos.getPrice());
            imgIcon.setImageResource(row_pos.getIcon());
        }else{
            convertView = mInflater.inflate(R.layout.drawer_group_header_item, null);
            ImageView imgIcon = (ImageView) convertView.findViewById(R.id.headericon);
            TextView txtTitle = (TextView) convertView.findViewById(R.id.header);

            imgIcon.setImageResource(row_pos.getIcon());
            txtTitle.setText(row_pos.getTitle());
        }
        return convertView;
    }

    @Override
    public int getCount() {
        return rowItem.size();
    }

    @Override
    public Object getItem(int position) {
        return rowItem.get(position);
    }

    @Override
    public long getItemId(int position) {
        return rowItem.indexOf(getItem(position));
    }
}