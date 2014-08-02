package br.com.flygowmobile.activity.navigationdrawer;

import android.app.Activity;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.VideoView;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import br.com.flygowmobile.Utils.FlygowAlertDialog;
import br.com.flygowmobile.Utils.FlygowServerUrl;
import br.com.flygowmobile.Utils.MediaUtils;
import br.com.flygowmobile.Utils.StringUtils;
import br.com.flygowmobile.activity.R;
import br.com.flygowmobile.entity.Product;
import br.com.flygowmobile.enums.PositionsEnum;
import br.com.flygowmobile.enums.StaticMessages;
import br.com.flygowmobile.enums.StaticTitles;
import br.com.flygowmobile.service.ClickProductContentService;

/**
 * Created by Tiago Rocha Gomes on 02/08/14.
 */
public abstract class ProductFragment extends Fragment{

    protected  void defineFonts(Activity activity, View rootView){
        // Font path
        String fontGabriolaPath = "fonts/GABRIOLA.TTF";
        String fontErasBoldPath = "fonts/ERASBD.TTF";
        String fontErasMediumPath = "fonts/ERASMD.TTF";

        // Loading Font Face
        Typeface gabriola = Typeface.createFromAsset(activity.getAssets(), fontGabriolaPath );
        Typeface erasBold = Typeface.createFromAsset(activity.getAssets(), fontErasBoldPath);
        Typeface erasMedium = Typeface.createFromAsset(activity.getAssets(), fontErasMediumPath);


        // text view label
        TextView priceView = (TextView)rootView.findViewById(R.id.price);
        TextView clickHereView = (TextView)rootView.findViewById(R.id.clickHere);
        TextView pecaView = (TextView)rootView.findViewById(R.id.peca);
        TextView titleView = (TextView)rootView.findViewById(R.id.title);
        TextView descriptionView = (TextView)rootView.findViewById(R.id.description);

        // Applying font
        priceView.setTypeface(gabriola);
        clickHereView.setTypeface(gabriola);
        pecaView.setTypeface(gabriola);

        titleView.setTypeface(erasBold);
        descriptionView.setTypeface(erasMedium);
    }

    protected void defineDirectionalArrows(
            final Activity activity,
            View rootView,
            final int itemPosition,
            final ListView mDrawerList
    ){
        Button btnLeft = (Button)rootView.findViewById(R.id.btnArrowLeft);
        Button btnRight = (Button)rootView.findViewById(R.id.btnArrowRight);
        btnLeft.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                boolean fromArrow = true;
                try{
                    Map<Integer, RowItem> item = previousItem(itemPosition, mDrawerList);
                    if(item != null){
                        new ClickProductContentService(
                                activity,
                                item.keySet().iterator().next()
                        ).updateDisplay(item.values().iterator().next(), fromArrow);
                    }else{
                        RowItem lastItem = (RowItem) mDrawerList.getItemAtPosition( mDrawerList.getCount()-1);
                        if(lastItem != null) {
                            new ClickProductContentService(
                                    activity,
                                    mDrawerList.getCount()-1
                            ).updateDisplay(lastItem, fromArrow);
                        }
                    }
                }catch(Exception e){
                    RowItem lastItem = (RowItem) mDrawerList.getItemAtPosition( mDrawerList.getCount()-1);
                    if(lastItem != null) {
                        new ClickProductContentService(
                                activity,
                                mDrawerList.getCount()-1
                        ).updateDisplay(lastItem, fromArrow);
                    }
                }
            }
        });

        btnRight.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                boolean fromArrow = true;
                try{
                    Map<Integer, RowItem> item = nextItem(itemPosition, mDrawerList);
                    if(item != null){
                        new ClickProductContentService(
                                activity,
                                item.keySet().iterator().next()
                        ).updateDisplay(item.values().iterator().next(), fromArrow);
                    }else{
                        RowItem firstItem = getFirstProductItem(mDrawerList).values().iterator().next();
                        if(firstItem != null) {
                            new ClickProductContentService(
                                    activity,
                                    getFirstProductItem(mDrawerList).keySet().iterator().next()
                            ).updateDisplay(firstItem, fromArrow);
                        }
                    }
                }catch (Exception e){
                    RowItem firstItem = getFirstProductItem(mDrawerList).values().iterator().next();
                    if(firstItem != null) {
                        new ClickProductContentService(
                                activity,
                                getFirstProductItem(mDrawerList).keySet().iterator().next()
                        ).updateDisplay(firstItem, fromArrow);
                    }
                }
            }
        });
    }

    protected void defineOrderButton(final Activity activity, View rootView){
        Button btnOrder = (Button) rootView.findViewById(R.id.btnOrder);
        btnOrder.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                FlygowAlertDialog.createWarningPopup(activity, StaticTitles.WARNING, StaticMessages.TIMEOUT);
            }
        });
    }

    protected void setFoodMedia(Activity activity, View rootView, Product item){
        ProgressDialog progressPromotionInfoDialog = ProgressDialog.show(activity, StaticTitles.LOAD.getName(),
                StaticMessages.LOAD_PROMOTION.getName(), true);
        String videoName = item.getVideoName();
        if(StringUtils.isNotEmpty(videoName)){
            try {
                String foodVideoPath = MediaUtils.getVideo(activity, videoName);
                VideoView videoView = new VideoView(getActivity());
                RelativeLayout.LayoutParams videoLayout = new RelativeLayout.LayoutParams(
                        480, 360);
                videoView.setLayoutParams(videoLayout);
                videoView.setX(280);
                videoView.setY(5);
                videoView.setVideoPath(foodVideoPath);
                RelativeLayout mainLayout = (RelativeLayout) rootView.findViewById(R.id.imageBackground);
                mainLayout.addView(videoView);
                videoView.start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        PhotoWorkerTask task = new PhotoWorkerTask(activity, rootView, item, progressPromotionInfoDialog);
        task.execute();
    }


    protected void setProductPrice(View rootView, RowItem item){
        TextView priceView = (TextView)rootView.findViewById(R.id.price);
        String price = item.getPrice();
        priceView.setText(price);
    }

    protected void setProductTitle(View rootView, RowItem item){
        TextView titleView = (TextView)rootView.findViewById(R.id.title);
        String title = item.getTitle();
        titleView.setText(title);
    }

    protected void setProductDescription(View rootView, Product item){
        TextView descriptionView = (TextView)rootView.findViewById(R.id.description);
        if(item != null && item.getDescription() != null && !item.getDescription().isEmpty()){
            descriptionView.setText(item.getDescription());
        }
    }

    protected void alignProductDetailsToCenter(View rootView){
        final int MARGIN = PositionsEnum.PRODUCT_DETAILS.getMargin();
        try{
            TextView priceView = (TextView)rootView.findViewById(R.id.price);
            TextView clickHereView = (TextView)rootView.findViewById(R.id.clickHere);
            TextView pecaView = (TextView)rootView.findViewById(R.id.peca);
            TextView titleView = (TextView)rootView.findViewById(R.id.title);
            TextView descriptionView = (TextView)rootView.findViewById(R.id.description);
            Button btnOrder = (Button) rootView.findViewById(R.id.btnOrder);
            Button btnIdentifier = (Button) rootView.findViewById(R.id.btnNutritionalInfo);

            priceView.setX(priceView.getLeft()-MARGIN);
            clickHereView.setX(clickHereView.getLeft()-MARGIN);
            pecaView.setX(pecaView.getLeft()-MARGIN);
            titleView.setX(titleView.getLeft()-MARGIN);
            descriptionView.setX(descriptionView.getLeft()-MARGIN);
            btnOrder.setX(btnOrder.getLeft()-MARGIN);
            btnIdentifier.setX(btnIdentifier.getLeft()-MARGIN);

        }catch(Exception e){
            Log.w("Main Activity", "Dont Align Food Details to CENTER FROM ARROW");
        }
    }

    private Map<Integer, RowItem> getFirstProductItem(final ListView mDrawerList){
        Map<Integer, RowItem> item = new HashMap<Integer, RowItem>();
        for(int i = 0; i < mDrawerList.getCount(); i++){
            RowItem rowItem = (RowItem) mDrawerList.getItemAtPosition(i);
            if(!rowItem.isGroupHeader()){
                item.put(i, rowItem);
                break;
            }
        }
        return item;
    }

    private Map<Integer, RowItem> previousItem(int actualPosition, final ListView mDrawerList){
        Map<Integer, RowItem> item = null;
        int previousPosition = actualPosition - 1;
        RowItem rowItem = (RowItem) mDrawerList.getItemAtPosition(previousPosition);
        if(rowItem != null){
            if(rowItem.isGroupHeader()){
                return previousItem(previousPosition, mDrawerList);
            }else{
                item = new HashMap<Integer, RowItem>();
                item.put(previousPosition, rowItem);
                return item;
            }
        } else {
            return null;
        }
    }

    private Map<Integer, RowItem> nextItem(int actualPosition, final ListView mDrawerList){
        Map<Integer, RowItem> item = null;
        int nextPosition = actualPosition + 1;
        RowItem rowItem = (RowItem) mDrawerList.getItemAtPosition(nextPosition);
        if(rowItem != null){
            if(rowItem.isGroupHeader()){
                return nextItem(nextPosition, mDrawerList);
            }else{
                item = new HashMap<Integer, RowItem>();
                item.put(nextPosition, rowItem);
                return item;
            }
        } else {
            return null;
        }
    }

    class PhotoWorkerTask extends AsyncTask<Integer, Void, Bitmap> {
        private View rootView;
        private Product item;
        private byte[] photo;
        private  ProgressDialog progressProductInfoDialog;
        private Activity activity;
        private FlygowServerUrl serverAddressObj;

        public PhotoWorkerTask(
                Activity activity,
                View rootView,
                Product item,
                ProgressDialog progressProductInfoDialog
        ) {
            this.activity = activity;
            this.rootView = rootView;
            this.item = item;
            this.progressProductInfoDialog = progressProductInfoDialog;
            this.serverAddressObj = (FlygowServerUrl) activity.getApplication();
            if(this.item != null){
                this.photo = item.getPhoto();
            }
        }

        // Decode image in background.
        @Override
        protected Bitmap doInBackground(Integer... params) {
            Bitmap bitmap = null;
            if(photo != null && photo.length > 0){
                ByteArrayInputStream imageStream = new ByteArrayInputStream(photo);
                bitmap = BitmapFactory.decodeStream(imageStream);
            }
            return bitmap;
        }

        // Once complete, see if ImageView is still around and set bitmap.
        @Override
        protected void onPostExecute(Bitmap bitmap) {
            if(bitmap != null){
                Drawable drawable = new BitmapDrawable(getResources(),bitmap);
                rootView.setBackground(drawable);
            }else{
                rootView.setBackgroundResource(R.drawable.wihout_product);
            }
            progressProductInfoDialog.dismiss();
        }
    }
}
