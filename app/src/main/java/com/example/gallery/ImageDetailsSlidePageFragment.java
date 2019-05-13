package com.example.gallery;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Iterator;


public class ImageDetailsSlidePageFragment extends Fragment {

    private ListView listView;
    private ArrayAdapter adapter;

    private ArrayList<String> titles;
    private ArrayList<String> values;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.fragment_slide_page_image_details, container, false);


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

        ArrayList<ArrayList<String>> finalList = new ArrayList<ArrayList<String>>();
        finalList.add(values);
        finalList.add(titles);


        TextView title = (TextView)rootView.findViewById(R.id.title);

        listView = (ListView) rootView.findViewById(R.id.list_view);

        /*
        String[] titles = new String[] {"dupa", "dupaa", "dupaaa", "dupaaaaa"};
        String[] values = new String[] {"pa", "paa", "paaa", "paaaaa"};

        ArrayList<String> listTitles = new ArrayList<String>();
        ArrayList<String> listValues = new ArrayList<String>();

        for(int i=0; i<titles.length; i++){
            listTitles.add(titles[i]);
            listValues.add(values[i]);
        }
        ArrayList<ArrayList<String>> finalList = new ArrayList<ArrayList<String>>();
        finalList.add(listTitles);
        finalList.add(listValues);
        */

        title.setText(bundle.getString("title"));

        adapter = new ImageDetailsSlidePageFragmentAdapter(getContext(), R.layout.item_image_details, finalList);

        listView.setAdapter(adapter);

        return rootView;
    }

}
