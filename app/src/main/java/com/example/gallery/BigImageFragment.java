package com.example.gallery;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;


public class BigImageFragment extends Fragment{


    private ViewPager mPager;
    private PagerAdapter pagerAdapter;

    private ArrayList<ImageItem> data;

    private int currentPage;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //Log.e("debug", "in big image fragment oncreateview!");
        super.onCreate(savedInstanceState);

        //get arguments - get image metadata and save them in titles and values
        data = getArguments().getParcelableArrayList("data");

        View view =  inflater.inflate(R.layout.fragment_big_image, container, false);


        //create toolbar
        setHasOptionsMenu(true);
        final Toolbar toolbar = (Toolbar)view.findViewById(R.id.toolbar1);
        if(toolbar != null) {
            Log.e("debug", "in bigImageSlidePageFragment, found toolbar for position: " + getArguments().getInt("position"));
            ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
            ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(data.get(getArguments().getInt("position")).getTitle());
        }
        else{Log.e("debug", "in bigImageSlidePageFragment, toolbar not found!");}


        // Instantiate a ViewPager and a PagerAdapter.
        mPager = (ViewPager) view.findViewById(R.id.pager);
        ViewPager.OnPageChangeListener pageChangeListener = new ViewPager.SimpleOnPageChangeListener(){
            @Override
            public void onPageSelected(int position) {
                currentPage = position;
                if(toolbar != null) {
                    Log.e("debug", "in bigImageSlidePageFragment, found toolbar for position: " + position);
                    ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(data.get(currentPage).getTitle());
                }
            }
        };
        mPager.addOnPageChangeListener(pageChangeListener);
        pagerAdapter = new ScreenSlidePagerAdapter(((AppCompatActivity)getActivity()).getSupportFragmentManager());
        mPager.setAdapter(pagerAdapter);
        mPager.setCurrentItem(getArguments().getInt("position"));

        return view;
    }


    //this method creates toolbar menu
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        Log.e("debug", "in bigImageSlidePageFragment, creating toolbar for position: "+ getArguments().getInt("position"));
        menu.clear();
        inflater.inflate(R.menu.manu_image_details, menu);
        inflater.inflate(R.menu.menu_app_info, menu);
    }


    //this method defines action for click at menu
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.e("debug", "in bigImageSlidePageFragment in TOOLBAR! "+currentPage);
        int id = item.getItemId();
        switch (id) {
            case R.id.image_info:
                Log.e("debug", "in bigImageSlidePageFragment in IMAGE INFO!");
                showImageInfo();
                break;
        }
        return false;
    }


    private void showImageInfo() {
        Log.e("debug", "in bigImageSlidePageFragment in showimageinfo!!!! "+currentPage);
        ArrayList<ArrayList<String>> list = data.get(currentPage).getMetaData();
        Bundle bundle = new Bundle();
        bundle.putStringArrayList("titles", list.get(0));
        bundle.putStringArrayList("values", list.get(1));
        bundle.putString("title", data.get(currentPage).getTitle());

        ImageDetailsFragment imageDetailsFragment = new ImageDetailsFragment();
        imageDetailsFragment.setArguments(bundle);
        getActivity().getSupportFragmentManager().beginTransaction().
                replace(R.id.big_image_fragment_container, imageDetailsFragment).addToBackStack(null).commit();
    }


    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
        public ScreenSlidePagerAdapter(FragmentManager fm) {
            super(fm);
        }


        @Override
        public Fragment getItem(int position) {
            Bundle bundle = new Bundle();
            for(int i = 0 ; i < getCount() ; i++){
                if(position == i){
                    Log.e("debug", "in bigimagefragment, creating view for position: "+position);
                    BigImageSlidePageFragment bigImageSlidePageFragment =  new BigImageSlidePageFragment();
                    bundle.putParcelable("imageItem", data.get(position));
                    bundle.putInt("position", position);
                    bigImageSlidePageFragment.setArguments(bundle);
                    return bigImageSlidePageFragment;
                }
            }
            return null;
        }

        @Override
        public int getCount() {
            return data.size();
        }
    }

}

