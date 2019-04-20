package com.example.gallery;


public class ImageItem {

    private String path;
    private String title;

    public ImageItem(String path, String title) {
        super();
        this.path = path;
        this.title = title;
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
}



