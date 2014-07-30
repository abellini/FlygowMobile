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
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.VideoView;
import android.widget.ViewFlipper;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import br.com.flygowmobile.Utils.FlygowAlertDialog;
import br.com.flygowmobile.Utils.FlygowServerUrl;
import br.com.flygowmobile.Utils.MediaUtils;
import br.com.flygowmobile.Utils.StringUtils;
import br.com.flygowmobile.activity.R;
import br.com.flygowmobile.database.RepositoryFood;
import br.com.flygowmobile.entity.Food;
import br.com.flygowmobile.enums.PositionsEnum;
import br.com.flygowmobile.enums.ServerController;
import br.com.flygowmobile.enums.StaticMessages;
import br.com.flygowmobile.enums.StaticTitles;
import br.com.flygowmobile.service.ClickProductContentService;

/**
 * Created by Tiago Rocha Gomes on 21/07/14.
 */
public class FoodFragment extends Fragment {

    private RepositoryFood repositoryFood;
    private RowItem item;
    private int itemPosition;
    private boolean fromArrow;
    private Food foodItem;
    private ListView mDrawerList;
    private Activity activity;
    private ProgressDialog progressProductInfoDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.food_fragment, container, false);
        activity = getActivity();

        repositoryFood = new RepositoryFood(activity);
        item = (RowItem) getArguments().get("item");
        itemPosition = getArguments().getInt("itemPosition");
        fromArrow = getArguments().getBoolean("fromArrow");
        mDrawerList = (ListView) activity.findViewById(R.id.slider_list);


        defineFonts(rootView);
        setFoodMedia(rootView);
        setProductPrice(rootView);
        setProductTitle(rootView);
        setProductDescription(rootView);
        setProductNutritionalInfo(rootView);
        defineDirectionalArrows(rootView);

        if(fromArrow){
            alignProductDetailsToCenter(rootView);
        }
        return rootView;
    }

    private void defineFonts(View rootView){
        // Font path
        String fontGabriolaPath = "fonts/GABRIOLA.TTF";
        String fontErasBoldPath = "fonts/ERASBD.TTF";
        String fontErasMediumPath = "fonts/ERASMD.TTF";

        // text view label
        TextView priceView = (TextView)rootView.findViewById(R.id.productPrice);
        TextView clickHereView = (TextView)rootView.findViewById(R.id.productClickHere);
        TextView pecaView = (TextView)rootView.findViewById(R.id.productPeca);

        TextView titleView = (TextView)rootView.findViewById(R.id.productTitle);
        TextView descriptionView = (TextView)rootView.findViewById(R.id.productDescription);

        // Loading Font Face
        Typeface gabriola = Typeface.createFromAsset(activity.getAssets(), fontGabriolaPath );
        Typeface erasBold = Typeface.createFromAsset(activity.getAssets(), fontErasBoldPath);
        Typeface erasMedium = Typeface.createFromAsset(activity.getAssets(), fontErasMediumPath);

        // Applying font
        priceView.setTypeface(gabriola);
        clickHereView.setTypeface(gabriola);
        pecaView.setTypeface(gabriola);

        titleView.setTypeface(erasBold);
        descriptionView.setTypeface(erasMedium);
    }

    private void setFoodMedia(View rootView){
        foodItem = repositoryFood.findById(item.getId());
        progressProductInfoDialog = ProgressDialog.show(activity, StaticTitles.LOAD.getName(),
                StaticMessages.LOAD_PRODUCT.getName(), true);
        if(StringUtils.isNotEmpty(foodItem.getVideoName())){
            try {
                String foodVideoPath = MediaUtils.getVideo(activity, foodItem.getVideoName());
                VideoView videoView = new VideoView(getActivity());
                RelativeLayout.LayoutParams videoLayout = new RelativeLayout.LayoutParams(
                        480, 360);
                videoView.setLayoutParams(videoLayout);
                videoView.setX(280);
                videoView.setY(5);
                videoView.setVideoPath(foodVideoPath);
                RelativeLayout mainLayout = (RelativeLayout) rootView.findViewById(R.id.foodImageBackground);
                mainLayout.addView(videoView);
                videoView.start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        PhotoWorkerTask task = new PhotoWorkerTask(rootView, foodItem);
        task.execute();
    }


    private void setProductPrice(View rootView){
        TextView priceView = (TextView)rootView.findViewById(R.id.productPrice);
        String price = item.getPrice();
        priceView.setText(price);
    }

    private void setProductTitle(View rootView){
        TextView titleView = (TextView)rootView.findViewById(R.id.productTitle);
        String title = item.getTitle();
        titleView.setText(title);
    }

    private void setProductDescription(View rootView){
        TextView descriptionView = (TextView)rootView.findViewById(R.id.productDescription);
        if(foodItem != null && foodItem.getDescription() != null && !foodItem.getDescription().isEmpty()){
            descriptionView.setText(foodItem.getDescription());
        }
    }

    private void setProductNutritionalInfo(View rootView){
        Button btnInfo = (Button)rootView.findViewById(R.id.btnNutritionalInfo);
        if(foodItem != null && foodItem.getNutritionalInfo() != null && !foodItem.getNutritionalInfo().isEmpty()){
            btnInfo.setVisibility(View.VISIBLE);
            btnInfo.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    FlygowAlertDialog.createInfoPopup(activity, StaticTitles.INFORMATION, foodItem.getNutritionalInfo());
                }
            });
        }else{
            btnInfo.setVisibility(View.INVISIBLE);
        }
    }

    private void defineDirectionalArrows(View rootView){
        Button btnLeft = (Button)rootView.findViewById(R.id.btnArrowLeft);
        Button btnRight = (Button)rootView.findViewById(R.id.btnArrowRight);
        btnLeft.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                boolean fromArrow = true;
                try{
                    Map<Integer, RowItem> item = previousItem(itemPosition);
                    if(item != null){
                        new ClickProductContentService(
                                activity,
                                item.keySet().iterator().next()
                        ).updateDisplay(item.values().iterator().next(), fromArrow);
                    }else{
                        Log.i("MainActivity", "Begin of list :: " + mDrawerList.getChildCount() + " :: " + mDrawerList.getCount());
                    }
                }catch(Exception e){
                    Log.i("MainActivity", "Begin of list :: " + mDrawerList.getChildCount() + " :: " + mDrawerList.getCount());
                }
            }
        });

        btnRight.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                boolean fromArrow = true;
                try{
                    Map<Integer, RowItem> item = nextItem(itemPosition);
                    if(item != null){
                        new ClickProductContentService(
                                activity,
                                item.keySet().iterator().next()
                        ).updateDisplay(item.values().iterator().next(), fromArrow);
                    }else{
                        Log.i("MainActivity", "End of list");
                    }
                }catch (Exception e){
                    Log.i("MainActivity", "End of list :: " + mDrawerList.getChildCount() + " :: " + mDrawerList.getCount());
                }
            }
        });
    }

    private Map<Integer, RowItem> previousItem(int actualPosition){
        Map<Integer, RowItem> item = null;
        int previousPosition = actualPosition - 1;
        RowItem rowItem = (RowItem) mDrawerList.getItemAtPosition(previousPosition);
        if(rowItem != null){
            if(rowItem.isGroupHeader()){
                return previousItem(previousPosition);
            }else{
                item = new HashMap<Integer, RowItem>();
                item.put(previousPosition, rowItem);
                return item;
            }
        } else {
            return null;
        }
    }

    private Map<Integer, RowItem> nextItem(int actualPosition){
        Map<Integer, RowItem> item = null;
        int nextPosition = actualPosition + 1;
        RowItem rowItem = (RowItem) mDrawerList.getItemAtPosition(nextPosition);
        if(rowItem != null){
            if(rowItem.isGroupHeader()){
                return nextItem(nextPosition);
            }else{
                item = new HashMap<Integer, RowItem>();
                item.put(nextPosition, rowItem);
                return item;
            }
        } else {
            return null;
        }
    }

    private void alignProductDetailsToCenter(View rootView){
        final int MARGIN = PositionsEnum.PRODUCT_DETAILS.getMargin();
        try{
            TextView priceView = (TextView)rootView.findViewById(R.id.productPrice);
            TextView clickHereView = (TextView)rootView.findViewById(R.id.productClickHere);
            TextView pecaView = (TextView)rootView.findViewById(R.id.productPeca);
            TextView titleView = (TextView)rootView.findViewById(R.id.productTitle);
            TextView descriptionView = (TextView)rootView.findViewById(R.id.productDescription);
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

    class PhotoWorkerTask extends AsyncTask<Integer, Void, Bitmap> {
        private View rootView;
        private Food foodItem;
        private byte[] photo;
        private byte[] video;

        FlygowServerUrl serverAddressObj = (FlygowServerUrl) activity.getApplication();

        public PhotoWorkerTask(View rootView, Food foodItem) {
            this.rootView = rootView;
            this.foodItem = foodItem;
            if(this.foodItem != null){
                this.photo = foodItem.getPhoto();
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