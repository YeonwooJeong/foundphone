package com.example.foundphone;

public class ItemObject {
    private String assetNumber;
    private String itemNumber;
    private String phoneName;


    public ItemObject(String assetNumber, String itemNumber, String phoneNumber){
        this.assetNumber = assetNumber;
        this.itemNumber = itemNumber;
        this.phoneName = phoneNumber;
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
}
