package br.com.flygowmobile.service;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Arrays;

import br.com.flygowmobile.Utils.App;
import br.com.flygowmobile.database.RepositoryTablet;
import br.com.flygowmobile.entity.Tablet;
import br.com.flygowmobile.enums.ServerController;
import br.com.flygowmobile.enums.TabletStatusEnum;

/**
 * Created by Tiago Rocha Gomes on 29/09/14.
 */
public class TabletStatusService {
    private Activity activity;
    private RepositoryTablet repositoryTablet;

    private static final String TABLET_STATUS_SERVICE = "TabletStatusService";

    public TabletStatusService(Activity activity){
        this.activity = activity;
        this.repositoryTablet = new RepositoryTablet(activity);
    }

    public void changeTabletStatus(TabletStatusEnum status, boolean refreshTabletStatusInServer){
        Tablet tablet = repositoryTablet.findLast();
        tablet.setStatusId(status.getId());
        long saved = repositoryTablet.save(tablet);
        if(refreshTabletStatusInServer && saved > 0){
            RefreshTabletStatusTask refreshTabletStatusTask = new RefreshTabletStatusTask(tablet, status);
            refreshTabletStatusTask.execute((Void) null);
        }
    }

    private class RefreshTabletStatusTask extends AsyncTask<Void, Void, String> {

        private App serverAddressObj = (App) activity.getApplication();
        private Tablet tablet;
        private TabletStatusEnum status;

        public RefreshTabletStatusTask(Tablet tablet, TabletStatusEnum status) {
            this.tablet = tablet;
            this.status = status;
        }

        @Override
        protected String doInBackground(Void... params) {
            String serverUrl = serverAddressObj.getServerUrl(ServerController.REFRESH_TABLET_STATUS);
            NameValuePair statusPair = new BasicNameValuePair("statusId", status.getId()+"");
            NameValuePair tabletPair = new BasicNameValuePair("tabletNumber", tablet.getNumber()+"");
            try {
                return ServiceHandler.makeServiceCall(serverUrl, ServiceHandler.POST, Arrays.asList(statusPair, tabletPair));
            } catch (IOException e) {
                Log.e(TABLET_STATUS_SERVICE, e.getMessage(), e);
            }
            return null;
        }

        @Override
        protected void onPostExecute(String response) {
            JSONObject jsonObject = null;
            try {
                jsonObject = new JSONObject(response);
                if (jsonObject.getBoolean("success")) {
                    Log.i(TABLET_STATUS_SERVICE, "Tablet Status refresh to:" + TabletStatusEnum.fromId(tablet.getStatusId()));
                }
            } catch (JSONException e) {
                Log.e(TABLET_STATUS_SERVICE, e.getMessage(), e);
            }
        }
    }
}
