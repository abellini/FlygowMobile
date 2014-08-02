package br.com.flygowmobile.activity.navigationdrawer;

import java.io.ByteArrayInputStream;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.text.Html;
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
            if(row_pos.isPromoItem()){
                convertView = mInflater.inflate(R.layout.drawer_promotion_item, null);
                ImageView imgIcon = (ImageView) convertView.findViewById(R.id.promoitemicon);
                TextView txtTitle = (TextView) convertView.findViewById(R.id.promotitle);
                TextView txtItems = (TextView) convertView.findViewById(R.id.promoitems);
                TextView txtSubTitle = (TextView) convertView.findViewById(R.id.promosubtitle);
                TextView txtPrice = (TextView) convertView.findViewById(R.id.promoprice);

                txtTitle.setText(row_pos.getTitle());
                txtItems.setText(Html.fromHtml(row_pos.getPromoItems()));
                txtSubTitle.setText(row_pos.getSubtitle());
                txtPrice.setText(row_pos.getPrice());
                imgIcon.setImageResource(row_pos.getIcon());
            }else {
                convertView = mInflater.inflate(R.layout.drawer_fragment_layout, null);
                ImageView imgIcon = (ImageView) convertView.findViewById(R.id.itemicon);
                TextView txtTitle = (TextView) convertView.findViewById(R.id.title);
                TextView txtSubTitle = (TextView) convertView.findViewById(R.id.subtitle);
                TextView txtPrice = (TextView) convertView.findViewById(R.id.price);

                txtTitle.setText(row_pos.getTitle());
                txtSubTitle.setText(row_pos.getSubtitle());
                txtPrice.setText(row_pos.getPrice());
                imgIcon.setImageResource(row_pos.getIcon());
            }
        }else{
            if(row_pos.isTitle()){
                convertView = mInflater.inflate(R.layout.drawer_menu_title_item, null);
                String fontChillerPath = "fonts/CHILLER.TTF";
                Typeface chiller = Typeface.createFromAsset(context.getAssets(), fontChillerPath );
                TextView txtTitle = (TextView) convertView.findViewById(R.id.menu_title);
                TextView txtDescr = (TextView) convertView.findViewById(R.id.menu_description);
                txtTitle.setTypeface(chiller);
                txtTitle.setText(row_pos.getTitle());
                txtDescr.setText(row_pos.getSubtitle());
            }else{
                convertView = mInflater.inflate(R.layout.drawer_group_header_item, null);
                ImageView imgIcon = (ImageView) convertView.findViewById(R.id.headericon);
                TextView txtTitle = (TextView) convertView.findViewById(R.id.header);

                byte[] outImage = row_pos.getImage();
                if(outImage != null && outImage.length > 0){
                    ByteArrayInputStream imageStream = new ByteArrayInputStream(outImage);
                    Bitmap theImage = BitmapFactory.decodeStream(imageStream);
                    imgIcon.setImageBitmap(theImage);
                }else{
                    imgIcon.setImageResource(row_pos.getIcon());
                }
                txtTitle.setText(row_pos.getTitle());
            }
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