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
import org.json.JSONObject;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;

import br.com.flygowmobile.database.RepositoryTablet;
import br.com.flygowmobile.entity.Tablet;
import br.com.flygowmobile.service.ServiceHandler;

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

        populateFields();

        //populateAutoComplete();

        //mPasswordView = (EditText) findViewById(R.id.password);
        //mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
        //    @Override
        //    public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
        //        if (id == R.id.login || id == EditorInfo.IME_NULL) {
        //            attemptLogin();
        //            return true;
        //        }
        //        return false;
        //    }
        //});

        Button mRegisterButton = (Button) findViewById(R.id.registger_button);
        mRegisterButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                tabletRegister();
            }
        });

        mRegisterFormView = findViewById(R.id.register_form);
        mProgressView = findViewById(R.id.register_progress);
    }

    private void populateFields() {

        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces();
                 en.hasMoreElements();) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements();) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress()) {
                        String ipHost = inetAddress.getHostAddress().toString();
                        mip.setText(ipHost);
                    }
                }
            }
        } catch (Exception ex) {
            Log.e("IP Address", ex.toString());
        }
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
        return new CursorLoader(this,
                // Retrieve data rows for the device user's 'profile' contact.
                Uri.withAppendedPath(ContactsContract.Profile.CONTENT_URI,
                        ContactsContract.Contacts.Data.CONTENT_DIRECTORY), ProfileQuery.PROJECTION,

                // Select only email addresses.
                ContactsContract.Contacts.Data.MIMETYPE +
                        " = ?", new String[]{ContactsContract.CommonDataKinds.Email
                                                                     .CONTENT_ITEM_TYPE},

                // Show primary email addresses first. Note that there won't be
                // a primary email address if the user hasn't specified one.
                ContactsContract.Contacts.Data.IS_PRIMARY + " DESC");
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {
    }

    private interface ProfileQuery {
        String[] PROJECTION = {
                ContactsContract.CommonDataKinds.Email.ADDRESS,
                ContactsContract.CommonDataKinds.Email.IS_PRIMARY,
        };

        int ADDRESS = 0;
        int IS_PRIMARY = 1;
    }

    /**
     * Represents an asynchronous registration task used to salve Tablet
     */
    public class RegisterTabletTask extends AsyncTask<Void, Void, String> {

        private final Tablet tablet;
        private final static String URL = "http://192.168.1.34:8080/flygow/webservice/connect";

        RegisterTabletTask(Tablet tablet) {
            this.tablet = tablet;
        }

        @Override
        protected String doInBackground(Void... params) {
            try {
                String tabletJson = tablet.toJSONInitialConfig();
                NameValuePair valuePair = new BasicNameValuePair("tabletJson", tabletJson);
                return ServiceHandler.makeServiceCall(URL, ServiceHandler.POST, Arrays.asList(valuePair));
            } catch (HttpHostConnectException ex) {
                Log.i(REGISTER_ACTIVITY, "Timeout");
            } catch (Exception e) {
                Log.i(REGISTER_ACTIVITY, "Not Service");
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
                    it.putExtra("jsonObject", jsonObject.toString());
                    startActivity(it);

                    finish();
                } else {
                    mNumero.requestFocus();
                }
            } catch (Exception e) {
                Log.i(REGISTER_ACTIVITY, "Not Service");
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



