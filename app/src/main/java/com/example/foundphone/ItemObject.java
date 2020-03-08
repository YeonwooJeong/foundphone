package com.example.foundphone;

public class ItemObject {
    private String assetNumber;
    private String itemNumber;
    private String phoneName;
    private String status;


    public ItemObject(String assetNumber, String itemNumber, String phoneName, String status){
        this.assetNumber = assetNumber;
        this.itemNumber = itemNumber;
        this.phoneName = phoneName;
        this.status = status;
    }


    public String getAssetNumber() {
        return assetNumber;
    }

    public String getItemNumber() {
        return itemNumber;
    }

    public String getPhoneName() {
        return phoneName;
    }
    public String getStatus() {
        return status;
    }
}
