package com.example.gallery;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Iterator;


public class ImageDetailsFragment extends Fragment {

    private static final int NUM_PAGES = 2;

    private ViewPager mPager;

    private PagerAdapter pagerAdapter;

    private ArrayList<String> titles;
    private ArrayList<String> values;
    String title;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //get arguments - get image metadata and save them in titles and values
        Bundle bundle = getArguments();

        ArrayList<String> val = bundle.getStringArrayList("values");
        ArrayList<String> tit = bundle.getStringArrayList("titles");

        titles = new ArrayList<String>();
        values = new ArrayList<String>();

        Iterator<String> iteratorVal = val.iterator();
        Iterator<String> iteratorTit = tit.iterator();
        while(iteratorVal.hasNext()){
            titles.add(new String(iteratorVal.next().toString()));
            values.add(new String(iteratorTit.next().toString()));
        }

        title = bundle.getString("title");


        View view =  inflater.inflate(R.layout.fragment_image_details, container, false);

        // Instantiate a ViewPager and a PagerAdapter.
        mPager = (ViewPager) view.findViewById(R.id.pager);
        pagerAdapter = new ScreenSlidePagerAdapter(((AppCompatActivity)getActivity()).getSupportFragmentManager());
        mPager.setAdapter(pagerAdapter);

        return view;
    }


    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
        public ScreenSlidePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            Bundle bundle = new Bundle();

            switch(position) {
                case 0:
                    ImageDetailsSlidePageFragment mainMetadata =  new ImageDetailsSlidePageFragment();
                    bundle.putStringArrayList("titles", new ArrayList<String> (titles.subList(0, 6)));
                    bundle.putStringArrayList("values", new ArrayList<String> (values.subList(0, 6)));
                    bundle.putString("title", title);
                    mainMetadata.setArguments(bundle);
                    return mainMetadata;

                case 1:
                    ImageDetailsSlidePageFragment supportMetadata =  new ImageDetailsSlidePageFragment();
                    bundle.clear();
                    bundle.putStringArrayList("titles", new ArrayList<String> (titles.subList(6, 17)));
                    bundle.putStringArrayList("values", new ArrayList<String> (values.subList(6, 17)));
                    bundle.putString("title", title);
                    supportMetadata.setArguments(bundle);
                    return supportMetadata;

                default:
                    return null;
            }

        }

        @Override
        public int getCount() {
            return NUM_PAGES;
        }
    }
}

