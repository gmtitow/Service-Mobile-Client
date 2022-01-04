package ru.bstu.it41.service.any;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.constraint.ConstraintLayout;

import com.activeandroid.query.Delete;

import java.util.List;
import java.util.Map;

import okhttp3.Cookie;
import ru.bstu.it41.service.models.Category;
import ru.bstu.it41.service.models.Offer;
import ru.bstu.it41.service.models.Reviews;
import ru.bstu.it41.service.models.Settings;
import ru.bstu.it41.service.models.Tasks;
import ru.bstu.it41.service.models.Tender;
import ru.bstu.it41.service.models.User;
import ru.bstu.it41.service.models.Userinfo;

/**
 * Created by Герман on 20.05.2018.
 */

public class DataStore {
    public static final String PREF_NAME = "ru.bstu.it41.service.prefs";
    public static final String PREF_CURRENT_USER_ID = "current_user_id";
    //public static final String SHARE_CURRENT_SESSION_ID = "current_session_id";

    public static int getUserId(Context context){
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return preferences.getInt(PREF_CURRENT_USER_ID,-1);
    }

    public static void setUserId(Context context, int userId){
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        preferences.edit().putInt(PREF_CURRENT_USER_ID,userId).apply();
    }

    public static void clearDB(){
        new Delete().from(Tasks.class).execute();
        new Delete().from(Tender.class).execute();
        new Delete().from(User.class).execute();
        new Delete().from(Userinfo.class).execute();
        new Delete().from(Settings.class).execute();
        new Delete().from(Offer.class).execute();
        new Delete().from(Reviews.class).execute();
    }

    public static void clearPreferences(Context context){
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        preferences.edit().clear().apply();
    }

    public static void clearAll(Context context){
        clearDB();
        clearPreferences(context);
    }
}
