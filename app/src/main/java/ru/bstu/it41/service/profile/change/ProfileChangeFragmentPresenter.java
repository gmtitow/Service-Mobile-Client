package ru.bstu.it41.service.profile.change;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.Toast;

import com.activeandroid.ActiveAndroid;

import org.greenrobot.eventbus.EventBus;

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
import ru.bstu.it41.service.any.UserRequest;
import ru.bstu.it41.service.models.Status;
import ru.bstu.it41.service.models.StatusPhoto;
import ru.bstu.it41.service.serviceAPI;

import static ru.bstu.it41.service.any.UserRequest.STATUS_OK;

/**
 * Created by Герман on 18.10.2017.
 */

public class ProfileChangeFragmentPresenter extends ReampPresenter<ProfileChangeFragmentState> implements Serializable {
    public SimpleDateFormat mFormatForDate = new SimpleDateFormat("dd MMMM yyyy", Locale.getDefault());

    @Override
    public void onPresenterCreated() {
        super.onPresenterCreated();

        //getStateModel().setCurrentFragment(0);

        //getStateModel().setEnabledButton(true);

        sendStateModel();
    }

    public void saveViewChanges() {
        getStateModel().setSaveButtonEnabled(false);

        getStateModel().setShowProgress(true);

        sendStateModel();

        serviceAPI api = Controller.getGsonAPI();
        Date date;
        if (getStateModel().getTemporaryUserInfo().getBirthday() != null)
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
                            if (response.body().getStatus().equals(STATUS_OK)) {
                                EventBus.getDefault().postSticky(true);
                                getStateModel().getTemporaryUserInfo().save();
                                if(!getStateModel().isDownloadPhoto())
                                    loadPhotoOnServer();
                                else
                                    ((Activity)getView().getContext()).finish();
                            } else {
                                Toast.makeText(getView().getContext(), R.string.profile_change_error, Toast.LENGTH_SHORT).show();
                                sendStateModel();
                            }


                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        getStateModel().setShowProgress(false);
                        getStateModel().setSaveButtonEnabled(true);
                    }

                    @Override
                    public void onFailure(Call<Status> call, Throwable t) {
                        getStateModel().setShowProgress(false);
                        Toast.makeText(getView().getContext(), R.string.profile_change_connection_error, Toast.LENGTH_SHORT).show();
                        getStateModel().setSaveButtonEnabled(true);
                        sendStateModel();
                    }
                });

    }

    public void loadPhotoOnServer() {
        File file = getPhotoFile(getStateModel().getFileName());

        RequestBody requestFile =
                RequestBody.create(MediaType.parse("multipart/form-data"), file);

        MultipartBody.Part body =
                MultipartBody.Part.createFormData(UserRequest.PARAM_IMAGE, file.getName(), requestFile);

        serviceAPI api = Controller.getGsonAPI();
        api.loadPhoto(body)
                .enqueue(new Callback<StatusPhoto>() {
                    @Override
                    public void onResponse(Call<StatusPhoto> call, Response<StatusPhoto> response) {

                        if(response.body().getStatus().equals(STATUS_OK)) {
                            EventBus.getDefault().postSticky(true);
                            getStateModel().getTemporaryUserInfo().setPathToPhoto(response.body().getPathToPhoto());
                            getStateModel().getTemporaryUserInfo().save();

                            getStateModel().setShowProgress(false);
                            getStateModel().setSaveButtonEnabled(true);

                            Toast.makeText(getView().getContext(), R.string.profile_change_success, Toast.LENGTH_SHORT).show();

                            //getStateModel().setToViewPofile(true);
                            ((Activity) getView().getContext()).finish();
                        }
                        else{
                            if(response.body().getError().get(0)!= null && !response.body().getError().get(0).trim().equals("")){
                                Toast.makeText(getView().getContext(),response.body().getError().get(0) , Toast.LENGTH_SHORT).show();
                            }
                            else
                                Toast.makeText(getView().getContext(), R.string.profile_change_photo_error , Toast.LENGTH_SHORT).show();
                        }
                        getStateModel().setShowProgress(false);
                        getStateModel().setSaveButtonEnabled(true);
                        sendStateModel();
                    }

                    @Override
                    public void onFailure(Call<StatusPhoto> call, Throwable t) {
                        getStateModel().setShowProgress(false);
                        getStateModel().setSaveButtonEnabled(true);

                        Toast.makeText(getView().getContext(), R.string.profile_change_photo_error, Toast.LENGTH_SHORT).show();
                        sendStateModel();
                    }
                });
    }

    public void savePhotoToFile(Bitmap mBitmap, String fileName) {
        File file = new File(getView().getContext().getCacheDir(), fileName);
        try {
            FileOutputStream fOut = new FileOutputStream(file);

            mBitmap.compress(Bitmap.CompressFormat.JPEG, 85, fOut);

            fOut.flush();
            fOut.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public File getPhotoFile(String fileName) {
        File filesDir = getView().getContext().getCacheDir();
        return new File(filesDir, fileName);
    }

    public void updateView() {
        sendStateModel();
    }
}