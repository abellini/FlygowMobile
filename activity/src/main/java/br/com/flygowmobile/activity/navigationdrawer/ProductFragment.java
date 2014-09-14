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
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.VideoView;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.com.flygowmobile.Utils.App;
import br.com.flygowmobile.Utils.MediaUtils;
import br.com.flygowmobile.Utils.StringUtils;
import br.com.flygowmobile.activity.R;
import br.com.flygowmobile.entity.Accompaniment;
import br.com.flygowmobile.entity.Product;
import br.com.flygowmobile.enums.PositionsEnum;
import br.com.flygowmobile.enums.StaticMessages;
import br.com.flygowmobile.enums.StaticTitles;
import br.com.flygowmobile.service.ClickProductContentService;
import br.com.flygowmobile.service.OrderService;

/**
 * Created by Tiago Rocha Gomes on 02/08/14.
 */
public class ProductFragment extends Fragment {

    private static final int SWIPE_MIN_DISTANCE = 120;
    private static final int SWIPE_MAX_OFF_PATH = 250;
    private static final int SWIPE_THRESHOLD_VELOCITY = 200;
    private GestureDetector gestureDetector;
    private Activity activity;
    private OrderService orderService;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        activity = getActivity();
        orderService = new OrderService(activity);
        return null;
    }

    protected void defineTexts(View rootView){
        TextView pecaView = (TextView) rootView.findViewById(R.id.peca);
        TextView clickHereView = (TextView) rootView.findViewById(R.id.clickHere);

        pecaView.setText(StaticMessages.PRICE_DESCRIPTION.getName());
        clickHereView.setText(StaticMessages.PRICE_SUBDESCRIPTION.getName());
    }

    protected void defineFonts(View rootView) {
        // Font path
        String fontGabriolaPath = "fonts/GABRIOLA.TTF";
        String fontErasBoldPath = "fonts/ERASBD.TTF";
        String fontErasMediumPath = "fonts/ERASMD.TTF";

        // Loading Font Face
        Typeface gabriola = Typeface.createFromAsset(activity.getAssets(), fontGabriolaPath);
        Typeface erasBold = Typeface.createFromAsset(activity.getAssets(), fontErasBoldPath);
        Typeface erasMedium = Typeface.createFromAsset(activity.getAssets(), fontErasMediumPath);


        // text view label
        TextView priceView = (TextView) rootView.findViewById(R.id.price);
        TextView clickHereView = (TextView) rootView.findViewById(R.id.clickHere);
        TextView pecaView = (TextView) rootView.findViewById(R.id.peca);
        TextView titleView = (TextView) rootView.findViewById(R.id.title);
        TextView descriptionView = (TextView) rootView.findViewById(R.id.description);

        // Applying font
        priceView.setTypeface(gabriola);
        clickHereView.setTypeface(gabriola);
        pecaView.setTypeface(gabriola);

        titleView.setTypeface(erasBold);
        descriptionView.setTypeface(erasMedium);
    }

    protected void defineDirectionalArrows(
            View rootView,
            final int itemPosition,
            final ListView mDrawerList,
            final boolean fromArrowOrSwipe
    ) {
        Button btnLeft = (Button) rootView.findViewById(R.id.btnArrowLeft);
        Button btnRight = (Button) rootView.findViewById(R.id.btnArrowRight);
        if (fromArrowOrSwipe) {
            btnLeft.setVisibility(View.VISIBLE);
            btnRight.setVisibility(View.VISIBLE);
        }
        btnLeft.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                boolean fromArrow = true;
                toNextItem(itemPosition, mDrawerList, fromArrow);
            }
        });

        btnRight.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                boolean fromArrow = true;
                toPreviousItem(itemPosition, mDrawerList, fromArrow);
            }
        });
    }


    protected void setFoodMedia(View rootView, Product item) {
        ProgressDialog progressPromotionInfoDialog = ProgressDialog.show(activity, StaticTitles.LOAD.getName(),
                StaticMessages.LOAD_PROMOTION.getName(), true);
        String videoName = item.getVideoName();
        if (StringUtils.isNotEmpty(videoName)) {
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


    protected void setProductPrice(View rootView, RowItem item) {
        TextView priceView = (TextView) rootView.findViewById(R.id.price);
        String price = item.getPrice();
        priceView.setText(price);
    }

    protected void setProductTitle(View rootView, RowItem item) {
        TextView titleView = (TextView) rootView.findViewById(R.id.title);
        String title = item.getTitle();
        titleView.setText(title);
    }

    protected void setProductDescription(View rootView, Product item) {
        TextView descriptionView = (TextView) rootView.findViewById(R.id.description);
        if (item != null && item.getDescription() != null && !item.getDescription().isEmpty()) {
            descriptionView.setText(item.getDescription());
        }
    }

    protected void alignProductDetailsToCenter(View rootView) {
        final int MARGIN = PositionsEnum.PRODUCT_DETAILS.getMargin();
        try {
            TextView priceView = (TextView) rootView.findViewById(R.id.price);
            TextView clickHereView = (TextView) rootView.findViewById(R.id.clickHere);
            TextView pecaView = (TextView) rootView.findViewById(R.id.peca);
            TextView titleView = (TextView) rootView.findViewById(R.id.title);
            TextView descriptionView = (TextView) rootView.findViewById(R.id.description);
            Button btnOrder = (Button) rootView.findViewById(R.id.btnOrder);
            Button btnIdentifier = (Button) rootView.findViewById(R.id.btnNutritionalInfo);

            priceView.setX(priceView.getLeft() - MARGIN);
            clickHereView.setX(clickHereView.getLeft() - MARGIN);
            pecaView.setX(pecaView.getLeft() - MARGIN);
            titleView.setX(titleView.getLeft() - MARGIN);
            descriptionView.setX(descriptionView.getLeft() - MARGIN);
            btnOrder.setX(btnOrder.getLeft() - MARGIN);
            btnIdentifier.setX(btnIdentifier.getLeft() - MARGIN);

        } catch (Exception e) {
            Log.w("Main Activity", "Dont Align Food Details to CENTER FROM ARROW");
        }
    }

    protected void defineSwipe(View rootView, int itemPosition, ListView mDrawerList) {
        gestureDetector = new GestureDetector(activity, new ProductGestureDetector(activity, itemPosition, mDrawerList));
        rootView.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                return gestureDetector.onTouchEvent(event);
            }
        });
    }

    protected void defineOrderButton(
            View rootView,
            final List<Accompaniment> accompanimentList,
            final Product productItem,
            final Map<Long, CheckBox> selects) {

        Button btnOrder = (Button) rootView.findViewById(R.id.btnOrder);
        TextView price = (TextView) rootView.findViewById(R.id.price);
        price.setOnClickListener(getOrderClick(accompanimentList, productItem, selects));
        btnOrder.setOnClickListener(getOrderClick(accompanimentList, productItem, selects));
    }

    protected View.OnClickListener getOrderClick(
            final List<Accompaniment> accompanimentList,
            final Product productItem,
            final Map<Long, CheckBox> selects
    ) {

        return new View.OnClickListener() {
            public void onClick(View v) {
                orderService.orderAction(activity, productItem, accompanimentList, selects);
            }
        };
    }

    private void toNextItem(
            final int itemPosition,
            final ListView mDrawerList,
            boolean fromArrowOrSwipe) {
        try {
            Map<Integer, RowItem> item = previousItem(itemPosition, mDrawerList);
            if (item != null) {
                new ClickProductContentService(
                        activity,
                        item.keySet().iterator().next()
                ).updateDisplay(item.values().iterator().next(), fromArrowOrSwipe);
            } else {
                RowItem lastItem = (RowItem) mDrawerList.getItemAtPosition(mDrawerList.getCount() - 1);
                if (lastItem != null) {
                    new ClickProductContentService(
                            activity,
                            mDrawerList.getCount() - 1
                    ).updateDisplay(lastItem, fromArrowOrSwipe);
                }
            }
        } catch (Exception e) {
            RowItem lastItem = (RowItem) mDrawerList.getItemAtPosition(mDrawerList.getCount() - 1);
            if (lastItem != null) {
                new ClickProductContentService(
                        activity,
                        mDrawerList.getCount() - 1
                ).updateDisplay(lastItem, fromArrowOrSwipe);
            }
        }
    }

    private void toPreviousItem(
            final int itemPosition,
            final ListView mDrawerList,
            boolean fromArrowOrSwipe) {
        try {
            Map<Integer, RowItem> item = nextItem(itemPosition, mDrawerList);
            if (item != null) {
                new ClickProductContentService(
                        activity,
                        item.keySet().iterator().next()
                ).updateDisplay(item.values().iterator().next(), fromArrowOrSwipe);
            } else {
                RowItem firstItem = getFirstProductItem(mDrawerList).values().iterator().next();
                if (firstItem != null) {
                    new ClickProductContentService(
                            activity,
                            getFirstProductItem(mDrawerList).keySet().iterator().next()
                    ).updateDisplay(firstItem, fromArrowOrSwipe);
                }
            }
        } catch (Exception e) {
            RowItem firstItem = getFirstProductItem(mDrawerList).values().iterator().next();
            if (firstItem != null) {
                new ClickProductContentService(
                        activity,
                        getFirstProductItem(mDrawerList).keySet().iterator().next()
                ).updateDisplay(firstItem, fromArrowOrSwipe);
            }
        }
    }

    private Map<Integer, RowItem> getFirstProductItem(final ListView mDrawerList) {
        Map<Integer, RowItem> item = new HashMap<Integer, RowItem>();
        for (int i = 0; i < mDrawerList.getCount(); i++) {
            RowItem rowItem = (RowItem) mDrawerList.getItemAtPosition(i);
            if (!rowItem.isGroupHeader()) {
                item.put(i, rowItem);
                break;
            }
        }
        return item;
    }

    private Map<Integer, RowItem> previousItem(int actualPosition, final ListView mDrawerList) {
        Map<Integer, RowItem> item = null;
        int previousPosition = actualPosition - 1;
        RowItem rowItem = (RowItem) mDrawerList.getItemAtPosition(previousPosition);
        if (rowItem != null) {
            if (rowItem.isGroupHeader()) {
                return previousItem(previousPosition, mDrawerList);
            } else {
                item = new HashMap<Integer, RowItem>();
                item.put(previousPosition, rowItem);
                return item;
            }
        } else {
            return null;
        }
    }

    private Map<Integer, RowItem> nextItem(int actualPosition, final ListView mDrawerList) {
        Map<Integer, RowItem> item = null;
        int nextPosition = actualPosition + 1;
        RowItem rowItem = (RowItem) mDrawerList.getItemAtPosition(nextPosition);
        if (rowItem != null) {
            if (rowItem.isGroupHeader()) {
                return nextItem(nextPosition, mDrawerList);
            } else {
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
        private ProgressDialog progressProductInfoDialog;
        private Activity activity;
        private App serverAddressObj;

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
            this.serverAddressObj = (App) activity.getApplication();
            if (this.item != null) {
                this.photo = item.getPhoto();
            }
        }

        // Decode image in background.
        @Override
        protected Bitmap doInBackground(Integer... params) {
            Bitmap bitmap = null;
            if (photo != null && photo.length > 0) {
                ByteArrayInputStream imageStream = new ByteArrayInputStream(photo);
                bitmap = BitmapFactory.decodeStream(imageStream);
            }
            return bitmap;
        }

        // Once complete, see if ImageView is still around and set bitmap.
        @Override
        protected void onPostExecute(Bitmap bitmap) {
            if (bitmap != null) {
                Drawable drawable = new BitmapDrawable(getResources(), bitmap);
                rootView.setBackground(drawable);
            } else {
                rootView.setBackgroundResource(R.drawable.wihout_product);
            }
            progressProductInfoDialog.dismiss();
        }
    }

    class ProductGestureDetector extends GestureDetector.SimpleOnGestureListener {
        private Activity activity;
        private int itemPosition;
        private ListView mDrawerList;

        public ProductGestureDetector(Activity activity, int itemPosition, final ListView mDrawerList) {
            this.activity = activity;
            this.itemPosition = itemPosition;
            this.mDrawerList = mDrawerList;
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            try {
                if (Math.abs(e1.getY() - e2.getY()) > SWIPE_MAX_OFF_PATH)
                    return false;
                // right to left swipe
                if (e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                    boolean fromArrow = true;
                    toPreviousItem(itemPosition, mDrawerList, fromArrow);
                } else if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                    boolean fromArrow = true;
                    toNextItem(itemPosition, mDrawerList, fromArrow);
                }
            } catch (Exception e) {
                // nothing
            }
            return false;
        }

        @Override
        public boolean onDown(MotionEvent e) {
            return true;
        }
    }
}
