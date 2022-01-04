package ru.bstu.it41.service;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.ActionMenuView;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.TextWatcher;
import android.util.Log;
import android.view.SubMenu;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.activeandroid.query.Select;
import com.google.firebase.iid.FirebaseInstanceId;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.io.Serializable;
import java.util.List;

import etr.android.reamp.mvp.ReampAppCompatActivity;
import etr.android.reamp.mvp.ReampPresenter;
import ru.bstu.it41.service.any.DataStore;
import ru.bstu.it41.service.any.FragmentRetainer;
import ru.bstu.it41.service.any.UserRequest;
import ru.bstu.it41.service.models.AllForUser;
import ru.bstu.it41.service.models.Offer;
import ru.bstu.it41.service.models.Settings;
import ru.bstu.it41.service.models.User;
import ru.bstu.it41.service.models.Userinfo;
import ru.bstu.it41.service.offers.OffersFragment;
import ru.bstu.it41.service.offers.view.OfferViewActivity;
import ru.bstu.it41.service.profile.ProfileFragment;
import ru.bstu.it41.service.tasks.TaskFragment;
import ru.bstu.it41.service.login.LoginFragment;
import ru.bstu.it41.service.register.RegisterFragment;
import ru.bstu.it41.service.tasks.view.TaskViewActivity;
import ru.bstu.it41.service.tenders.TenderFragment;
import ru.bstu.it41.service.tenders.pager.TenderPagerFragment;
import ru.bstu.it41.service.tenders.tendersMap.TendersMapFragment;


public class MainActivity extends ReampAppCompatActivity<MainActivityPresenter, MainActivityState>
        implements NavigationView.OnNavigationItemSelectedListener, Serializable {

    static public boolean sIsCoordination;
    static public int sTaskId;
    static public int sOfferId;
    static public boolean sIsClient;
    private final String TAG = "mainTag";

    public final String STACK_MAIN = "myStack";
    public final String STACK_LOCAL = "localStack";
    MenuItem mPreviousItem;

    TextView mTextFIO;
    ImageView mImagePhoto;

    MenuItem item_exit;
    MenuItem item_enter;
    MenuItem item_register;

    MenuItem mItemTasks;
    MenuItem mItemOffers;
    MenuItem mItemProfile;

    MenuItem mItemExecutedTasks;
    MenuItem mItemSelectedOffers;

     @Override
    public void onStateChanged(MainActivityState stateModel) {
        if (!stateModel.isCategoryDownloaded() && !stateModel.isCategoryDownloadInProgress())
            getPresenter().downloadCategories();

        item_enter.setVisible(!stateModel.getAuthorized());
        item_register.setVisible(!stateModel.getAuthorized());

        item_exit.setVisible(stateModel.getAuthorized());
        mItemProfile.setVisible(stateModel.getAuthorized());
        mItemTasks.setVisible(stateModel.getAuthorized());
        mItemOffers.setVisible(stateModel.getAuthorized());
        mItemExecutedTasks.setVisible(stateModel.getAuthorized());
        mItemSelectedOffers.setVisible(stateModel.getAuthorized());

        if(stateModel.getUserinfo()!= null){
            mTextFIO.setText(
                    (stateModel.getUserinfo().getUserinfo().getFirstname() == null ? "" : stateModel.getUserinfo().getUserinfo().getFirstname()) +
                            " " + (stateModel.getUserinfo().getUserinfo().getLastname() == null ? "" : stateModel.getUserinfo().getUserinfo().getLastname()));


            if (stateModel.getUserinfo().getUserinfo().getPathToPhoto() != null &&
                    !stateModel.getUserinfo().getUserinfo().getPathToPhoto().trim().equals("")) {

                //Picasso.with(getContext()).invalidate(UserRequest.URL_SERVER + stateModel.getUserinfo().getPathToPhoto());

                Picasso.with(getContext()) //передаем контекст приложения
                        .load(UserRequest.URL_SERVER + stateModel.getUserinfo().getUserinfo().getPathToPhoto())
                        .networkPolicy(NetworkPolicy.NO_CACHE)
                        .memoryPolicy(MemoryPolicy.NO_CACHE)
                        .placeholder(R.drawable.ic_menu_camera)
                        .into(mImagePhoto);
            }
        }
        else{
            mTextFIO.setText("");
            mImagePhoto.setImageResource(R.drawable.ic_menu_camera);
        }
    }

    @Override
    public ReampPresenter<MainActivityState> onCreatePresenter() {
        return new MainActivityPresenter();
    }

    @Override
    public MainActivityState onCreateStateModel() {

        MainActivityState state = new MainActivityState();
        state.setController(new Controller(this));
        if (DataStore.getUserId(this.getApplicationContext()) != -1) {
            state.setAuthorized(true);
            state.setCategoryDownloaded(true);

            int userId = DataStore.getUserId(this.getApplicationContext());

            AllForUser allForUser = new AllForUser();
            allForUser.setUserinfo(new Select().from(Userinfo.class).where("userId = ?",userId).executeSingle());
            allForUser.setUser(new Select().from(User.class).where("userId = ?",userId).executeSingle());
            allForUser.setSettings(new Select().from(Settings.class).where("userId = ?",userId).executeSingle());

            state.setUserinfo(allForUser);
        }

        return state;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();


        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);

        //View navHeaderMain = navigationView.inflateHeaderView(R.layout.nav_header_main);

        View navHeaderMain2  = navigationView.getHeaderView(0);

        navigationView.setNavigationItemSelectedListener(this);

        navigationView.setNavigationItemSelectedListener(this);
        Menu mMenu = navigationView.getMenu();

        //
        mTextFIO = navHeaderMain2.findViewById(R.id.text_fio);
        mImagePhoto= navHeaderMain2.findViewById(R.id.image_photo);


        item_exit = mMenu.findItem(R.id.nav_exit);

        //item_exit.setTitle(wrapInSpan("Выйти"));
        item_enter = mMenu.findItem(R.id.nav_enter);
        item_register = mMenu.findItem(R.id.nav_register);
        mItemProfile = mMenu.findItem(R.id.nav_profile);
        mItemTasks = mMenu.findItem(R.id.nav_tasks);
        mItemOffers = mMenu.findItem(R.id.nav_offers);

        mItemExecutedTasks = mMenu.findItem(R.id.nav_executed_tasks);
        mItemSelectedOffers = mMenu.findItem(R.id.nav_selected_offers);

        mPreviousItem = item_exit;

        if (getPresenter().getStateModel().isFirst()) {
            getPresenter().getStateModel().setFirst(false);
            Bundle extras = getIntent().getExtras();
            if (extras != null) {
                Fragment fragm = FragmentRetainer.getFragment(getIntent().getIntExtra(FragmentRetainer.EXTRA_FRAGMENT, 0),extras);
                replaceFragment(fragm,
                        null, STACK_MAIN, R.id.container, true);

            } else {
                Fragment fragm = FragmentRetainer.getFragment(FragmentRetainer.FRAGMENT_LOGIN,null);
                replaceFragment(fragm,
                        null, STACK_MAIN, R.id.container, true);
            }
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        //FragmentManager fragmentManager = getSupportFragmentManager();
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }
        else{
            if(sIsCoordination){
                if(sIsClient){
                    Intent intent = TaskViewActivity.getIntent(getContext(),sTaskId);
                    startActivity(intent);
                }
                else{
                    Intent intent = OfferViewActivity.getIntent(getContext(),sOfferId);
                    startActivity(intent);
                }

            }else {
                finish();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        Fragment fragment = null;
        Class fragmentClass = null;
        int titleId = 0;

        switch (id) {
            case R.id.nav_register: {
                fragment = new RegisterFragment();
                titleId = R.string.title_register;
                break;
            }

            case R.id.nav_enter: {
                titleId = R.string.title_authorization;
                fragment = new LoginFragment();
                break;
            }

            case R.id.nav_view: {

                break;
            }
            case R.id.nav_exit: {
                getPresenter().clearTokens();
                getPresenter().getStateModel().setAuthorized(false);
                getPresenter().getStateModel().setUserinfo(null);
                DataStore.clearPreferences(this.getApplicationContext());
                //getPresenter().exit();
                getPresenter().sendStateModel();
                fragment = new LoginFragment();
                titleId = R.string.title_authorization;
                break;
            }
            case R.id.nav_profile: {
                fragment = new ProfileFragment();
                titleId = R.string.title_profile;
                break;
            }
            case R.id.nav_tasks: {
                fragment = new TaskFragment();
                titleId = R.string.title_tasks;
                break;
            }
            case R.id.nav_offers: {
                fragment = new OffersFragment();
                titleId = R.string.title_offers;
                break;
            }

            case R.id.nav_tenders: {
                fragment = new TenderFragment();
                titleId = R.string.title_tenders;
                break;
            }
            case R.id.nav_executed_tasks:{
                fragment = TaskFragment.getInstanceExecuted();
                titleId = R.string.title_tasks;
                break;
            }
            case R.id.nav_selected_offers:{
                fragment = OffersFragment.getInstanceExecuted();
                titleId = R.string.title_offers;
                break;
            }
        }
        try {
            replaceFragment(fragment, null, STACK_MAIN, R.id.container, false, titleId);

            item.setChecked(true);
            mPreviousItem.setChecked(false);
            mPreviousItem = item;
        } catch (Exception e) {
            Log.e("act_main", "ошибка при выборе фрагмента:");
            e.printStackTrace();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public static class SimpleTextWatcher implements TextWatcher {

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    }

    public void replaceFragment(Class fragmentClass, Bundle bundle, String stackName, int container) {
        try {
            android.support.v4.app.Fragment fragment = (android.support.v4.app.Fragment) fragmentClass.newInstance();
            if (bundle != null)
                fragment.setArguments(bundle);
            FragmentManager fragmentManager = getSupportFragmentManager();
            if (stackName != null) {
                fragmentManager.beginTransaction().replace(container, fragment).addToBackStack("myStack").commit();
            } else
                fragmentManager.beginTransaction().replace(container, fragment).commit();

        } catch (Exception e) {
            Log.e("act_main", "ошибка при выборе фрагмента в профиле:");
            e.printStackTrace();
        }

    }

    public void replaceFragment(Class fragmentClass, Bundle bundle, String stackName, int container, boolean popFromBackStack) {
        try {
            android.support.v4.app.Fragment fragment = (android.support.v4.app.Fragment) fragmentClass.newInstance();
            if (bundle != null)
                fragment.setArguments(bundle);

            FragmentManager fragmentManager = getSupportFragmentManager();

            if (popFromBackStack)
                fragmentManager.popBackStack();
            if (stackName != null) {
                fragmentManager.beginTransaction().replace(container, fragment).addToBackStack("myStack").commit();
            } else
                fragmentManager.beginTransaction().replace(container, fragment).commit();

        } catch (Exception e) {
            Log.e("act_main", "ошибка при выборе фрагмента в профиле:");
            e.printStackTrace();
        }

    }

    public void replaceFragment(Fragment fragment, Bundle bundle, String stackName, int container, boolean popFromBackStack) {
        try {
            if (bundle != null) {
                Bundle existsBundle = fragment.getArguments();
                existsBundle.putAll(bundle);
                fragment.setArguments(existsBundle);
            }

            FragmentManager fragmentManager = getSupportFragmentManager();

            if (popFromBackStack)
                fragmentManager.popBackStack();
            if (stackName != null) {
                fragmentManager.beginTransaction().replace(container, fragment).addToBackStack("myStack").commit();
            } else
                fragmentManager.beginTransaction().replace(container, fragment).commit();

        } catch (Exception e) {
            Log.e("act_main", "ошибка при выборе фрагмента в профиле:");
            e.printStackTrace();
        }

    }

    public void replaceFragment(Fragment fragment, Bundle bundle, String stackName, int container, boolean popFromBackStack,
                                int titleId) {
        try {
            if (bundle != null) {
                Bundle existsBundle = fragment.getArguments();
                existsBundle.putAll(bundle);
                fragment.setArguments(existsBundle);
            }

            if (titleId == 0)
                setTitle(R.string.app_name);
            else
                setTitle(titleId);

            FragmentManager fragmentManager = getSupportFragmentManager();

            if (popFromBackStack)
                fragmentManager.popBackStack();
            if (stackName != null) {
                fragmentManager.beginTransaction().replace(container, fragment).addToBackStack("myStack").commit();
            } else
                fragmentManager.beginTransaction().replace(container, fragment).commit();

        } catch (Exception e) {
            Log.e("act_main", "ошибка при выборе фрагмента в профиле:");
            e.printStackTrace();
        }

    }

    private CharSequence wrapInSpan(CharSequence value) {
        SpannableStringBuilder sb = new SpannableStringBuilder(value);
        sb.setSpan(Typeface.create("Times New Romans", Typeface.BOLD), 0, value.length(), 0);
        return sb;
    }

    private void applyFontToMenuItem(MenuItem mi) {
        Typeface font = Typeface.create("Times New Romans", Typeface.BOLD);
        SpannableString mNewTitle = new SpannableString(mi.getTitle());
        mNewTitle.setSpan(new CustomTypefaceSpan("", font), 0, mNewTitle.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        mi.setTitle(mNewTitle);
    }

    private void applyFontToMenuItem(MenuItem mi, int textSize) {
        Typeface font = Typeface.create("Times New Romans", Typeface.NORMAL);
        SpannableString mNewTitle = new SpannableString(mi.getTitle());
        mNewTitle.setSpan(new CustomTypefaceSpan("", font, textSize), 0, mNewTitle.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        mi.setTitle(mNewTitle);
    }

    private void applyFontToMenuItem(MenuItem mi, Typeface font) {
        SpannableString mNewTitle = new SpannableString(mi.getTitle());
        mNewTitle.setSpan(new CustomTypefaceSpan("", font), 0, mNewTitle.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        mi.setTitle(mNewTitle);
    }

    private void applyFontToMenuItem(MenuItem mi, Typeface font, int textSize) {
        SpannableString mNewTitle = new SpannableString(mi.getTitle());
        mNewTitle.setSpan(new CustomTypefaceSpan("", font, textSize), 0, mNewTitle.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        mi.setTitle(mNewTitle);
    }

    private void applyFontToTitle(Typeface font) {
        SpannableString mNewTitle = new SpannableString(getTitle());
        mNewTitle.setSpan(new CustomTypefaceSpan("", font), 0, mNewTitle.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        setTitle(mNewTitle);
    }

    private void applyFontToTitle(Typeface font, int textSize) {
        SpannableString mNewTitle = new SpannableString(getTitle());
        mNewTitle.setSpan(new CustomTypefaceSpan("", font, textSize), 0, mNewTitle.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        setTitle(mNewTitle);
    }
}