package com.example.gallery;

import android.content.SharedPreferences;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.TextView;


public class GalleryFragmentSettings extends DialogFragment {

    private String intentMode = "Wallpaper";
    private String layoutMode = "List";
    private TextView switch1TextView;
    private RadioGroup radioGroup;
    private Switch switch1;
    private SharedPreferences settings;
    private Button button;


    public interface GalleryFragmentSettingsListener {
        void onSettingsPassed(String intentMode, String layoutMode);
    }


    public GalleryFragmentSettings() {
        // Empty constructor is required for DialogFragment
    }


    public static GalleryFragmentSettings newInstance() {
        GalleryFragmentSettings frag = new GalleryFragmentSettings();
        Bundle args = new Bundle();
        frag.setArguments(args);
        return frag;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_gallery_settings, container);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);

        button = (Button) view.findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                GalleryFragmentSettingsListener listener = (GalleryFragmentSettingsListener) getActivity();
                listener.onSettingsPassed(intentMode, layoutMode);
                dismiss();
            }
        });


        //radio button group
        radioGroup = (RadioGroup) view.findViewById(R.id.radioGroup);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton rb = group.findViewById(checkedId);
                if (rb.getId() == R.id.radioButtonShare) {
                    intentMode = "Share";
                }
                else if (rb.getId() == R.id.radioButtonWallpaper) {
                    intentMode = "Wallpaper";
                }
                System.out.println(intentMode);
            }
        });


        //switch button
        //Handling switch1 and switch1_textview - setting textview as it was remembered by settings var
        //in setOnCheckedChangeListener saving state of the switch
        switch1 = view.findViewById(R.id.switch1);
        switch1TextView = view.findViewById(R.id.switch1_textview);

        settings = getActivity().getSharedPreferences("recentLayoutChoice", 0);
        boolean silent = settings.getBoolean("switchkey", false);
        switch1.setChecked(silent);

        if(switch1.isChecked()) {
            switch1TextView.setText(R.string.activity_start_switch_layout_list);
            layoutMode="List";
        }
        else {
            switch1TextView.setText(R.string.activity_start_switch_layout_grid);
            layoutMode="Grid";
        }


        switch1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(switch1.isChecked()) {
                    switch1TextView.setText(R.string.activity_start_switch_layout_list);
                    layoutMode="List";
                }
                else {
                    switch1TextView.setText(R.string.activity_start_switch_layout_grid);
                    layoutMode="Grid";
                }

                SharedPreferences settings = getActivity().getSharedPreferences("recentLayoutChoice", 0);
                SharedPreferences.Editor editor = settings.edit();
                editor.putBoolean("switchkey", isChecked);
                editor.commit();
                System.out.println(layoutMode);
            }
        });

    }

}


