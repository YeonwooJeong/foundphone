package com.example.foundphone;

import android.provider.BaseColumns;

public final class AssetListData {
    // To prevent someone from accidentally instantiating the contract class,
    // make the constructor private.
    private AssetListData() {}

    /* Inner class that defines the table contents */
    public static class AssetEntry implements BaseColumns {
        public static final String TABLE_NAME = "assets";
        public static final String COLUMN_NAME_ASSETNUMBER = "asset_number";
        public static final String COLUMN_NAME_ITEMNUMBER = "item_number";
        public static final String COLUMN_NAME_PHONENAME = "phone_name";
        public static final String COLUMN_NAME_STATUS = "status";
    }

    public static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + AssetEntry.TABLE_NAME + " (" +
                    AssetEntry._ID + " INTEGER PRIMARY KEY," +
                    AssetEntry.COLUMN_NAME_ASSETNUMBER + " TEXT," +
                    AssetEntry.COLUMN_NAME_ITEMNUMBER + " TEXT," +
                    AssetEntry.COLUMN_NAME_PHONENAME + " TEXT," +
                    AssetEntry.COLUMN_NAME_STATUS + " TEXT)";

    public static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + AssetEntry.TABLE_NAME;
}
