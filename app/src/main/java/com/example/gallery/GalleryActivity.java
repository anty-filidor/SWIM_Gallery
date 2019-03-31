package com.example.gallery;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.Toast;

import android.app.WallpaperManager;


public class GalleryActivity extends AppCompatActivity {

    private ListView listView;
    private GridView gridView;
    private GalleryViewAdapter Adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        //messages from start activity. They determine layout and onclickitem listener
        Intent fromStartActivity = getIntent();
        final String intentMode = fromStartActivity.getStringExtra("intentChoice");
        String layoutMode = fromStartActivity.getStringExtra("layoutChoice");
        System.out.println("Message: " + layoutMode);


        if(layoutMode.equals("List")) {
            System.out.println("in list block");
            setContentView(R.layout.activity_gallery_list);
            listView = (ListView) findViewById(R.id.activity_gallery_listView);
            Adapter = new GalleryViewAdapter(this, R.layout.item_layout_list, getData());
            listView.setAdapter(Adapter);

            listView.setOnItemClickListener(new OnItemClickListener() {
                public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                    ImageItem item = (ImageItem) parent.getItemAtPosition(position);

                    if(intentMode.equals("Wallpaper")) {
                        setWallpaper(item);
                    }
                    else if(intentMode.equals("Share")) {
                        shareImage(item);
                    }
                }
            });
        }


        else if(layoutMode.equals("Grid")){
            System.out.println("in grid block");
            setContentView(R.layout.activity_gallery_grid);
            gridView = (GridView) findViewById(R.id.activity_gallery_gridView);
            Adapter = new GalleryViewAdapter(this, R.layout.item_layout_grid, getData());
            gridView.setAdapter(Adapter);

            gridView.setOnItemClickListener(new OnItemClickListener() {
                public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                    ImageItem item = (ImageItem) parent.getItemAtPosition(position);

                    if(intentMode.equals("Wallpaper")) {
                        setWallpaper(item);
                    }
                    else if(intentMode.equals("Share")) {
                        shareImage(item);
                    }
                }
            });
        }

    }


    private void setWallpaper(ImageItem item){
        final WallpaperManager wallpaperManager = WallpaperManager.getInstance(getApplicationContext());
        try {
            wallpaperManager.setBitmap(item.getImage());
            Toast.makeText(getApplicationContext(), "New wallpaper: "+ item.getTitle(), Toast.LENGTH_SHORT).show();
        }
        catch (IOException e) {}
    }

    private Uri getImageUri(Context inContext, Bitmap inImage, String title) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, title, null);
        return Uri.parse(path);
    }

    private void shareImage(ImageItem item){
        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        Uri image = getImageUri(getApplicationContext(), item.getImage(), item.getTitle());

        sharingIntent.setAction(Intent.ACTION_SEND);
        sharingIntent.putExtra(Intent.EXTRA_STREAM, image);
        sharingIntent.setType("image/jpeg");

        startActivity(Intent.createChooser(sharingIntent, "Share via"));

        Toast.makeText(getApplicationContext(), "Shared: "+ item.getTitle(), Toast.LENGTH_SHORT).show();
    }


    private ArrayList getData() {
        final ArrayList imageItems = new ArrayList();
        String path = Environment.getExternalStorageDirectory().toString()+"/DCIM/Camera";
        File directory = new File(path);
        File[] imageFiles = directory.listFiles();

        for (int i = 0; i < imageFiles.length; i++) {
            Bitmap bitmap = BitmapFactory.decodeFile(imageFiles[i].getAbsolutePath());
            imageItems.add(new ImageItem(bitmap, imageFiles[i].getName()));
        }
        return imageItems;
    }
}