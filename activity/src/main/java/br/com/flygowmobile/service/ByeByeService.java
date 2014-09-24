package br.com.flygowmobile.service;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.text.method.PasswordTransformationMethod;
import android.widget.EditText;

import br.com.flygowmobile.Utils.FlygowAlertDialog;
import br.com.flygowmobile.activity.MainActivity;
import br.com.flygowmobile.activity.R;
import br.com.flygowmobile.enums.StaticMessages;
import br.com.flygowmobile.enums.StaticTitles;


/**
 * Created by Tiago Rocha Gomes on 20/09/14.
 */
public class ByeByeService {
    private Activity activity;

    private static final String PASS_SECURITY = "flygonow";

    public ByeByeService (Activity activity){
        this.activity = activity;
    }

    public void onNewAttendance(){
        showNewAttendanceDialog();
    }

    private void showNewAttendanceDialog(){
        AlertDialog dialog = null;
        final AlertDialog.Builder alert = new AlertDialog.Builder(activity);

        alert.setTitle(StaticTitles.NEW_ATTENDANCE.getName());
        alert.setMessage(StaticMessages.NEW_ATTENDANCE.getName());

        final EditText input = new EditText(activity);
        input.setTransformationMethod(PasswordTransformationMethod.getInstance());
        alert.setView(input);
        alert.setIcon(R.drawable.security);
        alert.setPositiveButton(StaticTitles.OK.getName(), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                String value = input.getText().toString();
                if(PASS_SECURITY.equals(value)){
                    dialog.dismiss();
                    Intent it = new Intent(activity, MainActivity.class);
                    activity.startActivity(it);
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
}
