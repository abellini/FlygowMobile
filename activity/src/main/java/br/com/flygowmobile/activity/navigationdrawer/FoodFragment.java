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
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.VideoView;
import android.widget.ViewFlipper;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import br.com.flygowmobile.Utils.FlygowServerUrl;
import br.com.flygowmobile.Utils.MediaUtils;
import br.com.flygowmobile.Utils.StringUtils;
import br.com.flygowmobile.activity.R;
import br.com.flygowmobile.database.RepositoryFood;
import br.com.flygowmobile.entity.Food;
import br.com.flygowmobile.enums.ServerController;
import br.com.flygowmobile.enums.StaticMessages;
import br.com.flygowmobile.enums.StaticTitles;

/**
 * Created by Tiago Rocha Gomes on 21/07/14.
 */
public class FoodFragment extends Fragment {

    private RepositoryFood repositoryFood;
    private RowItem item;
    private Food foodItem;
    private Activity activity;
    private ProgressDialog progressProductInfoDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.food_fragment, container, false);
        activity = getActivity();
        repositoryFood = new RepositoryFood(activity);
        item = (RowItem) getArguments().get("item");

        defineFonts(rootView);

        //define image food in backgroud layout
        setFoodMedia(rootView);
        setProductPrice(rootView);
        setProductTitle(rootView);
        setProductDescription(rootView);

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
                //VERIFICAR FORMA DE CRIAR UM VIDEOVIEW... DENTRO DO ASYNC TASK NÃO FUNFA!
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
        Food food = repositoryFood.findById(item.getId());
        if(food != null && food.getDescription() != null && !food.getDescription().isEmpty()){
            descriptionView.setText(food.getDescription());
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