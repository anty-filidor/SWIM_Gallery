package com.example.gallery;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.TextView;
import android.content.SharedPreferences;


public class StartActivity extends AppCompatActivity {

    private String galleryMode = "Wallpaper";
    private String layoutMode = "List";
    private TextView textView1;
    private TextView textView2;
    private TextView textView3;
    private RadioGroup radioGroup;
    private Switch switch1;
    private SharedPreferences settings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);


        textView1 =(TextView)findViewById(R.id.textView1);
        textView1.setMovementMethod(LinkMovementMethod.getInstance());

        textView2 =(TextView)findViewById(R.id.textView2);
        textView2.setMovementMethod(LinkMovementMethod.getInstance());

        radioGroup = findViewById(R.id.radioGroup);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton rb = group.findViewById(checkedId);
                if (rb.getId() == R.id.radioButtonShare) {
                    galleryMode = "Share";
                }
                else if (rb.getId() == R.id.radioButtonWallpaper) {
                    galleryMode = "Wallpaper";
                }
            }
        });

        //Handling switch3 and textview3 - setting tevtview3 as it was remembered by settings var
        //in setOnCheckedChangeListener saving state of the switch
        switch1 = findViewById(R.id.switch1);
        textView3 = findViewById(R.id.textView3);

        settings = getSharedPreferences("recentLayoutChoice", 0);
        boolean silent = settings.getBoolean("switchkey", false);
        switch1.setChecked(silent);

        if(switch1.isChecked()) {
            textView3.setText(R.string.activity_start_switch_layout_list);
            layoutMode="List";
        }
        else {
            textView3.setText(R.string.activity_start_switch_layout_grid);
            layoutMode="Grid";
        }


        switch1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(switch1.isChecked()) {
                    textView3.setText(R.string.activity_start_switch_layout_list);
                    layoutMode="List";
                }
                else {
                    textView3.setText(R.string.activity_start_switch_layout_grid);
                    layoutMode="Grid";
                }

                SharedPreferences settings = getSharedPreferences("recentLayoutChoice", 0);
                SharedPreferences.Editor editor = settings.edit();
                editor.putBoolean("switchkey", isChecked);
                editor.commit();
            }
        });
    }


    public void runGallery(View view) {
        final Intent intentGallery = new Intent(this, GalleryActivity.class);
        intentGallery.putExtra("intentChoice", galleryMode);
        intentGallery.putExtra("layoutChoice", layoutMode);
        startActivity(intentGallery);
    }
}
