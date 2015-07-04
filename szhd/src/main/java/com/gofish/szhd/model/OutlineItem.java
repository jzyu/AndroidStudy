package com.gofish.szhd.model;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.google.gson.annotations.SerializedName;

@Table(name = "outline")
public class OutlineItem extends Model {
    @Column
    @SerializedName("id")
    public short oid;
    @Column
    public short category_id;
    @Column
    public String title;
    @Column
    public String desc;
    @Column
    public String detail_url;
    @Column
    public String img_url;
    @Column
    public String update_dt;

    public OutlineItem() {
        super();
    }

    public OutlineItem(short category_id, String title, String desc,
                       String detail_url, String img_url, String update_dt, short oid) {
        this.category_id = category_id;
        this.title = title;
        this.desc = desc;
        this.detail_url = detail_url;
        this.img_url = img_url;
        this.update_dt = update_dt;
        this.oid = oid;
    }
}