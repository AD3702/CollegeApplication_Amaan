package com.example.collegeproject.Room;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class ImagesData {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "image_id")
    private int image_id;

    @ColumnInfo(name = "image_list", typeAffinity = ColumnInfo.BLOB)
    private byte[] images;

    public int getImage_id() {
        return image_id;
    }

    public void setImage_id(int image_id) {
        this.image_id = image_id;
    }

    public byte[] getImages() {
        return images;
    }

    public void setImages(byte[] images) {
        this.images = images;
    }
}
