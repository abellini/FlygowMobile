package br.com.flygowmobile.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.conn.HttpHostConnectException;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Arrays;
import java.util.Enumeration;

import br.com.flygowmobile.Utils.FlygowServerUrl;
import br.com.flygowmobile.database.RepositoryTablet;
import br.com.flygowmobile.entity.Tablet;
import br.com.flygowmobile.enums.ServerController;
import br.com.flygowmobile.enums.StaticMessages;
import br.com.flygowmobile.enums.StaticTitles;
import br.com.flygowmobile.service.ServiceHandler;

public class RegisterActivity extends Activity {

    private static final String REGISTER_ACTIVITY = "RegisterActivity";
    public static RepositoryTablet repositoryTablet;
    public Context context;
    private RegisterTabletTask mRegisterTask = null;
    // UI references.
    private EditText mip;
    private EditText mNumero;
    private EditText mport;
    private EditText mserverIP;
    private EditText mserverPort;
    private View mRegisterFormView;

    private ProgressDialog progressRegisterDialog;
    private ProgressDialog progressLocalRegisterDialog;

    private Bundle bundle;

    private boolean isReconnect;
    private boolean isChangeConfiguration;
    private Tablet tabletToTask;
    private long previousTabletNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        context = getApplicationContext();
        repositoryTablet = new RepositoryTablet(this);

        // Set up the login form.
        mip = (EditText) findViewById(R.id.ip_tablet);
        mNumero = (EditText) findViewById(R.id.number_tablet);
        mport = (EditText) findViewById(R.id.port_tablet);
        mserverIP = (EditText) findViewById(R.id.ip_server);
        mserverPort = (EditText) findViewById(R.id.port_server);

        Button mRegisterButton = (Button) findViewById(R.id.registger_button);
        mRegisterButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                tabletRegister();
            }
        });

        mRegisterFormView = findViewById(R.id.register_form);

        bundle = getIntent().getExtras();
        boolean hasConfigs = bundle.getBoolean("configs");
        if (hasConfigs) {
            populateFields(bundle.getString("configData"));
        } else {
            isReconnect = bundle.getBoolean("isReconnect");
            isChangeConfiguration = bundle.getBoolean("isChangeConfiguration");
            if (isReconnect) {
                populateFieldsFromDataBase();
            } else {
                populateFields(null);
            }
        }
    }

    private void populateFields(String configData) {
        if (configData != null) {
            try {
                JSONObject jsonConfigData = new JSONObject(configData);
                mNumero.setText(jsonConfigData.getString("number"));
                if (getCurrentTabletIP().equals(jsonConfigData.getString("ip"))) {
                    mip.setText(jsonConfigData.getString("ip"));
                } else {
                    mip.setText(getCurrentTabletIP());
                }
                mport.setText(jsonConfigData.getString("port"));
                mserverIP.setText(jsonConfigData.getString("serverIP"));
                mserverPort.setText(jsonConfigData.getString("serverPort"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            mip.setText(getCurrentTabletIP());
        }
    }

    private void populateFieldsFromDataBase() {
        try {
            tabletToTask = repositoryTablet.findLast();
            mNumero.setText("" + tabletToTask.getNumber());
            mip.setText(tabletToTask.getIp());
            mport.setText("" + tabletToTask.getPort());
            mserverIP.setText(tabletToTask.getServerIP());
            mserverPort.setText("" + tabletToTask.getServerPort());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String getCurrentTabletIP() {
        String ipHost = "";
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces();
                 en.hasMoreElements(); ) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress()) {
                        ipHost = inetAddress.getHostAddress().toString();
                    }
                }
            }
        } catch (Exception ex) {
            Log.e("IP Address", ex.toString());
        }
        return ipHost;
    }


    public void tabletRegister() {
        if (mRegisterTask != null) {
            return;
        }

        // Reset errors.
        mNumero.setError(null);
        mip.setError(null);
        mport.setError(null);
        mserverIP.setError(null);
        mserverPort.setError(null);

        //Store values at the time of the register attempt.
        String numero = mNumero.getText().toString();
        String ip = mip.getText().toString();
        String port = mport.getText().toString();
        String serverIP = mserverIP.getText().toString();
        String serverPort = mserverPort.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid fields.
        if (TextUtils.isEmpty(numero)) {
            mNumero.setError(getString(R.string.error_field_required));
            focusView = mNumero;
            cancel = true;
        }
        if (TextUtils.isEmpty(ip)) {
            mip.setError(getString(R.string.error_field_required));
            focusView = mip;
            cancel = true;
        }
        if (TextUtils.isEmpty(port)) {
            mport.setError(getString(R.string.error_field_required));
            focusView = mport;
            cancel = true;
        }
        if (TextUtils.isEmpty(serverIP)) {
            mserverIP.setError(getString(R.string.error_field_required));
            focusView = mserverIP;
            cancel = true;
        }
        if (TextUtils.isEmpty(serverPort)) {
            mserverPort.setError(getString(R.string.error_field_required));
            focusView = mserverPort;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            int nNumber = Integer.parseInt(numero);
            int nport = Integer.parseInt(port);
            int nserverPort = Integer.parseInt(serverPort);

            FlygowServerUrl url = (FlygowServerUrl) getApplication();
            url.setServerIp(serverIP);
            url.setServerPort(Integer.parseInt(serverPort));
            Tablet tabletToTask = null;
            if (!isReconnect) {
                tabletToTask = new Tablet(nNumber, ip, nport, serverIP, nserverPort);
            } else {
                tabletToTask = repositoryTablet.findLast();
                previousTabletNumber = tabletToTask.getNumber();
                tabletToTask.setNumber(nNumber);
                tabletToTask.setIp(ip);
                tabletToTask.setPort(nport);
                tabletToTask.setServerIP(serverIP);
                tabletToTask.setServerPort(nserverPort);
            }

            //LOADING
            progressRegisterDialog = ProgressDialog.show(RegisterActivity.this, StaticTitles.LOAD.getName(),
                    StaticMessages.REGISTER_FROM_SERVER.getName(), true);

            mRegisterTask = new RegisterTabletTask(tabletToTask, isReconnect, isChangeConfiguration);
            mRegisterTask.setPreviousTabletNumber(previousTabletNumber);
            mRegisterTask.execute((Void) null);
        }

    }

    public void saveTablet(Tablet tablet) {
        repositoryTablet.removeAll();
        repositoryTablet.save(tablet);
    }

    /**
     * Represents an asynchronous registration task used to salve Tablet
     */
    public class RegisterTabletTask extends AsyncTask<Void, Void, String> {

        private final Tablet tablet;
        FlygowServerUrl serverAddressObj = (FlygowServerUrl) getApplication();
        String url = serverAddressObj.getServerUrl(ServerController.CONNECT);
        private long previousTabletNumber;
        private boolean isReconnect, isChangeConfiguration;

        RegisterTabletTask(Tablet tablet, boolean isReconnect, boolean isChangeConfiguration) {
            this.tablet = tablet;
            this.isChangeConfiguration = isChangeConfiguration;
            this.isReconnect = isReconnect;
        }

        public boolean isChangeConfiguration() {
            return isChangeConfiguration;
        }

        public boolean isReconnect() {
            return isReconnect;
        }

        public long getPreviousTabletNumber() {
            return previousTabletNumber;
        }

        public void setPreviousTabletNumber(long previousTabletNumber) {
            this.previousTabletNumber = previousTabletNumber;
        }

        @Override
        protected String doInBackground(Void... params) {
            try {
                String tabletJson = tablet.toJSONInitialConfig();
                String isReconnect = isReconnect() ? "true" : "false";
                String isChangeConfiguration = isChangeConfiguration() ? "true" : "false";
                NameValuePair tabletJsonPair = new BasicNameValuePair("tabletJson", tabletJson);
                NameValuePair isReconnectPair = new BasicNameValuePair("isReconnect", isReconnect);
                NameValuePair isChangeConfigurationPair = new BasicNameValuePair("isChangeConfiguration", isChangeConfiguration);
                NameValuePair previousTabletNumberPair = new BasicNameValuePair("previousTabletNumber", "" + previousTabletNumber);
                Log.i(REGISTER_ACTIVITY, "URL -->>>>>>>> " + url);
                return ServiceHandler.makeServiceCall(url, ServiceHandler.POST, Arrays.asList(tabletJsonPair, isReconnectPair, isChangeConfigurationPair, previousTabletNumberPair));
            } catch (HttpHostConnectException ex) {
                Log.i(REGISTER_ACTIVITY, StaticMessages.TIMEOUT.getName());
                Toast.makeText(RegisterActivity.this, StaticMessages.TIMEOUT.getName(), Toast.LENGTH_LONG).show();
            } catch (Exception e) {
                Log.i(REGISTER_ACTIVITY, StaticMessages.NOT_SERVICE.getName());
                Toast.makeText(RegisterActivity.this, StaticMessages.NOT_SERVICE.getName(), Toast.LENGTH_LONG).show();
            }
            return "";
        }

        @Override
        protected void onPostExecute(final String response) {
            mRegisterTask = null;

            Log.i(REGISTER_ACTIVITY, "Service: " + response);
            try {
                JSONObject jsonObject = new JSONObject(response);
                Boolean success = jsonObject.getBoolean("success");
                if (success) {
                    progressRegisterDialog.dismiss();
                    Toast.makeText(RegisterActivity.this, StaticMessages.SUCCESS_SAVE_IN_SERVER.getName(), Toast.LENGTH_LONG).show();
                    progressLocalRegisterDialog = ProgressDialog.show(RegisterActivity.this, StaticTitles.LOAD.getName(),
                            StaticMessages.LOCAL_LOAD.getName(), true);

                    saveTablet(tablet);
                    Log.i(REGISTER_ACTIVITY, "Salved record(s)");

                    //FINISH LOADING...
                    progressLocalRegisterDialog.dismiss();
                    Toast.makeText(RegisterActivity.this, jsonObject.getString("message"), Toast.LENGTH_LONG).show();

                    Intent it = new Intent(RegisterActivity.this, RegisterDetailActivity.class);
                    it.putExtra("jsonDetailDomain", jsonObject.toString());
                    startActivity(it);

                    finish();
                } else {
                    //FINISH LOADING...
                    progressRegisterDialog.dismiss();
                    Toast.makeText(RegisterActivity.this, jsonObject.getString("message"), Toast.LENGTH_LONG).show();
                    mNumero.requestFocus();
                }
            } catch (Exception e) {
                //FINISH LOADING...
                progressRegisterDialog.dismiss();
                progressLocalRegisterDialog.dismiss();
                Log.i(REGISTER_ACTIVITY, StaticMessages.EXCEPTION.getName());
                Toast.makeText(RegisterActivity.this, StaticMessages.EXCEPTION.getName(), Toast.LENGTH_LONG).show();
            }
        }

        @Override
        protected void onCancelled() {
            mRegisterTask = null;
            //FINISH LOADING...
        }
    }
}