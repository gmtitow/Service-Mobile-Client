package ru.bstu.it41.service.tasks.map;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.location.Address;
import android.location.Geocoder;
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
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.PixelCopy;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.yandex.mapkit.GeoObjectCollection;
import com.yandex.mapkit.MapKitFactory;
import com.yandex.mapkit.ScreenPoint;
import com.yandex.mapkit.geometry.Point;
import com.yandex.mapkit.layers.GeoObjectTapEvent;
import com.yandex.mapkit.layers.GeoObjectTapListener;
import com.yandex.mapkit.map.CameraPosition;
import com.yandex.mapkit.map.InputListener;
import com.yandex.mapkit.map.Map;
import com.yandex.mapkit.map.MapObject;
import com.yandex.mapkit.map.MapObjectCollection;
import com.yandex.mapkit.map.PlacemarkMapObject;
import com.yandex.mapkit.map.VisibleRegionUtils;
import com.yandex.mapkit.mapview.MapView;
import com.yandex.mapkit.search.Response;
import com.yandex.mapkit.search.SearchManager;
import com.yandex.mapkit.search.SearchManagerType;
import com.yandex.mapkit.search.SearchOptions;
import com.yandex.mapkit.search.Session;
import com.yandex.mapkit.user_location.UserLocationLayer;
import com.yandex.runtime.Error;
import com.yandex.runtime.image.ImageProvider;
import com.yandex.runtime.network.NetworkError;
import com.yandex.runtime.network.RemoteError;

import java.util.List;

import ru.bstu.it41.service.R;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;
import static android.graphics.Paint.ANTI_ALIAS_FLAG;

/**
 * Created by Герман on 29.04.2018.
 */

public class ChooseLocationFragment extends Fragment implements Session.SearchListener {

    private final String MAPKIT_API_KEY = "16f543ac-4888-4e1f-b32d-a3eaf5ab1e00";
    private final int REQUEST_LOCATION = 187;
    public static final String EXTRA_LONGITUDE = "ru.bstu.it41.service.tasks.map.choice_longitude";
    public static final String EXTRA_LATITUDE = "ru.bstu.it41.service.tasks.map.choice_latitude";
    public static final String EXTRA_ADDRESS = "ru.bstu.it41.service.tasks.map.choice_address";


    public static final String EXTRA_CHANGE = "ru.bstu.it41.service.tasks.map.change";

    MapView mapView;
    private Point curPoint = null;
    private Address curAddr = null;

    private SearchManager searchManager;
    private Session searchSession;

    private void submitQuery(String query) {
        searchSession = searchManager.submit(
                query,
                VisibleRegionUtils.toPolygon(mapView.getMap().getVisibleRegion()),
                new SearchOptions(),
                this);
    }


    static public ChooseLocationFragment getInstance(Double latitude, Double longitude,String address) {
        ChooseLocationFragment fragment = new ChooseLocationFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(EXTRA_LONGITUDE, longitude);
        bundle.putSerializable(EXTRA_LATITUDE, latitude);
        bundle.putSerializable(EXTRA_ADDRESS, address);
        fragment.setArguments(bundle);
        return fragment;
    }

    static public ChooseLocationFragment getInstance(Double latitude, Double longitude,String address, boolean nochange) {
        ChooseLocationFragment fragment = new ChooseLocationFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(EXTRA_LONGITUDE, longitude);
        bundle.putSerializable(EXTRA_LATITUDE, latitude);
        bundle.putSerializable(EXTRA_ADDRESS, address);
        bundle.putBoolean(EXTRA_CHANGE, nochange);
        fragment.setArguments(bundle);
        return fragment;
    }

    static public ChooseLocationFragment getInstance(Bundle bundle) {
        ChooseLocationFragment fragment = new ChooseLocationFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        MapKitFactory.setApiKey(MAPKIT_API_KEY);
        MapKitFactory.initialize(getContext());
        View v = inflater.from(getActivity()).inflate(R.layout.choice_location_dialog, null);

        setHasOptionsMenu(true);


        searchManager = MapKitFactory.getInstance().createSearchManager(SearchManagerType.COMBINED);

        mapView = v.findViewById(R.id.mapview);
        MapKitFactory.initialize(getContext());
        super.onCreate(savedInstanceState);
        boolean nochange = false;

        if(getArguments()!=null && getArguments().containsKey(EXTRA_CHANGE)){
            nochange = getArguments().getBoolean(EXTRA_CHANGE);
        }


        Button mButtonCancel = v.findViewById(R.id.button_cancel);

        mButtonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().setResult(RESULT_CANCELED);
                getActivity().finish();
            }
        });

        Button mButtonOK = v.findViewById(R.id.button_ok);

        mButtonOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.putExtra(EXTRA_LATITUDE, curPoint.getLatitude());
                intent.putExtra(EXTRA_LONGITUDE, curPoint.getLongitude());

                getActivity().setResult(RESULT_OK, intent);
                getActivity().finish();
            }
        });

        if(!nochange) {
            getActivity().setTitle(R.string.title_task_location);
            mapView.getMap().addInputListener(new InputListener() {
                @Override
                public void onMapTap(Map map, Point point) {
                    setTaskPoint(point.getLatitude(), point.getLongitude());
                    mapView.getMap().getMapObjects().clear();
                    showObject();
                }

                @Override
                public void onMapLongTap(Map map, Point point) {

                }
            });
        }
        else{
            getActivity().setTitle(R.string.title_location);
            mButtonOK.setVisibility(View.GONE);
            mButtonCancel.setVisibility(View.GONE);
        }

        mapView.getMap().setRotateGesturesEnabled(false);

        if (savedInstanceState != null) {
            Double lat = (Double) savedInstanceState.getSerializable(EXTRA_LATITUDE);
            Double lon = (Double) savedInstanceState.getSerializable(EXTRA_LONGITUDE);

            setTaskPoint(lat,lon);

        } else {
            if (getArguments() != null && getArguments().containsKey(EXTRA_LONGITUDE)
                    && getArguments().containsKey(EXTRA_LATITUDE)) {
                Double lat = (Double) getArguments().getSerializable(EXTRA_LATITUDE);
                Double lon = (Double) getArguments().getSerializable(EXTRA_LONGITUDE);
                String address = (String)getArguments().getSerializable(EXTRA_ADDRESS);

                if (lon != null && lat != null) {
                    setTaskPoint(lat,lon);
                }else if(address!=null && !address.equals("")) {
                    setAddress(address);
                }
            }
        }

        //mapObjects = mapView.getMap().getMapObjects().addCollection();
        if (curPoint == null) {
// Создаём менеджер местоположения и "слушателя"
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
                curPoint = new Point(0, 0);
                mapView.getMap().move(new CameraPosition(curPoint, 14, 0, 0));
                showObject();
            }
        } else {
            mapView.getMap().move(new CameraPosition(curPoint, 14, 0, 0));
            showObject();
        }



        return v;
    }

    private void setAddress(String address){
        Geocoder geocoder = new Geocoder(getContext().getApplicationContext());
        List<Address> addresses = null;
        try {
            addresses = geocoder.getFromLocationName(address, 1);
        }catch(Exception e){
            e.printStackTrace();
        }

        if(addresses == null || addresses.size() == 0)
            Toast.makeText(getContext(),R.string.notify_point_not_found,Toast.LENGTH_SHORT).show();
        else
        {
            curAddr = addresses.get(0);
            curPoint = new Point(curAddr.getLatitude(),curAddr.getLongitude());
            moveCameraToPoint(curPoint);
        }
    }

    private void moveCameraToPoint(Point point) {
        mapView.getMap().move(new CameraPosition(point, mapView.getMap().getCameraPosition().getZoom(), mapView.getMap().getCameraPosition().getAzimuth(),
                mapView.getMap().getCameraPosition().getTilt()));
    }

    private void setTaskPoint(double latitude, double longitude) {
        curPoint = new Point(latitude,longitude);
    }

    private void setTaskPoint(Point point) {
        curPoint = new Point(point.getLatitude(),point.getLongitude());
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putSerializable(EXTRA_LONGITUDE, curPoint.getLongitude());
        outState.putSerializable(EXTRA_LATITUDE, curPoint.getLatitude());
        super.onSaveInstanceState(outState);
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
                public void onLocationChanged(android.location.Location location) {

                }
            };
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 10, locationListener);
            // Получаем свои координаты (можно использовать LocationManager.NETWORK_PROVIDER, для иного способа определения позиции)
            Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            //curPoint = new Point(location.getLatitude(), location.getLongitude());
            setTaskPoint(location.getLatitude(), location.getLongitude());
            mapView.getMap().move(new CameraPosition(curPoint, 14, 0, 0));
            showObject();
        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }

    public void showObject() {
        PlacemarkMapObject mark = mapView.getMap().getMapObjects().addPlacemark(curPoint);
        mark.setIcon(ImageProvider.fromResource(getContext(), R.drawable.ic_place));
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        inflater.inflate(R.menu.location_choose_menu, menu);
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
    public void onSearchResponse(Response response) {
        try {
            MapObjectCollection mapObjects = mapView.getMap().getMapObjects();
            mapObjects.clear();

            Point position = mapView.getMap().getCameraPosition().getTarget();
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
                setTaskPoint(response.getCollection().getChildren().get(minDistanceResult).getObj().getGeometry().get(0).getPoint());
                mapObjects.clear();
                showObject();
                moveCameraToPoint(curPoint);
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    //

    /*public static double CalculationDistance(LatLng StartP, LatLng EndP) {
        return CalculationDistanceByCoord(StartP.latitude, StartP.longitude, EndP.latitude, EndP.longitude);
    }

    private static double CalculationDistanceByCoord(double startPointLat,double startPointLon,double endPointLat,double endPointLon){
        float[] results = new float[1];
        Location.distanceBetween(startPointLat, startPointLon, endPointLat, endPointLon, results);
        return results[0];
    }*/

    //
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
}
