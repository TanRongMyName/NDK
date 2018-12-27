package com.coffe.shentao.mvp.bean;

public class Girl {
    private int drawableid;
    private String title;
    private String disctri;

    public Girl(int image1, String s, String s1) {
        this.drawableid=image1;
        this.title=s;
        this.disctri=s1;
    }

    public int getDrawableid() {
        return drawableid;
    }

    public void setDrawableid(int drawableid) {
        this.drawableid = drawableid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDisctri() {
        return disctri;
    }

    public void setDisctri(String disctri) {
        this.disctri = disctri;
    }
}
