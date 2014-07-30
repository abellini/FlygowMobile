package br.com.flygowmobile.Utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.text.Html;
import android.util.Log;

import br.com.flygowmobile.activity.R;
import br.com.flygowmobile.enums.StaticMessages;
import br.com.flygowmobile.enums.StaticTitles;

/**
 * Created by Tiago Rocha Gomes on 13/05/14.
 */
public class FlygowAlertDialog {

    private final static String TEXT_OK_BTN = "OK";
    private final static String FLYGOW_ALERT_DIALOG = "FlygowAlertDialog";

    public static void createWarningPopupWithIntent(final Activity activity, StaticTitles title, StaticMessages message, final Intent intent){
        final AlertDialog alertDialog = new AlertDialog.Builder(activity).create();
        alertDialog.setTitle(title.getName());
        alertDialog.setMessage(message.getName());
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, TEXT_OK_BTN, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Log.i(FLYGOW_ALERT_DIALOG, "CLICK OK DIALOG");
                alertDialog.dismiss();
                if(intent != null){
                    activity.startActivity(intent);
                    activity.finish();
                }
            }
        });
        // Set the Icon for the Dialog
        alertDialog.setIcon(R.drawable.ic_dialog_warning);
        alertDialog.show();
    }

    public static void createWarningPopup(final Activity activity, StaticTitles title, StaticMessages message){
        final AlertDialog alertDialog = new AlertDialog.Builder(activity).create();
        alertDialog.setTitle(title.getName());
        alertDialog.setMessage(message.getName());

        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, TEXT_OK_BTN, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                alertDialog.dismiss();
            }
        });
        // Set the Icon for the Dialog
        alertDialog.setIcon(R.drawable.ic_dialog_warning);
        alertDialog.show();
    }

    public static void createInfoPopup(final Activity activity, StaticTitles title, String message){
        final AlertDialog alertDialog = new AlertDialog.Builder(activity).create();
        alertDialog.setTitle(title.getName());
        alertDialog.setMessage(Html.fromHtml(message));

        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, TEXT_OK_BTN, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                alertDialog.dismiss();
            }
        });
        // Set the Icon for the Dialog
        alertDialog.setIcon(R.drawable.ic_dialog_info);
        alertDialog.show();
    }
}
