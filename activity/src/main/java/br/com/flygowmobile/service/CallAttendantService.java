package br.com.flygowmobile.service;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.conn.HttpHostConnectException;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.util.Arrays;

import br.com.flygowmobile.Utils.App;
import br.com.flygowmobile.database.RepositoryTablet;
import br.com.flygowmobile.entity.Tablet;
import br.com.flygowmobile.enums.AlertMessageTypeEnum;
import br.com.flygowmobile.enums.ServerController;
import br.com.flygowmobile.enums.StaticMessages;
import br.com.flygowmobile.enums.StaticTitles;

/**
 * Created by Tiago Rocha Gomes on 29/09/14.
 */
public class CallAttendantService {
    private Activity activity;
    private ProgressDialog callAttendantDialog;
    private RepositoryTablet repositoryTablet;

    private static final String CALL_ATTENDANT_SERVICE = "CallAttendantService";

    public CallAttendantService(Activity activity){
        this.activity = activity;
        this.repositoryTablet = new RepositoryTablet(activity);
    }

    public void callAttendant(){
        callAttendantDialog = ProgressDialog.show(activity, StaticTitles.LOAD.getName(),
                StaticMessages.CALLING_ATTENDANT.getName(), true);
        CallAttendantTask callAttendantTask = new CallAttendantTask();
        callAttendantTask.execute((Void) null);
    }

    private class CallAttendantTask extends AsyncTask<Void, Void, String> {

        App serverAddressObj = (App) activity.getApplication();
        String url = serverAddressObj.getServerUrl(ServerController.CALL_ATTENDANT);

        @Override
        protected String doInBackground(Void... params) {
            try {
                Tablet tablet = repositoryTablet.findLast();
                Integer tabletNumber = tablet.getNumber();
                Long attendantId = tablet.getAttendantId();
                Long alertTypeId = AlertMessageTypeEnum.TO_ATTENDANCE.getId();

                NameValuePair tabletIdPair = new BasicNameValuePair("tabletNumber", String.valueOf(tabletNumber));
                NameValuePair attendantIdPair = new BasicNameValuePair("attendantId", String.valueOf(attendantId));
                NameValuePair alertTypeIdPair = new BasicNameValuePair("alertTypeId", String.valueOf(alertTypeId));

                return ServiceHandler.makeServiceCall(url, ServiceHandler.POST,
                        Arrays.asList(tabletIdPair, attendantIdPair, alertTypeIdPair));
            } catch (HttpHostConnectException ex) {
                callAttendantDialog.dismiss();
                Log.i(CALL_ATTENDANT_SERVICE, StaticMessages.TIMEOUT.getName());
            } catch (Exception e) {
                callAttendantDialog.dismiss();
                Log.i(CALL_ATTENDANT_SERVICE, StaticMessages.NOT_SERVICE.getName());
            }
            return "";
        }

        @Override
        protected void onPostExecute(final String response) {

            Log.i(CALL_ATTENDANT_SERVICE, "Call Attendant Service: " + response);
            try {
                JSONObject jsonObject = new JSONObject(response);
                Boolean success = jsonObject.getBoolean("success");
                if (success) {
                    callAttendantDialog.dismiss();
                    Toast.makeText(activity, StaticMessages.CALL_ATTENDANT.getName(), Toast.LENGTH_LONG).show();
                } else {
                    callAttendantDialog.dismiss();
                    Toast.makeText(activity, StaticMessages.CALL_ATTENDANT_ERROR.getName(), Toast.LENGTH_LONG).show();
                }
            } catch (Exception e) {
                callAttendantDialog.dismiss();
                Log.i(CALL_ATTENDANT_SERVICE, StaticMessages.EXCEPTION.getName(), e);
                Toast.makeText(activity, StaticMessages.EXCEPTION.getName(), Toast.LENGTH_LONG).show();
            }
        }

        @Override
        protected void onCancelled() {
            callAttendantDialog.dismiss();
        }
    }
}
