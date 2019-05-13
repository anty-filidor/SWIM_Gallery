package com.example.gallery;


import android.graphics.BitmapFactory;
import android.media.ExifInterface;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Iterator;

public class ImageItem implements Parcelable {

    private String path;
    private String title;


    public ImageItem(String path, String title) {
        super();
        this.path = path;
        this.title = title;
    }


    public ImageItem(Parcel in) {
        super();
        readFromParcel(in);
    }


    public static final Parcelable.Creator<ImageItem> CREATOR = new Parcelable.Creator<ImageItem>() {
        public ImageItem createFromParcel(Parcel in) {
            return new ImageItem(in);
        }
        public ImageItem[] newArray(int size) {
            return new ImageItem[size];
        }
    };


    public void readFromParcel(Parcel in) {
        path = in.readString();
        title = in.readString();
    }


    public int describeContents() {
        return 0;
    }


    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(path);
        dest.writeString(title);
    }


    public String getPath() {
        return path;
    }


    public void setPath(String path) {
        this.path = path;
    }


    public String getTitle() {
        return title;
    }


    public void setTitle(String title) {
        this.title = title;
    }


    public ArrayList<ArrayList<String>> getMetaData() {
        ArrayList<String> metaDataValues = new ArrayList<String>();
        ArrayList<String> metaDataTitles = new ArrayList<String>();


        BitmapFactory.Options opts=new BitmapFactory.Options();
        opts.inJustDecodeBounds=true;
        BitmapFactory.decodeFile(path, opts);

        metaDataTitles.add("Type:");
        metaDataValues.add(opts.outMimeType);

        metaDataTitles.add("Width (px):");
        metaDataValues.add("" + opts.outWidth);

        metaDataTitles.add("Height (px):");
        metaDataValues.add("" + opts.outHeight);


        File file = new File(path);
        Date lastModified = new Date(file.lastModified());

        metaDataTitles.add("Last modified:");
        metaDataValues.add(lastModified.toString());

        metaDataTitles.add("Size (kB):");
        metaDataValues.add(file.length()/1024 + "");

        metaDataTitles.add("Is hidden:");
        metaDataValues.add(file.isHidden() ? "Yes" : "No");


        try{
            ExifInterface exif = new ExifInterface(path);


            metaDataTitles.add("Image description:");
            metaDataValues.add(addMetaData(exif, ExifInterface.TAG_IMAGE_DESCRIPTION));

            metaDataTitles.add("Artist:");
            metaDataValues.add(addMetaData(exif, ExifInterface.TAG_ARTIST));

            metaDataTitles.add("Model:");
            metaDataValues.add(addMetaData(exif, ExifInterface.TAG_MODEL));

            metaDataTitles.add("Flash:");
            metaDataValues.add(addMetaData(exif, ExifInterface.TAG_FLASH));

            metaDataTitles.add("Aspect frame:");
            metaDataValues.add(addMetaData(exif, ExifInterface.TAG_ORF_ASPECT_FRAME));

            metaDataTitles.add("Saturation:");
            metaDataValues.add(addMetaData(exif, ExifInterface.TAG_SATURATION));

            metaDataTitles.add("White balance:");
            metaDataValues.add(addMetaData(exif, ExifInterface.TAG_WHITE_BALANCE));

            metaDataTitles.add("Color space:");
            metaDataValues.add(addMetaData(exif, ExifInterface.TAG_COLOR_SPACE));

            metaDataTitles.add("Aperture (1/m):");
            metaDataValues.add(addMetaData(exif, ExifInterface.TAG_APERTURE_VALUE));

            metaDataTitles.add("Shuttler speed (s):");
            metaDataValues.add(addMetaData(exif, ExifInterface.TAG_SHUTTER_SPEED_VALUE));

            metaDataTitles.add("Orientation:");
            metaDataValues.add(addMetaData(exif, ExifInterface.TAG_ORIENTATION));

            /*
            Iterator itr1 = metaDataValues.listIterator();
            Iterator itr2 = metaDataTitles.listIterator();
            while(itr1.hasNext()) {
                Log.e("metadata Title", (String) itr2.next());
                Log.e("metadata Value", (String) itr1.next());
            }
            */


        } catch(IOException e){
            Log.e("ImageItem", "Unrecognised path!" + e.getMessage());
        }


        ArrayList<ArrayList<String>> finalList = new ArrayList<ArrayList<String>>();
        finalList.add(metaDataTitles);
        finalList.add(metaDataValues);
        return finalList;

    }


    private String addMetaData(ExifInterface exif, String TAG){
        if(exif.getAttribute(TAG)==null){
            return "No data";
        }
        else
            return exif.getAttribute(TAG);
    }

}



