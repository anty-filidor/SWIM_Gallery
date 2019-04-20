package com.example.gallery;

import java.io.File;
import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;


public class GalleryActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        //messages from start activity. They determine layout and onclickitem listener
        Intent fromStartActivity = getIntent();
        final String intentMode = fromStartActivity.getStringExtra("intentChoice");
        String layoutMode = fromStartActivity.getStringExtra("layoutChoice");

        setContentView(R.layout.activity_gallery_recycler);

        RecyclerView recyclerView = (RecyclerView)findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);

        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getApplicationContext(), 1);
        recyclerView.setLayoutManager(layoutManager);

        GalleryRecyclerAdapter adapter;
        if(layoutMode.equals("Grid")) {
            adapter = new GalleryRecyclerAdapter(getApplicationContext(), getData(), intentMode, R.layout.item_layout_grid);
            ((GridLayoutManager) layoutManager).setSpanCount(4);
        }
        else{
            adapter = new GalleryRecyclerAdapter(getApplicationContext(), getData(), intentMode, R.layout.item_layout_list);
        }
        recyclerView.setAdapter(adapter);
    }

    private ArrayList getData() {
        final ArrayList imageItems = new ArrayList();
        String path = Environment.getExternalStorageDirectory().toString()+"/DCIM/Camera";
        File directory = new File(path);
        File[] imageFiles = directory.listFiles();

        for (int i = 0; i < imageFiles.length; i++) {
            String imagePath = imageFiles[i].getAbsolutePath();
            imageItems.add(new ImageItem(imagePath, imageFiles[i].getName()));
        }
        return imageItems;
    }
}