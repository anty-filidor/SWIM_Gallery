package com.example.gallery;


import android.app.WallpaperManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.view.View.OnClickListener;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class GalleryRecyclerAdapter extends RecyclerView.Adapter <GalleryRecyclerAdapter.ViewHolder> {

    private ArrayList data;
    private Context context;
    private String intentMode;
    private int layoutName;

    public GalleryRecyclerAdapter(Context context, ArrayList galleryList, String intentMode, int layoutName) {
        this.data = galleryList;
        this.context = context;
        this.intentMode = intentMode;
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
        viewHolder.image.setImageBitmap(BitmapFactory.decodeFile(item.getPath()));

        viewHolder.image.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                if(intentMode.equals("Wallpaper")) {
                    setWallpaper(item);
                }
                else if(intentMode.equals("Share")) {
                    shareImage(item);
                }
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

    private void setWallpaper(ImageItem item){
        final WallpaperManager wallpaperManager = WallpaperManager.getInstance(context);
        try {
            wallpaperManager.setBitmap(BitmapFactory.decodeFile(item.getPath()));
            Toast.makeText(context, "New wallpaper: "+ item.getTitle(), Toast.LENGTH_SHORT).show();
        }
        catch (IOException e) {}
    }

    private Uri getImageUri(Context inContext, Bitmap inImage, String title) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, title, null);
        return Uri.parse(path);
    }

    private void shareImage(ImageItem item){
        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        Uri image = getImageUri(context, BitmapFactory.decodeFile(item.getPath()), item.getTitle());

        sharingIntent.setAction(Intent.ACTION_SEND);
        sharingIntent.putExtra(Intent.EXTRA_STREAM, image);
        sharingIntent.setType("image/jpeg");

        context.startActivity(Intent.createChooser(sharingIntent, "Share via"));

        Toast.makeText(context, "Shared: "+ item.getTitle(), Toast.LENGTH_SHORT).show();
    }

}