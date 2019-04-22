package com.example.gallery;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.DialogFragment;
import android.util.Log;

import java.io.File;
import java.io.FilenameFilter;

import static android.content.ContentValues.TAG;

public class Deprecated_GalleryFolderChoiceFragment extends DialogFragment {

    private String[] mFileList;
    private File mPath = new File(Environment.getExternalStorageDirectory() + "/");
    private String mChosenFile;
    private static final String FTYPE = "";

    public interface GalleryFolderChoiceFragmentListener{
        void onFolderPassed(String mChosenFile);

    }

    public Deprecated_GalleryFolderChoiceFragment() {
        //empty constructor is required
    }


    public static Deprecated_GalleryFolderChoiceFragment newInstance(int title) {
        Deprecated_GalleryFolderChoiceFragment frag = new Deprecated_GalleryFolderChoiceFragment();
        Bundle args = new Bundle();
        args.putInt("title", title);
        frag.setArguments(args);
        return frag;
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = null;
        AlertDialog.Builder builder = new android.app.AlertDialog.Builder(getContext());
        loadFileList();
        builder.setTitle("Choose your file");
        if(mFileList == null) {
            Log.e(TAG, "Showing file picker before loading the file list");
            dialog = builder.create();
            return dialog;
        }
        builder.setItems(mFileList, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                mChosenFile = mFileList[which];
                GalleryFolderChoiceFragmentListener listener = (GalleryFolderChoiceFragmentListener) getActivity();
                listener.onFolderPassed(mPath + "/" + mChosenFile);

                Deprecated_GalleryFolderChoiceFragment f = new Deprecated_GalleryFolderChoiceFragment();
            }
        });
        dialog = builder.show();
        return dialog;
    }


    private void loadFileList() {
        try {
            mPath.mkdirs();
            Environment.getExternalStorageDirectory();
        } catch (SecurityException e) {
            Log.e(TAG, "unable to write on the sd card " + e.toString());
        }
        if (mPath.exists()) {
            FilenameFilter filter = new FilenameFilter() {

                @Override
                public boolean accept(File dir, String filename) {
                    File sel = new File(dir, filename);
                    return filename.contains(FTYPE) || sel.isDirectory();
                }

            };
            mFileList = mPath.list(filter);
        } else {
            mFileList = new String[0];
        }

    }
}
