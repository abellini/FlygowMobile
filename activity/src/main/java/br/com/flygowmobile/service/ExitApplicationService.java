package br.com.flygowmobile.service;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.text.Editable;
import android.text.InputType;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.widget.EditText;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Arrays;

import br.com.flygowmobile.Utils.App;
import br.com.flygowmobile.Utils.FlygowAlertDialog;
import br.com.flygowmobile.activity.R;
import br.com.flygowmobile.database.RepositoryTablet;
import br.com.flygowmobile.entity.Tablet;
import br.com.flygowmobile.enums.ServerController;
import br.com.flygowmobile.enums.StaticMessages;
import br.com.flygowmobile.enums.StaticTitles;
import br.com.flygowmobile.enums.TabletStatusEnum;

/**
 * Created by Tiago Rocha Gomes on 13/09/14.
 */
public class ExitApplicationService {

    private final static String PASS_SECURITY = "flygonow";

    private Activity activity;
    private TabletStatusService tabletStatusService;

    private RepositoryTablet repositoryTablet;

    private static final String EXIT_APPLICATION_SERVICE = "ExitApplicationService";

    public ExitApplicationService(Activity activity){
        this.activity = activity;
        this.tabletStatusService = new TabletStatusService(activity);
        this.repositoryTablet = new RepositoryTablet(activity);
    }

    public void onExitApplication(){
        showExitDialog();
    }

    public void refreshOrdersInServerOnExitApplication(){
        ExitApplicationTask exitApplicationTask = new ExitApplicationTask(repositoryTablet.findLast());
        exitApplicationTask.execute((Void) null);
    }

    private void showExitDialog(){
        AlertDialog dialog = null;
        final AlertDialog.Builder alert = new AlertDialog.Builder(activity);

        alert.setTitle(StaticTitles.EXIT_APPLICATION.getName());
        alert.setMessage(StaticMessages.EXIT_APP.getName());

        final EditText input = new EditText(activity);
        input.setTransformationMethod(PasswordTransformationMethod.getInstance());
        alert.setView(input);
        alert.setIcon(R.drawable.security);
        alert.setPositiveButton(StaticTitles.OK.getName(), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                String value = input.getText().toString();
                if(PASS_SECURITY.equals(value)){
                    tabletStatusService.changeTabletStatus(TabletStatusEnum.UNAVALIABLE, true);
                    refreshOrdersInServerOnExitApplication();
                    dialog.dismiss();
                    activity.finish();
                }else{
                    FlygowAlertDialog.createWarningPopup(activity, StaticTitles.WARNING, StaticMessages.INVALID_PASSWORD);
                }
            }
        });
        alert.setNegativeButton(StaticTitles.CANCEL.getName(), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                dialog.dismiss();
            }
        });
        dialog = alert.create();
        dialog.show();
    }

    private class ExitApplicationTask extends AsyncTask<Void, Void, String> {

        private App serverAddressObj = (App) activity.getApplication();
        private Tablet tablet;

        public ExitApplicationTask(Tablet tablet) {
            this.tablet = tablet;
        }

        @Override
        protected String doInBackground(Void... params) {
            String serverUrl = serverAddressObj.getServerUrl(ServerController.EXIT_APPLICATION);

            NameValuePair tabletPair = new BasicNameValuePair("tabletNumber", tablet.getNumber()+"");
            try {
                return ServiceHandler.makeServiceCall(serverUrl, ServiceHandler.POST, Arrays.asList(tabletPair));
            } catch (IOException e) {
                Log.e(EXIT_APPLICATION_SERVICE, e.getMessage(), e);
            }
            return null;
        }

        @Override
        protected void onPostExecute(String response) {
            JSONObject jsonObject = null;
            try {
                jsonObject = new JSONObject(response);
                if (jsonObject.getBoolean("success")) {
                    Log.i(EXIT_APPLICATION_SERVICE, "Application finished with success");
                }
            } catch (JSONException e) {
                Log.e(EXIT_APPLICATION_SERVICE, e.getMessage(), e);
            }
            activity.finish();
        }
    }
}
