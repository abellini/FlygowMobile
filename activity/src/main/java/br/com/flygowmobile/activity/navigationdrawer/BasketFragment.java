package br.com.flygowmobile.activity.navigationdrawer;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import br.com.flygowmobile.activity.R;

public class BasketFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.basket_fragment, container, false);
        return rootView;
    }
}