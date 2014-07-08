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


        TextView lblDescription = (TextView) rootView.findViewById(R.id.lblDescription);
        TextView lblName = (TextView) rootView.findViewById(R.id.lblName);
        TextView lblNutritional = (TextView) rootView.findViewById(R.id.lblNutritional);
        ImageView imgFood = (ImageView) rootView.findViewById(R.id.imgFood);


        Bundle args = getArguments();
        if (args != null) {

            RowItem item = (RowItem) args.getSerializable("item");

            Food food = repositoryFood.findById(item.getId());
            lblName.setText(food.getName());
            lblDescription.setText(food.getDescription());
            lblNutritional.setText(food.getNutritionalInfo());
            byte[] photo = food.getPhoto();
            if (photo != null && photo.length > 0) {
                ByteArrayInputStream imageStream = new ByteArrayInputStream(photo);
                Bitmap theImage = BitmapFactory.decodeStream(imageStream);
                imgFood.setImageBitmap(theImage);
            }
        }

        return rootView;
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }


}
