package br.com.flygowmobile.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
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

import br.com.flygowmobile.database.RepositoryTablet;
import br.com.flygowmobile.entity.Tablet;
import br.com.flygowmobile.enums.ServerController;
import br.com.flygowmobile.enums.StaticMessages;
import br.com.flygowmobile.service.ServiceHandler;
import br.com.flygowmobile.Utils.FlygowServerUrl;

public class RegisterActivity extends Activity implements LoaderCallbacks<Cursor>{

    private RegisterTabletTask mRegisterTask = null;
    private static final String REGISTER_ACTIVITY = "RegisterActivity";

    public static RepositoryTablet repositoryTablet;

    public Context context;

    // UI references.
    private EditText mip;
    private EditText mNumero;
    private EditText mport;
    private EditText mserverIP;
    private EditText mserverPort;
    private View mProgressView;
    private View mRegisterFormView;

    private Bundle bundle;

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
        mProgressView = findViewById(R.id.register_progress);

        bundle = getIntent().getExtras();
        boolean hasConfigs = bundle.getBoolean("configs");
        if(hasConfigs){
            populateFields(bundle.getString("configData"));
        }else{
            populateFields(null);
        }
    }

    private void populateFields(String configData) {
        if(configData != null){
            try {
                JSONObject jsonConfigData = new JSONObject(configData);
                mNumero.setText(jsonConfigData.getString("number"));
                if(getCurrentTabletIP().equals(jsonConfigData.getString("ip"))){
                    mip.setText(jsonConfigData.getString("ip"));
                }else{
                    mip.setText(getCurrentTabletIP());
                }
                mport.setText(jsonConfigData.getString("port"));
                mserverIP.setText(jsonConfigData.getString("serverIP"));
                mserverPort.setText(jsonConfigData.getString("serverPort"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }else{
            mip.setText(getCurrentTabletIP());
        }
    }

    private String getCurrentTabletIP(){
        String ipHost = "";
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces();
                 en.hasMoreElements();) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements();) {
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

    private void populateAutoComplete() {
        getLoaderManager().initLoader(0, null, this);
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

            FlygowServerUrl url = (FlygowServerUrl)getApplication();
            url.setServerIp(serverIP);
            url.setServerPort(Integer.parseInt(serverPort));
            Tablet tablet = new Tablet(nNumber, ip, nport, serverIP, nserverPort);

            showProgress(true);
            mRegisterTask = new RegisterTabletTask(tablet);
            mRegisterTask.execute((Void) null);
        }

    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    public void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mRegisterFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mRegisterFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mRegisterFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mRegisterFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {
    }

    /**
     * Represents an asynchronous registration task used to salve Tablet
     */
    public class RegisterTabletTask extends AsyncTask<Void, Void, String> {

        private final Tablet tablet;
        FlygowServerUrl serverAddressObj = (FlygowServerUrl)getApplication();
        String url = serverAddressObj.getServerUrl(ServerController.CONNECT);

        RegisterTabletTask(Tablet tablet) {
            this.tablet = tablet;
        }

        @Override
        protected String doInBackground(Void... params) {
            try {
                String tabletJson = tablet.toJSONInitialConfig();
                String isReconnect = bundle.getString("isReconnect");
                NameValuePair tabletJsonPair = new BasicNameValuePair("tabletJson", tabletJson);
                NameValuePair isReconnectPair = new BasicNameValuePair("isReconnect", isReconnect);
                Log.i(REGISTER_ACTIVITY, "URL -->>>>>>>> " + url);
                return ServiceHandler.makeServiceCall(url, ServiceHandler.POST, Arrays.asList(tabletJsonPair, isReconnectPair));
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
            showProgress(false);

            Log.i(REGISTER_ACTIVITY, "Service: " + response);
            try {
                JSONObject jsonObject = new JSONObject(response);
                Boolean success = jsonObject.getBoolean("success");
                Toast.makeText(RegisterActivity.this, jsonObject.getString("message"), Toast.LENGTH_LONG).show();
                if (success) {

                    saveTablet(tablet);
                    Log.i(REGISTER_ACTIVITY, "Salved record(s)");

                    Intent it = new Intent(RegisterActivity.this, RegisterDetailActivity.class);
                    it.putExtra("jsonDetailDomain", jsonObject.toString());
                    startActivity(it);

                    finish();
                } else {
                    mNumero.requestFocus();
                }
            } catch (Exception e) {
                Log.i(REGISTER_ACTIVITY, StaticMessages.NOT_SERVICE.getName());
                Toast.makeText(RegisterActivity.this, StaticMessages.NOT_SERVICE.getName(), Toast.LENGTH_LONG).show();
            }
        }

        @Override
        protected void onCancelled() {
            mRegisterTask = null;
            showProgress(false);
        }
    }

    public void saveTablet(Tablet tablet) {
        repositoryTablet.save(tablet);
    }
}



