package br.com.flygowmobile.activity;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.com.flygowmobile.Utils.FontUtils;
import br.com.flygowmobile.activity.navigationdrawer.OrderAdapter;
import br.com.flygowmobile.activity.navigationdrawer.OrderRowItem;
import br.com.flygowmobile.enums.FontTypeEnum;
import br.com.flygowmobile.enums.StaticMessages;
import br.com.flygowmobile.enums.StaticTitles;
import br.com.flygowmobile.service.BuildCartActionBarService;
import br.com.flygowmobile.service.CartButtonActionsService;
import br.com.flygowmobile.service.OrderService;

/**
 * Created by Tiago Rocha Gomes on 30/08/14.
 */
public class CartActivity extends Activity {

    private ViewGroup header;
    private ViewGroup footer;
    private View cartView;
    private OrderService orderService;
    private CartButtonActionsService cartButtonActionsService;
    private BuildCartActionBarService cartActionBarService;
    private Map<Long, CheckBox> selects = new HashMap<Long, CheckBox>();
    private FontUtils fontUtils;
    private static final String CART_ACTIVITY = "CartActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater inflater = LayoutInflater.from(this);

        try{
            boolean saved = this.getIntent().getExtras().getBoolean("saved");
            if(saved){
                Toast.makeText(this, StaticMessages.SUCCESS_SAVE_IN_SERVER.getName(), Toast.LENGTH_LONG).show();
            }
        }catch(Exception e){
            Log.i(CART_ACTIVITY, "First Time");
        }


        this.cartView = inflater.inflate(R.layout.cart_list_orders, null);
        setContentView(cartView);

        this.header = (ViewGroup) inflater.inflate(R.layout.header_order, null, false);
        this.footer = (ViewGroup) inflater.inflate(R.layout.footer_order, null, false);
        this.orderService = new OrderService(this);
        this.cartButtonActionsService = new CartButtonActionsService(this, this.cartView);
        this.fontUtils = new FontUtils(this);

        //Action Bar
        LayoutInflater mInflater = LayoutInflater.from(this);
        View actionBarView = mInflater.inflate(R.layout.cart_action_bar, null);
        cartActionBarService = new BuildCartActionBarService(this, actionBarView);
        cartActionBarService.buildActionBar();

        defineTexts();
        defineFonts();
        fillCartOrders();
        cartButtonActionsService.defineCartButtons();
    }

    public Map<Long, CheckBox> getCheckedItems() {
        return selects;
    }

    private void defineTexts() {
        TextView cartTitle = (TextView) this.cartView.findViewById(R.id.cartTitle);
        TextView cartSubTitle = (TextView) this.cartView.findViewById(R.id.cartSubTitle);

        TextView txtDescription = (TextView) this.header.findViewById(R.id.lblDescriptionOrder);
        TextView txtQtde = (TextView) this.header.findViewById(R.id.lblQtdTitle);
        TextView txtAccompaniments = (TextView) this.header.findViewById(R.id.lblAccompaniments);
        TextView txtPriceUnit = (TextView) this.header.findViewById(R.id.lblPriceUnit);
        TextView txtPriceTotal = (TextView) this.header.findViewById(R.id.lblPriceTotal);

        cartTitle.setText(StaticTitles.CART_TITLE.getName());
        cartSubTitle.setText(StaticTitles.CART_SUBTITLE.getName());

        txtDescription.setText(StaticTitles.CART_HEADER_DESCRIPTION.getName());
        txtQtde.setText(StaticTitles.CART_HEADER_QUANTITY.getName());
        txtAccompaniments.setText(StaticTitles.CART_HEADER_ACCOMPANIMENTS.getName());
        txtPriceUnit.setText(StaticTitles.CART_HEADER_UNIT_VALUE.getName());
        txtPriceTotal.setText(StaticTitles.CART_HEADER_TOTAL_VALUE.getName());

    }

    private void defineFonts() {
        //Title
        TextView cartTitle = (TextView) this.cartView.findViewById(R.id.cartTitle);
        TextView cartSubTitle = (TextView) this.cartView.findViewById(R.id.cartSubTitle);

        //Header
        TextView txtDescription = (TextView) this.header.findViewById(R.id.lblDescriptionOrder);
        TextView txtQtde = (TextView) this.header.findViewById(R.id.lblQtdTitle);
        TextView txtAccompaniments = (TextView) this.header.findViewById(R.id.lblAccompaniments);
        TextView txtPriceUnit = (TextView) this.header.findViewById(R.id.lblPriceUnit);
        TextView txtPriceTotal = (TextView) this.header.findViewById(R.id.lblPriceTotal);

        // Footer
        TextView txtVlTotal = (TextView) this.footer.findViewById(R.id.lbTotalPedido);

        fontUtils.applyFont(FontTypeEnum.LOGOTYPE,
                cartTitle,
                txtDescription,
                txtQtde,
                txtAccompaniments,
                txtPriceUnit,
                txtPriceTotal,
                txtVlTotal
        );
        fontUtils.applyFont(FontTypeEnum.GENERAL_DESCRIPTIONS, cartSubTitle);
    }

    private void fillCartOrders() {
        ListView listView = (ListView) cartView.findViewById(R.id.cartList);

        listView.addHeaderView(this.header);
        listView.addFooterView(this.footer);

        List<OrderRowItem> orderItemRow = this.orderService.populateOrderItemList();
        listView.setAdapter(new OrderAdapter(this, orderItemRow, selects));

        TextView txtTotal = (TextView) footer.findViewById(R.id.lbTotalPedido);
        txtTotal.setText(StaticMessages.CART_TOTAL_VALUE.getName() + " " + this.orderService.getFormatedTotalValue());
    }

}
