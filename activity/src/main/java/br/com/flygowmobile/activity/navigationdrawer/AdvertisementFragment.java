package br.com.flygowmobile.activity.navigationdrawer;

import android.app.Fragment;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ViewFlipper;

import java.io.ByteArrayInputStream;

import java.util.List;



import br.com.flygowmobile.activity.R;

import br.com.flygowmobile.database.RepositoryAdvertisement;
import br.com.flygowmobile.entity.Advertisement;

public class AdvertisementFragment extends Fragment {

    private ViewFlipper viewFlipper;
    private RepositoryAdvertisement repositoryAdvertisement;

    private static final String ADVERTISEMENT_FRAGMENT_ACTIVITY = "AdvertisementFragment";

    @Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        viewFlipper = (ViewFlipper)inflater.inflate(R.layout.advertisement_fragment, container, false);
        repositoryAdvertisement = new RepositoryAdvertisement(getActivity());
        List<Advertisement> allAdvertisements = repositoryAdvertisement.listAll();
        defineAdvertisementViews(viewFlipper, allAdvertisements);
        if(allAdvertisements.size() > 1){
            viewFlipper.startFlipping();
        }
        return viewFlipper;
    }

    private void defineAdvertisementViews(ViewFlipper viewFlipper, List<Advertisement> allAdvertisements){
        if(viewFlipper != null && allAdvertisements != null && !allAdvertisements.isEmpty()){
            for(Advertisement advertisement : allAdvertisements){
                ImageView imgview = new ImageView(getActivity());
                if(advertisement.getVideoName() != null && !advertisement.getVideoName().equals("")){
                    //TODO:: Implements video on flipper
                }else {
                    byte[] photo = advertisement.getPhoto();
                    if (photo != null && photo.length > 0) {
                        ByteArrayInputStream imageStream = new ByteArrayInputStream(photo);
                        Bitmap theImage = BitmapFactory.decodeStream(imageStream);
                        imgview.setImageBitmap(theImage);
                        viewFlipper.addView(imgview);
                    }
                }
            }
        }
    }
}