package ru.bstu.it41.service.profile;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.Toast;

import com.activeandroid.query.Select;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileOutputStream;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import etr.android.reamp.mvp.ReampPresenter;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.bstu.it41.service.Controller;
import ru.bstu.it41.service.R;
import ru.bstu.it41.service.any.DataStore;
import ru.bstu.it41.service.models.Status;
import ru.bstu.it41.service.any.UserRequest;
import ru.bstu.it41.service.models.AllForUser;
import ru.bstu.it41.service.models.Place;
import ru.bstu.it41.service.models.Settings;
import ru.bstu.it41.service.models.User;
import ru.bstu.it41.service.models.Userinfo;
import ru.bstu.it41.service.serviceAPI;

/**
 * Created by Герман on 18.10.2017.
 */

public class ProfileFragmentPresenter extends ReampPresenter<ProfileFragmentState> implements Serializable {
    public SimpleDateFormat mFormatForDate = new SimpleDateFormat("dd MMMM yyyy", Locale.getDefault());

    @Override
    public void onPresenterCreated() {
        super.onPresenterCreated();

        //getStateModel().setCurrentFragment(0);

        //getStateModel().setEnabledButton(true);

        sendStateModel();
    }

    public void updateProfile(){
        getStateModel().setUser(new Select().from(User.class).where("userId = ?",
                DataStore.getUserId(getView().getContext().getApplicationContext())).<User>executeSingle());
        getStateModel().setUserinfo(new Select().from(Userinfo.class).where("userId = ?",
                DataStore.getUserId(getView().getContext().getApplicationContext())).<Userinfo>executeSingle());

        getStateModel().setSettings(new Select().from(Settings.class).where("userId = ?",
                DataStore.getUserId(getView().getContext().getApplicationContext())).<Settings>executeSingle());

        Picasso.with(getView().getContext()).invalidate(UserRequest.URL_SERVER + getStateModel().getUserinfo().getPathToPhoto());

    }

    public void download() {
        getStateModel().setShowProgress(true);
        getStateModel().setEnableDownload(false);
        sendStateModel();
        //Загрузка
        serviceAPI itableApi = Controller.getGsonAPI();

        itableApi.getProfileView()
                .enqueue(new Callback<AllForUser>() {
                    @Override
                    public void onResponse(Call<AllForUser> call, Response<AllForUser> response) {
                        try {
                            getStateModel().setUserinfo(response.body().getUserinfo());
                            getStateModel().setUser(response.body().getUser());
                            getStateModel().setSettings(response.body().getSettings());

                            getStateModel().getSettings().save();
                            getStateModel().getUser().save();
                            getStateModel().getUserinfo().save();

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        getStateModel().setShowProgress(false);
                        getStateModel().setDownloaded(true);
                        sendStateModel();
                    }

                    @Override
                    public void onFailure(Call<AllForUser> call, Throwable t) {
                        getStateModel().setShowProgress(false);
                        Toast.makeText(getView().getContext(), R.string.profile_change_connection_error, Toast.LENGTH_SHORT).show();
                        sendStateModel();
                    }
                });
    }

    /*public void saveViewChanges() {
        getStateModel().setSaveButtonEnabled(false);
        //getStateModel().fromTemporaryToRegular();
        getStateModel().setShowProgress(true);

        sendStateModel();
        //Запрос к серверу
        serviceAPI api = Controller.getGsonAPI();
        Date date;
        if(getStateModel().getTemporaryUserInfo().getBirthday() != null)
            date = getStateModel().getTemporaryUserInfo().getBirthday().getTime();
        else
            date = null;
        api.changeProfileView(
                getStateModel().getTemporaryUserInfo().getFirstname(),
                getStateModel().getTemporaryUserInfo().getLastname(),
                getStateModel().getTemporaryUserInfo().getPatronymic(),
                date,
                getStateModel().getTemporaryUserInfo().getMale(),
                getStateModel().getTemporaryUserInfo().getAddress())
                .enqueue(new Callback<Status>() {
                    @Override
                    public void onResponse(Call<Status> call, Response<Status> response) {
                        try {

                            if (response.body().getStatus().equals(UserRequest.STATUS_OK)) {
                                //Toast.makeText(getView().getContext(), R.string.profile_change_success, Toast.LENGTH_SHORT).show();
                                //getStateModel().setToViewPofile(true);
                                loadPhoto();
                            } else {
                                getStateModel().setShowProgress(false);
                                getStateModel().setSaveButtonEnabled(true);
                                Toast.makeText(getView().getContext(), R.string.profile_change_error, Toast.LENGTH_SHORT).show();
                                sendStateModel();
                            }


                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(Call<Status> call, Throwable t) {
                        getStateModel().setShowProgress(false);
                        Toast.makeText(getView().getContext(), R.string.profile_change_connection_error, Toast.LENGTH_SHORT).show();
                        getStateModel().setSaveButtonEnabled(true);
                        sendStateModel();
                    }
                });

    }*/

    /*public void loadPhoto(){
        File file = getPhotoFile(getStateModel().getTemporaryPhotoFileName());

        RequestBody requestFile =
                RequestBody.create(MediaType.parse("multipart/form-data"), file);

        MultipartBody.Part body =
                MultipartBody.Part.createFormData(UserRequest.PARAM_IMAGE, file.getName(), requestFile);

        serviceAPI api = Controller.getStaticAPI();
        api.loadPhoto(body,1)
                .enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                        getStateModel().setShowProgress(false);
                        getStateModel().setSaveButtonEnabled(true);


                            Toast.makeText(getView().getContext(), R.string.profile_change_success, Toast.LENGTH_SHORT).show();

                        getStateModel().setToViewPofile(true);
                        sendStateModel();
                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {
                        getStateModel().setShowProgress(false);
                        getStateModel().setSaveButtonEnabled(true);

                        Toast.makeText(getView().getContext(), R.string.profile_change_photo_error, Toast.LENGTH_SHORT).show();
                        sendStateModel();
                    }
                });
    }*/

    public void savePhotoToFile(Bitmap mBitmap,File file){
        //File file = new File(getView().getContext().getCacheDir(),fileName);
        try {
            FileOutputStream fOut = new FileOutputStream(file);

            mBitmap.compress(Bitmap.CompressFormat.JPEG, 85, fOut);

            fOut.flush();
            fOut.close();

            getStateModel().setPhotoChanged(true);

        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public void savePhotoToFile(Bitmap mBitmap,String fileName){
        File file = new File(getView().getContext().getCacheDir(),fileName);
        try {
            FileOutputStream fOut = new FileOutputStream(file);

            mBitmap.compress(Bitmap.CompressFormat.JPEG, 85, fOut);

            fOut.flush();
            fOut.close();

            getStateModel().setPhotoChanged(true);

        }catch(Exception e){
            e.printStackTrace();
        }
    }

    /*public void changePhoto(Bitmap bitmap) {
        //getStateModel().setBitmapPhoto(bitmap);
        getStateModel().setPhotoChanged(true);
    }*/

    public File getPhotoFile(String fileName) {
        File filesDir = getView().getContext().getCacheDir();
        return new File(filesDir, fileName);
    }

    public void setTempSettingsFromRegular(){
        Settings settings= new Settings();
        /*settings.setNotificationPush(getStateModel().getSettings().getNotificationPush());
        settings.setNotificationEmail(getStateModel().getSettings().getNotificationEmail());
        settings.setNotificationSms(getStateModel().getSettings().getNotificationSms());*/
        getStateModel().copySettings(settings,getStateModel().getSettings());
        getStateModel().setTempSettings(settings);
    }

    public void changeSettings(){
        //getStateModel().setSaveButtonEnabled(false);
        //getStateModel().fromTemporaryToRegular();
        getStateModel().setShowProgress(true);

        sendStateModel();
        //Запрос к серверу
        serviceAPI api = Controller.getGsonAPI();
        api.changeSettings(
                getStateModel().getTempSettings().getNotificationEmail(),
                getStateModel().getTempSettings().getNotificationSms(),
                getStateModel().getTempSettings().getNotificationPush())
                .enqueue(new Callback<Status>() {
                    @Override
                    public void onResponse(Call<Status> call, Response<Status> response) {
                        try {
                            getStateModel().setShowProgress(false);
                           // getStateModel().setSaveButtonEnabled(true);
                            if (response.body().getStatus().equals(UserRequest.STATUS_OK)) {
                                Toast.makeText(getView().getContext(), R.string.profile_change_success, Toast.LENGTH_SHORT).show();
                                getStateModel().copySettings(getStateModel().getSettings(),getStateModel().getTempSettings());
                            } else {
                                Toast.makeText(getView().getContext(), R.string.profile_change_error, Toast.LENGTH_SHORT).show();
                            }

                            sendStateModel();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(Call<Status> call, Throwable t) {
                        getStateModel().setShowProgress(false);
                        Toast.makeText(getView().getContext(), R.string.profile_change_connection_error, Toast.LENGTH_SHORT).show();
                       // getStateModel().setSaveButtonEnabled(true);
                        sendStateModel();
                    }
                });
    }

    public void updateView() {
        sendStateModel();
    }

    public List<Place> getPlaces() {
        return getStateModel().getPlaces();
    }

    public void deletePlace(Place place) {
        getStateModel().getPlaces().remove(place);
        sendStateModel();
    }

    public void addPlace(Place place) {
        getStateModel().getPlaces().add(place);
        sendStateModel();
    }
}