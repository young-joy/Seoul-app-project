package edu.skku.jonadan.hangangmongttang;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.transition.AutoTransition;
import android.transition.TransitionManager;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import net.daum.mf.map.api.CalloutBalloonAdapter;
import net.daum.mf.map.api.MapPOIItem;
import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapView;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindAnim;
import butterknife.BindDimen;
import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MapActivity extends AppCompatActivity {

    @BindView(R.id.map_back_btn)
    ImageButton backBtn;
    @BindView(R.id.map_park_list)
    RecyclerView parkListView;
    @BindView(R.id.map_view)
    MapView mapView;
    @BindView(R.id.map_container)
    ConstraintLayout mapLayout;
    @BindView(R.id.map_fab_parking)
    FloatingActionButton menuParkingBtn;
    @BindView(R.id.map_fab_toilet)
    FloatingActionButton menuToiletBtn;
    @BindView(R.id.map_fab_shop)
    FloatingActionButton menuShopBtn;
    @BindView(R.id.map_fab_water)
    FloatingActionButton menuWaterBtn;
    @BindView(R.id.map_fab_entertain)
    FloatingActionButton menuEntertainBtn;
    @BindView(R.id.map_fab_athletic)
    FloatingActionButton menuAthleticBtn;
    @BindView(R.id.map_fab_menu)
    FloatingActionButton menuBtn;
    @BindView(R.id.map_fab_cur)
    FloatingActionButton curLocBtn;

    @BindAnim(R.anim.fab_open)
    Animation fabOpen;
    @BindAnim(R.anim.fab_close)
    Animation fabClose;

    @BindDimen(R.dimen.fab_margin) int fabMargin;

    private CustomLoadingDialog loadingDialog;

    private MapView.MapViewEventListener mapEventListener;
    private MapView.CurrentLocationEventListener curLocationListener;
    private MapView.POIItemEventListener markerListener;
    private ArrayList<MapPOIItem> markerList;
    private CalloutBalloonAdapter balloonAdapter;

    private MapParkListAdapter parkListAdapter;
    private ArrayList<Park> parkList;

    private ArrayList<FloatingActionButton> fabList;
    private ConstraintSet constraintSet;
    private Boolean isFabOpen = false;

    private ArrayList<ParkingLot> parkingLotList;
    private ArrayList<Location> toiletList;
    private ArrayList<Location> shopList;
    private ArrayList<Location> waterList;
    private ArrayList<Location> entertainList;
    private ArrayList<Location> athleticList;

    private SeoulApiProvider apiProvider;
    private ArrayList<Callback<SeoulApiResult>> callbacks;

    private int selectedParkId;
    private MapPOIItem parkMarker;
    private MapPOIItem curMarker;
    private Location refLocation;
    private int callCount;

    private static final int GPS_ENABLE_REQUEST_CODE = 2001;
    private static final int PERMISSIONS_REQUEST_CODE = 100;
    private String[] REQUIRED_PERMISSIONS  = {Manifest.permission.ACCESS_FINE_LOCATION};

    private final int DEFAULT_ZOOM_LEVEL = 3;
    private final double MARKING_SCOPE = 1.0;
    private final int MARKER_SIZE = 100;

    private enum FABS {
        PARKING, TOILET, SHOP, WATER, ENTERTAIN, ATHELETIC
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        ButterKnife.bind(this);

        // Get initial park from intent
        Intent intent = getIntent();
        selectedParkId = intent.getExtras().getInt("park_id");
        refLocation = ConstantPark.PARK_LIST.get(selectedParkId);

        initParkList();
        initFabs();
        initMap(refLocation);

        apiProvider = new SeoulApiProvider();
        initCallbacks();

        loadingDialog = new CustomLoadingDialog(MapActivity.this);

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        curLocBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!checkLocationServicesStatus()) {
                    showDialogForLocationServiceSetting();
                } else {
                    checkRunTimePermission();
                }
            }
        });

        menuBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isFabOpen) {
                    for (FloatingActionButton fab : fabList) {
                        fab.startAnimation(fabClose);
                        constraintSet.connect(
                                fab.getId(), ConstraintSet.BOTTOM,
                                menuBtn.getId(), ConstraintSet.BOTTOM);
                        constraintSet.setMargin(fab.getId(), ConstraintSet.BOTTOM, 0);
                        fab.setClickable(false);
                    }
                } else {
                    for (int i = fabList.size()-1; i >= 0; i--) {
                        fabList.get(i).startAnimation(fabOpen);
                        if (i == fabList.size()-1) {
                            constraintSet.connect(
                                    fabList.get(i).getId(), ConstraintSet.BOTTOM,
                                    menuBtn.getId(), ConstraintSet.TOP);
                        } else {
                            constraintSet.connect(
                                    fabList.get(i).getId(), ConstraintSet.BOTTOM,
                                    fabList.get(i + 1).getId(), ConstraintSet.TOP);
                        }
                        constraintSet.setMargin(
                                fabList.get(i).getId(), ConstraintSet.BOTTOM, fabMargin);
                        fabList.get(i).setClickable(true);
                    }
                }
                AutoTransition transition = new AutoTransition();
                transition.setDuration(300);
                transition.setInterpolator(new AccelerateDecelerateInterpolator());
                TransitionManager.beginDelayedTransition(mapLayout, transition);
                constraintSet.applyTo(mapLayout);

                isFabOpen = !isFabOpen;
            }
        });
    }

    private void initParkList() {
        parkListAdapter = new MapParkListAdapter(ConstantPark.PARK_LIST.get(selectedParkId),
                new MapParkListAdapter.ParkClickListener() {
                    @Override
                    public void movePark(Park park) {
                        for (int idx = 0; idx < ConstantPark.PARK_LIST.size(); idx++) {
                            if (ConstantPark.PARK_LIST.get(idx).getName().equals(park.getName())) {
                                selectedParkId = idx;
                                break;
                            }
                        }
                        refLocation = park;
                        setMap(park);

                        mapView.removePOIItem(parkMarker);
                        parkMarker = new MapPOIItem();
                        parkMarker.setItemName(park.getName());
                        parkMarker.setTag(park.getObjectId());
                        parkMarker.setMapPoint(MapPoint.mapPointWithGeoCoord(park.getLat(), park.getLng()));

                        // For custom marker
                        parkMarker.setMarkerType(MapPOIItem.MarkerType.CustomImage);
                        parkMarker.setSelectedMarkerType(MapPOIItem.MarkerType.CustomImage);
                        parkMarker.setCustomImageBitmap(
                                Bitmap.createScaledBitmap(getBitmapFromVectorDrawable(
                                        getApplicationContext(), R.drawable.ic_map_marker_park),
                                        MARKER_SIZE,MARKER_SIZE, true));
                        parkMarker.setCustomSelectedImageBitmap(
                                Bitmap.createScaledBitmap(getBitmapFromVectorDrawable(
                                        getApplicationContext(), R.drawable.ic_map_marker_park),
                                        MARKER_SIZE,MARKER_SIZE, true));
                        parkMarker.setCustomImageAutoscale(true);
                        parkMarker.setCustomImageAnchor(0.5f, 1.0f);
                        mapView.addPOIItem(parkMarker);
                    }

                    @Override
                    public void focusParkList() {
                        if (isFabOpen) {
                            for (FloatingActionButton fab : fabList) {
                                fab.startAnimation(fabClose);
                                constraintSet.connect(
                                        fab.getId(), ConstraintSet.BOTTOM,
                                        menuBtn.getId(), ConstraintSet.BOTTOM);
                                constraintSet.setMargin(fab.getId(), ConstraintSet.BOTTOM, 0);
                                fab.setClickable(false);
                            }
                            isFabOpen = !isFabOpen;
                        }
                    }
                });
        parkListView.setAdapter(parkListAdapter);
        parkListView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void initMap(Location location) {
        setMap(location);
        mapEventListener = new MapView.MapViewEventListener() {
            @Override
            public void onMapViewInitialized(MapView mapView) {

            }

            @Override
            public void onMapViewCenterPointMoved(MapView mapView, MapPoint mapPoint) {

            }

            @Override
            public void onMapViewZoomLevelChanged(MapView mapView, int i) {

            }

            @Override
            public void onMapViewSingleTapped(MapView mapView, MapPoint mapPoint) {

            }

            @Override
            public void onMapViewDoubleTapped(MapView mapView, MapPoint mapPoint) {

            }

            @Override
            public void onMapViewLongPressed(MapView mapView, MapPoint mapPoint) {

            }

            @Override
            public void onMapViewDragStarted(MapView mapView, MapPoint mapPoint) {

            }

            @Override
            public void onMapViewDragEnded(MapView mapView, MapPoint mapPoint) {

            }

            @Override
            public void onMapViewMoveFinished(MapView mapView, MapPoint mapPoint) {

            }
        };
        mapView.setMapViewEventListener(mapEventListener);

        curLocationListener = new MapView.CurrentLocationEventListener() {
            @Override
            public void onCurrentLocationUpdate(MapView mapView, MapPoint mapPoint, float v) {
                MapPoint.GeoCoordinate mapPointGeo = mapPoint.getMapPointGeoCoord();

                if (curMarker != null) {
                    mapView.removePOIItem(curMarker);
                }

                curMarker = new MapPOIItem();
                curMarker.setItemName("사용자 위치");
                curMarker.setTag(0);
                curMarker.setMapPoint(MapPoint.mapPointWithGeoCoord(mapPointGeo.latitude, mapPointGeo.longitude));

                // For custom marker
                curMarker.setMarkerType(MapPOIItem.MarkerType.CustomImage);
                curMarker.setSelectedMarkerType(MapPOIItem.MarkerType.CustomImage);
                curMarker.setCustomImageBitmap(
                        Bitmap.createScaledBitmap(getBitmapFromVectorDrawable(
                                getApplicationContext(), R.drawable.ic_map_marker_user),
                                MARKER_SIZE,MARKER_SIZE, true));
                curMarker.setCustomSelectedImageBitmap(
                        Bitmap.createScaledBitmap(getBitmapFromVectorDrawable(
                                getApplicationContext(), R.drawable.ic_map_marker_user),
                                MARKER_SIZE,MARKER_SIZE, true));
                curMarker.setCustomImageAutoscale(true);
                curMarker.setCustomImageAnchor(0.5f, 1.0f);
                mapView.addPOIItem(curMarker);

                mapView.setCurrentLocationTrackingMode(MapView.CurrentLocationTrackingMode.TrackingModeOff);
                mapView.setShowCurrentLocationMarker(false);
            }

            @Override
            public void onCurrentLocationDeviceHeadingUpdate(MapView mapView, float v) {

            }

            @Override
            public void onCurrentLocationUpdateFailed(MapView mapView) {

            }

            @Override
            public void onCurrentLocationUpdateCancelled(MapView mapView) {

            }
        };
        mapView.setCurrentLocationEventListener(curLocationListener);

        markerListener = new MapView.POIItemEventListener() {
            @Override
            public void onPOIItemSelected(MapView mapView, MapPOIItem mapPOIItem) {
                // search location from marker
            }

            @Override
            public void onCalloutBalloonOfPOIItemTouched(MapView mapView, MapPOIItem mapPOIItem) {
                int id = mapPOIItem.getTag();
                int type = mapPOIItem.getTag() / SeoulApiProvider.SERVICE_PAD;
                switch (SeoulApiProvider.SERVICE_CODE.values()[type]) {
                    case QUAY:
                    case WATER_LEISURE:
                    case BOAT:
                    case DUCK_BOAT:
                    case WATER_TAXI:
                    case PLAYGROUND:
                    case ROCK:
                    case SKATE:
                    case JOKGU:
                    case TRACK:
                    case BADMINTON:
                        Intent intent = new Intent(MapActivity.this, InfoActivity.class);
                        intent.putExtra("facility_id", id);
                        startActivity(intent);
                        break;
                }
            }

            @Override
            public void onCalloutBalloonOfPOIItemTouched(MapView mapView, MapPOIItem mapPOIItem, MapPOIItem.CalloutBalloonButtonType calloutBalloonButtonType) {

            }

            @Override
            public void onDraggablePOIItemMoved(MapView mapView, MapPOIItem mapPOIItem, MapPoint mapPoint) {

            }
        };
        mapView.setPOIItemEventListener(markerListener);

        balloonAdapter = new CustomBalloonAdapter(this);
        mapView.setCalloutBalloonAdapter(balloonAdapter);

        markerList = new ArrayList<>();

        parkMarker = new MapPOIItem();
        parkMarker.setItemName(location.getName());
        parkMarker.setTag(location.getObjectId());
        parkMarker.setMapPoint(MapPoint.mapPointWithGeoCoord(location.getLat(), location.getLng()));

        // For custom marker
        parkMarker.setMarkerType(MapPOIItem.MarkerType.CustomImage);
        parkMarker.setSelectedMarkerType(MapPOIItem.MarkerType.CustomImage);
        parkMarker.setCustomImageBitmap(
                Bitmap.createScaledBitmap(getBitmapFromVectorDrawable(
                        getApplicationContext(), R.drawable.ic_map_marker_park),
                        MARKER_SIZE,MARKER_SIZE, true));
        parkMarker.setCustomSelectedImageBitmap(
                Bitmap.createScaledBitmap(getBitmapFromVectorDrawable(
                        getApplicationContext(), R.drawable.ic_map_marker_park),
                        MARKER_SIZE,MARKER_SIZE, true));
        parkMarker.setCustomImageAutoscale(true);
        parkMarker.setCustomImageAnchor(0.5f, 1.0f);
        mapView.addPOIItem(parkMarker);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {

            case GPS_ENABLE_REQUEST_CODE:

                //사용자가 GPS 활성 시켰는지 검사
                if (checkLocationServicesStatus()) {
                    if (checkLocationServicesStatus()) {
                        Log.d("@@@", "onActivityResult : GPS 활성화 되있음");
                        checkRunTimePermission();
                        return;
                    }
                }

                break;
        }
    }

    /*
     * ActivityCompat.requestPermissions를 사용한 퍼미션 요청의 결과를 리턴받는 메소드입니다.
     */
    @Override
    public void onRequestPermissionsResult(int permsRequestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grandResults) {

        if ( permsRequestCode == PERMISSIONS_REQUEST_CODE && grandResults.length == REQUIRED_PERMISSIONS.length) {

            // 요청 코드가 PERMISSIONS_REQUEST_CODE 이고, 요청한 퍼미션 개수만큼 수신되었다면

            boolean check_result = true;


            // 모든 퍼미션을 허용했는지 체크합니다.

            for (int result : grandResults) {
                if (result != PackageManager.PERMISSION_GRANTED) {
                    check_result = false;
                    break;
                }
            }


            if ( check_result ) {
                Log.d("@@@", "start");
                //위치 값을 가져올 수 있음
                mapView.setCurrentLocationTrackingMode(MapView.CurrentLocationTrackingMode.TrackingModeOnWithoutHeading);
            }
            else {
                // 거부한 퍼미션이 있다면 앱을 사용할 수 없는 이유를 설명해주고 앱을 종료합니다.2 가지 경우가 있습니다.

                if (ActivityCompat.shouldShowRequestPermissionRationale(this, REQUIRED_PERMISSIONS[0])) {

                    Toast.makeText(MapActivity.this, "퍼미션이 거부되었습니다. 앱을 다시 실행하여 퍼미션을 허용해주세요.", Toast.LENGTH_LONG).show();
                    finish();


                }else {

                    Toast.makeText(MapActivity.this, "퍼미션이 거부되었습니다. 설정(앱 정보)에서 퍼미션을 허용해야 합니다. ", Toast.LENGTH_LONG).show();

                }
            }

        }
    }

    void checkRunTimePermission(){

        //런타임 퍼미션 처리
        // 1. 위치 퍼미션을 가지고 있는지 체크합니다.
        int hasFineLocationPermission = ContextCompat.checkSelfPermission(MapActivity.this,
                Manifest.permission.ACCESS_FINE_LOCATION);


        if (hasFineLocationPermission == PackageManager.PERMISSION_GRANTED ) {

            // 2. 이미 퍼미션을 가지고 있다면
            // ( 안드로이드 6.0 이하 버전은 런타임 퍼미션이 필요없기 때문에 이미 허용된 걸로 인식합니다.)


            // 3.  위치 값을 가져올 수 있음
            mapView.setCurrentLocationTrackingMode(MapView.CurrentLocationTrackingMode.TrackingModeOnWithoutHeading);


        } else {  //2. 퍼미션 요청을 허용한 적이 없다면 퍼미션 요청이 필요합니다. 2가지 경우(3-1, 4-1)가 있습니다.

            // 3-1. 사용자가 퍼미션 거부를 한 적이 있는 경우에는
            if (ActivityCompat.shouldShowRequestPermissionRationale(MapActivity.this, REQUIRED_PERMISSIONS[0])) {

                // 3-2. 요청을 진행하기 전에 사용자가에게 퍼미션이 필요한 이유를 설명해줄 필요가 있습니다.
                Toast.makeText(MapActivity.this, "이 앱을 실행하려면 위치 접근 권한이 필요합니다.", Toast.LENGTH_LONG).show();
                // 3-3. 사용자게에 퍼미션 요청을 합니다. 요청 결과는 onRequestPermissionResult에서 수신됩니다.
                ActivityCompat.requestPermissions(MapActivity.this, REQUIRED_PERMISSIONS,
                        PERMISSIONS_REQUEST_CODE);
            } else {
                // 4-1. 사용자가 퍼미션 거부를 한 적이 없는 경우에는 퍼미션 요청을 바로 합니다.
                // 요청 결과는 onRequestPermissionResult에서 수신됩니다.
                ActivityCompat.requestPermissions(MapActivity.this, REQUIRED_PERMISSIONS,
                        PERMISSIONS_REQUEST_CODE);
            }

        }

    }

    public boolean checkLocationServicesStatus() {
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
                || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }

    //여기부터는 GPS 활성화를 위한 메소드들
    private void showDialogForLocationServiceSetting() {

        AlertDialog.Builder builder = new AlertDialog.Builder(MapActivity.this);
        builder.setTitle("위치 서비스 비활성화");
        builder.setMessage("앱을 사용하기 위해서는 위치 서비스가 필요합니다.\n"
                + "위치 설정을 수정하실래요?");
        builder.setCancelable(true);
        builder.setPositiveButton("설정", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                Intent callGPSSettingIntent
                        = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivityForResult(callGPSSettingIntent, GPS_ENABLE_REQUEST_CODE);
            }
        });
        builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });
        builder.create().show();
    }

    public static Bitmap getBitmapFromVectorDrawable(Context context, int drawableId) {
        Drawable drawable = ContextCompat.getDrawable(context, drawableId);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            drawable = (DrawableCompat.wrap(drawable)).mutate();
        }

        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(),
                drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);

        return bitmap;
    }

    private void setMap(Location location) {
        double latitude, longitude;
        latitude = location.getLat();
        longitude = location.getLng();
        mapView.setMapCenterPointAndZoomLevel(
                MapPoint.mapPointWithGeoCoord(latitude, longitude), DEFAULT_ZOOM_LEVEL, true);
    }

    private <T extends Location> void setMarker(ArrayList<T> locationArrayList) {
        double latitude, longitude;
        for (Location location : locationArrayList) {
            latitude = location.getLat();
            longitude = location.getLng();
            MapPOIItem marker = new MapPOIItem();
            marker.setItemName(location.getName());
            marker.setTag(location.getObjectId());
            marker.setMapPoint(MapPoint.mapPointWithGeoCoord(latitude, longitude));

            // For custom marker
            marker.setMarkerType(MapPOIItem.MarkerType.CustomImage);
            marker.setSelectedMarkerType(MapPOIItem.MarkerType.CustomImage);
            marker.setCustomImageBitmap(
                    Bitmap.createScaledBitmap(getBitmapFromVectorDrawable(
                            getApplicationContext(), R.drawable.ic_map_marker_not),
                            MARKER_SIZE,MARKER_SIZE, true));
            marker.setCustomSelectedImageBitmap(
                    Bitmap.createScaledBitmap(getBitmapFromVectorDrawable(
                            getApplicationContext(), R.drawable.ic_map_marker_yes),
                            MARKER_SIZE,MARKER_SIZE, true));
            marker.setCustomImageAutoscale(true);
            marker.setCustomImageAnchor(0.5f, 1.0f);

            markerList.add(marker);
        }
        for (MapPOIItem marker: markerList) {
            mapView.addPOIItem(marker);
        }
    }

    private void removeAllMarkers() {
        for (MapPOIItem marker : markerList) {
            mapView.removePOIItem(marker);
        }
        markerList.clear();
    }

    private void initFabs() {
        fabList = new ArrayList<>();
        constraintSet = new ConstraintSet();

        fabList.add(menuParkingBtn);
        fabList.add(menuToiletBtn);
        fabList.add(menuShopBtn);
        fabList.add(menuWaterBtn);
        fabList.add(menuEntertainBtn);
        fabList.add(menuAthleticBtn);

        toiletList = new ArrayList<>();
        shopList = new ArrayList<>();
        waterList = new ArrayList<>();
        entertainList = new ArrayList<>();
        athleticList = new ArrayList<>();

        constraintSet.clone(mapLayout);
        for (FloatingActionButton fab : fabList) {
            constraintSet.connect(
                    fab.getId(), ConstraintSet.BOTTOM, menuBtn.getId(), ConstraintSet.BOTTOM);
        }

        menuParkingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadingDialog.show();
                removeAllMarkers();
                parkingLotList = ConstantPark.PARK_LIST.get(selectedParkId).getParkingLots();
                setMarker(parkingLotList);
                Timer timer = new Timer();
                TimerTask timerTask = new TimerTask() {
                    @Override
                    public void run() {
                        if (loadingDialog.isShowing()) loadingDialog.dismiss();
                    }
                };
                timer.schedule(timerTask, 500);
            }
        });

        menuToiletBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadingDialog.show();
                removeAllMarkers();
                toiletList.clear();
                disableFabs();
                callCount = 0;
                ArrayList<Call<SeoulApiResult>> calls = apiProvider.callToilet();
                for (Call call: calls) {
                    call.enqueue(callbacks.get(FABS.TOILET.ordinal()));
                }
            }
        });

        menuShopBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadingDialog.show();
                removeAllMarkers();
                shopList.clear();
                disableFabs();
                callCount = 0;
                Call<SeoulApiResult> call = apiProvider.callShop();
                call.enqueue(callbacks.get(FABS.SHOP.ordinal()));
            }
        });

        menuWaterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadingDialog.show();
                removeAllMarkers();
                waterList.clear();
                disableFabs();
                callCount = 0;
                Call<SeoulApiResult> call = apiProvider.callWater();
                call.enqueue(callbacks.get(FABS.WATER.ordinal()));
            }
        });

        menuEntertainBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadingDialog.show();
                removeAllMarkers();
                entertainList.clear();
                disableFabs();
                callCount = 0;
                ArrayList<Call<SeoulApiResult>> calls = apiProvider.callEntertain();
                for (Call call: calls) {
                    call.enqueue(callbacks.get(FABS.ENTERTAIN.ordinal()));
                }
            }
        });

        menuAthleticBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadingDialog.show();
                removeAllMarkers();
                athleticList.clear();
                disableFabs();
                callCount = 0;
                ArrayList<Call<SeoulApiResult>> calls = apiProvider.callAthletic();
                for (Call call: calls) {
                    call.enqueue(callbacks.get(FABS.ATHELETIC.ordinal()));
                }
            }
        });
    }

    private void initCallbacks() {
        callbacks = new ArrayList<>();
        callbacks.add(new Callback<SeoulApiResult>() {
            @Override
            public void onResponse(Call<SeoulApiResult> call, Response<SeoulApiResult> response) {

            }

            @Override
            public void onFailure(Call<SeoulApiResult> call, Throwable t) {

            }
        });
        callbacks.add(new Callback<SeoulApiResult>() {
            @Override
            public void onResponse(Call<SeoulApiResult> call, Response<SeoulApiResult> response) {
                if (!response.isSuccessful()) {
                    Log.d("Callback", "Response fail");
                    return;
                }
                SeoulApiResult result = response.body();
                ArrayList<Location> toilets = result.getServiceWGS().getItems();
                for (Location toilet: toilets) {
                    if (getDistance(refLocation, toilet) < MARKING_SCOPE) {
                        toilet.setObjectId(
                                SeoulApiProvider.SERVICE_CODE.TOILET.ordinal() * SeoulApiProvider.SERVICE_PAD
                                + toilet.getObjectId()
                        );
                        toiletList.add(toilet);
                    }
                }
                setMarker(toiletList);
                callCount += 1;
                if (callCount == SeoulApiProvider.CALL_NUM.TOTILET.getValue()) {
                    enableFabs();
                    Timer timer = new Timer();
                    TimerTask timerTask = new TimerTask() {
                        @Override
                        public void run() {
                            if (loadingDialog.isShowing()) loadingDialog.dismiss();
                        }
                    };
                    timer.schedule(timerTask, 500);
                }
            }

            @Override
            public void onFailure(Call<SeoulApiResult> call, Throwable t) {
                Log.d("Callback", "" + t);
            }
        });
        callbacks.add(new Callback<SeoulApiResult>() {
            @Override
            public void onResponse(Call<SeoulApiResult> call, Response<SeoulApiResult> response) {
                if (!response.isSuccessful()) {
                    Log.d("Callback", "Response fail");
                    return;
                }
                SeoulApiResult result = response.body();
                ArrayList<Location> shops = result.getServiceWGS().getItems();
                if (shops.size() > 0) {
                    for (Location shop: shops) {
                        if (getDistance(refLocation, shop) < MARKING_SCOPE) {
                            shop.setObjectId(
                                    SeoulApiProvider.SERVICE_CODE.SHOP.ordinal() * SeoulApiProvider.SERVICE_PAD
                                            + shop.getObjectId()
                            );
                            shopList.add(shop);
                        }
                    }
                } else {
                    // No result
                }
                setMarker(shopList);
                callCount += 1;
                if (callCount == SeoulApiProvider.CALL_NUM.SHOP.getValue()) {
                    enableFabs();
                    Timer timer = new Timer();
                    TimerTask timerTask = new TimerTask() {
                        @Override
                        public void run() {
                            if (loadingDialog.isShowing()) loadingDialog.dismiss();
                        }
                    };
                    timer.schedule(timerTask, 500);
                }
            }

            @Override
            public void onFailure(Call<SeoulApiResult> call, Throwable t) {
                Log.d("Callback", "" + t);
            }
        });
        callbacks.add(new Callback<SeoulApiResult>() {
            @Override
            public void onResponse(Call<SeoulApiResult> call, Response<SeoulApiResult> response) {
                if (!response.isSuccessful()) {
                    Log.d("Callback", "Response fail");
                    return;
                }
                SeoulApiResult result = response.body();
                ArrayList<Location> waters = result.getServiceWGS().getItems();
                if (waters.size() > 0) {
                    for (Location water: waters) {
                        if (getDistance(refLocation, water) < MARKING_SCOPE) {
                            water.setObjectId(
                                    SeoulApiProvider.SERVICE_CODE.WATER.ordinal() * SeoulApiProvider.SERVICE_PAD
                                            + water.getObjectId()
                            );
                            waterList.add(water);
                        }
                    }
                } else {
                    // No result
                }
                setMarker(waterList);
                callCount += 1;
                if (callCount == SeoulApiProvider.CALL_NUM.WATER.getValue()) {
                    enableFabs();
                    Timer timer = new Timer();
                    TimerTask timerTask = new TimerTask() {
                        @Override
                        public void run() {
                            if (loadingDialog.isShowing()) loadingDialog.dismiss();
                        }
                    };
                    timer.schedule(timerTask, 500);
                }
            }

            @Override
            public void onFailure(Call<SeoulApiResult> call, Throwable t) {
                Log.d("Callback", "" + t);
            }
        });
        callbacks.add(new Callback<SeoulApiResult>() {
            @Override
            public void onResponse(Call<SeoulApiResult> call, Response<SeoulApiResult> response) {
                if (!response.isSuccessful()) {
                    Log.d("Callback", "Response fail");
                    return;
                }
                SeoulApiResult result = response.body();
                ArrayList<Location> entertains = result.getServiceWGS().getItems();
                if (entertains.size() > 0) {
                    for (Location entertain: entertains) {
                        if (getDistance(refLocation, entertain) < MARKING_SCOPE) {
                            switch (response.toString().split("/")[6]) {
                                case "GeoInfoQuayWGS":
                                    entertain.setObjectId(
                                            SeoulApiProvider.SERVICE_CODE.QUAY.ordinal() * SeoulApiProvider.SERVICE_PAD
                                                    + entertain.getObjectId()
                                    );
                                    break;
                                case "GeoInfoWaterLeisureWGS":
                                    entertain.setObjectId(
                                            SeoulApiProvider.SERVICE_CODE.WATER_LEISURE.ordinal() * SeoulApiProvider.SERVICE_PAD
                                                    + entertain.getObjectId()
                                    );
                                    break;
                                case "GeoInfoBoatStorageWGS":
                                    entertain.setObjectId(
                                            SeoulApiProvider.SERVICE_CODE.BOAT.ordinal() * SeoulApiProvider.SERVICE_PAD
                                                    + entertain.getObjectId()
                                    );
                                    break;
                                case "GeoInfoDuckBoatWGS":
                                    entertain.setObjectId(
                                            SeoulApiProvider.SERVICE_CODE.DUCK_BOAT.ordinal() * SeoulApiProvider.SERVICE_PAD
                                                    + entertain.getObjectId()
                                    );
                                    break;
                                case "GeoInfoWaterTaxiWGS":
                                    entertain.setObjectId(
                                            SeoulApiProvider.SERVICE_CODE.WATER_TAXI.ordinal() * SeoulApiProvider.SERVICE_PAD
                                                    + entertain.getObjectId()
                                    );
                                    break;
                                case "GeoInfoPlaygroundWGS":
                                    entertain.setObjectId(
                                            SeoulApiProvider.SERVICE_CODE.PLAYGROUND.ordinal() * SeoulApiProvider.SERVICE_PAD
                                                    + entertain.getObjectId()
                                    );
                                    entertain.setName("어린이 놀이터");
                                    break;
                            }
                            entertainList.add(entertain);
                        }
                    }
                } else {
                    // No result
                }
                setMarker(entertainList);
                callCount += 1;
                if (callCount == SeoulApiProvider.CALL_NUM.ENTERTAIN.getValue()) {
                    enableFabs();
                    Timer timer = new Timer();
                    TimerTask timerTask = new TimerTask() {
                        @Override
                        public void run() {
                            if (loadingDialog.isShowing()) loadingDialog.dismiss();
                        }
                    };
                    timer.schedule(timerTask, 500);
                }
            }

            @Override
            public void onFailure(Call<SeoulApiResult> call, Throwable t) {
                Log.d("Callback", "" + t);
            }
        });
        callbacks.add(new Callback<SeoulApiResult>() {
            @Override
            public void onResponse(Call<SeoulApiResult> call, Response<SeoulApiResult> response) {
                if (!response.isSuccessful()) {
                    Log.d("Callback", "Response fail");
                    return;
                }
                SeoulApiResult result = response.body();
                ArrayList<Location> athletics = result.getServiceWGS().getItems();
                if (athletics.size() > 0) {
                    for (Location athletic: athletics) {
                        if (getDistance(refLocation, athletic) < MARKING_SCOPE) {
                            switch (response.toString().split("/")[6]) {
                                case "GeoInfoRockClimbWGS":
                                    athletic.setObjectId(
                                            SeoulApiProvider.SERVICE_CODE.ROCK.ordinal() * SeoulApiProvider.SERVICE_PAD
                                                    + athletic.getObjectId()
                                    );
                                    break;
                                case "GeoInfoInlineSkateWGS":
                                    athletic.setObjectId(
                                            SeoulApiProvider.SERVICE_CODE.SKATE.ordinal() * SeoulApiProvider.SERVICE_PAD
                                                    + athletic.getObjectId()
                                    );
                                    break;
                                case "GeoInfoJokguWGS":
                                    athletic.setObjectId(
                                            SeoulApiProvider.SERVICE_CODE.JOKGU.ordinal() * SeoulApiProvider.SERVICE_PAD
                                                    + athletic.getObjectId()
                                    );
                                    break;
                                case "GeoInfoTrackWGS":
                                    athletic.setObjectId(
                                            SeoulApiProvider.SERVICE_CODE.TRACK.ordinal() * SeoulApiProvider.SERVICE_PAD
                                                    + athletic.getObjectId()
                                    );
                                    break;
                                case "GeoInfoBadmintonWGS":
                                    athletic.setObjectId(
                                            SeoulApiProvider.SERVICE_CODE.BADMINTON.ordinal() * SeoulApiProvider.SERVICE_PAD
                                                    + athletic.getObjectId()
                                    );
                                    break;
                            }
                            athleticList.add(athletic);
                        }
                    }
                } else {
                    // No result
                }
                setMarker(athleticList);
                callCount += 1;
                if (callCount == SeoulApiProvider.CALL_NUM.ATHLETIC.getValue()) {
                    enableFabs();
                    Timer timer = new Timer();
                    TimerTask timerTask = new TimerTask() {
                        @Override
                        public void run() {
                            if (loadingDialog.isShowing()) loadingDialog.dismiss();
                        }
                    };
                    timer.schedule(timerTask, 500);
                }
            }

            @Override
            public void onFailure(Call<SeoulApiResult> call, Throwable t) {
                Log.d("Callback", "" + t);
            }
        });
    }

    // Get distance using Haversine formula
    private double getDistance(Location refPoint, Location targetPoint) {
        int R = 6371;
        
        double dLat = deg2rad(refPoint.getLat() - targetPoint.getLat());
        double dLng = deg2rad(refPoint.getLng() - targetPoint.getLng());
        double a = Math.sin(dLat/2) * Math.sin(dLat/2)
                + Math.cos(deg2rad(refPoint.getLat())) * Math.cos(deg2rad(targetPoint.getLat()))
                                                       * Math.sin(dLng/2) * Math.sin(dLng/2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
        double distance = R * c;

        return distance;
    }

    private double deg2rad(double degree) {
        return degree * Math.PI / 180;
    }

    private void disableFabs() {
        for (FloatingActionButton fab: fabList) {
            fab.setEnabled(false);
        }
    }

    private void enableFabs() {
        for (FloatingActionButton fab: fabList) {
            fab.setEnabled(true);
        }
    }
}
