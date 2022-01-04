package ru.bstu.it41.service.tenders.tendersMap;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PathEffect;
import android.graphics.RectF;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.SearchView;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.activeandroid.query.Select;
import com.yandex.mapkit.GeoObjectCollection;
import com.yandex.mapkit.MapKitFactory;
import com.yandex.mapkit.ScreenPoint;
import com.yandex.mapkit.geometry.Point;
import com.yandex.mapkit.map.CameraListener;
import com.yandex.mapkit.map.CameraPosition;
import com.yandex.mapkit.map.CameraUpdateSource;
import com.yandex.mapkit.map.IconStyle;
import com.yandex.mapkit.map.InputListener;
import com.yandex.mapkit.map.Map;
import com.yandex.mapkit.map.MapObject;
import com.yandex.mapkit.map.MapObjectCollectionListener;
import com.yandex.mapkit.map.MapObjectDragListener;
import com.yandex.mapkit.map.MapObjectTapListener;
import com.yandex.mapkit.map.MapWindow;
import com.yandex.mapkit.map.PlacemarkMapObject;
import com.yandex.mapkit.map.SizeChangedListener;
import com.yandex.mapkit.map.VisibleRegionUtils;
import com.yandex.mapkit.map.internal.PlacemarkMapObjectBinding;
import com.yandex.mapkit.mapview.MapView;
import com.yandex.mapkit.search.Response;
import com.yandex.mapkit.search.SearchManager;
import com.yandex.mapkit.search.SearchManagerType;
import com.yandex.mapkit.search.SearchOptions;
import com.yandex.mapkit.search.Session;
import com.yandex.runtime.Error;
import com.yandex.runtime.image.AnimatedImageProvider;
import com.yandex.runtime.image.ImageProvider;
import com.yandex.runtime.network.NetworkError;
import com.yandex.runtime.network.RemoteError;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import etr.android.reamp.mvp.ReampFragment;
import etr.android.reamp.mvp.ReampPresenter;
import ru.bstu.it41.service.MainActivity;
import ru.bstu.it41.service.R;
import ru.bstu.it41.service.any.DataStore;
import ru.bstu.it41.service.models.Category;
import ru.bstu.it41.service.models.OtherTender;
import ru.bstu.it41.service.models.Tasks;
import ru.bstu.it41.service.models.Tender;
import ru.bstu.it41.service.models.Userinfo;
import ru.bstu.it41.service.tenders.TenderFragment;
import ru.bstu.it41.service.tenders.TenderFragmentPresenter;
import ru.bstu.it41.service.tenders.TenderFragmentState;
import ru.bstu.it41.service.tenders.tenderView.TenderViewActivity;

/**
 * Created by Герман on 29.04.2018.
 */

public class TendersMapFragment extends ReampFragment<TenderFragmentPresenter, TenderFragmentState> implements Session.SearchListener {

    private final String MAPKIT_API_KEY = "16f543ac-4888-4e1f-b32d-a3eaf5ab1e00";
    private final int REQUEST_LOCATION = 187;

    public static final String EXTRA_LONGITUDE = "ru.bstu.it41.service.tender.map.choice_longitude";
    public static final String EXTRA_LATITUDE = "ru.bstu.it41.service.tender.map.choice_latitude";


    int titleId = R.string.title_tenders;

    MapView mapView;
    private float mZoom = 0;

    private SearchManager searchManager;
    private Session searchSession;
    private boolean skipClose;

    Map map;

    List<MapObject> mapObjects = null;

    static public TendersMapFragment getInstance(Double latitude, Double longitude) {
        TendersMapFragment fragment = new TendersMapFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(EXTRA_LONGITUDE, longitude);
        bundle.putSerializable(EXTRA_LATITUDE, latitude);
        fragment.setArguments(bundle);
        return fragment;
    }

    static public TendersMapFragment getInstance(Bundle extra) {
        TendersMapFragment fragment = new TendersMapFragment();
        fragment.setArguments(extra);
        return fragment;
    }

    InputListener mInputListener = new InputListener() {
        @Override
        public void onMapTap(Map map, Point point) {
            if (!skipClose) {
                closeOpenedMark();
            } else
                skipClose = false;
        }

        @Override
        public void onMapLongTap(Map map, Point point) {

        }
    };

    CameraListener mCameraListener = new CameraListener() {
        @Override
        public void onCameraPositionChanged(Map map, CameraPosition cameraPosition, CameraUpdateSource cameraUpdateSource, boolean finished) {
            if (cameraPosition.getZoom() != mZoom) {
                showTenders(false);
                mZoom = cameraPosition.getZoom();
            }
        }
    };

    private MapObjectTapListener mMapObjectTapListener = new MapObjectTapListener() {
        @Override
        public boolean onMapObjectTap(MapObject mapObject, Point point) {
            try {
                int tenderId = (int) mapObject.getUserData();
                skipClose = true;
                if (tenderId < 0) {
                    if ((-tenderId) != getPresenter().getStateModel().getOpenedTender()) {
                        //Закрытие предыдущего открытого
                        closeOpenedMark();

                        //Далее...
                        OtherTender mTender = getPresenter().getStateModel().getTenderById(-tenderId);
                        getPresenter().getStateModel().setOpenedTender(-tenderId);

                        if (mTender != null) {
                            //PlacemarkMapObject mark = (PlacemarkMapObject) mapObject;
                            Category category = new Select().from(Category.class).where("categoryId = ?",
                                    mTender.getTasks().getCategoryId()).executeSingle();
                            String text = mTender.getTasks().getName() + "\n\r" +
                                    "Категория: " + category.getCategoryName() + "\n\r" +
                                    "Цена: " + mTender.getTasks().getPrice() + "\n\r" +
                                    "Дата: " + getPresenter().mFormatForDate.format(mTender.getTasks().getDeadline());
                            Bitmap bm = drawText(text, 28, 300, Color.BLACK);

                            for (MapObject object : mapObjects) {
                                if (((int) object.getUserData()) == -tenderId) {
                                    ((PlacemarkMapObject) object).setIcon(ImageProvider.fromBitmap(bm));
                                    break;
                                }
                            }
                        }
                    }
                    else{
                        Intent intent = TenderViewActivity.getIntent(getContext(),-tenderId);
                        startActivity(intent);
                    }
                }
            }catch(Exception e){
                Toast.makeText(getContext(),"Ошибка при обновлении карты",Toast.LENGTH_SHORT).show();
            }

            return false;
        }
    };

    private void closeOpenedMark() {
        int tenderId = getPresenter().getStateModel().getOpenedTender();

        OtherTender tender = getPresenter().getStateModel().getTenderById(tenderId);

        if(tender != null) {

            Bitmap bm = drawText(tender.getTasks().getName(), 28, 300, Color.BLACK);

            for (MapObject object : mapObjects) {
                if (((int) object.getUserData()) == tenderId) {
                    ((PlacemarkMapObject) object).setIcon(ImageProvider.fromBitmap(bm));
                    break;
                }
            }
            getPresenter().getStateModel().setOpenedTender(0);
        }
    }

    private void submitQuery(String query) {
        searchSession = searchManager.submit(
                query,
                VisibleRegionUtils.toPolygon(mapView.getMap().getVisibleRegion()),
                new SearchOptions(),
                this);
    }

    @Override
    public void onStateChanged(TenderFragmentState stateModel) {
        mapView.getMap().getMapObjects().clear();
        getActivity().setTitle(titleId);
        showTenders(true);
    }

    @Override
    public ReampPresenter<TenderFragmentState> onCreatePresenter() {
        return new TenderFragmentPresenter();
    }

    @Override
    public TenderFragmentState onCreateStateModel() {
        TenderFragmentState state = new TenderFragmentState();
        OtherTender otherTender;
        List<Tender> tenders = new Select().from(Tender.class).where("dateEnd > ?",
                Tender.mFormatFromServer.format(new Date())).execute();
        state.setTenders(new ArrayList<OtherTender>());
        for (Tender tender : tenders) {
            otherTender = new OtherTender();
            otherTender.setTender(tender);
            otherTender.setTasks(new Select().from(Tasks.class).where("taskId = ?", otherTender.getTender().getTaskId())
                    .<Tasks>executeSingle());
            otherTender.setUserinfo(new Select().from(Userinfo.class).where("userId = ?", otherTender.getTasks().getUserId())
                    .<Userinfo>executeSingle());

            if (otherTender.getUserinfo().getUserId() != DataStore.getUserId(getContext().getApplicationContext()))
                state.getTenders().add(otherTender);
        }

        return state;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        MapKitFactory.setApiKey(MAPKIT_API_KEY);
        MapKitFactory.initialize(getContext());
        View v = inflater.from(getActivity()).inflate(R.layout.fragment_tender_map, null);

        setHasOptionsMenu(true);

        getPresenter().getAllTendersFromServer();

        searchManager = MapKitFactory.getInstance().createSearchManager(SearchManagerType.COMBINED);

        mapView = v.findViewById(R.id.mapview);
        MapKitFactory.initialize(getContext());
        super.onCreate(savedInstanceState);

        map = mapView.getMap();

        map.addInputListener(mInputListener);


        map.setRotateGesturesEnabled(false);
        map.addCameraListener(mCameraListener);

        boolean skip = false;
        if (savedInstanceState != null) {
            Double lat = (double) savedInstanceState.getSerializable(EXTRA_LATITUDE);
            Double lon = (double) savedInstanceState.getSerializable(EXTRA_LONGITUDE);

            //moveCameraToPoint(new Point(lat,lon));
            map.move(new CameraPosition(new Point(lat,lon), 14, 0, 0));
            skip = true;

        } else {
            if (getArguments() != null && getArguments().containsKey(EXTRA_LONGITUDE)
                    && getArguments().containsKey(EXTRA_LATITUDE)) {
                Double lat = (double) getArguments().getSerializable(EXTRA_LATITUDE);
                Double lon = (double) getArguments().getSerializable(EXTRA_LONGITUDE);

                //moveCameraToPoint(new Point(lat,lon));
                map.move(new CameraPosition(new Point(lat,lon), 14, 0, 0));
                skip = true;
            }
        }

        if(!skip) {
            boolean granted = true;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                int permissionStatus = getActivity().checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION);
                if (permissionStatus == PackageManager.PERMISSION_GRANTED)
                    granted = true;
                else
                    granted = false;

                if (!granted) {
                    if (shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION)) {
                        Toast.makeText(getContext(), "Для перемещения к вашему текущему " +
                                        "местоположению нужно разрешение на его определение",
                                Toast.LENGTH_SHORT).show();
                    }

                    requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
                }

            }
            if (granted) {
                moveCameraToUserPosition();
            } else {
                map.move(new CameraPosition(new Point(0, 0), 14, 0, 0));
            }
        }
        return v;
    }

    private void moveCameraToPoint(Point point) {
        map.move(new CameraPosition(point, map.getCameraPosition().getZoom(), map.getCameraPosition().getAzimuth(),
                map.getCameraPosition().getTilt()));
    }

    @Override
    public void onStop() {
        mapView.onStop();
        MapKitFactory.getInstance().onStop();
        super.onStop();
    }

    @Override
    public void onStart() {
        super.onStart();
        MapKitFactory.getInstance().onStart();
        mapView.onStart();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_LOCATION) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
                moveCameraToUserPosition();
        }
    }

    private void moveCameraToUserPosition() {
        try {
            final LocationManager locationManager = (LocationManager) getContext().getSystemService(Context.LOCATION_SERVICE);

            final LocationListener locationListener = new LocationListener() {

                @Override
                public void onStatusChanged(String provider, int status, Bundle extras) {
                }

                @Override
                public void onProviderEnabled(String provider) {
                }

                @Override
                public void onProviderDisabled(String provider) {
                }

                @Override
                public void onLocationChanged(Location location) {

                }
            };
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 10, locationListener);
            // Получаем свои координаты (можно использовать LocationManager.NETWORK_PROVIDER, для иного способа определения позиции)
            Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

            map.move(new CameraPosition(new Point(location.getLatitude(), location.getLongitude()), 14, 0, 0));
            //showTenders();
        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }

    public void showTenders(boolean rebuild) {
        //mapObjects = new ArrayList<>();
        if (rebuild) {
            mapObjects = new ArrayList<>();
            for (OtherTender tender : getPresenter().getStateModel().getTenders()) {
                if (tender.getTasks().getLatitude() != null && tender.getTasks().getLongitude() != null) {

                    Point point = new Point(tender.getTasks().getLatitude(), tender.getTasks().getLongitude());
                    PlacemarkMapObject mark = map.getMapObjects().addPlacemark(
                            point, ImageProvider.fromResource(getContext(), R.drawable.ic_place));

                    Bitmap bm;
                    boolean opened = false;
                    if (tender.getTender().getTenderId() == getPresenter().getStateModel().getOpenedTender()) {

                        Category category = new Select().from(Category.class).where("categoryId = ?",
                                tender.getTasks().getCategoryId()).executeSingle();
                        String text = tender.getTasks().getName() + "\n\r" +
                                "Категория: " + category.getCategoryName() + "\n\r" +
                                "Цена: " + tender.getTasks().getPrice() + "\n\r" +
                                "Дата: " + getPresenter().mFormatForDate.format(tender.getTasks().getDeadline());
                        bm = drawText(text, 28, 300, Color.BLACK);

                        opened = true;
                    } else
                        bm = drawText(tender.getTasks().getName(), 28, 300, Color.BLACK);

                    ScreenPoint screenPoint = mapView.worldToScreen(point);

                    ScreenPoint screenPoint2 = new ScreenPoint(screenPoint.getX() + 170, screenPoint.getY() - 10);

                    Point point2 = mapView.screenToWorld(screenPoint2);
                    IconStyle style = new IconStyle();

                    PlacemarkMapObject mark2 = map.getMapObjects().addPlacemark(point2, ImageProvider.fromBitmap(bm), style);
                    mark2.setDraggable(true);

                    OtherTenderAndOther otherTender2 = new OtherTenderAndOther(tender, mark2.getGeometry());
                    otherTender2.setOpenned(opened);

                    mark2.setUserData(tender.getTender().getTenderId());

                    mark2.addTapListener(mMapObjectTapListener);

                    OtherTenderAndOther otherTender = new OtherTenderAndOther(tender, false, mark.getGeometry(), mark2);

                    mark.setUserData(-tender.getTender().getTenderId());
                    mark.addTapListener(mMapObjectTapListener);
                    mapObjects.add(mark);
                    mapObjects.add(mark2);
                }
            }
        } else{
            //Просто перемещаем надписи
            try {
                if (mapObjects != null) {
                    for (MapObject object : mapObjects) {
                        int tenderId = (int) object.getUserData();

                        if (tenderId < 0) {
                            for (MapObject objText : mapObjects) {
                                int tenderId2 = (int) objText.getUserData();

                                if (tenderId2 == (-tenderId)) {
                                    ScreenPoint screenPoint = mapView.worldToScreen(((PlacemarkMapObject) object).getGeometry());

                                    ScreenPoint screenPoint2 = new ScreenPoint(screenPoint.getX() + 170, screenPoint.getY() - 10);

                                    Point point2 = mapView.screenToWorld(screenPoint2);

                                    ((PlacemarkMapObject) objText).setGeometry(point2);
                                    break;
                                }
                            }
                        }
                    }
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putSerializable(EXTRA_LONGITUDE, map.getCameraPosition().getTarget().getLongitude());
        outState.putSerializable(EXTRA_LATITUDE, map.getCameraPosition().getTarget().getLatitude());
        super.onSaveInstanceState(outState);
    }

    public static Bitmap drawText(String text, int textSize, int maxTextWidth, int textColor) {
// Get text dimensions


        TextPaint textPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG
                | Paint.LINEAR_TEXT_FLAG);
        textPaint.setStyle(Paint.Style.FILL);
        textPaint.setColor(textColor);
        textPaint.setTextSize(textSize);

        int textWidth = Math.round(textPaint.measureText(text) + 0.5f) < maxTextWidth ?
                Math.round(textPaint.measureText(text) + 0.5f) : maxTextWidth;

        StaticLayout mTextLayout = new StaticLayout(text, textPaint,
                textWidth, Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);

        Bitmap b = Bitmap.createBitmap(textWidth + 15, mTextLayout.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(b);

// Draw background
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG
                | Paint.LINEAR_TEXT_FLAG);
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.WHITE);
        paint.setStrokeWidth(5);

        RectF rect = new RectF(0.0f, 0.0f, textWidth + 15, mTextLayout.getHeight());

        //Радиус закругления
        float radius = 15;

        c.drawRoundRect(rect, radius, radius, paint);
        //c.drawPaint(paint);

// Draw text
        c.save();
        c.translate(15, 0);
        mTextLayout.draw(c);
        c.restore();

        return b;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        inflater.inflate(R.menu.fragment_tender_map, menu);
        MenuItem searchItem = menu.findItem(R.id.menu_item_search);

        final SearchView searchView = (SearchView) searchItem.getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                //setAddress(s);
                submitQuery(s);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_item_list:
                Fragment fragment = new TenderFragment();
                ((MainActivity)getActivity()).replaceFragment(fragment,null,null,R.id.container,false);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onSearchResponse(Response response) {
        try {
            Point position = map.getCameraPosition().getTarget();
            Integer minDistanceResult = null;
            Float minDistance = null;
            int i = 0;
            for (GeoObjectCollection.Item searchResult : response.getCollection().getChildren()) {
                Point resultLocation = searchResult.getObj().getGeometry().get(0).getPoint();

                float[] results = new float[1];
                Location.distanceBetween(position.getLatitude(), position.getLongitude(),
                        resultLocation.getLatitude(), resultLocation.getLongitude(), results);

                if (minDistance == null || minDistance > results[0]) {
                    minDistanceResult = i;
                    minDistance = results[0];
                }
                i++;
            }
            if (minDistanceResult != null) {
                moveCameraToPoint(response.getCollection().getChildren().get(minDistanceResult).getObj().getGeometry().get(0).getPoint());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onSearchError(Error error) {
        String errorMessage = getString(R.string.unknown_error_message);
        if (error instanceof RemoteError) {
            errorMessage = getString(R.string.remote_error_message);
        } else if (error instanceof NetworkError) {
            errorMessage = getString(R.string.network_error_message);
        }

        Toast.makeText(getContext(), errorMessage, Toast.LENGTH_SHORT).show();
    }

    public class OtherTenderAndOther {
        private OtherTender mOtherTender;
        private boolean mOpenned = false;
        private boolean mText = false;
        //private OtherTenderAndOther textForTender = null;
        private Point mGeometry;

        private MapObject mapObjectText;

        public OtherTenderAndOther() {

        }

        public OtherTenderAndOther(OtherTender otherTender, Point point) {
            mOtherTender = otherTender;
            mText = true;
            mGeometry = point;
        }

        public OtherTenderAndOther(OtherTender otherTender, boolean text, Point point, MapObject mapObjectText) {
            mOtherTender = otherTender;
            mText = text;
            mGeometry = point;
            this.mapObjectText = mapObjectText;
        }

        public OtherTender getOtherTender() {
            return mOtherTender;
        }

        public void setOtherTender(OtherTender otherTender) {
            mOtherTender = otherTender;
        }

        public boolean isOpenned() {
            return mOpenned;
        }

        public void setOpenned(boolean openned) {
            mOpenned = openned;
        }

        /*public MapObject getMapObjectText() {
            return mMapObjectText;
        }

        public void setMapObjectText(MapObject mapObjectText) {
            mMapObjectText = mapObjectText;
        }*/

        public Point getGeometry() {
            return mGeometry;
        }

        public void setGeometry(Point geometry) {
            mGeometry = geometry;
        }
    }
}
