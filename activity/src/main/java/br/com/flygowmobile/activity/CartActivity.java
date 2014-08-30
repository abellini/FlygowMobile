package br.com.flygowmobile.activity;

import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.com.flygowmobile.activity.navigationdrawer.OrderAdapter;
import br.com.flygowmobile.activity.navigationdrawer.OrderRowItem;
import br.com.flygowmobile.service.OrderService;

/**
 * Created by Tiago Rocha Gomes on 30/08/14.
 */
public class CartActivity extends Activity {

    private static final String CART_ACTIVITY = "CartActivity";

    private ViewGroup header;
    private ViewGroup footer;
    private View cartView;
    private OrderService orderService;
    private Map<Long, CheckBox> selects = new HashMap<Long, CheckBox>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater inflater = LayoutInflater.from(this);

        this.cartView = inflater.inflate(R.layout.cart_list_orders, null);
        setContentView(cartView);

        this.header = (ViewGroup) inflater.inflate(R.layout.header_order, null, false);
        this.footer = (ViewGroup) inflater.inflate(R.layout.footer_order, null, false);
        this.orderService = new OrderService(this);

        defineFonts();
        fillCartOrders();
    }

    protected void defineFonts() {
        // Font path
        String fontGabriolaPath = "fonts/GABRIOLA.TTF";
        String fontErasBoldPath = "fonts/ERASBD.TTF";
        String fontErasMediumPath = "fonts/ERASMD.TTF";

        Typeface gabriola = Typeface.createFromAsset(getAssets(), fontGabriolaPath);

        //Header
        TextView txtCheckbox = (TextView) this.header.findViewById(R.id.lblCheckbox);
        TextView txtDescription = (TextView) this.header.findViewById(R.id.lblDescriptionOrder);
        TextView txtQtde = (TextView) this.header.findViewById(R.id.lblQtdTitle);
        TextView txtPriceUnit = (TextView) this.header.findViewById(R.id.lblPriceUnit);
        TextView txtPriceTotal = (TextView) this.header.findViewById(R.id.lblPriceTotal);
        txtCheckbox.setTypeface(gabriola);
        txtDescription.setTypeface(gabriola);
        txtQtde.setTypeface(gabriola);
        txtPriceUnit.setTypeface(gabriola);
        txtPriceTotal.setTypeface(gabriola);

        // Footer
        TextView txtTotal = (TextView) this.footer.findViewById(R.id.vlTotal);
        TextView txtVlTotal = (TextView) this.footer.findViewById(R.id.lbTotalPedido);
        txtTotal.setTypeface(gabriola);
        txtVlTotal.setTypeface(gabriola);

    }

    private void fillCartOrders() {
        ListView listView = (ListView) cartView.findViewById(R.id.cartList);

        listView.addHeaderView(this.header);
        listView.addFooterView(this.footer);

        List<OrderRowItem> orderItemRow = this.orderService.populateOrderItemList();
        listView.setAdapter(new OrderAdapter(this, orderItemRow, selects));

        TextView txtTotal = (TextView) footer.findViewById(R.id.vlTotal);
        txtTotal.setText(this.orderService.getFormatedTotalValue());

        Button mRegisterButton = (Button) footer.findViewById(R.id.btnEnviarPedido);
        mRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                orderService.sendOrderToServer();
            }
        });
    }
}
