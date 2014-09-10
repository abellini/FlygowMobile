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
        String fontChillerPath = "fonts/CHILLER.TTF";
        Typeface chiller = Typeface.createFromAsset(getAssets(), fontChillerPath);

        String fontErasPath = "fonts/ERASMD.TTF";
        Typeface eras = Typeface.createFromAsset(getAssets(), fontErasPath);

        //Title
        TextView cartTitle = (TextView) this.cartView.findViewById(R.id.cartTitle);
        TextView cartSubTitle = (TextView) this.cartView.findViewById(R.id.cartSubTitle);
        cartTitle.setTypeface(chiller);
        cartSubTitle.setTypeface(eras);

        //Header
        TextView txtDescription = (TextView) this.header.findViewById(R.id.lblDescriptionOrder);
        TextView txtQtde = (TextView) this.header.findViewById(R.id.lblQtdTitle);
        TextView txtAccompaniments = (TextView) this.header.findViewById(R.id.lblAccompaniments);
        TextView txtPriceUnit = (TextView) this.header.findViewById(R.id.lblPriceUnit);
        TextView txtPriceTotal = (TextView) this.header.findViewById(R.id.lblPriceTotal);
        txtDescription.setTypeface(chiller);
        txtQtde.setTypeface(chiller);
        txtAccompaniments.setTypeface(chiller);
        txtPriceUnit.setTypeface(chiller);
        txtPriceTotal.setTypeface(chiller);

        // Footer
        TextView txtTotal = (TextView) this.footer.findViewById(R.id.vlTotal);
        TextView txtVlTotal = (TextView) this.footer.findViewById(R.id.lbTotalPedido);
        txtTotal.setTypeface(chiller);
        txtVlTotal.setTypeface(chiller);

    }

    private void fillCartOrders() {
        ListView listView = (ListView) cartView.findViewById(R.id.cartList);

        listView.addHeaderView(this.header);
        listView.addFooterView(this.footer);

        List<OrderRowItem> orderItemRow = this.orderService.populateOrderItemList();
        listView.setAdapter(new OrderAdapter(this, orderItemRow, selects));

        TextView txtTotal = (TextView) footer.findViewById(R.id.vlTotal);
        txtTotal.setText(this.orderService.getFormatedTotalValue());

        Button mRegisterButton = (Button) cartView.findViewById(R.id.btnEnviarPedido);
        mRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                orderService.sendOrderToServer();
            }
        });
    }
}
