package com.jiepier.pictureflash.bean;

/**
 * Created by JiePier on 16/11/20.
 */

public class ImageBean {

    private String path;
    private String alumb;

    public String getPath() {
        return path;
    }

    public ImageBean setPath(String path) {
        this.path = path;
        return this;
    }

    public String getAlumb() {
        return alumb;
    }

    public ImageBean setAlumb(String alumb) {
        this.alumb = alumb;
        return this;
    }

    @Override
    public String toString() {
        return "ImageBean{" +
                "path='" + path + '\'' +
                ", alumb='" + alumb + '\'' +
                '}';
    }
}
