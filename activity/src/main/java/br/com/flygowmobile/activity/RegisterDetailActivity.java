package br.com.flygowmobile.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.conn.HttpHostConnectException;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.com.flygowmobile.entity.Tablet;
import br.com.flygowmobile.service.ServiceHandler;


public class RegisterDetailActivity extends Activity {

    private static final String REGISTER_DETAIL_ACTIVITY = "RegisterDetailActivity";
    private RegisterDetailsTabletTask mRegisterDetailsTask = null;

    private Spinner spinnerCoin, spinnerAttendant;
    private View mProgressDetailView;
    private View mRegisterFormDetailView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_detail);

        // Get Params
        Bundle bundle = getIntent().getExtras();
        String json = bundle.getString("jsonObject");

        spinnerCoin = (Spinner) findViewById(R.id.spinnerCoin);
        spinnerAttendant = (Spinner) findViewById(R.id.spinnerAttendant);
        try {
            if (json != null) {
                JSONObject jsonObject = new JSONObject(json);
                JSONArray jsonCoins = jsonObject.getJSONArray("coins");
                this.populateSpinner(jsonCoins, spinnerCoin);

                JSONArray jsonAttendants = jsonObject.getJSONArray("attendants");
                this.populateSpinner(jsonAttendants, spinnerAttendant);

            }
        } catch (Exception e) {

        }

        Button mRegisterButton = (Button) findViewById(R.id.register_detail_button);
        mRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tabletRegisterDetails();
            }
        });
    }

    private void tabletRegisterDetails() {
        String coin = (String) spinnerCoin.getSelectedItem();
        String attendant = (String) spinnerAttendant.getSelectedItem();
        List<String> promotions = new ArrayList<String>(); // TODO: Implementar a lista de promoções
        showProgress(true);
        mRegisterDetailsTask = new RegisterDetailsTabletTask(coin, attendant, promotions);
        mRegisterDetailsTask.execute((Void) null);
    }

    private void populateSpinner(JSONArray jsonArray, Spinner spinner) {
        HashMap<Integer, String> list = new HashMap<Integer, String>();
        try {
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject obj = jsonArray.getJSONObject(i);
                String name = obj.getString("name");
                Integer id = obj.getInt("id");
                list.put(id, name);
            }

            ArrayAdapter<HashMap<Integer, String>> dataAdapter = new ArrayAdapter<HashMap<Integer, String>>(this,
                    android.R.layout.simple_spinner_item);
            dataAdapter.add(list);
            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(dataAdapter);
        } catch (Exception e) {

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

            mRegisterFormDetailView.setVisibility(show ? View.GONE : View.VISIBLE);
            mRegisterFormDetailView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mRegisterFormDetailView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressDetailView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressDetailView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressDetailView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressDetailView.setVisibility(show ? View.VISIBLE : View.GONE);
            mRegisterFormDetailView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.register_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Intent it = new Intent(RegisterDetailActivity.this, MainActivity.class);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    /**
     * Represents an asynchronous registration task used to salve Tablet
     */
    public class RegisterDetailsTabletTask extends AsyncTask<Void, Void, String> {

        private String coin;
        private String attendant;
        private List<String> promotions;
        private final static String URL = "http://192.168.1.101:8082/flygow/webservice/registerDetails";


        public RegisterDetailsTabletTask(String coin, String attendant, List<String> promotions) {
            Log.i(REGISTER_DETAIL_ACTIVITY, coin + " ::: " + attendant);
            this.coin = coin;
            this.attendant = attendant;
            this.promotions = promotions;
        }

        @Override
        protected String doInBackground(Void... params) {
            try {
                String tabletDetailsJson = "{coin: \"" + coin + "\", attendant: \" " + attendant + "\"}";
                NameValuePair valuePair = new BasicNameValuePair("tabletDetailsJson", tabletDetailsJson);
                return ServiceHandler.makeServiceCall(URL, ServiceHandler.POST, Arrays.asList(valuePair));
            } catch (HttpHostConnectException ex) {
                Log.i(REGISTER_DETAIL_ACTIVITY, "Timeout");
            } catch (Exception e) {
                Log.i(REGISTER_DETAIL_ACTIVITY, "Not Service");
            }
            return "";
        }

        @Override
        protected void onPostExecute(final String response) {
            mRegisterDetailsTask = null;
            showProgress(false);

            Log.i(REGISTER_DETAIL_ACTIVITY, "Service: " + response);
            try {
                JSONObject jsonObject = new JSONObject(response);
                Boolean success = jsonObject.getBoolean("success");
                Toast.makeText(RegisterDetailActivity.this, jsonObject.getString("message"), Toast.LENGTH_LONG).show();
                if (success) {

                    //saveTabletDetails(coin, attendant, promotions); TODO: Salvar informações na volta do server
                    //Log.i(REGISTER_DETAIL_ACTIVITY, "Salved record(s)");

                    //Intent it = new Intent(RegisterDetailActivity.this, PrincipalMenu.class);
                    //it.putExtra("jsonObject", jsonObject.toString());
                    //startActivity(it);// TODO: Próxima tela

                    finish();
                } else {
                    Toast.makeText(RegisterDetailActivity.this, jsonObject.getString("message"), Toast.LENGTH_LONG).show();
                }
            } catch (Exception e) {
                Log.i(REGISTER_DETAIL_ACTIVITY, "Not Service");
            }
        }

        @Override
        protected void onCancelled() {
            mRegisterDetailsTask = null;
            showProgress(false);
        }
    }
}