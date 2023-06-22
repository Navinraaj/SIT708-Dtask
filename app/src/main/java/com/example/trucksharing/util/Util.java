public class Util {
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "VehicleRental";

    public static final String TABLE_NAME_USER = "Customer";
    public static final String TABLE_NAME_VEHICLE = "Vehicle";
    public static final String TABLE_NAME_ORDER = "RentalOrder";

    public static final String USER_ID = "customer_id";
    public static final String USER_USERNAME = "email";
    public static final String USER_PASSWORD = "password";
    public static final String USER_FULLNAME = "name";
    public static final String USER_PHONENUMBER = "phone";
    public static final String USER_AVATARNAME = "profile_picture";

    public static final String VEHICLE_ID = "vehicle_id";
    public static final String VEHICLE_NAME = "vehicle_name";
    public static final String VEHICLE_IMAGE_NAME = "vehicle_picture";
    public static final String VEHICLE_STATUS = "availability_status";

    public static final String ORDER_ID = "order_id";
    public static final String ORDER_RECEIVER_NAME = "customer_name";
    public static final String ORDER_IMAGE_NAME = "order_image";
    public static final String ORDER_PICKUP_LOCATION = "pickup_point";
    public static final String ORDER_DROP_OFF_LOCATION = "drop_off_point";
    public static final String PICKUP_LAT = "pickup_latitude";
    public static final String PICKUP_LNG = "pickup_longitude";
    public static final String DROP_OFF_LAT = "drop_off_latitude";
    public static final String DROP_OFF_LNG = "drop_off_longitude";

    public static final String ORDER_DRIVER_PHONE_NUMBER = "driver_phone";
    public static final String ORDER_PICKUP_DATE = "start_date";
    public static final String ORDER_PICKUP_TIME = "start_time";
    public static final String ORDER_VEHICLE_TYPE = "vehicle_model";
    public static final String ORDER_DRIVER_NAME = "driver_name";
    public static final String ORDER_WEIGHT = "luggage_weight";
    public static final String ORDER_WIDTH = "vehicle_width";
    public static final String ORDER_HEIGHT = "vehicle_height";
    public static final String ORDER_LENGTH = "vehicle_length";
    public static final String ORDER_QUANTITY = "vehicle_quantity";
    public static final String ORDER_FARE = "total_price";

    public static final int LOCATION_REQUEST_CODE = 1000;
    public static final int PICK_UP_CODE = 2000;
    public static final int DROP_OFF_CODE = 3000;
}
