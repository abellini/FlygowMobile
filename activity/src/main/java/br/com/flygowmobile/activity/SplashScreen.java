package br.com.flygowmobile.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.conn.HttpHostConnectException;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.util.Arrays;

import br.com.flygowmobile.Utils.FlygowAlertDialog;
import br.com.flygowmobile.Utils.FlygowServerUrl;
import br.com.flygowmobile.database.RepositoryScript;
import br.com.flygowmobile.database.RepositoryTablet;
import br.com.flygowmobile.entity.Tablet;
import br.com.flygowmobile.enums.ServerController;
import br.com.flygowmobile.enums.StaticMessages;
import br.com.flygowmobile.enums.StaticTitles;
import br.com.flygowmobile.service.ServiceHandler;


public class SplashScreen extends Activity implements Runnable {

    private final int DELAY = 1000;
    public static RepositoryTablet repositoryTablet;
    private static final String SPLASH_SCREEN_ACTIVITY = "SplashScreen";
    private ProgressDialog progressDialog;
    private ProgressDialog loadServerDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);
        repositoryTablet = new RepositoryTablet(this);
        progressDialog = ProgressDialog.show(SplashScreen.this, StaticTitles.LOAD.getName(),
              StaticMessages.WAIT.getName(), true);
        Handler h = new Handler();
        h.postDelayed(this, DELAY);

    }

    @Override
    public void run() {
        RepositoryScript repository = new RepositoryScript(this);
        initialize();
    }

    private void initialize(){
        Tablet configs = getConfigurations();
        proceed(configs);
    }

    private Tablet getConfigurations(){
        Tablet config = repositoryTablet.findLast();
        return config;
    }

    private void proceed(Tablet configs){
        Intent it = new Intent(this, RegisterActivity.class);;
        if(configs == null){
            it.putExtra("configs", false);
            progressDialog.dismiss();
            startActivity(it);
            finish();
        } else {
            progressDialog.dismiss();
            loadServerDialog = ProgressDialog.show(SplashScreen.this, StaticTitles.LOAD.getName(),
                    StaticMessages.CALL_CONFIGURATION_FROM_SERVER.getName(), true);
            PingTask task = new PingTask(configs);
            task.execute((Void) null);
        }
    }


    /**
     * Represents an asynchronous registration task used to salve Tablet
     */
    public class PingTask extends AsyncTask<Void, Void, String> {

        private final Tablet configs;
        FlygowServerUrl serverAddressObj = (FlygowServerUrl)getApplication();

        public PingTask(Tablet configs){
            serverAddressObj.setServerIp(configs.getServerIP());
            serverAddressObj.setServerPort(configs.getServerPort());
            this.configs = configs;
        }

        @Override
        protected String doInBackground(Void... params) {
            String url = serverAddressObj.getServerUrl(ServerController.CONNECT);
            try {
                String tabletJson = configs.toJSONInitialConfig();
                NameValuePair valuePairJson = new BasicNameValuePair("tabletJson", tabletJson);
                NameValuePair reconnect = new BasicNameValuePair("isReconnect", "true");
                return ServiceHandler.makeServiceCall(url, ServiceHandler.POST, Arrays.asList(valuePairJson, reconnect));
            } catch (HttpHostConnectException ex) {
                Log.i(SPLASH_SCREEN_ACTIVITY, StaticMessages.TIMEOUT.getName());
                loadServerDialog.dismiss();
                return StaticMessages.TIMEOUT.getName();
            } catch (Exception e) {
                Log.i(SPLASH_SCREEN_ACTIVITY, StaticMessages.NOT_SERVICE.getName());
                loadServerDialog.dismiss();
                return StaticMessages.NOT_SERVICE.getName();
            }
        }

        @Override
        protected void onPostExecute(String response) {
            try {
                if(StaticMessages.TIMEOUT.getName().equals(response)){
                    response = "{success: false}";
                }else if (StaticMessages.NOT_SERVICE.getName().equals(response)){
                    response = "{success: false}";
                }
                JSONObject jsonObject = new JSONObject(response);
                boolean success = jsonObject.getBoolean("success");
                Intent it;
                if (success) {
                    it = new Intent(SplashScreen.this, RegisterDetailActivity.class);
                    it.putExtra("jsonDetailDomain", jsonObject.toString());
                    loadServerDialog.dismiss();
                    startActivity(it);
                    finish();
                } else {
                    loadServerDialog.dismiss();
                    it = new Intent(SplashScreen.this, RegisterActivity.class);
                    it.putExtra("configs", true);
                    it.putExtra("isReconnect", "true");
                    it.putExtra("configData", configs.toJSONInitialConfig());
                    FlygowAlertDialog.createWarningPopupWithIntent(SplashScreen.this, StaticTitles.WARNING, StaticMessages.TIMEOUT, it);
                }
            } catch (Exception e) {
                Log.i(SPLASH_SCREEN_ACTIVITY, StaticMessages.NOT_SERVICE.getName());
                loadServerDialog.dismiss();
                Toast.makeText(SplashScreen.this, StaticMessages.NOT_SERVICE.getName(), Toast.LENGTH_LONG).show();
            }
        }
    }
}