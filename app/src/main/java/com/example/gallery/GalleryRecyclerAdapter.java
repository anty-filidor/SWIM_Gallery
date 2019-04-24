package com.example.gallery;



import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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

public class GalleryRecyclerAdapter extends RecyclerView.Adapter <GalleryRecyclerAdapter.ViewHolder> {


    private ArrayList data;
    private Context context;
    private int layoutName;


    public GalleryRecyclerAdapter(Context context, ArrayList galleryList, int layoutName) {
        this.data = galleryList;
        this.context = context;
        this.layoutName = layoutName;
    }


    @Override
    public GalleryRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(layoutName, viewGroup, false);
        return new ViewHolder(view);
    }



    @Override
    public void onBindViewHolder(GalleryRecyclerAdapter.ViewHolder viewHolder, int position) {

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

            }
        });
    }


    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView title;
        private ImageView image;
        public ViewHolder(View view) {
            super(view);
            title = (TextView)view.findViewById(R.id.text);
            image = (ImageView) view.findViewById(R.id.image);
        }
    }


    //this method calculate optimised height and width of image
    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) >= reqHeight
                    && (halfWidth / inSampleSize) >= reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }


    //this method is resemble for BitmapFactory.decodeFile(item.getPath()
    public static Bitmap decodeSampledBitmapFromFile(String path, int reqWidth, int reqHeight) {

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(path, options);
    }


}