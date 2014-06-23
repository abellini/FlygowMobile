package br.com.flygowmobile.activity.navigationdrawer;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import br.com.flygowmobile.activity.R;

public class FB_Fragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fb_fragment, container, false);

        //Bundle args = getArguments().getBundle("item");
        //RowItem item = (RowItem)args.getSerializable("item");
        //Toast.makeText(getActivity(), item.getTitle().toString(), Toast.LENGTH_LONG).show();
        Toast.makeText(getActivity(), "onCreateView", Toast.LENGTH_LONG).show();
        return rootView;
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle args = getArguments();
        if (args == null) {
            Toast.makeText(getActivity(), "arguments is null ", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(getActivity(), "text " + args, Toast.LENGTH_LONG).show();
        }

    }


}
