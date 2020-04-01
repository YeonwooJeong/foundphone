package com.example.foundphone;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
@Entity
public class Todo {
    @PrimaryKey(autoGenerate = true)
    private int id;

    private String assetNumber;
    private String itemNumber;
    private String phoneName;
    private String status;

    public Todo(String assetNumber) {
        this.assetNumber = assetNumber;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAssetNumber() {
        return assetNumber;
    }

    public void setAssetNumber(String assetNumber) {
        this.assetNumber = assetNumber;
    }

    public String getItemNumber() {
        return itemNumber;
    }

    public void setItemNumber(String itemNumber) {
        this.itemNumber = itemNumber;
    }

    public String getPhoneName() {
        return phoneName;
    }

    public void setPhoneName(String phoneName) {
        this.phoneName = phoneName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Todo{" +
                "id=" + id +
                ", assetNumber='" + assetNumber + '\'' +
                ", itemNumber='" + itemNumber + '\'' +
                ", phoneName='" + phoneName + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}
