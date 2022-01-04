package ru.bstu.it41.service.profile.change;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.Spinner;

import com.activeandroid.query.Select;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;

import etr.android.reamp.mvp.ReampFragment;
import etr.android.reamp.mvp.ReampPresenter;
import ru.bstu.it41.service.MainActivity;
import ru.bstu.it41.service.R;
import ru.bstu.it41.service.any.DataStore;
import ru.bstu.it41.service.any.PictureUtils;
import ru.bstu.it41.service.any.UserRequest;
import ru.bstu.it41.service.models.Userinfo;
import ru.bstu.it41.service.profile.ChoicePhotoFragment;

import static android.app.Activity.RESULT_OK;


public class ProfileChangeFragment extends ReampFragment<ProfileChangeFragmentPresenter, ProfileChangeFragmentState> {

    private int currentTitle = R.string.title_profile_change;
    private static final int REQUEST_CHOICE = 1;
    private static final String DIALOG_CHOICE = "DialogChoice";
    private int CAMERA_RESULT = 2;
    private int GALLERY_RESULT = 3;

    private EditText mEditFirstname;
    private EditText mEditLastname;
    private EditText mEditPatronymic;
    private ImageView mImagePhoto;
    private Button mEditBirthday;
    private Spinner mListMale;
    private EditText mEditAddress;
    private Button mButtonChangePhoto;
    private ProgressBar mProgressBar;
    private ScrollView mScrollView;

    private Button mButtonSaveChanges;
    //
    private Fragment mThisFragment = this;
    //
    DatePickerDialog.OnDateSetListener mOnDateSetListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            Calendar birthday = Calendar.getInstance();
            birthday.set(Calendar.YEAR, year);
            birthday.set(Calendar.MONTH, month);
            birthday.set(Calendar.DAY_OF_MONTH, dayOfMonth);

            getPresenter().getStateModel().getTemporaryUserInfo().setBirthday(birthday.getTime());

            getPresenter().updateView();
        }
    };


    @Override
    public void onStateChanged(ProfileChangeFragmentState stateModel) {
        getActivity().setTitle(currentTitle);

        mProgressBar.setVisibility(stateModel.isShowProgress() ? View.VISIBLE : View.GONE);

        mEditFirstname.setText(stateModel.getTemporaryUserInfo().getFirstname());
        mEditLastname.setText(stateModel.getTemporaryUserInfo().getLastname());
        mEditPatronymic.setText(stateModel.getTemporaryUserInfo().getPatronymic());

        mEditBirthday.setText(stateModel.getTemporaryUserInfo().getBirthday() != null ?
                getPresenter().mFormatForDate.format(stateModel.getTemporaryUserInfo().getBirthday().getTime())
                : "не указано");
        //mEditMale.setText(stateModel.getMale()==1?"Мужской":"Женский");
        mListMale.setSelection(stateModel.getTemporaryUserInfo().getMale() ^ 1);

        mEditAddress.setText(stateModel.getTemporaryUserInfo().getAddress());

        mButtonSaveChanges.setEnabled(stateModel.isSaveButtonEnabled());

        if (stateModel.isDownloadPhoto()) {
            //Picasso.with(getActivity()).invalidate(UserRequest.URL_SERVER + stateModel.getTemporaryUserInfo().getPathToPhoto());
            getPresenter().getPhotoFile(stateModel.getFileName()).delete();
            //stateModel.setDownloadPhoto(false);

            if (stateModel.getTemporaryUserInfo().getPathToPhoto() != null &&
                    !stateModel.getTemporaryUserInfo().getPathToPhoto().trim().equals("")) {
                Picasso.with(getView().getContext())
                        .load(UserRequest.URL_SERVER + stateModel.getTemporaryUserInfo().getPathToPhoto())
                        .memoryPolicy(MemoryPolicy.NO_CACHE)
                        .placeholder(R.drawable.ic_menu_camera)
                        .into(mImagePhoto);
            }
        }
        else {
            if (BitmapFactory.decodeFile(getPresenter().getPhotoFile(stateModel.getFileName()).getPath()) != null)
                mImagePhoto.setImageBitmap(PictureUtils.getScaledBitmap(getPresenter().getPhotoFile(stateModel.getFileName()).getPath(), getActivity()));
            else
                mImagePhoto.setImageResource(R.drawable.ic_menu_camera);
        }
    }

    public ProfileChangeFragment() {
        // Required empty public constructor
    }

    @Override
    public ReampPresenter<ProfileChangeFragmentState> onCreatePresenter() {
            return new ProfileChangeFragmentPresenter();
    }

    @Override
    public ProfileChangeFragmentState onCreateStateModel() {
        ProfileChangeFragmentState state = new ProfileChangeFragmentState();

        Userinfo userinfo = new Select().from(Userinfo.class).where("userId = ?",
                DataStore.getUserId(getContext().getApplicationContext())).executeSingle();
        state.setTemporaryUserInfo(userinfo);

        return state;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile_change, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        mImagePhoto = view.findViewById(R.id.image_photo);
        mProgressBar = view.findViewById(R.id.progress);
        mEditFirstname = view.findViewById(R.id.edit_firstname);
        mEditLastname = view.findViewById(R.id.edit_lastname);
        mEditPatronymic = view.findViewById(R.id.edit_patronymic);
        mEditBirthday = view.findViewById(R.id.edit_birthday);
        mListMale = view.findViewById(R.id.edit_male);
        mScrollView = view.findViewById(R.id.scrollView);

        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, new String[]{
                "Мужской", "Женский"

        });
        mListMale.setAdapter(adapter);

        mListMale.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                getPresenter().getStateModel().getTemporaryUserInfo().setMale((byte) (i ^ 1));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });


        mEditAddress = view.findViewById(R.id.edit_address);

        mButtonSaveChanges = view.findViewById(R.id.buttonSaveChanges);

        mButtonSaveChanges.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getPresenter().saveViewChanges();
                //mScrollView.scrollBy(0,100);
            }
        });

        mEditAddress.addTextChangedListener(new MainActivity.SimpleTextWatcher() {
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                getPresenter().getStateModel().getTemporaryUserInfo().setAddress(charSequence.toString());
            }
        });

        mEditFirstname.addTextChangedListener(new MainActivity.SimpleTextWatcher() {
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                getPresenter().getStateModel().getTemporaryUserInfo().setFirstname(charSequence.toString());
            }
        });
        mEditLastname.addTextChangedListener(new MainActivity.SimpleTextWatcher() {
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                getPresenter().getStateModel().getTemporaryUserInfo().setLastname(charSequence.toString());
            }
        });
        mEditPatronymic.addTextChangedListener(new MainActivity.SimpleTextWatcher() {
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                getPresenter().getStateModel().getTemporaryUserInfo().setPatronymic(charSequence.toString());
            }
        });
        mEditBirthday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getPresenter().getStateModel().getTemporaryUserInfo().getBirthday() != null)
                    new DatePickerDialog(getContext(), mOnDateSetListener,
                            getPresenter().getStateModel().getTemporaryUserInfo().getBirthday().get(Calendar.YEAR),
                            getPresenter().getStateModel().getTemporaryUserInfo().getBirthday().get(Calendar.MONTH),
                            getPresenter().getStateModel().getTemporaryUserInfo().getBirthday().get(Calendar.DAY_OF_MONTH))
                            .show();
                else {
                    Calendar calendar = Calendar.getInstance();
                    new DatePickerDialog(getContext(), mOnDateSetListener,
                            calendar.get(Calendar.YEAR) - 25,
                            1,
                            1)
                            .show();
                }
            }
        });

        mButtonChangePhoto = view.findViewById(R.id.buttonChangePhoto);

        mButtonChangePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager manager = getFragmentManager();
                ChoicePhotoFragment dialog = new ChoicePhotoFragment();
                dialog.setTargetFragment(mThisFragment, REQUEST_CHOICE);
                //dialog.setTitle("Выберите действие");


                dialog.show(manager, DIALOG_CHOICE);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CHOICE) {
            if (resultCode == RESULT_OK) {
                int choice = data.getIntExtra(ChoicePhotoFragment.EXTRA_CHOICE, ChoicePhotoFragment.GALLERY);

                if (choice == ChoicePhotoFragment.GALLERY) {
                    Intent photoIntent = new Intent(Intent.ACTION_PICK);
                    photoIntent.setType("image/*");
                    startActivityForResult(photoIntent, GALLERY_RESULT);
                } else if (choice == ChoicePhotoFragment.CAMERA) {

                    Intent photoIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);


                    photoIntent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(getActivity().getExternalCacheDir(), "tmp")));

                    startActivityForResult(photoIntent, CAMERA_RESULT);
                    //}
                }

                //Bitmap thumbnailBitmap = (Bitmap) data.getExtras().get("data");

                //mImagePhoto.setImageBitmap(thumbnailBitmap);
            }
        } else if (requestCode == GALLERY_RESULT) {
            if (resultCode == RESULT_OK) {

                Uri selectedImage = data.getData();
                Bitmap bitmap = null;
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), selectedImage);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                File file = getPresenter().getPhotoFile("tempFile");

                //bitmap = PictureUtils.getCompressedBitmap(bitmap,file);

                //bitmap = ThumbnailUtils.extractThumbnail(bitmap, 400, 400);
                getPresenter().savePhotoToFile(bitmap, getPresenter().getStateModel().getFileName());
                getPresenter().getStateModel().setDownloadPhoto(false);
                bitmap.recycle();

                file.delete();
            }
        } else if (requestCode == CAMERA_RESULT) {
            if (resultCode == RESULT_OK) {

                File fi = new File(getActivity().getExternalCacheDir(), "tmp");
                Bitmap bm = BitmapFactory.decodeFile(fi.getPath());

                File file = getPresenter().getPhotoFile("tempFile");

                //bm = PictureUtils.getCompressedBitmap(bm,file);

                getPresenter().savePhotoToFile(bm, getPresenter().getStateModel().getFileName());
                if (!fi.delete()) {
                    Log.i("logMarker", "Failed to delete " + fi);
                }
                getPresenter().getStateModel().setDownloadPhoto(false);
                bm.recycle();
                file.delete();
            }
        }
    }
}
