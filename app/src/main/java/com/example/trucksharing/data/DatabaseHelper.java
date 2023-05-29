package com.example.trucksharing.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.example.trucksharing.model.Order;
import com.example.trucksharing.model.Truck;
import com.example.trucksharing.model.User;
import com.example.trucksharing.util.Util;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {
    public DatabaseHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, Util.DATABASE_NAME, factory, Util.DATABASE_VERSION);
    }

    public DatabaseHelper(@Nullable Context context) {
        super(context, Util.DATABASE_NAME, null, Util.DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String CREATE_USER_TABLE = "CREATE TABLE " + Util.TABLE_NAME_USER +
                "("
                + Util.USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + Util.USER_USERNAME + " TEXT, "
                + Util.USER_PASSWORD + " TEXT, "
                + Util.USER_FULLNAME + " TEXT, "
                + Util.USER_PHONENUMBER + " TEXT, "
                + Util.USER_AVATARNAME + " TEXT)";
        sqLiteDatabase.execSQL(CREATE_USER_TABLE);

        String CREATE_TRUCK_TABLE = "CREATE TABLE " + Util.TABLE_NAME_TRUCK +
                "("
                + Util.TRUCK_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + Util.TRUCK_NAME + " TEXT, "
                + Util.TRUCK_IMAGE_NAME + " TEXT, "
                + Util.TRUCK_STATUS + " TEXT)";
        sqLiteDatabase.execSQL(CREATE_TRUCK_TABLE);

        String CREATE_ORDER_TABLE = "CREATE TABLE " + Util.TABLE_NAME_ORDER +
                "("
                + Util.ORDER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + Util.USER_ID + " INTEGER, "
                + Util.ORDER_RECEIVER_NAME + " TEXT, "
                + Util.ORDER_IMAGE_NAME + " TEXT, "
                + Util.ORDER_PICKUP_LOCATION + " TEXT, "
                + Util.PICKUP_LAT + " DOUBLE, "
                + Util.PICKUP_LNG + " DOUBLE, "
                + Util.ORDER_DROP_OFF_LOCATION + " TEXT, "
                + Util.DROP_OFF_LAT + " DOUBLE, "
                + Util.DROP_OFF_LNG + " DOUBLE, "
                + Util.ORDER_DRIVER_PHONE_NUMBER + " TEXT, "
                + Util.ORDER_PICKUP_DATE + " TEXT, "
                + Util.ORDER_PICKUP_TIME + " TEXT, "
                + Util.ORDER_GOOD_TYPE + " TEXT, "
                + Util.ORDER_VEHICLE_TYPE + " TEXT, "
                + Util.ORDER_DRIVER_NAME + " TEXT, "
                + Util.ORDER_WEIGHT + " DOUBLE, "
                + Util.ORDER_WIDTH + " DOUBLE, "
                + Util.ORDER_HEIGHT + " DOUBLE, "
                + Util.ORDER_LENGTH + " DOUBLE, "
                + Util.ORDER_QUANTITY + " INTEGER)";
        sqLiteDatabase.execSQL(CREATE_ORDER_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public void insertTruck(Truck truck) {
        SQLiteDatabase db = this.getReadableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(Util.TRUCK_NAME, truck.getName());
        contentValues.put(Util.TRUCK_IMAGE_NAME, truck.getImageName());
        contentValues.put(Util.TRUCK_STATUS, truck.getStatus());

        long newRowId = db.insert(Util.TABLE_NAME_TRUCK, null, contentValues);
        db.close();
    }

    public List<Truck> fetchAllTrucks() {
        List<Truck> truckList = new ArrayList<Truck>();
        SQLiteDatabase db = this.getReadableDatabase();

        String selectAll = "SELECT * FROM " + Util.TABLE_NAME_TRUCK;
        Cursor cursor = db.rawQuery(selectAll, null);

        if (cursor.moveToFirst()) {
            do {
                Truck truck = new Truck();
                truck.setId(cursor.getInt(0));
                truck.setName(cursor.getString(1));
                truck.setImageName(cursor.getString(2));
                truck.setStatus(cursor.getString(3));
                truckList.add(truck);

            } while (cursor.moveToNext());
        }

        return truckList;
    }

    public long insertUser(User user) {
        SQLiteDatabase db = this.getReadableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(Util.USER_USERNAME, user.getUsername());
        contentValues.put(Util.USER_PASSWORD, user.getPassword());
        contentValues.put(Util.USER_FULLNAME, user.getFullName());
        contentValues.put(Util.USER_PHONENUMBER, user.getPhoneNumber());
        contentValues.put(Util.USER_AVATARNAME, user.getAvatarName());

        long newRowId = db.insert(Util.TABLE_NAME_USER, null, contentValues);
        db.close();

        return newRowId;
    }

    public int fetchUser(String username, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(Util.TABLE_NAME_USER, new String[]{Util.USER_ID}, Util.USER_USERNAME + "=? and " + Util.USER_PASSWORD + "=?",
                new String[]{username, password}, null, null, null);
        int numberOfRows = cursor.getCount();

        if (numberOfRows > 0 && cursor.moveToFirst()) {
            int userId = Integer.valueOf(cursor.getString(0));
            db.close();
            return userId;
        } else {
            db.close();
            return -1;
        }
    }

    public List<User> fetchAllUsers() {
        List<User> userList = new ArrayList<User>();
        SQLiteDatabase db = this.getReadableDatabase();

        String selectAll = "SELECT * FROM " + Util.TABLE_NAME_USER;
        Cursor cursor = db.rawQuery(selectAll, null);

        if (cursor.moveToFirst()) {
            do {
                User user = new User();
                user.setUserId(cursor.getInt(0));
                user.setUsername(cursor.getString(1));
                user.setPassword(cursor.getString(2));
                user.setFullName(cursor.getString(3));
                user.setPhoneNumber(cursor.getString(4));
                user.setAvatarName(cursor.getString(5));
                userList.add(user);

            } while (cursor.moveToNext());
        }

        return userList;
    }

    public long insertOrder(Order order) {
        SQLiteDatabase db = this.getReadableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(Util.USER_ID, order.getUserId());
        contentValues.put(Util.ORDER_IMAGE_NAME, order.getImageName());
        contentValues.put(Util.ORDER_RECEIVER_NAME, order.getReceiverName());
        contentValues.put(Util.ORDER_PICKUP_DATE, order.getPickupDate());
        contentValues.put(Util.ORDER_PICKUP_TIME, order.getPickupTime());
        contentValues.put(Util.ORDER_PICKUP_LOCATION, order.getPickupLocation());
        contentValues.put(Util.PICKUP_LAT, order.getPickupLatitude());
        contentValues.put(Util.PICKUP_LNG, order.getPickupLongitude());

        contentValues.put(Util.ORDER_DROP_OFF_LOCATION, order.getDropOffLocation());
        contentValues.put(Util.DROP_OFF_LAT, order.getDropOffLatitude());
        contentValues.put(Util.DROP_OFF_LNG, order.getDropOffLongitude());

        contentValues.put(Util.ORDER_DRIVER_PHONE_NUMBER, order.getDriverPhoneNumber());
        contentValues.put(Util.ORDER_GOOD_TYPE, order.getGoodType());
        contentValues.put(Util.ORDER_WEIGHT, order.getWeight());
        contentValues.put(Util.ORDER_WIDTH, order.getWidth());
        contentValues.put(Util.ORDER_LENGTH, order.getLength());
        contentValues.put(Util.ORDER_HEIGHT, order.getHeight());
        contentValues.put(Util.ORDER_VEHICLE_TYPE, order.getVehicleType());
        contentValues.put(Util.ORDER_QUANTITY, order.getQuantity());
        contentValues.put(Util.ORDER_DRIVER_NAME, order.getDriverName());

        long newRowId = db.insert(Util.TABLE_NAME_ORDER, null, contentValues);
        db.close();
        return newRowId;
    }

    public List<Order> fetchAllOrders(int userId) {
        List<Order> orderList = new ArrayList<Order>();
        SQLiteDatabase db = this.getReadableDatabase();

        String selectAll = "SELECT * FROM " + Util.TABLE_NAME_ORDER + " WHERE " + Util.USER_ID + " = " + userId;
        Cursor cursor = db.rawQuery(selectAll, null);

        if (cursor.moveToFirst()) {
            do {
                Order order = new Order();
                order.setOrderId(cursor.getInt(0));
                order.setUserId(cursor.getInt(1));
                order.setReceiverName(cursor.getString(2));
                order.setImageName(cursor.getString(3));
                order.setPickupLocation(cursor.getString(4));
                order.setPickupLatitude(cursor.getDouble(5));
                order.setPickupLongitude(cursor.getDouble(6));

                order.setDropOffLocation(cursor.getString(7));
                order.setDropOffLatitude(cursor.getDouble(8));
                order.setDropOffLongitude(cursor.getDouble(9));

                order.setDriverPhoneNumber(cursor.getString(10));
                order.setPickupDate(cursor.getString(11));
                order.setPickupTime(cursor.getString(12));
                order.setGoodType(cursor.getString(13));
                order.setVehicleType(cursor.getString(14));
                order.setDriverName(cursor.getString(15));
                order.setWeight(cursor.getDouble(16));
                order.setWidth(cursor.getDouble(17));
                order.setHeight(cursor.getDouble(18));
                order.setLength(cursor.getDouble(19));
                order.setQuantity(cursor.getInt(20));

                orderList.add(order);

            } while (cursor.moveToNext());
        }

        return orderList;
    }
}
