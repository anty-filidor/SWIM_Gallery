package com.example.gallery;

import java.io.File;
import java.util.ArrayList;

import android.app.AlertDialog;
import android.content.Intent;

import android.content.SharedPreferences;
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


public class GalleryActivity extends AppCompatActivity implements
        GallerySettingsFragment.GalleryFragmentSettingsListener{
        //, Deprecated_GalleryFolderChoiceFragment.GalleryFolderChoiceFragmentListener{

    String intentMode;
    String layoutMode;

    GalleryRecyclerAdapter adapter;
    RecyclerView recyclerView;
    String path;

    SharedPreferences folderChoice;

    ArrayList imageItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery_recycler);


        //messages from start activity. They determine layout and onclickitem listener
        Intent fromStartActivity = getIntent();
        intentMode = fromStartActivity.getStringExtra("intentChoice");
        layoutMode = fromStartActivity.getStringExtra("layoutChoice");


        //set path to folder with photos and get data
        folderChoice = getSharedPreferences("recentFolderChoice", 0);
        if (folderChoice.getString("folderName", "") != null) {
            path = folderChoice.getString("folderName", path);
        }
        else{
            path = Environment.getExternalStorageDirectory().toString()+"/DCIM/Camera";
        }
        getData();


        //create toolbar
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        if(toolbar != null) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setTitle(path);
        }


        //create recycler view to display gallery
        recyclerView = (RecyclerView)findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getApplicationContext(), 1);
        recyclerView.setLayoutManager(layoutManager);


        //view accurate layout (in adapter) to user preferences
        if(layoutMode.equals("Grid")) {
            adapter = new GalleryRecyclerAdapter(getApplicationContext(), imageItems, intentMode, R.layout.item_layout_grid);
            ((GridLayoutManager) layoutManager).setSpanCount(4);
        }
        else{
            adapter = new GalleryRecyclerAdapter(getApplicationContext(), imageItems, intentMode, R.layout.item_layout_list);
        }
        recyclerView.setAdapter(adapter);
    }


    //this method prepares data to display as images and titles
    private ArrayList getData() {
        imageItems = new ArrayList();
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


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.preferences:
                showPreferencesDialog();
                break;
            case R.id.folder:
                showFolderDialog();
                break;
            case R.id.info:
                showAppInfo();
        }
        return true;
    }


    //this method shows galleryfragmentsettings fragment when selected in toolbar menu
    private void showPreferencesDialog() {
        FragmentManager fm = getSupportFragmentManager();
        GallerySettingsFragment editNameDialogFragment = GallerySettingsFragment.newInstance();
        editNameDialogFragment.show(fm, "fragment_edit_name");
    }


    //this method shows galleryfragmentfolderchoice fragment when selected in toolbar menu
    private void showFolderDialog() {
        final Intent jj = new Intent(this, GalleryFolderChoiceActivity.class);
        startActivityForResult(jj, 1);
    }


    //this method calls simple alertdialog to show author name
    private void showAppInfo() {
        new AlertDialog.Builder(this)
                .setTitle(R.string.info)
                .setMessage(R.string.activity_start_author)
                .setPositiveButton("OK", null).show();
    }


    public void onSettingsPassed(String intentMode, String layoutMode) {
        this.intentMode = intentMode;
        this.layoutMode = layoutMode;
        Toast.makeText(this, intentMode.concat(" ").concat(layoutMode), Toast.LENGTH_SHORT).show();
    }


    //deprecated
    /*
    public void onFolderPassed(String path){
        Toast.makeText(this, path, Toast.LENGTH_SHORT).show();
    }
    */


    //this method reciewes data from folder explorer to save new folder
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if(resultCode == RESULT_OK) {
                path = data.getStringExtra("editTextValue");
                Toast.makeText(this, ("New folder is ").concat(path), Toast.LENGTH_SHORT).show();

                SharedPreferences folderChoice = getSharedPreferences("recentFolderChoice", 0);
                SharedPreferences.Editor editor = folderChoice.edit();
                editor.putString("folderName", path);
                editor.commit();

                imageItems.clear();
                getData();
                adapter.notifyDataSetChanged();
            }
        }
    }
}