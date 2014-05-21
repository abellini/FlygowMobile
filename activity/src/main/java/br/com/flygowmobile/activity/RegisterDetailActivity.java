package br.com.flygowmobile.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
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
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import br.com.flygowmobile.Utils.FlygowAlertDialog;
import br.com.flygowmobile.Utils.FlygowServerUrl;
import br.com.flygowmobile.custom.MultiSelectionSpinner;
import br.com.flygowmobile.database.RepositoryAccompaniment;
import br.com.flygowmobile.database.RepositoryAdvertisement;
import br.com.flygowmobile.database.RepositoryAttendant;
import br.com.flygowmobile.database.RepositoryCategory;
import br.com.flygowmobile.database.RepositoryCoin;
import br.com.flygowmobile.database.RepositoryFood;
import br.com.flygowmobile.database.RepositoryPaymentForm;
import br.com.flygowmobile.database.RepositoryTablet;
import br.com.flygowmobile.entity.Accompaniment;
import br.com.flygowmobile.entity.Advertisement;
import br.com.flygowmobile.entity.Attendant;
import br.com.flygowmobile.entity.Category;
import br.com.flygowmobile.entity.Coin;
import br.com.flygowmobile.entity.Food;
import br.com.flygowmobile.entity.PaymentForm;
import br.com.flygowmobile.entity.Tablet;
import br.com.flygowmobile.enums.ServerController;
import br.com.flygowmobile.enums.StaticMessages;
import br.com.flygowmobile.enums.StaticTitles;
import br.com.flygowmobile.service.ServiceHandler;


public class RegisterDetailActivity extends Activity {

    private static final String REGISTER_DETAIL_ACTIVITY = "RegisterDetailActivity";
    public static RepositoryCoin repositoryCoin;
    public static RepositoryAttendant repositoryAttendant;
    public static RepositoryAdvertisement repositoryAdvertisement;
    public static RepositoryTablet repositoryTablet;
    public static RepositoryCategory repositoryCategory;
    public static RepositoryFood repositoryFood;
    public static RepositoryPaymentForm repositoryPaymentForm;
    public static RepositoryAccompaniment repositoryAccompaniment;
    private RegisterDetailsTabletTask mRegisterDetailsTask = null;
    private Spinner spinnerCoin, spinnerAttendant;
    private MultiSelectionSpinner spinnerAdvertisements;
    private Map<Integer, String> spinnerCoinValues = new TreeMap<Integer, String>();
    private Map<Integer, String> spinnerAttendantValues = new TreeMap<Integer, String>();
    private Map<Integer, String> spinnerAdvertisementValues = new TreeMap<Integer, String>();
    private Map<Integer, Coin> listCoins = new Hashtable<Integer, Coin>();
    private Map<Integer, Attendant> listAttendant = new Hashtable<Integer, Attendant>();
    private Map<Integer, Advertisement> listAdvertisements = new Hashtable<Integer, Advertisement>();

    private int coinId;
    private int attendantId;

    private boolean hasCoin, hasAttendant, hasAdvertisement;

    private View mProgressDetailView;
    private View mRegisterFormDetailView;
    private ProgressDialog progressRegisterDialog;
    private ProgressDialog progressLocalRegisterDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_detail);

        // Get Params
        Bundle bundle = getIntent().getExtras();
        String json = bundle.getString("jsonDetailDomain");

        // Repositories
        repositoryCoin = new RepositoryCoin(this);
        repositoryAttendant = new RepositoryAttendant(this);
        repositoryTablet = new RepositoryTablet(this);
        repositoryAdvertisement = new RepositoryAdvertisement(this);
        repositoryCategory = new RepositoryCategory(this);
        repositoryFood = new RepositoryFood(this);
        repositoryPaymentForm = new RepositoryPaymentForm(this);
        repositoryAccompaniment = new RepositoryAccompaniment(this);

        spinnerCoin = (Spinner) findViewById(R.id.spinnerCoin);
        spinnerAttendant = (Spinner) findViewById(R.id.spinnerAttendant);
        spinnerAdvertisements = (MultiSelectionSpinner) findViewById(R.id.spinnerAdvertisements);
        mRegisterFormDetailView = findViewById(R.id.register_detail_form);
        mProgressDetailView = findViewById(R.id.register_detail_progress);
        try {
            if (json != null) {
                JSONObject jsonObject = new JSONObject(json);

                JSONArray jsonCoins = jsonObject.getJSONArray("coins");
                this.populateSpinnerCoin(jsonCoins, spinnerCoin, spinnerCoinValues);

                JSONArray jsonAttendants = jsonObject.getJSONArray("attendants");
                this.populateSpinnerAttendant(jsonAttendants, spinnerAttendant, spinnerAttendantValues);

                JSONArray jsonAdvertisements = jsonObject.getJSONArray("advertisements");
                this.populateSpinnerAdvertisements(jsonAdvertisements, spinnerAdvertisements, spinnerAdvertisementValues);
            }
            if(!hasCoin || !hasAttendant || !hasAdvertisement){
                String details = "";
                if(!hasCoin){
                    details += " - " + StaticMessages.COIN_CONFIGURATION.getName() + "\n";
                }
                if(!hasAttendant){
                    details += " - " + StaticMessages.ATTENDANT_CONFIGURATION.getName() + "\n";
                }
                if(!hasAdvertisement){
                    details += " - " + StaticMessages.ADVERTISEMENT_CONFIGURATION.getName() + "\n";
                }
                details += "\n" + StaticMessages.MANDATORY_CONFIGURATIONS.getName();
                details += "\n" + StaticMessages.DEFINE_CHOICES.getName();
                        StaticMessages generic = StaticMessages.getCustomMessage(StaticMessages.MISSING_CONFIGURATIONS.getName() + details);
                FlygowAlertDialog.createWarningPopupWithIntent(this,
                    StaticTitles.WARNING,
                    generic,
                    null
                );
            }

        } catch (Exception e) {
            Log.i(REGISTER_DETAIL_ACTIVITY, "ERROR->>" + e.getStackTrace());
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
        progressRegisterDialog = ProgressDialog.show(RegisterDetailActivity.this, StaticTitles.LOAD.getName(),
                StaticMessages.REGISTER_FROM_SERVER.getName(), true);
        String coin = (String) spinnerCoin.getSelectedItem();
        String attendant = (String) spinnerAttendant.getSelectedItem();
        mRegisterDetailsTask = new RegisterDetailsTabletTask(coin, attendant);
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
                coin.setCoinId(id);
                coin.setName(name);
                coin.setSymbol(obj.getString("symbol"));
                coin.setConversion(obj.getDouble("conversion"));

                Log.i(REGISTER_DETAIL_ACTIVITY, "listCoins: " + coin.getCoinId());
                listCoins.put(coin.getCoinId(), coin);

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
        //Verificando se já existe configurações salvas para esse campo
        Coin savedCoin = repositoryCoin.findLast();
        if(savedCoin != null){
            ArrayAdapter myAdap = (ArrayAdapter) spinnerCoin.getAdapter();
            int spinnerPosition = myAdap.getPosition(savedCoin.getName());
            spinnerCoin.setSelection(spinnerPosition, true);
            hasCoin = true;
        }else{
            spinnerCoin.setSelection(0);
        }
    }

    private void populateSpinnerAttendant(JSONArray jsonArray, Spinner spinner, final Map<Integer, String> store) {
        DateFormat fm = new SimpleDateFormat("yyyy-MM-dd");
        List<String> list = new ArrayList<String>();
        try {
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject obj = jsonArray.getJSONObject(i);

                Integer id = obj.getInt("id");
                String name = obj.getString("name");

                Attendant attendant = new Attendant();

                attendant.setAttendantId(id);
                attendant.setName(name);
                attendant.setLastName(obj.getString("lastName"));
                attendant.setAddress(obj.getString("address"));
                attendant.setBirthDate(fm.parse(obj.getString("birthDate")));
                attendant.setLogin(obj.getString("login"));
                attendant.setPassword(obj.getString("password"));
                attendant.setEmail(obj.getString("email"));

                Log.i(REGISTER_DETAIL_ACTIVITY, "listAttendant: " + attendant.getAttendantId());
                listAttendant.put(attendant.getAttendantId(), attendant);

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
        //Verificando se já existe configurações salvas para esse campo
        Attendant savedAttendant = repositoryAttendant.findById(repositoryTablet.findLast().getAttendantId());
        if(savedAttendant != null){
            ArrayAdapter myAdap = (ArrayAdapter) spinnerAttendant.getAdapter();
            int spinnerPosition = myAdap.getPosition(savedAttendant.getName());
            spinnerAttendant.setSelection(spinnerPosition, true);
            hasAttendant = true;
        }else{
            spinnerAttendant.setSelection(0);
        }
    }

    private void populateSpinnerAdvertisements(JSONArray jsonArray, MultiSelectionSpinner spinner, final Map<Integer, String> store){
        DateFormat fm = new SimpleDateFormat("yyyy-MM-dd");
        List<String> list = new ArrayList<String>();
        try {
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject obj = jsonArray.getJSONObject(i);

                Integer id = obj.getInt("id");
                String name = obj.getString("name");

                Advertisement advertisement = new Advertisement();
                advertisement.setAdvertisementId(id);
                advertisement.setName(name);
                advertisement.setInicialDate(fm.parse(obj.getString("inicialDate")));
                advertisement.setFinalDate(fm.parse(obj.getString("finalDate")));
                advertisement.setActive(obj.getBoolean("active"));

                listAdvertisements.put(advertisement.getAdvertisementId(), advertisement);

                list.add(name);
                store.put(id, name);
            }

            spinner.setItems(list);
        } catch (Exception e) {
            Log.i(REGISTER_DETAIL_ACTIVITY, "Error: " + e.getMessage());
        }
        //Verificando se já existe configurações salvas para esse campo
        List<Advertisement> savedAdvertisements = repositoryAdvertisement.listAll();
        if(savedAdvertisements != null && !savedAdvertisements.isEmpty()){
            List<String> options = new ArrayList<String>();
            for(Advertisement adv : savedAdvertisements){
                options.add(adv.getName());
            }
            spinnerAdvertisements.setSelection(options);
            hasAdvertisement = true;
        }else{
            if(!listAdvertisements.isEmpty()){
                spinnerAdvertisements.setSelection(Arrays.asList(listAdvertisements.values().iterator().next().getName()));
            }
        }
    }

    private String getAdvertisementIds(String choosedAdvertisements){
        String advIds = "";
        if(choosedAdvertisements != null && !"".equals(choosedAdvertisements)){
            List<String> listNameAdv = Arrays.asList(choosedAdvertisements.split(","));
            for(String name : listNameAdv){
                for(int id : spinnerAdvertisementValues.keySet()){
                    String advName = spinnerAdvertisementValues.get(id).trim();
                    if(advName.equals(name.trim())){
                        advIds += id + ",";
                    }
                }
            }
        }
        return advIds.toString();
    }

    private void saveMenuInformations(JSONObject initialData) {

        JSONObject obj = null;
        try {

            // Categories
            JSONArray categories  = initialData.getJSONArray("category");
            for (int i = 0; i < categories.length(); i++) {
                obj = categories.getJSONObject(i);
                Category category = new Category();
                category.setCategoryId(obj.getInt("id"));
                category.setName(obj.getString("name"));
                category.setDescription(obj.getString("description"));

                Log.i(REGISTER_DETAIL_ACTIVITY, "Save Category(ies): " + category);
                repositoryCategory.save(category);
            }

            // Foods
            JSONArray foods  = initialData.getJSONArray("foods");
            for (int i = 0; i < foods.length(); i++) {
                obj = foods.getJSONObject(i);
                Food food = new Food();
                food.setFoodId(obj.getInt("id"));
                food.setName(obj.getString("name"));
                food.setValue(obj.getDouble("value"));
                food.setDescription(obj.getString("description"));
                food.setNutritionalInfo(obj.getString("nutritionalInfo"));
                food.setActive(Boolean.parseBoolean(obj.getString("active")));
                food.setCategoryId(obj.getInt("categoryId"));

                Log.i(REGISTER_DETAIL_ACTIVITY, "Save Food(s): " + food);
                repositoryFood.save(food);
            }

            //paymentForms
            JSONArray paymentForms  = initialData.getJSONArray("paymentForms");
            for (int i = 0; i < paymentForms.length(); i++) {
                obj = paymentForms.getJSONObject(i);
                PaymentForm paymentForm = new PaymentForm();
                paymentForm.setPaymentFormId(obj.getInt("id"));
                paymentForm.setName(obj.getString("name"));
                paymentForm.setDescription(obj.getString("description"));

                Log.i(REGISTER_DETAIL_ACTIVITY, "Save Payment Form(s): " + paymentForm);
                repositoryPaymentForm.save(paymentForm);
            }

            //categoryAccompaniments
            JSONArray categoryAccompaniments  = initialData.getJSONArray("categoryAccompaniments");
            for (int i = 0; i < categoryAccompaniments.length(); i++) {
                obj = categoryAccompaniments.getJSONObject(i);
                Accompaniment accompaniment = new Accompaniment();
                accompaniment.setAccompanimentId(obj.getInt("id"));
                accompaniment.setName(obj.getString("name"));
                accompaniment.setValue(obj.getDouble("value"));
                accompaniment.setDescription(obj.getString("description"));
                accompaniment.setActive(obj.getBoolean("active"));
                accompaniment.setCategoryId(obj.getInt("categoryId"));

                Log.i(REGISTER_DETAIL_ACTIVITY, "Save Accompaniment(s): " + accompaniment);
                repositoryAccompaniment.save(accompaniment);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void saveTabletDetails() {
        repositoryCoin.removeAll();
        repositoryAttendant.removeAll();
        repositoryAdvertisement.removeAll();

        Log.i(REGISTER_DETAIL_ACTIVITY, "saveTabletDetails");
        Tablet tablet = repositoryTablet.findLast();

        Coin coin = listCoins.get(coinId);
        repositoryCoin.save(coin);

        Attendant attendant = listAttendant.get(attendantId);
        repositoryAttendant.save(attendant);

        List<String> selectedAdvertisementsString = spinnerAdvertisements.getSelectedStrings();
        for(String selected : selectedAdvertisementsString){
            for(Advertisement advertisement : listAdvertisements.values()){
                if(selected.equals(advertisement.getName())){
                    advertisement.setTabletId(tablet.getTabletId());
                    repositoryAdvertisement.save(advertisement);
                }
            }
        }


        tablet.setAttendantId(attendant.getAttendantId());
        tablet.setCoinId(coin.getCoinId());
        repositoryTablet.save(tablet);
    }

    /**
     * Represents an asynchronous registration task used to salve Tablet
     */
    public class RegisterDetailsTabletTask extends AsyncTask<Void, Void, String> {

        FlygowServerUrl serverAddressObj = (FlygowServerUrl)getApplication();
        String url = serverAddressObj.getServerUrl(ServerController.REGISTER_DETAILS);
        private String coin;
        private String attendant;


        public RegisterDetailsTabletTask(String coin, String attendant) {
            Log.i(REGISTER_DETAIL_ACTIVITY, coin + " ::: " + attendant);
            this.coin = coin;
            this.attendant = attendant;
        }

        @Override
        protected String doInBackground(Void... params) {
            try {
                Tablet tablet = repositoryTablet.findLast();
                String choosedAdvertisements = spinnerAdvertisements.getSelectedItemsAsString();
                String arrayAdvertisements = getAdvertisementIds(choosedAdvertisements);
                Log.i(REGISTER_DETAIL_ACTIVITY, "ADVERTISEMENTS -->>>>>>>> " + arrayAdvertisements);
                String tabletDetailsJson = "{tabletNumber: "+ tablet.getNumber() + ", coinId: " + coinId + ", attendantId: " + attendantId + ", advertisements: " + (arrayAdvertisements.equals("") ? "\"\"" : "\"" + arrayAdvertisements + "\"")  + "}";
                NameValuePair valuePair = new BasicNameValuePair("tabletDetailsJson", tabletDetailsJson);
                Log.i(REGISTER_DETAIL_ACTIVITY, "URL -->>>>>>>> " + url);
                return ServiceHandler.makeServiceCall(url, ServiceHandler.POST, Arrays.asList(valuePair));
            } catch (HttpHostConnectException ex) {
                progressRegisterDialog.dismiss();
                Log.i(REGISTER_DETAIL_ACTIVITY, StaticMessages.TIMEOUT.getName());
            } catch (Exception e) {
                progressRegisterDialog.dismiss();
                Log.i(REGISTER_DETAIL_ACTIVITY, StaticMessages.NOT_SERVICE.getName());
            }
            return "";
        }

        @Override
        protected void onPostExecute(final String response) {
            mRegisterDetailsTask = null;

            Log.i(REGISTER_DETAIL_ACTIVITY, "Service: " + response);
            try {
                JSONObject jsonObject = new JSONObject(response);
                Boolean success = jsonObject.getBoolean("success");
                if (success) {
                    progressRegisterDialog.dismiss();
                    Toast.makeText(RegisterDetailActivity.this, StaticMessages.SUCCESS_SAVE_IN_SERVER.getName(), Toast.LENGTH_SHORT).show();
                    progressLocalRegisterDialog = ProgressDialog.show(RegisterDetailActivity.this, StaticTitles.LOAD.getName(),
                            StaticMessages.LOCAL_LOAD.getName(), true);
                    //Salva
                    saveTabletDetails();

                    JSONObject initialData = jsonObject.getJSONObject("initialData");
                    saveMenuInformations(initialData);

                    progressLocalRegisterDialog.dismiss();
                    Toast.makeText(RegisterDetailActivity.this, jsonObject.getString("message"), Toast.LENGTH_LONG).show();

                    //Intent it = new Intent(RegisterDetailActivity.this, PrincipalMenu.class);
                    //it.putExtra("jsonObject", jsonObject.toString());
                    //startActivity(it);// TODO: Próxima tela

                    //finish();
                } else {
                    progressRegisterDialog.dismiss();
                    Toast.makeText(RegisterDetailActivity.this, jsonObject.getString("message"), Toast.LENGTH_LONG).show();
                }
            } catch (Exception e) {
                progressRegisterDialog.dismiss();
                progressLocalRegisterDialog.dismiss();
                Log.i(REGISTER_DETAIL_ACTIVITY, StaticMessages.EXCEPTION.getName());
                Toast.makeText(RegisterDetailActivity.this, StaticMessages.EXCEPTION.getName(), Toast.LENGTH_LONG).show();
            }
        }

        @Override
        protected void onCancelled() {
            progressRegisterDialog.dismiss();
            mRegisterDetailsTask = null;
        }
    }
}
