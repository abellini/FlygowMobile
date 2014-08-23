package br.com.flygowmobile.activity.navigationdrawer;

import android.app.ListFragment;
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

import br.com.flygowmobile.activity.R;
import br.com.flygowmobile.service.OrderService;

public class BasketFragment extends ListFragment {

    private ViewGroup header;
    private ViewGroup footer;
    private OrderService orderService;
    private Map<Long, CheckBox> selects = new HashMap<Long, CheckBox>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View rootView = inflater.inflate(R.layout.list_basket_fragment, container, false);

        this.header = (ViewGroup) inflater.inflate(R.layout.header_order, null, false);
        this.footer = (ViewGroup) inflater.inflate(R.layout.footer_order, null, false);

        this.orderService = new OrderService(getActivity());

        defineFonts(rootView);

        return rootView;
    }

    protected void defineFonts(View rootView) {
        // Font path
        String fontGabriolaPath = "fonts/GABRIOLA.TTF";
        String fontErasBoldPath = "fonts/ERASBD.TTF";
        String fontErasMediumPath = "fonts/ERASMD.TTF";

        Typeface gabriola = Typeface.createFromAsset(getActivity().getAssets(), fontGabriolaPath);

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

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        ListView listView = (ListView) getListView();

        listView.addHeaderView(this.header);
        listView.addFooterView(this.footer);

        List<OrderRowItem> orderItemRow = this.orderService.populateOrderItemList();
        setListAdapter(new OrderAdapter(getActivity(), orderItemRow, selects));

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