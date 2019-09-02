package com.example.macbookpro.tracker;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.GeoDataClient;
import com.google.android.gms.location.places.PlaceDetectionClient;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MyMapFragment extends Fragment implements
        OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, LocationListener {

    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    SupportMapFragment mapFragment;
    GoogleMap mMap;
    List<LatLng> poly;
    LocationManager myLocalManager;
    private boolean mLocationPermissionGranted = false;
    private Location mLastKnownLocation;
    private int DEFAULT_ZOOM = 15;
    private String TAG = "Tag....";
    private LatLng mDefaultLocation = new LatLng(44.447813, -95.788508);
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private GeoDataClient mGeoDataClient;
    private PlaceDetectionClient mPlaceDetectionClient;
    private GoogleApiClient mGoogleApiClient;
    private boolean mRequestingLocationUpdates = false;
    private FusedLocationProviderClient mFusedLocationClient;
    private LocationRequest mLocationRequest;
    private LocationCallback mLocationCallback;
    private   String waypoints="";


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.mapfragment, container, false);
        if (googleservicesavailable()) {
            initUserLocation();

        } else {
            Toast.makeText(getActivity(), "Google Services not available", Toast.LENGTH_SHORT).show();

        }
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        for(int i=0;i<=MyIntentService.stop.size()-2;i++){
            waypoints=waypoints+MyIntentService.stop.get(i).getLatLng()+"|";
        }
        waypoints=waypoints+MyIntentService.stop.get(MyIntentService.stop.size()-1).getLatLng();

    }

    private void initUserLocation() {
        mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.mymap);
        mapFragment.getMapAsync(this);

        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                    .addConnectionCallbacks(this)
                    .addApi(LocationServices.API)
                    .build();
            mGoogleApiClient.connect();
        }

        // Construct a PlaceDetectionClient.
        mPlaceDetectionClient = Places.getPlaceDetectionClient(getActivity(), null);

        // Construct a FusedLocationProviderClient.
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getActivity());

        mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                for (Location location : locationResult.getLocations()) {
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                            new LatLng(location.getLatitude(),
                                    location.getLongitude()), DEFAULT_ZOOM));
                    mMap.addMarker(new MarkerOptions()
                            .position(new LatLng(mLastKnownLocation.getLatitude(),
                                    mLastKnownLocation.getLongitude()))
                            .title("user")
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));


                }
            }

            ;
        };
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        // Add a marker in Sydney, Australia, and move the camera.


//        mMap.addMarker(new MarkerOptions().position(mDefaultLocation).title("Marker in Sydney"));
//        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mDefaultLocation, 20));

        // Turn on the My Location layer and the related control on the map.
        updateLocationUI();

        // Get the current location of the device and set the position of the map.
        getDeviceLocation();

        updateUIWithMarkers();

    }

    private void updateUIWithMarkers(){
        for(int i=0;i<=MyIntentService.stop.size()-2;i++){
            String[] latlng = MyIntentService.stop.get(i).getLatLng().split(",");
            String lat = latlng[0];
            String lng= latlng[1];
            mMap.addMarker(new MarkerOptions()
                    .position(new LatLng(Double.parseDouble(lat), Double.parseDouble(lng))));
        }

    }


    public boolean googleservicesavailable() {
        GoogleApiAvailability googleApiAvailability = GoogleApiAvailability.getInstance();
        int isavailable = googleApiAvailability.isGooglePlayServicesAvailable(getActivity());
        if (isavailable == ConnectionResult.SUCCESS) {
            return true;
        } else if (googleApiAvailability.isUserResolvableError(isavailable)) {
            Dialog dialog = googleApiAvailability.getErrorDialog(getActivity(), isavailable, 0);
            dialog.show();
        } else {
            Toast.makeText(getActivity(), "Google play services not available", Toast.LENGTH_SHORT).show();
        }
        return false;
    }

    private void startLocationUpdates() {
        if (ContextCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.setMyLocationEnabled(true);
        mLocationRequest = LocationRequest.create();
        mFusedLocationClient.requestLocationUpdates(mLocationRequest,
                mLocationCallback,
                null /* Looper */);
    }

    @Override
    public void onResume() {
        super.onResume();
        myLocalManager =
                (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        if (ContextCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        myLocalManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                2000,
                10, this);
        if (mRequestingLocationUpdates) {
            startLocationUpdates();
        }

        ApplicationInfo ai = null;
        String value = "";
        String origin =MyIntentService.stop.get(0).getLatLng();
        String destination = MyIntentService.stop.get(MyIntentService.stop.size()-1).getLatLng();
//        String waypoints =
//                "44.458816,-95.771984|" + "44.457074,-95.774794|44.452161,-95.762448|44.453203,-95.759723|" +
//                        "44.452299,-95.762062|44.449146,-95.765557|44.447724,-95.763867|44.447346,-95.764728|" +
//                        "44.435397,-95.770419|44.432011,-95.767672|44.437548,-95.777474|44.433739,-95.785356|" +
//                        "44.430491,-95.797433|44.434882,-95.796271|44.442313,-95.794285|44.440570,-95.785025|44.443726,-95.786866";

        try {
            ai = getActivity().getPackageManager().getApplicationInfo(getActivity().getPackageName(), PackageManager.GET_META_DATA);
            value = (String) ai.metaData.get("com.google.android.direction.API_KEY");
            Log.e(".....", value);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }


        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.addInterceptor(httpLoggingInterceptor);

        Retrofit.Builder retrofitbuilder = new Retrofit.Builder()
                .baseUrl("https://maps.googleapis.com/maps/api/directions/")
                .addConverterFactory(GsonConverterFactory.create());

        Retrofit retrofit = retrofitbuilder.client(builder.build()).build();
        GoogleApiContract myGoogleApiClient = retrofit.create(GoogleApiContract.class);
            Call<GeoCoded_WayPoints> myGoogleApiClientRequest = myGoogleApiClient.getDirection(origin, destination, waypoints, value);
            myGoogleApiClientRequest.enqueue(new Callback<GeoCoded_WayPoints>() {
                @Override
                public void onResponse(Call<GeoCoded_WayPoints> call, Response<GeoCoded_WayPoints> response) {
                    if(response.body().getRoutes().size()!=0) {
                        String polyline = response.body().getRoutes().get(0).getOverview_polyline().getPoints();
                        poly = decodePoly(polyline);
                        Polyline line = mMap.addPolyline(new PolylineOptions()
                                .addAll(poly)
                                .color(Color.BLUE));
                    }


                }

                @Override
                public void onFailure(Call<GeoCoded_WayPoints> call, Throwable t) {

                }
            });


    }

    private void getLocationPermission() {
    /*
     * Request location permission, so that we can get the location of the
     * device. The result of the permission request is handled by a callback,
     * onRequestPermissionsResult.
     */
        if (ContextCompat.checkSelfPermission(getActivity(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mLocationPermissionGranted = true;
        } else {
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[],
                                           @NonNull int[] grantResults) {
        mLocationPermissionGranted = false;
        switch (requestCode) {
            case PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mLocationPermissionGranted = true;
                }
            }
        }
        updateLocationUI();
    }

    private void updateLocationUI() {
        if (mMap == null) {
            return;
        }
        try {
            if (mLocationPermissionGranted) {
                mMap.setMyLocationEnabled(true);
                mMap.getUiSettings().setMyLocationButtonEnabled(true);
                mLastKnownLocation = myLocalManager.getLastKnownLocation("userlocation");

            } else {
                mMap.setMyLocationEnabled(false);
                mMap.getUiSettings().setMyLocationButtonEnabled(false);
                mLastKnownLocation = null;
                getLocationPermission();
            }
        } catch (SecurityException e) {
            Log.e("Exception: %s", e.getMessage());
        }
    }

    private void getDeviceLocation() {
    /*
     * Get the best and most recent location of the device, which may be null in rare
     * cases when a location is not available.
     */
        try {
            if (mLocationPermissionGranted) {
                Task<Location> locationResult = mFusedLocationProviderClient.getLastLocation();
                locationResult.addOnCompleteListener(getActivity(), new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if (task.isSuccessful()) {
                            // Set the map's camera position to the current location of the device.
                            mLastKnownLocation = (Location) task.getResult();
                            if (mLastKnownLocation != null) {
                                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                                        new LatLng(mLastKnownLocation.getLatitude(),
                                                mLastKnownLocation.getLongitude()), DEFAULT_ZOOM));


                            }

                        } else {
                            Log.d(TAG, "Current location is null. Using defaults.");
                            Log.e(TAG, "Exception: %s", task.getException());
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mDefaultLocation, DEFAULT_ZOOM));
                            mMap.getUiSettings().setMyLocationButtonEnabled(false);
                        }
                    }
                });
            }

        } catch (SecurityException e) {
            Log.e("Exception: %s", e.getMessage());
        } catch (NullPointerException e) {
            Log.e("....", "Null pointer exception");
        }
    }

    private List<LatLng> decodePoly(String encoded) {

        List<LatLng> poly = new ArrayList<>();
        int index = 0, len = encoded.length();
        int lat = 0, lng = 0;

        while (index < len) {
            int b, shift = 0, result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lat += dlat;

            shift = 0;
            result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lng += dlng;

            LatLng p = new LatLng((((double) lat / 1E5)),
                    (((double) lng / 1E5)));
            poly.add(p);
        }

        return poly;
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Log.e("....", "COnnected");
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.e("....", "COnnected");
    }

    @Override
    public void onPause() {
        super.onPause();
        stopLocationUpdates();
    }

    private void stopLocationUpdates() {
//        mFusedLocationClient.removeLocationUpdates(mLocationCallback);
    }

    @Override
    public void onLocationChanged(Location location) {
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                new LatLng(location.getLatitude(),
                        location.getLongitude()), DEFAULT_ZOOM));
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

}
