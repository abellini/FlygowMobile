package br.com.flygowmobile.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;

import br.com.flygowmobile.service.ByeByeService;
import br.com.flygowmobile.service.EndOrderService;

/**
 * Created by Tiago Rocha Gomes on 20/09/14.
 */
public class ByeByeActivity extends Activity {

    private View byeByeView;
    private EndOrderService endOrderService;
    private ByeByeService byeByeService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater inflater = LayoutInflater.from(this);

        this.byeByeView = inflater.inflate(R.layout.byebye_layout, null);
        setContentView(byeByeView);

        this.endOrderService = new EndOrderService(this);
        this.byeByeService = new ByeByeService(this);

        defineNewAttendanceButton();
        endOrderService.resetTabletOrder(false);
    }

    private void defineNewAttendanceButton(){
        ImageButton btnNewAttendance = (ImageButton)byeByeView.findViewById(R.id.btnNewAttendance);
        btnNewAttendance.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                byeByeService.onNewAttendance();
            }
        });
    }
}