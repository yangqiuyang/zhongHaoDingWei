package org.zhonghao.gps.entity;

import java.io.Serializable;

/**
 * Created by lenovo on 2016/11/7.
 */

public class UpdateInfo implements Serializable {

    private String version;
    private String description;
    private String url;

    public String getUrl() {
        return url;
    }

    public String getDescription() {

        return description;
    }

    public String getVersion() {

        return version;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setVersion(String version) {

        this.version = version;
    }
}
