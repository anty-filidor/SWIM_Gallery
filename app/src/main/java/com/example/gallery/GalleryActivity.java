package com.example.gallery;

import java.io.File;
import java.util.ArrayList;

import android.app.AlertDialog;
import android.content.Intent;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;


import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import android.view.Menu;
import android.view.MenuItem;

import android.view.View;
import android.widget.TextView;
import android.widget.Toast;



public class GalleryActivity extends AppCompatActivity implements
        SettingsFragment.GalleryFragmentSettingsListener{


    GalleryRecyclerAdapter adapter;
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    String directoryToImages;
    String layoutMode;

    SharedPreferences folderChoice;
    SharedPreferences layoutChoice;

    ArrayList imageItems;

    TextView emptyFolder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery_recycler);


        //set directoryToImages to folder with photos and get data
        folderChoice = getSharedPreferences("recentFolderChoice", 0);
        if (folderChoice.getString("folderName", "") != null) {
            directoryToImages = folderChoice.getString("folderName", "");
        }
        else{
            directoryToImages = Environment.getExternalStorageDirectory().toString();//+"/DCIM/Camera";
        }
        imageItems = getData();


        //set layout
        layoutChoice = getSharedPreferences("recentLayoutChoice", 0);
        if (layoutChoice.getString("layoutMode", "") != null) {
            layoutMode = layoutChoice.getString("layoutMode", "");
        }
        else{
            layoutMode = "Grid";
        }


        //create toolbar
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        if(toolbar != null) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setTitle(directoryToImages);
        }


        //create recycler view to display gallery
        recyclerView = (RecyclerView)findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        layoutManager = new GridLayoutManager(getApplicationContext(), 1);
        recyclerView.setLayoutManager(layoutManager);


        //view accurate layout (in adapter) to user preferences
        if(imageItems==null || imageItems.isEmpty()) {
            emptyFolder = findViewById(R.id.empty_folder);
            emptyFolder.setVisibility(View.VISIBLE);
        }
        if(layoutMode.equals("Grid")) {
            adapter = new GalleryRecyclerAdapter(getApplicationContext(), imageItems, R.layout.item_layout_grid);
            ((GridLayoutManager) layoutManager).setSpanCount(4);
        }
        else{
            adapter = new GalleryRecyclerAdapter(getApplicationContext(), imageItems, R.layout.item_layout_list);
            ((GridLayoutManager) layoutManager).setSpanCount(1);
        }
        recyclerView.setAdapter(adapter);
    }


    //this method prepares data to display as images and titles
    private ArrayList getData() {
        imageItems = new ArrayList();
        File directory = new File(directoryToImages);
        File[] imageFiles = directory.listFiles();
        if(imageFiles!=null) {
            for (int i = 0; i < imageFiles.length; i++) {
                String imagePath = imageFiles[i].getAbsolutePath();
                if (imagePath.endsWith(".jpg") || imagePath.endsWith(".png") || imagePath.endsWith(".JPEG")) {
                    imageItems.add(new ImageItem(imagePath, imageFiles[i].getName()));
                }
            }
        }
        return imageItems;
    }


    //this method creates toolbar menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.manu_main, menu);
        getMenuInflater().inflate(R.menu.menu_app_info, menu);
        return true;
    }


    //this method defines action for click at menu
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.preferences:
                showPreferencesDialog();
                break;
            case R.id.folder:
                showFolderDialog();
                break;
            case R.id.app_info:
                showAppInfo();
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    //this method shows galleryfragmentsettings fragment when selected in toolbar menu
    private void showPreferencesDialog() {
        FragmentManager fm = getSupportFragmentManager();
        SettingsFragment editNameDialogFragment = SettingsFragment.newInstance();
        editNameDialogFragment.show(fm, "fragment_edit_name");
    }


    //this method shows galleryfragmentfolderchoice fragment when selected in toolbar menu
    private void showFolderDialog() {
        final Intent intent = new Intent(this, FolderChoiceActivity.class);
        startActivityForResult(intent, 1);
    }


    //this method calls simple alertdialog to show author name
    private void showAppInfo() {
        new AlertDialog.Builder(this)
                .setTitle(R.string.app_info)
                .setMessage(R.string.author)
                .setPositiveButton("OK", null).show();
    }


    //this method saves and updates current layout
    public void onSettingsPassed(String layoutMode) {
        this.layoutMode = layoutMode;
        Toast.makeText(this, layoutMode, Toast.LENGTH_SHORT).show();

        SharedPreferences layoutChoice = getSharedPreferences("recentLayoutChoice", 0);
        SharedPreferences.Editor layoutEditor = layoutChoice.edit();
        layoutEditor.putString("layoutMode", layoutMode);
        layoutEditor.commit();


        //view accurate layout (in adapter) to user preferences
        if (layoutMode.equals("Grid")) {
            adapter = new GalleryRecyclerAdapter(getApplicationContext(), imageItems, R.layout.item_layout_grid);
            ((GridLayoutManager) layoutManager).setSpanCount(4);
        } else {
            adapter = new GalleryRecyclerAdapter(getApplicationContext(), imageItems, R.layout.item_layout_list);
            ((GridLayoutManager) layoutManager).setSpanCount(1);
        }
        recyclerView.setAdapter(adapter);
    }


    //this method receives data from folder explorer to save new folder
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if(resultCode == RESULT_OK) {
                directoryToImages = data.getStringExtra("editTextValue");
                Toast.makeText(this, ("New folder is ").concat(directoryToImages), Toast.LENGTH_SHORT).show();

                SharedPreferences folderChoice = getSharedPreferences("recentFolderChoice", 0);
                SharedPreferences.Editor folderEditor = folderChoice.edit();
                folderEditor.putString("folderName", directoryToImages);
                folderEditor.commit();

                imageItems.clear();
                getData();
                adapter.notifyDataSetChanged();
            }
        }
    }
}