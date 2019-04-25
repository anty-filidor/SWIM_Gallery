package com.example.gallery;


import android.app.WallpaperManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import static com.example.gallery.RescaleImage.decodeSampledBitmapFromFile;

public class BigImageFragment extends Fragment {

    private TextView title;
    private ImageView image;
    private Button setWallpaper;
    private Button share;


    public BigImageFragment() {}


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_big_image, container, false);


        image = view.findViewById(R.id.image);
        image.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        image.setImageBitmap(decodeSampledBitmapFromFile(getArguments().getString("image"), 700, 700));

        share = view.findViewById(R.id.buttonShare);
        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shareImage(getArguments().getString("image"));
            }
        });



        setWallpaper = view.findViewById(R.id.buttonWallpaper);
        setWallpaper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setWallpaper(getArguments().getString("image"));
            }
        });


        //create toolbar
        setHasOptionsMenu(true);
        Toolbar toolbar = (Toolbar)view.findViewById(R.id.toolbar1);
        if(toolbar != null) {
            ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
            ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(getArguments().getString("title"));
        }
        return view;
    }


    //this method creates toolbar menu
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.manu_image_details, menu);
        inflater.inflate(R.menu.menu_app_info, menu);
    }


    //this method sets choosen image as wallpaper
    private void setWallpaper(String path){
        final WallpaperManager wallpaperManager = WallpaperManager.getInstance(getContext());
        try {
            wallpaperManager.setBitmap(BitmapFactory.decodeFile(path));
            Toast.makeText(getContext(), "Set wallpaper", Toast.LENGTH_SHORT).show();
        }
        catch (IOException e) {}
    }


    //this method returns image uri to share
    private Uri getImageUri(Context inContext, Bitmap inImage, String title) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, title, null);
        return Uri.parse(path);
    }


    //this method calls share action to share choosen image
    private void shareImage(String path){
        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        Uri image = getImageUri(getContext(), BitmapFactory.decodeFile(path), path);

        sharingIntent.setAction(Intent.ACTION_SEND);
        sharingIntent.putExtra(Intent.EXTRA_STREAM, image);
        sharingIntent.setType("image/jpeg");

        getContext().startActivity(Intent.createChooser(sharingIntent, "Share via"));
    }

}
