package com.coffe.shentao.httpprocessor.bean;

import java.io.Serializable;

public class DongNao implements Serializable {
    private String name;
    private String network;
    private String creater;
    private String createDate;
    private String shoolName;
    private String part;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNetwork() {
        return network;
    }

    public void setNetwork(String network) {
        this.network = network;
    }

    public String getCreater() {
        return creater;
    }

    public void setCreater(String creater) {
        this.creater = creater;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public String getShoolName() {
        return shoolName;
    }

    public void setShoolName(String shoolName) {
        this.shoolName = shoolName;
    }

    public String getPart() {
        return part;
    }

    public void setPart(String part) {
        this.part = part;
    }
}
