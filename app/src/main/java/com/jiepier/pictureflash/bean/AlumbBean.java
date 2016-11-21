package com.jiepier.pictureflash.bean;

import java.util.List;

/**
 * Created by JiePier on 16/11/20.
 */

public class AlumbBean {

    private String path;
    private String alumb;

    public AlumbBean(String path, String alumb) {
        this.path = path;
        this.alumb = alumb;
    }

    public AlumbBean() {
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getAlumb() {
        return alumb;
    }

    public void setAlumb(String alumb) {
        this.alumb = alumb;
    }
}
