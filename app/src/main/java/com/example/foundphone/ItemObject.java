package com.example.foundphone;

public class ItemObject {
    private String assetNumber;
    private String itemNumber;
    private String phoneName;
    private String check;


    public ItemObject(String assetNumber, String itemNumber, String phoneNumber, String check){
        this.assetNumber = assetNumber;
        this.itemNumber = itemNumber;
        this.phoneName = phoneNumber;
        this.check = check;
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
    public String getCheck() {
        return check;
    }
}
