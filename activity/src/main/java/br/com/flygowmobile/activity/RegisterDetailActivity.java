package br.com.flygowmobile.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.conn.HttpHostConnectException;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.com.flygowmobile.database.RepositoryAttendant;
import br.com.flygowmobile.database.RepositoryCoin;
import br.com.flygowmobile.database.RepositoryTablet;
import br.com.flygowmobile.entity.Attendant;
import br.com.flygowmobile.entity.Coin;
import br.com.flygowmobile.entity.Tablet;
import br.com.flygowmobile.enums.ServerController;
import br.com.flygowmobile.service.ServiceHandler;
import br.com.flygowmobile.Utils.FlygowServerUrl;


public class RegisterDetailActivity extends Activity {

    private static final String REGISTER_DETAIL_ACTIVITY = "RegisterDetailActivity";

    private RegisterDetailsTabletTask mRegisterDetailsTask = null;
    final SimpleDateFormat parser = new SimpleDateFormat("dd/MM/yyyy");

    private Spinner spinnerCoin, spinnerAttendant;
    private Map<Integer, String> spinnerCoinValues = new HashMap<Integer, String>();
    private Map<Integer, String> spinnerAttendantValues = new HashMap<Integer, String>();
    private int coinId;
    private int attendantId;

    private View mProgressDetailView;
    private View mRegisterFormDetailView;

    public static RepositoryCoin repositoryCoin;
    public static RepositoryAttendant repositoryAttendant;
    public static RepositoryTablet repositoryTablet;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_detail);



        // Get Params
        Bundle bundle = getIntent().getExtras();
        String json = bundle.getString("jsonObject");

        // Repositories
        repositoryCoin = new RepositoryCoin(this);
        repositoryAttendant = new RepositoryAttendant(this);
        repositoryTablet = new RepositoryTablet(this);

        spinnerCoin = (Spinner) findViewById(R.id.spinnerCoin);
        spinnerAttendant = (Spinner) findViewById(R.id.spinnerAttendant);
        mRegisterFormDetailView = findViewById(R.id.register_detail_form);
        mProgressDetailView = findViewById(R.id.register_detail_progress);
        try {
            if (json != null) {
                JSONObject jsonObject = new JSONObject(json);

                JSONArray jsonCoins = jsonObject.getJSONArray("coins");
                this.populateSpinnerCoin(jsonCoins, spinnerCoin, spinnerCoinValues);

                JSONArray jsonAttendants = jsonObject.getJSONArray("attendants");
                this.populateSpinnerAttendant(jsonAttendants, spinnerAttendant, spinnerAttendantValues);
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

    private void populateSpinnerCoin(JSONArray jsonArray, Spinner spinner, final Map<Integer, String> store) {
        List<String> list = new ArrayList<String>();
        try {
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject obj = jsonArray.getJSONObject(i);

                Integer id = obj.getInt("id");
                String name = obj.getString("name");

                Coin coin = new Coin();
                coin.setCoinId(obj.getInt("id"));
                coin.setName(obj.getString("name"));
                coin.setSymbol(obj.getString("symbol"));
                coin.setConversion(obj.getDouble("conversion"));

                list.add(name);
                store.put(id, name);
            }

            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                    android.R.layout.simple_spinner_item, list);

            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(dataAdapter);
            spinner.setOnItemSelectedListener(
                    new AdapterView.OnItemSelectedListener() {
                        public void onItemSelected(
                                AdapterView<?> parent,
                                View view,
                                int position,
                                long id) {
                            int i = 0;
                            for (Integer key : store.keySet()) {
                                if (i == position) {
                                    coinId = key;
                                }
                            }
                            i++;
                        }

                        public void onNothingSelected(AdapterView<?> parent) {
                        }
                    }
            );
        } catch (Exception e) {
            Log.i(REGISTER_DETAIL_ACTIVITY, "Error: " + e.getMessage());
        }
    }

    private void populateSpinnerAttendant(JSONArray jsonArray, Spinner spinner, final Map<Integer, String> store) {
        List<String> list = new ArrayList<String>();
        try {
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject obj = jsonArray.getJSONObject(i);

                Integer id = obj.getInt("id");
                String name = obj.getString("name");

                Attendant attendant = new Attendant();

                attendant.setAttendantId(obj.getInt("id"));
                attendant.setName(obj.getString("name"));
                attendant.setLastName(obj.getString("lastName"));
                attendant.setAddress(obj.getString("address"));
                attendant.setBirthDate(parser.parse(obj.getString("birthDate")));
                attendant.setLogin(obj.getString("login"));
                attendant.setPassword(obj.getString("password"));
                attendant.setEmail(obj.getString("email"));

                list.add(name);
                store.put(id, name);
            }

            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                    android.R.layout.simple_spinner_item, list);

            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(dataAdapter);
            spinner.setOnItemSelectedListener(
                    new AdapterView.OnItemSelectedListener() {
                        public void onItemSelected(
                                AdapterView<?> parent,
                                View view,
                                int position,
                                long id) {
                            int i = 0;
                            for (Integer key : store.keySet()) {
                                if (i == position) {
                                    attendantId = key;
                                }
                                i++;
                            }

                        }

                        public void onNothingSelected(AdapterView<?> parent) {
                        }
                    }
            );
        } catch (Exception e) {
            Log.i(REGISTER_DETAIL_ACTIVITY, "Error: " + e.getMessage());
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

    public void saveCoins(JSONArray jsonCoins) {
        try {
            for (int i = 0; i < jsonCoins.length(); i++) {
                JSONObject obj = jsonCoins.getJSONObject(i);

            }
        } catch (Exception e) {

        }

    }


    /**
     * Represents an asynchronous registration task used to salve Tablet
     */
    public class RegisterDetailsTabletTask extends AsyncTask<Void, Void, String> {

        private String coin;
        private String attendant;
        private List<String> promotions;
        FlygowServerUrl serverAddressObj = (FlygowServerUrl)getApplication();
        String url = serverAddressObj.getServerUrl(ServerController.REGISTER_DETAILS);


        public RegisterDetailsTabletTask(String coin, String attendant, List<String> promotions) {
            Log.i(REGISTER_DETAIL_ACTIVITY, coin + " ::: " + attendant);
            this.coin = coin;
            this.attendant = attendant;
            this.promotions = promotions;
        }

        @Override
        protected String doInBackground(Void... params) {
            try {
                Tablet tablet
                 = repositoryTablet.findFirst();
                String tabletDetailsJson = "{tabletNumber: "+ tablet.getNumber() + ", coinId: " + coinId + ", attendantId: " + attendantId + "}";
                NameValuePair valuePair = new BasicNameValuePair("tabletDetailsJson", tabletDetailsJson);
                Log.i(REGISTER_DETAIL_ACTIVITY, "URL -->>>>>>>> " + url);
                return ServiceHandler.makeServiceCall(url, ServiceHandler.POST, Arrays.asList(valuePair));
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
