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
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import org.apache.http.NameValuePair;
import org.apache.http.conn.HttpHostConnectException;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import br.com.flygowmobile.Utils.FlygowServerUrl;
import br.com.flygowmobile.Utils.VideoUtils;
import br.com.flygowmobile.activity.navigationdrawer.AdvertisementFragment;
import br.com.flygowmobile.activity.navigationdrawer.CustomAdapter;
import br.com.flygowmobile.activity.navigationdrawer.FB_Fragment;
import br.com.flygowmobile.activity.navigationdrawer.RowItem;
import br.com.flygowmobile.activity.navigationdrawer.WelcomeFragment;
import br.com.flygowmobile.database.RepositoryAdvertisement;
import br.com.flygowmobile.entity.Advertisement;
import br.com.flygowmobile.enums.MediaTypeEnum;
import br.com.flygowmobile.enums.ServerController;
import br.com.flygowmobile.enums.StaticMessages;
import br.com.flygowmobile.enums.StaticTitles;
import br.com.flygowmobile.service.BuildMenuItemsService;
import br.com.flygowmobile.service.ServiceHandler;

import static br.com.flygowmobile.activity.R.id.action_settings;

public class MainActivity extends Activity {

    String[] menutitles;
    TypedArray menuIcons;

    // nav drawer title
    private CharSequence mDrawerTitle;
    private CharSequence mTitle;

    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;

    private BuildMenuItemsService menuItemsService;

    private List<RowItem> rowItems;
    private CustomAdapter adapter;

    private RepositoryAdvertisement repositoryAdvertisement;

    private AdvertisementMediaTask advertisementMediaTask;

    private ProgressDialog progressAdvertisementDialog;

    private static final String MAIN_ACTIVITY = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTitle = mDrawerTitle = getTitle();
        //menutitles = getResources().getStringArray(R.array.titles);

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

        //Set the inicial fragment... advertisements
        progressAdvertisementDialog = ProgressDialog.show(MainActivity.this, StaticTitles.LOAD.getName(),
                StaticMessages.LOADING_ADVERTISEMENTS.getName(), true);
        List<Advertisement> allAdvs = repositoryAdvertisement.listAll();
        advertisementMediaTask = new AdvertisementMediaTask(allAdvs);
        advertisementMediaTask.execute((Void) null);
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

    private void updateDisplay(RowItem item) {
        Fragment fragment = new FB_Fragment();

        Bundle args = new Bundle();
        args.putSerializable("item", item);
        fragment.setArguments(args);

        if (fragment != null) {
            FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.frame_container, fragment).commit();
            // update selected item and title, then close the drawer
            //setTitle(menutitles[position]);
            mDrawerLayout.closeDrawer(mDrawerList);
        } else {
            // error in creating fragment
            Log.e("MainActivity", "Error in creating fragment");
        }

    }

    @Override
    public void setTitle(CharSequence title) {
        mTitle = title;
        getActionBar().setTitle(mTitle);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.global, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // toggle nav drawer on selecting action bar app icon/title
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        // Handle action bar actions click
        switch (item.getItemId()) {
            case action_settings:
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
        menu.findItem(action_settings).setVisible(!drawerOpen);
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
            if(!item.isGroupHeader()){
                Toast.makeText(MainActivity.this, item.getId() + " - " + item.getTitle().toString(), Toast.LENGTH_LONG).show();
                updateDisplay(item);
            }
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
                VideoUtils.removeAllPhisicalVideos(MainActivity.this);
            } catch (IOException e) {
                e.printStackTrace();
                Log.w(MAIN_ACTIVITY, "Not videos to remove!");
            }
            String url = serverAddressObj.getServerUrl(ServerController.INITIALIZE_MEDIA_ADVERTISEMENTS);
            String chooseAdvertisementsIds = "";
            try {
                int i = 0;
                for(Advertisement adv : advertisements){
                    if(i != advertisements.size()-1){
                        chooseAdvertisementsIds += adv.getAdvertisementId() + ",";
                    }else{
                        chooseAdvertisementsIds += adv.getAdvertisementId();
                    }
                    i++;
                }
                NameValuePair advertisementPairJson = new BasicNameValuePair("chooseAdvertisementsIds", ""+chooseAdvertisementsIds);
                return ServiceHandler.makeServiceCall(url, ServiceHandler.POST, Arrays.asList(advertisementPairJson));

            } catch (HttpHostConnectException ex) {
                Log.i(MAIN_ACTIVITY, StaticMessages.TIMEOUT.getName());
                progressAdvertisementDialog.dismiss();
                return StaticMessages.TIMEOUT.getName();
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
                JSONArray advArrObj = jsonObject.getJSONArray("data");
                for(int i = 0; i < advArrObj.length(); i++){
                    JSONObject advObj = advArrObj.getJSONObject(i);
                    Long advertisementId = advObj.getLong("advertisementId");
                    Integer mediaTypeId = advObj.getInt("mediaType");
                    String mediaString;
                    try{
                        mediaString = advObj.getString("media");
                    }catch(JSONException je){
                        mediaString = null;
                    }
                    if (mediaString != null && !mediaString.equals("")) {
                        Advertisement fromServer = repositoryAdvertisement.findById(advertisementId);
                        if(fromServer != null){
                            byte[] media = null;
                            try{
                                media = Base64.decode(mediaString, Base64.DEFAULT);
                            }catch (Exception ex){
                                media = null;
                            }
                            if(MediaTypeEnum.PHOTO.getId().equals(mediaTypeId.byteValue())){
                                fromServer.setPhoto(media);
                                repositoryAdvertisement.save(fromServer);
                            }else if (MediaTypeEnum.VIDEO.getId().equals(mediaTypeId.byteValue())){
                                String videoName = new Date().getTime() + "";
                                try{
                                    VideoUtils.saveVideo(MainActivity.this, videoName, media);
                                    fromServer.setVideoName(videoName);
                                    repositoryAdvertisement.save(fromServer);
                                }catch(Exception e){
                                    fromServer.setVideoName(null);
                                }
                            }
                        }
                    }
                }
                Fragment fragment;
                if(advArrObj != null && advArrObj.length() > 0){
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
}