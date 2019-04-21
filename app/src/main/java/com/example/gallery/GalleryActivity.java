package com.example.gallery;

import java.io.File;
import java.util.ArrayList;

import android.content.Intent;

import android.os.Bundle;
import android.os.Environment;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import android.view.Menu;
import android.view.MenuItem;

import android.widget.Toast;


public class GalleryActivity extends AppCompatActivity implements GalleryFragmentSettings.GalleryFragmentSettingsListener {

    String intentMode;
    String layoutMode;

    GalleryRecyclerAdapter adapter;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery_recycler);

        //create toolbar
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        if(toolbar != null) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setTitle(R.string.app_name);
        }


        //messages from start activity. They determine layout and onclickitem listener
        Intent fromStartActivity = getIntent();
        intentMode = fromStartActivity.getStringExtra("intentChoice");
        layoutMode = fromStartActivity.getStringExtra("layoutChoice");


        //create recycler view to display gallery
        recyclerView = (RecyclerView)findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getApplicationContext(), 1);
        recyclerView.setLayoutManager(layoutManager);

        //view accurate layout (in adapter) to user preferences
        if(layoutMode.equals("Grid")) {
            adapter = new GalleryRecyclerAdapter(getApplicationContext(), getData(), intentMode, R.layout.item_layout_grid);
            ((GridLayoutManager) layoutManager).setSpanCount(4);
        }
        else{
            adapter = new GalleryRecyclerAdapter(getApplicationContext(), getData(), intentMode, R.layout.item_layout_list);
        }
        recyclerView.setAdapter(adapter);
    }

    //this method prepares data to display as images and titles
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

    //this method creates toolbar menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.manu_main, menu);

        //create fragment manager to manage fragments (gallery settings)
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        return true;
    }

    //this method shows galleryfragmentsettings fragment when selected in toolbar menu
    private void showEditDialog() {
        FragmentManager fm = getSupportFragmentManager();
        GalleryFragmentSettings editNameDialogFragment = GalleryFragmentSettings.newInstance();
        editNameDialogFragment.show(fm, "fragment_edit_name");
    }


    //method to delets
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.folder:
                Toast.makeText(this, "You clicked about", Toast.LENGTH_SHORT).show();
                showEditDialog();
                break;
        }
        return true;
    }


    public void onFinishEditDialog(String inputText) {
        Toast.makeText(this, "Hi, " + inputText, Toast.LENGTH_SHORT).show();
    }

    public void onSettingsPassed(String intentMode, String layoutMode) {
        this.intentMode = intentMode;
        this.layoutMode = layoutMode;
        Toast.makeText(this, intentMode, Toast.LENGTH_SHORT).show();
        //view accurate layout (in adapter) to user preferences
        if(layoutMode.equals("Grid")) {
            adapter = new GalleryRecyclerAdapter(getApplicationContext(), getData(), intentMode, R.layout.item_layout_grid);
            ((GridLayoutManager) layoutManager).setSpanCount(4);
        }
        else{
            adapter = new GalleryRecyclerAdapter(getApplicationContext(), getData(), intentMode, R.layout.item_layout_list);
        }
        recyclerView.setAdapter(adapter);
    }
}