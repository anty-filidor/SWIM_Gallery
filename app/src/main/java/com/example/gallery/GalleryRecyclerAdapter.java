package com.example.gallery;



import android.content.Context;
import android.os.Bundle;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.view.View.OnClickListener;


import java.util.ArrayList;

import static com.example.gallery.RescaleImage.decodeSampledBitmapFromFile;

public class GalleryRecyclerAdapter extends RecyclerView.Adapter <GalleryRecyclerAdapter.ViewHolder> {


    private ArrayList data;
    private Context context;
    private int layoutName;
    private static RecyclerViewClickListener itemListener;

    //interface to make litrener in galleryActivity
    public interface RecyclerViewClickListener {
        void recyclerViewListClicked(View v, int position);
    }

    //adapter constructor
    public GalleryRecyclerAdapter(Context context, ArrayList galleryList, int layoutName, RecyclerViewClickListener itemListener) {
        this.data = galleryList;
        this.context = context;
        this.layoutName = layoutName;
        this.itemListener = itemListener;
    }


    //this method creates custom viewholder
    @Override
    public GalleryRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(layoutName, viewGroup, false);
        return new ViewHolder(view);
    }


    //this method creates view for each item
    @Override
    public void onBindViewHolder(final GalleryRecyclerAdapter.ViewHolder viewHolder, final int position) {

        final ImageItem item = (ImageItem)data.get(position);
        viewHolder.title.setText(item.getTitle());

        viewHolder.image.setScaleType(ImageView.ScaleType.CENTER_CROP);
        viewHolder.image.setImageBitmap(decodeSampledBitmapFromFile(item.getPath(), 50, 50));

        viewHolder.image.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                AppCompatActivity activity = (AppCompatActivity) v.getContext();
                BigImageFragment bigImageFragment = new BigImageFragment();
                Bundle bundle = new Bundle();
                bundle.putString("image", item.getPath());
                bundle.putString("title", item.getTitle());
                bigImageFragment.setArguments(bundle);
                activity.getSupportFragmentManager().beginTransaction().
                        replace(R.id.child_fragment_container, bigImageFragment)
                        .addToBackStack(null).commit();

                itemListener.recyclerViewListClicked(v, position);
            }
        });
    }


    @Override
    public int getItemCount() {
        return data.size();
    }


    //This class is custom viewholder, it implements OnClickListener, sets clicklistener to itemView
    // and send message back to galleryActivity
    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private TextView title;
        private ImageView image;

        public ViewHolder(View convertView) {
            super(convertView);
            title = (TextView)convertView.findViewById(R.id.text);
            image = (ImageView) convertView.findViewById(R.id.image);
            convertView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) { }
    }
}