package br.com.flygowmobile.activity.navigationdrawer;

import android.app.Activity;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.io.ByteArrayInputStream;

import br.com.flygowmobile.activity.R;
import br.com.flygowmobile.database.RepositoryFood;
import br.com.flygowmobile.entity.Food;
import br.com.flygowmobile.enums.StaticMessages;
import br.com.flygowmobile.enums.StaticTitles;

/**
 * Created by Tiago Rocha Gomes on 21/07/14.
 */
public class FoodFragment extends Fragment {

    private RepositoryFood repositoryFood;
    private RowItem item;
    private Activity activity;
    private ProgressDialog progressProductInfoDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.food_fragment, container, false);
        activity = getActivity();
        repositoryFood = new RepositoryFood(activity);
        item = (RowItem) getArguments().get("item");

        //define image food in backgroud layout
        setBackgroundFoodImage(rootView);

        return rootView;
    }

    private void setBackgroundFoodImage(View rootView){
        Food foodItem = repositoryFood.findById(item.getId());
        progressProductInfoDialog = ProgressDialog.show(activity, StaticTitles.LOAD.getName(),
                StaticMessages.LOAD_PRODUCT.getName(), true);
        BitmapWorkerTask task = new BitmapWorkerTask(rootView, foodItem.getPhoto());
        task.execute();
    }

    class BitmapWorkerTask extends AsyncTask<Integer, Void, Bitmap> {
        private View rootView;
        private byte[] photo;

        public BitmapWorkerTask(View rootView, byte[] photo) {
            this.rootView = rootView;
            this.photo = photo;
        }

        // Decode image in background.
        @Override
        protected Bitmap doInBackground(Integer... params) {
            Bitmap bitmap = null;
            if(photo != null && photo.length > 0){
                ByteArrayInputStream imageStream = new ByteArrayInputStream(photo);
                bitmap = BitmapFactory.decodeStream(imageStream);
                return bitmap;
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

