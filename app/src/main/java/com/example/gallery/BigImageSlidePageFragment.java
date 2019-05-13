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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;
import java.io.ByteArrayOutputStream;
import java.io.IOException;


import static com.example.gallery.RescaleImage.decodeSampledBitmapFromFile;

public class BigImageSlidePageFragment extends Fragment {

    private ImageItem imageItem;

    private ImageView image;
    private Button setWallpaper;
    private Button share;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_slide_page_big_image, container, false);

        imageItem = getArguments().getParcelable("imageItem");

        image = view.findViewById(R.id.image);
        image.setScaleType(ImageView.ScaleType.FIT_CENTER);
        image.setImageBitmap(decodeSampledBitmapFromFile(imageItem.getPath(), 700, 700));

        //action for click on share button
        share = view.findViewById(R.id.buttonShare);
        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shareImage(imageItem.getPath());
            }
        });

        //action for click on setWallpaper button
        setWallpaper = view.findViewById(R.id.buttonWallpaper);
        setWallpaper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setWallpaper(imageItem.getPath());
            }
        });

        return view;
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
