package br.com.flygowmobile.service;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.widget.ListView;

import java.util.List;

import br.com.flygowmobile.Utils.FlygowAlertDialog;
import br.com.flygowmobile.activity.navigationdrawer.AccompanimentInfoAdapter;
import br.com.flygowmobile.database.RepositoryOrderItemAccompaniment;
import br.com.flygowmobile.entity.Accompaniment;
import br.com.flygowmobile.enums.StaticMessages;
import br.com.flygowmobile.enums.StaticTitles;
import br.com.flygowmobile.mapper.AccompanimentMapper;

/**
 * Created by Tiago Rocha Gomes on 14/09/14.
 */
public class BuildAccompanimentInfoService {

    private Activity activity;
    private RepositoryOrderItemAccompaniment repositoryOrderItemAccompaniment;
    private AccompanimentMapper accompanimentMapper;

    public BuildAccompanimentInfoService(Activity activity){
        this.activity = activity;
        this.repositoryOrderItemAccompaniment = new RepositoryOrderItemAccompaniment(activity);
        this.accompanimentMapper = new AccompanimentMapper(activity);
    }

    public void onAccompanimentInfoClick(long orderItemId){
        List<Accompaniment> accompanimentList = repositoryOrderItemAccompaniment.findByOrderItemId(orderItemId);
        if(accompanimentList != null && !accompanimentList.isEmpty()){
            AlertDialog dialog = null;
            AlertDialog.Builder builder = new AlertDialog.Builder(activity);
            String popupTitle = StaticTitles.ACCOMPANIMENT_INFO_POPUP.getName();
            builder.setTitle(popupTitle);
            builder.setNeutralButton(StaticTitles.OK.getName(), new
                    DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
            final ListView list = new ListView(activity);
            list.setAdapter(
                    new AccompanimentInfoAdapter(
                            activity,
                            accompanimentMapper.accompanimentToRowItem(accompanimentList)
                    )
            );
            builder.setView(list);
            dialog = builder.create();
            dialog.show();
        }else{
            FlygowAlertDialog.createInfoPopup(activity, StaticTitles.INFORMATION, StaticMessages.EMPTY_ACCOMPANIMENTS);
        }
    }
}
