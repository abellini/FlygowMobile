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
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ViewFlipper;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Arrays;
import java.util.List;

import br.com.flygowmobile.Utils.FlygowAlertDialog;
import br.com.flygowmobile.Utils.FlygowServerUrl;
import br.com.flygowmobile.Utils.MediaUtils;
import br.com.flygowmobile.Utils.StringUtils;
import br.com.flygowmobile.activity.navigationdrawer.AdvertisementFragment;
import br.com.flygowmobile.activity.navigationdrawer.BasketFragment;
import br.com.flygowmobile.activity.navigationdrawer.CallAtendantFragment;
import br.com.flygowmobile.activity.navigationdrawer.CustomAdapter;
import br.com.flygowmobile.activity.navigationdrawer.FinalizeServiceFragment;
import br.com.flygowmobile.activity.navigationdrawer.RowItem;
import br.com.flygowmobile.activity.navigationdrawer.WelcomeFragment;
import br.com.flygowmobile.database.RepositoryAdvertisement;
import br.com.flygowmobile.database.RepositoryCoin;
import br.com.flygowmobile.database.RepositoryFood;
import br.com.flygowmobile.database.RepositoryTablet;
import br.com.flygowmobile.entity.Advertisement;
import br.com.flygowmobile.entity.Coin;
import br.com.flygowmobile.entity.Food;
import br.com.flygowmobile.entity.Tablet;
import br.com.flygowmobile.enums.MediaTypeEnum;
import br.com.flygowmobile.enums.ServerController;
import br.com.flygowmobile.enums.StaticMessages;
import br.com.flygowmobile.enums.StaticTitles;
import br.com.flygowmobile.service.BuildMenuItemsService;
import br.com.flygowmobile.service.ClickProductContentService;
import br.com.flygowmobile.service.ServiceHandler;

public class MainActivity extends Activity {

    private static final String MAIN_ACTIVITY = "MainActivity";
    TypedArray menuIcons;
    // nav drawer title
    private CharSequence mDrawerTitle;
    private CharSequence mTitle;
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;
    private BuildMenuItemsService menuItemsService;
    private ClickProductContentService clickProductContentService;
    private List<RowItem> rowItems;
    private CustomAdapter adapter;
    private RepositoryAdvertisement repositoryAdvertisement;
    private RepositoryFood repositoryFood;
    private RepositoryTablet repositoryTablet;
    private RepositoryCoin repositoryCoin;
    private AdvertisementMediaTask advertisementMediaTask;
    private FoodPhotoTask foodPhotoTask;
    private ProgressDialog progressAdvertisementDialog;
    private ProgressDialog progressFoodDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTitle = mDrawerTitle = getTitle();

        menuIcons = getResources().obtainTypedArray(R.array.icons);
        menuItemsService = new BuildMenuItemsService(this, menuIcons);
        clickProductContentService = new ClickProductContentService(MainActivity.this);

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

        // enabling action bar app icon and behaving it as toggle button
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);

        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                R.drawable.ic_drawer, R.string.app_name,R.string.app_name) {
            public void onDrawerClosed(View view) {
                getActionBar().setTitle(mTitle);
                alignFragmentToCenter();
                // calling onPrepareOptionsMenu() to show action bar icons
                invalidateOptionsMenu();
            }

            public void onDrawerOpened(View drawerView) {
                getActionBar().setTitle(mDrawerTitle);
                alignFragmentToRight();
                // calling onPrepareOptionsMenu() to hide action bar icons
                invalidateOptionsMenu();
            }
        };

        mDrawerLayout.setDrawerListener(mDrawerToggle);

        if (savedInstanceState == null) {
            // on first time display view for first nav item
            //updateDisplay(0);
        }

        mDrawerLayout.setDrawerListener(mDrawerToggle);
        mDrawerLayout.openDrawer(Gravity.LEFT);

        //Set the inicial fragment... photo foods...
        //After this, load the advertisements and change to the next activity
        progressFoodDialog = ProgressDialog.show(MainActivity.this, StaticTitles.LOAD.getName(),
                StaticMessages.LOADING_PHOTO_PRODUCTS.getName(), true);
        List<Food> allFoods = repositoryFood.listAll();
        foodPhotoTask = new FoodPhotoTask(allFoods);
        foodPhotoTask.execute((Void) null);
    }

    private void alignFragmentToCenter(){
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

    private void alignFragmentToRight(){
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



    @Override
    public void setTitle(CharSequence title) {
        mTitle = title;
        getActionBar().setTitle(mTitle);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_activity_actions, menu);

        initializeOrderValue(menu);

        return super.onCreateOptionsMenu(menu);
    }

    private void initializeOrderValue(Menu menu){
        final String initialValue = "0,00";
        MenuItem menuItem = menu.findItem(R.id.allPrice);
        Tablet tablet = repositoryTablet.findLast();
        if(tablet != null){
            Coin coin = repositoryCoin.findById(tablet.getCoinId());
            if(coin != null && menuItem != null){
                menuItem.setTitle(coin.getSymbol() + " " + initialValue);
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // toggle nav drawer on selecting action bar app icon/title
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        Fragment fragment;
        FragmentManager fragmentManager = getFragmentManager();
        // Handle action bar actions click
        switch (item.getItemId()) {
            case R.id.callAtendant:
                fragment = new CallAtendantFragment();
                fragmentManager.beginTransaction().replace(R.id.frame_container, fragment).commit();
                return true;
            case R.id.basket:
                fragment = new BasketFragment();
                fragmentManager.beginTransaction().replace(R.id.frame_container, fragment).commit();
                return true;
            case R.id.finalizeService:
                fragment = new FinalizeServiceFragment();
                fragmentManager.beginTransaction().replace(R.id.frame_container, fragment).commit();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }


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
            clickProductContentService.onClickProductItem(item);
        }
    }

    public class AdvertisementMediaTask extends AsyncTask<Void, Void, String> {

        private final List<Advertisement> advertisements;
        FlygowServerUrl serverAddressObj = (FlygowServerUrl) getApplication();

        public AdvertisementMediaTask(List<Advertisement> advertisements) {
            this.advertisements = advertisements;
        }

        @Override
        protected String doInBackground(Void... params) {
            try {
                MediaUtils.removeAllPhisicalVideos(MainActivity.this);
            } catch (IOException e) {
                e.printStackTrace();
                Log.w(MAIN_ACTIVITY, "There aren't videos to remove!");
            }
            String serverUrl = serverAddressObj.getServerUrl(ServerController.INITIALIZE_MEDIA_ADVERTISEMENTS);
            JSONObject jsonSuccess = new JSONObject();

            try {
                for(Advertisement adv : advertisements){
                    try {
                        if(StringUtils.isNotEmpty(adv.getVideoName())){
                            MediaUtils.downloadVideoByEntityId(MainActivity.this, serverUrl, adv.getAdvertisementId(), adv.getVideoName());
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
                if(!jsonObject.getBoolean("success")){
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
                Log.i(MAIN_ACTIVITY, StaticMessages.EXCEPTION.getName());
                progressAdvertisementDialog.dismiss();
            }
        }
    }


    public class FoodPhotoTask extends AsyncTask<Void, Void, String> {

        private final List<Food> foods;
        FlygowServerUrl serverAddressObj = (FlygowServerUrl) getApplication();

        public FoodPhotoTask(List<Food> foods) {
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
                                    MainActivity.this, serverUrl, Food.class.getSimpleName(), food.getFoodId());
                            if(photo != null && photo.length > 0){
                                food.setPhoto(photo);
                                repositoryFood.save(food);
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
                            MainActivity.this, StaticTitles.WARNING, StaticMessages.WARNING_LOAD_PHOTO);
                }

                progressFoodDialog.dismiss();
            } catch (Exception e) {
                Log.i(MAIN_ACTIVITY, StaticMessages.EXCEPTION.getName());
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