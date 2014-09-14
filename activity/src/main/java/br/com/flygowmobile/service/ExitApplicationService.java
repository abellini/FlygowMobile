package br.com.flygowmobile.service;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.text.Editable;
import android.text.InputType;
import android.text.method.PasswordTransformationMethod;
import android.widget.EditText;

import br.com.flygowmobile.Utils.FlygowAlertDialog;
import br.com.flygowmobile.activity.R;
import br.com.flygowmobile.enums.StaticMessages;
import br.com.flygowmobile.enums.StaticTitles;

/**
 * Created by Tiago Rocha Gomes on 13/09/14.
 */
public class ExitApplicationService {

    private final static String PASS_SECURITY = "flygonow";

    private Activity activity;

    public ExitApplicationService(Activity activity){
        this.activity = activity;
    }

    public void onExitApplication(){
        showExitDialog();
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
}
