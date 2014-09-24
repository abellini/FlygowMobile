package br.com.flygowmobile.activity;


import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.ViewFlipper;

import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;

import br.com.flygowmobile.Utils.App;
import br.com.flygowmobile.Utils.FlygowAlertDialog;
import br.com.flygowmobile.Utils.MediaUtils;
import br.com.flygowmobile.Utils.StringUtils;
import br.com.flygowmobile.activity.navigationdrawer.AdvertisementFragment;
import br.com.flygowmobile.activity.navigationdrawer.CustomAdapter;
import br.com.flygowmobile.activity.navigationdrawer.RowItem;
import br.com.flygowmobile.activity.navigationdrawer.WelcomeFragment;
import br.com.flygowmobile.database.RepositoryAdvertisement;
import br.com.flygowmobile.database.RepositoryCoin;
import br.com.flygowmobile.database.RepositoryFood;
import br.com.flygowmobile.database.RepositoryPromotion;
import br.com.flygowmobile.database.RepositoryTablet;
import br.com.flygowmobile.entity.Advertisement;
import br.com.flygowmobile.entity.Food;
import br.com.flygowmobile.entity.Promotion;
import br.com.flygowmobile.enums.PositionsEnum;
import br.com.flygowmobile.enums.ServerController;
import br.com.flygowmobile.enums.StaticMessages;
import br.com.flygowmobile.enums.StaticTitles;
import br.com.flygowmobile.service.BuildMainActionBarService;
import br.com.flygowmobile.service.BuildMenuItemsService;
import br.com.flygowmobile.service.ClickProductContentService;
import br.com.flygowmobile.service.ExitApplicationService;

public class MainActivity extends Activity {

    private static final String MAIN_ACTIVITY = "MainActivity";
    TypedArray menuIcons;
    // nav drawer title
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;
    private BuildMenuItemsService menuItemsService;
    private ClickProductContentService clickProductContentService;
    private ExitApplicationService exitApplicationService;

    public BuildMainActionBarService mainActionBarService;

    private List<RowItem> rowItems;
    private CustomAdapter adapter;
    private RepositoryAdvertisement repositoryAdvertisement;
    private RepositoryFood repositoryFood;
    private RepositoryPromotion repositoryPromotion;
    private RepositoryTablet repositoryTablet;
    private RepositoryCoin repositoryCoin;
    private AdvertisementMediaTask advertisementMediaTask;
    private FoodMediaTask foodMediaTask;
    private PromotionMediaTask promotionMediaTask;
    private ProgressDialog progressAdvertisementDialog;
    private ProgressDialog progressFoodDialog;
    private ProgressDialog progressPromotionDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        menuIcons = getResources().obtainTypedArray(R.array.icons);
        menuItemsService = new BuildMenuItemsService(this, menuIcons);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.slider_list);
        mDrawerLayout.setScrimColor(Color.TRANSPARENT);

        rowItems = menuItemsService.getMenuItems();

        adapter = new CustomAdapter(getApplicationContext(), rowItems);

        mDrawerList.setAdapter(adapter);
        mDrawerList.setOnItemClickListener(new SlideitemListener());

        repositoryAdvertisement = new RepositoryAdvertisement(this);
        repositoryTablet = new RepositoryTablet(this);
        repositoryCoin = new RepositoryCoin(this);
        repositoryFood = new RepositoryFood(this);
        repositoryPromotion = new RepositoryPromotion(this);

        //build the main action bar
        LayoutInflater mInflater = LayoutInflater.from(this);
        View actionBarView = mInflater.inflate(R.layout.custom_action_bar, null);
        mainActionBarService = new BuildMainActionBarService(this, actionBarView);
        mainActionBarService.buildActionBar();

        mDrawerToggle = buildDrawerToggle();
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        mDrawerLayout.openDrawer(Gravity.LEFT);

        clickProductContentService = new ClickProductContentService(MainActivity.this, mDrawerLayout, mDrawerList);
        exitApplicationService = new ExitApplicationService(this);

        //Set the inicial fragment... promotions...
        //After this, load the foods and after advertisements. After all, change to the next activity
        progressPromotionDialog = ProgressDialog.show(MainActivity.this, StaticTitles.LOAD.getName(),
                StaticMessages.LOADING_MEDIA_PROMOTIONS.getName(), true);

        List<Promotion> allPromotions = repositoryPromotion.listAll();
        promotionMediaTask = new PromotionMediaTask(allPromotions);
        promotionMediaTask.execute((Void) null);
    }

    @Override
    protected void onRestart(){
        super.onRestart();
        mainActionBarService.refreshActionBarPrice();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }



    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        super.onKeyDown(keyCode, event);
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            exitApplicationService.onExitApplication();
        }
        return true;
    }

    private ActionBarDrawerToggle buildDrawerToggle(){
        return new ActionBarDrawerToggle(this, mDrawerLayout,
                R.drawable.ic_drawer, R.string.app_name,R.string.app_name) {
            public void onDrawerClosed(View view) {
                alignFragmentToCenter();
                showDirectionalArrows();
                // calling onPrepareOptionsMenu() to show action bar icons
                invalidateOptionsMenu();
            }

            public void onDrawerOpened(View drawerView) {
                alignFragmentToRight();
                hideDirectionalArrows();
                // calling onPrepareOptionsMenu() to hide action bar icons
                invalidateOptionsMenu();
            }
        };
    }

    public void showDirectionalArrows(){
        try{
            Button btnArrLeft = (Button)findViewById(R.id.btnArrowLeft);
            Button btnArrRight = (Button)findViewById(R.id.btnArrowRight);
            btnArrLeft.setVisibility(View.VISIBLE);
            btnArrRight.setVisibility(View.VISIBLE);
        }catch(Exception e){
            Log.w(MAIN_ACTIVITY, "Dont SHOW directional arrows");
        }
    }

    public void hideDirectionalArrows(){
        try{
            Button btnArrLeft = (Button)findViewById(R.id.btnArrowLeft);
            Button btnArrRight = (Button)findViewById(R.id.btnArrowRight);
            btnArrLeft.setVisibility(View.INVISIBLE);
            btnArrRight.setVisibility(View.INVISIBLE);
        }catch(Exception e){
            Log.w(MAIN_ACTIVITY, "Dont HIDE directional arrows");
        }
    }

    private void alignFragmentToCenter(){
        alignAdvertisementFragmentToCenter();
        alignProductDetailsToCenter();
    }

    private void alignFragmentToRight(){
        alignAdvertisementFragmentToRight();
        alignProductDetailsToRight();
    }


    private void alignAdvertisementFragmentToRight(){
        try{
            ViewFlipper view = (ViewFlipper)findViewById(R.id.switcher);
            ViewFlipper.LayoutParams layout = new ViewFlipper.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
            layout.gravity = Gravity.CENTER;
            layout.setMargins(0, 0, 0, 0);
            view.setLayoutParams(layout);
        }catch (Exception e){
            Log.w(MAIN_ACTIVITY, "Dont Align Advertisement to RIGHT");
        }
    }

    private void alignAdvertisementFragmentToCenter(){
        try{
            ViewFlipper view = (ViewFlipper)findViewById(R.id.switcher);
            ViewFlipper.LayoutParams layout = new ViewFlipper.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
            layout.gravity = Gravity.CENTER;
            layout.setMargins(-137, 0, 0, 0);
            view.setLayoutParams(layout);
        }catch(Exception e){
            Log.w(MAIN_ACTIVITY, "Dont Align Advertisement to CENTER");
        }
    }


    private void alignProductDetailsToCenter(){
        final int MARGIN = PositionsEnum.PRODUCT_DETAILS.getMargin();
        try{
            TextView priceView = (TextView)findViewById(R.id.price);
            TextView clickHereView = (TextView) findViewById(R.id.clickHere);
            TextView pecaView = (TextView)findViewById(R.id.peca);
            TextView titleView = (TextView)findViewById(R.id.title);
            TextView descriptionView = (TextView)findViewById(R.id.description);
            Button btnOrder = (Button) findViewById(R.id.btnOrder);
            Button btnIdentifier = (Button) findViewById(R.id.btnNutritionalInfo);

            priceView.setX(priceView.getLeft()-MARGIN);
            clickHereView.setX(clickHereView.getLeft()-MARGIN);
            pecaView.setX(pecaView.getLeft()-MARGIN);
            titleView.setX(titleView.getLeft()-MARGIN);
            descriptionView.setX(descriptionView.getLeft()-MARGIN);
            btnOrder.setX(btnOrder.getLeft()-MARGIN);
            btnIdentifier.setX(btnIdentifier.getLeft()-MARGIN);

        }catch(Exception e){
            Log.w(MAIN_ACTIVITY, "Dont Align Food Details to CENTER");
        }
    }

    private void alignProductDetailsToRight(){
        try{
            TextView priceView = (TextView)findViewById(R.id.price);
            TextView clickHereView = (TextView) findViewById(R.id.clickHere);
            TextView pecaView = (TextView)findViewById(R.id.peca);
            TextView titleView = (TextView)findViewById(R.id.title);
            TextView descriptionView = (TextView)findViewById(R.id.description);
            Button btnOrder = (Button) findViewById(R.id.btnOrder);
            Button btnIdentifier = (Button) findViewById(R.id.btnNutritionalInfo);

            priceView.setX(priceView.getLeft());
            clickHereView.setX(clickHereView.getLeft());
            pecaView.setX(pecaView.getLeft());
            titleView.setX(titleView.getLeft());
            descriptionView.setX(descriptionView.getLeft());
            btnOrder.setX(btnOrder.getLeft());
            btnIdentifier.setX(btnIdentifier.getLeft());

        }catch(Exception e){
            Log.w(MAIN_ACTIVITY, "Dont Align Food Details to RIGHT");
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // toggle nav drawer on selecting action bar app icon/title
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return false;
    }

    /**
     * Called when invalidateOptionsMenu() is triggered
     */
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // if nav drawer is opened, hide the action items
        boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
        return super.onPrepareOptionsMenu(menu);
    }

    /**
     * When using the ActionBarDrawerToggle, you must call it during
     * onPostCreate() and onConfigurationChanged()...
     */

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggles
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    class SlideitemListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            RowItem item = (RowItem) parent.getItemAtPosition(position);
            clickProductContentService.onClickProductItem(item, position);
        }
    }

    public class AdvertisementMediaTask extends AsyncTask<Void, Void, String> {

        private final List<Advertisement> advertisements;
        App serverAddressObj = (App) getApplication();

        public AdvertisementMediaTask(List<Advertisement> advertisements) {
            this.advertisements = advertisements;
        }

        @Override
        protected String doInBackground(Void... params) {
            String serverUrl = serverAddressObj.getServerUrl(ServerController.INITIALIZE_VIDEO_PRODUCTS);
            JSONObject jsonSuccess = new JSONObject();

            try {
                for(Advertisement adv : advertisements){
                    try {
                        if(StringUtils.isNotEmpty(adv.getVideoName())){
                            MediaUtils.downloadVideoByEntityId(
                                    MainActivity.this,
                                    serverUrl,
                                    Advertisement.class.getSimpleName(),
                                    adv.getAdvertisementId(),
                                    adv.getVideoName()
                            );
                        } else {
                            if (StringUtils.isNotEmpty(adv.getPhotoName())) {
                                serverUrl = serverAddressObj.getServerUrl(ServerController.INITIALIZE_PHOTO_PRODUCTS);
                                byte[] photo = MediaUtils.downloadPhotoByEntityId(
                                        MainActivity.this,
                                        serverUrl,
                                        Advertisement.class.getSimpleName(),
                                        Long.parseLong(adv.getAdvertisementId()+"")
                                );
                                adv.setPhoto(photo);
                                if(photo != null && photo.length > 0){
                                    repositoryAdvertisement.save(adv);
                                }
                            }
                        }
                        jsonSuccess.put("success", true);
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                        jsonSuccess.put("success", false);
                    } catch (IOException e) {
                        e.printStackTrace();
                        jsonSuccess.put("success", false);
                    }
                }
                return jsonSuccess.toString();
            } catch (Exception e) {
                Log.i(MAIN_ACTIVITY, StaticMessages.NOT_SERVICE.getName());
                progressAdvertisementDialog.dismiss();
                return StaticMessages.NOT_SERVICE.getName();
            }
        }

        @Override
        protected void onPostExecute(String response) {
            try {
                JSONObject jsonObject = new JSONObject(response);
                if(jsonObject.length() == 0 || !jsonObject.getBoolean("success")){
                    FlygowAlertDialog.createWarningPopup(
                            MainActivity.this, StaticTitles.WARNING, StaticMessages.WARNING_LOAD_ADVERTISEMENTS);
                }

                Fragment fragment;
                if(advertisements != null && advertisements.size() > 0){
                    fragment = new AdvertisementFragment();
                    FragmentManager fragmentManager = getFragmentManager();
                    fragmentManager.beginTransaction().replace(R.id.frame_container, fragment).commit();
                }else{
                    fragment = new WelcomeFragment();
                    FragmentManager fragmentManager = getFragmentManager();
                    fragmentManager.beginTransaction().replace(R.id.frame_container, fragment).commit();
                }
                progressAdvertisementDialog.dismiss();
            } catch (Exception e) {
                Log.i(MAIN_ACTIVITY, StaticMessages.EXCEPTION.getName(), e);
                progressAdvertisementDialog.dismiss();
            }
        }
    }

    public class PromotionMediaTask extends AsyncTask<Void, Void, String> {

        private final List<Promotion> promotions;
        App serverAddressObj = (App) getApplication();

        public PromotionMediaTask(List<Promotion> promotions) {
            this.promotions = promotions;
        }

        @Override
        protected String doInBackground(Void... params) {
            try {
                MediaUtils.removeAllPhisicalVideos(MainActivity.this);
            } catch (IOException e) {
                e.printStackTrace();
                Log.w(MAIN_ACTIVITY, "There aren't videos to remove!");
            }
            String serverUrl = serverAddressObj.getServerUrl(ServerController.INITIALIZE_PHOTO_PRODUCTS);
            JSONObject jsonSuccess = new JSONObject();
            try {
                for (Promotion promotion : promotions) {
                    JSONObject dataObj = new JSONObject();
                    dataObj.put("promoId", promotion.getPromotionId());
                    try {
                        if (StringUtils.isNotEmpty(promotion.getPhotoName())) {
                            byte[] photo = MediaUtils.downloadPhotoByEntityId(
                                    MainActivity.this,
                                    serverUrl,
                                    Promotion.class.getSimpleName(),
                                    Long.parseLong(promotion.getPromotionId() + "")
                            );
                            if(photo != null && photo.length > 0){
                                promotion.setPhoto(photo);
                                repositoryPromotion.save(promotion);
                            }
                        }
                        if(StringUtils.isNotEmpty(promotion.getVideoName())){
                            serverUrl = serverAddressObj.getServerUrl(ServerController.INITIALIZE_VIDEO_PRODUCTS);
                            try {
                                MediaUtils.downloadVideoByEntityId(
                                        MainActivity.this,
                                        serverUrl,
                                        Promotion.class.getSimpleName(),
                                        Integer.parseInt(promotion.getPromotionId() + ""),
                                        promotion.getVideoName()
                                );
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                        jsonSuccess.put("success", true);
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                        jsonSuccess.put("success", false);
                    } catch (IOException e) {
                        e.printStackTrace();
                        jsonSuccess.put("success", false);
                    }
                }
                return jsonSuccess.toString();
            } catch (Exception e) {
                Log.i(MAIN_ACTIVITY, StaticMessages.NOT_SERVICE.getName());
                progressPromotionDialog.dismiss();
                return StaticMessages.NOT_SERVICE.getName();
            }
        }

        @Override
        protected void onPostExecute(String response) {
            try {
                JSONObject jsonObject = new JSONObject(response);
                if (!jsonObject.getBoolean("success")) {
                    FlygowAlertDialog.createWarningPopup(
                            MainActivity.this, StaticTitles.WARNING, StaticMessages.WARNING_LOAD_PROMOTIONS);
                }

                progressPromotionDialog.dismiss();
            } catch (Exception e) {
                Log.i(MAIN_ACTIVITY, StaticMessages.EXCEPTION.getName(), e);
                progressPromotionDialog.dismiss();
            }

            //Set the inicial foods
            progressFoodDialog = ProgressDialog.show(MainActivity.this, StaticTitles.LOAD.getName(),
                    StaticMessages.LOADING_MEDIA_PRODUCTS.getName(), true);
            List<Food> allFoods = repositoryFood.listAll();
            foodMediaTask = new FoodMediaTask(allFoods);
            foodMediaTask.execute((Void) null);
        }
    }


    public class FoodMediaTask extends AsyncTask<Void, Void, String> {

        private final List<Food> foods;
        App serverAddressObj = (App) getApplication();

        public FoodMediaTask(List<Food> foods) {
            this.foods = foods;
        }

        @Override
        protected String doInBackground(Void... params) {
            String serverUrl = serverAddressObj.getServerUrl(ServerController.INITIALIZE_PHOTO_PRODUCTS);
            JSONObject jsonSuccess = new JSONObject();
            try {
                for (Food food : foods) {
                    JSONObject dataObj = new JSONObject();
                    dataObj.put("foodId", food.getFoodId());
                    try {
                        if (StringUtils.isNotEmpty(food.getPhotoName())) {
                            byte[] photo = MediaUtils.downloadPhotoByEntityId(
                                    MainActivity.this,
                                    serverUrl,
                                    Food.class.getSimpleName(),
                                    food.getFoodId()
                            );
                            if(photo != null && photo.length > 0){
                                food.setPhoto(photo);
                                repositoryFood.save(food);
                            }
                        }
                        if(StringUtils.isNotEmpty(food.getVideoName())){
                            serverUrl = serverAddressObj.getServerUrl(ServerController.INITIALIZE_VIDEO_PRODUCTS);
                            try {
                                MediaUtils.downloadVideoByEntityId(
                                        MainActivity.this,
                                        serverUrl,
                                        Food.class.getSimpleName(),
                                        Integer.parseInt(food.getFoodId() + ""),
                                        food.getVideoName()
                                );
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                        jsonSuccess.put("success", true);
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                        jsonSuccess.put("success", false);
                    } catch (IOException e) {
                        e.printStackTrace();
                        jsonSuccess.put("success", false);
                    }
                }
                return jsonSuccess.toString();
            } catch (Exception e) {
                Log.i(MAIN_ACTIVITY, StaticMessages.NOT_SERVICE.getName());
                progressFoodDialog.dismiss();
                return StaticMessages.NOT_SERVICE.getName();
            }
        }

        @Override
        protected void onPostExecute(String response) {
            try {
                JSONObject jsonObject = new JSONObject(response);
                if (!jsonObject.getBoolean("success")) {
                    FlygowAlertDialog.createWarningPopup(
                            MainActivity.this, StaticTitles.WARNING, StaticMessages.WARNING_LOAD_PRODUCTS);
                }

                progressFoodDialog.dismiss();
            } catch (Exception e) {
                Log.i(MAIN_ACTIVITY, StaticMessages.EXCEPTION.getName(), e);
                progressFoodDialog.dismiss();
            }

            //Set the inicial advertisements
            progressAdvertisementDialog = ProgressDialog.show(MainActivity.this, StaticTitles.LOAD.getName(),
                    StaticMessages.LOADING_ADVERTISEMENTS.getName(), true);
            List<Advertisement> allAdvs = repositoryAdvertisement.listAll();
            advertisementMediaTask = new AdvertisementMediaTask(allAdvs);
            advertisementMediaTask.execute((Void) null);
        }
    }
}