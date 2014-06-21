package br.com.flygowmobile.activity.navigationdrawer;

import android.app.Fragment;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.VideoView;
import android.widget.ViewFlipper;

import java.io.ByteArrayInputStream;

import java.io.IOException;
import java.util.List;


import br.com.flygowmobile.Utils.VideoUtils;
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

        viewFlipper.getInAnimation().setAnimationListener(new Animation.AnimationListener() {
            public void onAnimationStart(Animation animation) {
                if (viewFlipper.getCurrentView() instanceof VideoView) {
                    Log.i(ADVERTISEMENT_FRAGMENT_ACTIVITY, "START VIDEO");
                    viewFlipper.stopFlipping();
                    final VideoView videoView = (VideoView) viewFlipper.getCurrentView();
                    videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                        @Override
                        public void onCompletion(MediaPlayer mp) {
                            Log.i(ADVERTISEMENT_FRAGMENT_ACTIVITY, "COMPLETE VIDEO");
                            viewFlipper.startFlipping();
                            viewFlipper.showNext();
                        }
                    });
                    videoView.start();
                }
            }

            public void onAnimationRepeat(Animation animation) {}
            public void onAnimationEnd(Animation animation) {/*
                if (viewFlipper.getCurrentView() instanceof ImageView) {
                    Log.i(ADVERTISEMENT_FRAGMENT_ACTIVITY, "START IMAGE");
                    if(!viewFlipper.isFlipping()){
                        Log.i(ADVERTISEMENT_FRAGMENT_ACTIVITY, "NOT FLIPPING");
                        viewFlipper.startFlipping();
                    }
                }*/
            }
        });

        if(allAdvertisements.size() > 1){
            viewFlipper.startFlipping();
        }

        //Tenta executar caso a primeira view seja do tipo vídeo. Se não, cai no catch e continua o flipper
        try{
            final VideoView videoView = (VideoView) viewFlipper.getCurrentView();
            viewFlipper.stopFlipping();
            videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    Log.i(ADVERTISEMENT_FRAGMENT_ACTIVITY, "COMPLETE VIDEO");
                    viewFlipper.startFlipping();
                    viewFlipper.showNext();
                }
            });
            videoView.start();
        }catch (Exception e){
            Log.w(ADVERTISEMENT_FRAGMENT_ACTIVITY, "First advertisements element is not a video!");
        }

        return viewFlipper;
    }


    private void defineAdvertisementViews(ViewFlipper viewFlipper, List<Advertisement> allAdvertisements){
        if(viewFlipper != null && allAdvertisements != null && !allAdvertisements.isEmpty()){
            for(Advertisement advertisement : allAdvertisements){
                ImageView imgview = new ImageView(getActivity());
                VideoView videoView = new VideoView(getActivity());
                if(advertisement.getVideoName() != null && !advertisement.getVideoName().equals("")){
                    try {
                        String videoPath = VideoUtils.getVideo(getActivity().getApplicationContext(), advertisement.getVideoName());
                        videoView.setVideoPath(videoPath);
                        viewFlipper.addView(videoView);
                    } catch (IOException e) {
                        e.printStackTrace();
                        //Contingência caso de erro ao resgatar o vídeo, tenta puxar a foto
                        byte[] photo = advertisement.getPhoto();
                        if (photo != null && photo.length > 0) {
                            ByteArrayInputStream imageStream = new ByteArrayInputStream(photo);
                            Bitmap theImage = BitmapFactory.decodeStream(imageStream);
                            imgview.setImageBitmap(theImage);
                            viewFlipper.addView(imgview);
                        }
                    }
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