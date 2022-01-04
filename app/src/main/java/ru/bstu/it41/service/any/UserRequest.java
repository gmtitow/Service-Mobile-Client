package ru.bstu.it41.service.any;

/**
 * Created by Герман on 05.10.2017.
 */

public final class UserRequest{
    //private static String prefix = "http://192.168.2.108";
    //private static final String prefix = "http://192.168.43.245";
    //private static final String prefix = "http://192.168.2.110";
    //private static final String prefix = "http://62.76.93.40";
    public static final String URL_SCHEME = "http";
   // public static final String URL_HOST = "192.168.2.109";
    public static final String URL_HOST = "192.168.43.245";

    public static final String URL_SERVER = URL_SCHEME + "://"+URL_HOST;

    public  static final String URL_LOGIN = "/sessionAPI";

    public static final String URL_REGISTER = "/registerAPI";

    public static final String URL_LOGOUT = "/session/end";

    public static final String URL_PROFILE = "/userinfoAPI";

    public  static final String URL_LOAD_PHOTO = "/userinfoAPI/handler";

    public static final String URL_SETTINGS= "/userinfoAPI/settings";

    public static final String URL_ABOUT= "/userinfoAPI/about";

    //public static final String URL_REVIEWS= "/reviewsAPI";

    public static final String URL_TENDERS = "/tenderAPI/index";

    public static final String URL_CATEGORIES = "/categoriesAPI/index";

    public static final String URL_TASKS = "/tasksAPI/index";

    public static final String URL_OFFERS = "/offersAPI/";

    public static final String URL_COORDINATION = "/coordinationAPI/";

    //public static final String URL_ADD_TASK = "/tasksAPI/add";

    public static final String TAG_SUCCESS = "success";

    //нужны для авторизации
    public static final String PARAM_EMAIL = "email";
    public static final String PARAM_PASSWORD = "password";
    public static final String PARAM_MOBILE = "mobile";

    //недостающие для регистрации + пароль, e-mail
    public static final String PARAM_FIRSTNAME = "firstname";
    public static final String PARAM_LASTNAME = "lastname";
    public static final String PARAM_PATRONYMIC = "patronymic";
    public static final String PARAM_BIRTHDAY = "birthday";
    public static final String PARAM_MALE = "male";
    public static final String PARAM_TELEPHONE = "phone";
    public static final String PARAM_ADDRESS = "address";
    public static final String PARAM_ABOUT = "about";

    public static final String PARAM_IMAGE = "image";

    public static final String PARAM_USER_ID = "userId";
    public static final String PARAM_AUCTION_ID = "auctionId";
    public static final String PARAM_DATE = "date";

    public static final String PARAM_NOTIFICATION_EMAIL = "notificationEmail";
    public static final String PARAM_NOTIFICATION_PUSH = "notificationPush";
    public static final String PARAM_NOTIFICATION_SMS = "notificationSms";


    public static final String PARAM_CITY = "city";
    public static final String PARAM_COUNTRY = "country_id";
    public static final String PARAM_ZONE = "zone_id";
    public static final String PARAM_CONFIRM = "confirm";
    public static final String PARAM_AGREE = "agree";
    public static final String PARAM_FAX = "fax";
    public static final String PARAM_COMPANY= "company";
    public static final String PARAM_ADDRESS_2= "address_2";
    public static final String PARAM_POSTCODE= "postcode";

    //
    public static final String PARAM_API= "api";
    public static final String PARAM_MESSAGE= "message";
    public static final String PARAM_TOKEN_ID= "tokenId";

    public static final String PARAM_RATING= "rating";
    public static final String PARAM_TEXT_REVIEW= "textReview";

    //Для заданий
    public static final String PARAM_NAME= "name";
    public static final String PARAM_CATEGORY= "categoryId";
    public static final String PARAM_DESCRIPTION= "description";
    public static final String PARAM_DEADLINE= "deadline";
    public static final String PARAM_PRICE= "price";
    public static final String PARAM_LATITUDE= "latitude";
    public static final String PARAM_LONGITUDE= "longitude";

    public static final String PARAM_TASK_ID= "taskId";
    public static final String PARAM_DATE_START= "dateStart";
    public static final String PARAM_DATE_END= "dateEnd";
    public static final String PARAM_TENDER_ID= "tenderId";

    public static final String PARAM_OFFER_ID= "offerId";
    //status-ы
    public static final String STATUS_FAIL= "FAIL";
    public static final String STATUS_OK= "OK";
    public static final String STATUS_ALREADY_EXISTS= "ALREADY_EXISTS";
    public static final String STATUS_WRONG_DATA= "WRONG_DATA";

    public static final String URL_TASK_DELETE = "/tasksAPI/delete/{" + PARAM_TASK_ID + "}";
    public static final String URL_TASK_CHANGE = "/tasksAPI/change";
    public static final String URL_TENDER_DELETE = "/tenderAPI/delete/{" + PARAM_TENDER_ID + "}";

    public static final String URL_OFFERS_FOR_TENDER = "/offersAPI/getForTender/{" + PARAM_TENDER_ID + "}";

    public static final String URL_OFFERS_FOR_USER = "/offersAPI/getForUser";

    public static final String URL_ADD_OFFER = "/offersAPI/add";

    public static final String URL_REVIEWS= "/reviewsAPI/index/{"+PARAM_USER_ID + "}";

    public static final String URL_OFFERS_DELETE = URL_OFFERS + "delete/{" + PARAM_OFFER_ID + "}";

    public static final String URL_SELECT_OFFER = URL_COORDINATION + "selectOffer";

    public static final String URL_GET_ALL_MESSAGES = URL_COORDINATION + "getMessages/{" + PARAM_AUCTION_ID + "}";

    public static final String URL_SEND_MESSAGE = URL_COORDINATION + "addMessage";

    public static final String URL_SEND_TOKEN_ID = URL_COORDINATION + "addTokenId";

    public static final String URL_SEND_CLEAR_TOKENS = URL_COORDINATION + "clearTokens";

    public static final String URL_COMPLETE_TASK = URL_COORDINATION + "completeTask";

    public static final String URL_FINISH_TASK = URL_COORDINATION + "finishTask";

    public static final String URL_GET_LAST_MESSAGES = URL_COORDINATION +
            "getMessages/{" + PARAM_AUCTION_ID + "}/{"+PARAM_DATE +"}";


    public static final String URL_ADD_REVIEW= "/reviewsAPI/addReview";
}
