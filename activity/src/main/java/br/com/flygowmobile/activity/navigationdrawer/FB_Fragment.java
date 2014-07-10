package br.com.flygowmobile.activity.navigationdrawer;

import android.app.Fragment;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayInputStream;

import br.com.flygowmobile.activity.R;
import br.com.flygowmobile.database.RepositoryFood;
import br.com.flygowmobile.entity.Food;

public class FB_Fragment extends Fragment {

    private RepositoryFood repositoryFood;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Toast.makeText(getActivity(), "onCreateView", Toast.LENGTH_SHORT).show();

        View rootView = inflater.inflate(R.layout.fb_fragment, container, false);

        repositoryFood = new RepositoryFood(getActivity());

        return rootView;
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }


}
