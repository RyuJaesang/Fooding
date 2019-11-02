package com.example.fooding;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PointF;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;
import com.example.fooding.Target.TargetList;

import com.skt.Tmap.TMapCircle;
import com.skt.Tmap.TMapMarkerItem2;
import com.skt.Tmap.TMapPoint;
import com.skt.Tmap.TMapView;

import java.util.ArrayList;
import java.util.List;
import android.location.Address;
import org.json.JSONObject;


public class Search2Activity extends AppCompatActivity {
    TMapView tMapView;
    Intent intent;

    Button youtuberButton;
    Button refreshButton;

    SearchDB searchDB;
    TargetList targetList;
    ArrayList<JSONObject> jsonObjectArrayList;
    ArrayList<TMapMarkerItem2> markerList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search2);

        Intent getIntent = getIntent();
        jsonObjectArrayList = new ArrayList<>();
        markerList = new ArrayList<>();
        final double[] centerPointList = getIntent.getDoubleArrayExtra("point");
        TMapPoint centerPoint = new TMapPoint(centerPointList[0], centerPointList[1]);

        LinearLayout layoutTmap = findViewById(R.id.layout_tmap);
        tMapView = new TMapView(this);
        tMapView.setSKTMapApiKey("80e66504-97df-4d02-bc81-57c796cd67a1");   //API key setting
        layoutTmap.addView( tMapView );

        tMapView.setCenterPoint(centerPointList[1], centerPointList[0], true);
        //TMapPoint tMapPoint = new TMapPoint(37.497919, 127.027601);
        TMapCircle tMapCircle = new TMapCircle();
        tMapCircle.setCenterPoint( centerPoint ); // 센터 설정
        tMapCircle.setRadius(30);
        tMapCircle.setCircleWidth(15);
        tMapCircle.setLineColor(Color.BLUE);
        tMapCircle.setAreaColor(Color.GRAY);
        tMapCircle.setAreaAlpha(100);
        tMapView.addTMapCircle("circle1", tMapCircle);


        //지도 이벤트 설정
            // 클릭 이벤트 설정
        tMapView.setOnClickListenerCallBack(new TMapView.OnClickListenerCallback() {
            @Override
            public boolean onPressEvent(ArrayList arrayList, ArrayList arrayList1, TMapPoint tMapPoint, PointF pointF) {
                //Toast.makeText(MapEvent.this, "onPress~!", Toast.LENGTH_SHORT).show();
                return false;
            }

            @Override
            public boolean onPressUpEvent(ArrayList arrayList, ArrayList arrayList1, TMapPoint tMapPoint, PointF pointF) {
                //Toast.makeText(MapEvent.this, "onPressUp~!", Toast.LENGTH_SHORT).show();
                return false;
            }
        });
            // 롱 클릭 이벤트 설정
        tMapView.setOnLongClickListenerCallback(new TMapView.OnLongClickListenerCallback() {
            @Override
            public void onLongPressEvent(ArrayList arrayList, ArrayList arrayList1, TMapPoint tMapPoint) {
                //Toast.makeText(MapEvent.this, "onLongPress~!", Toast.LENGTH_SHORT).show();
            }
        });
            // 지도 스크롤 종료
        tMapView.setOnDisableScrollWithZoomLevelListener(new TMapView.OnDisableScrollWithZoomLevelCallback() {
            @Override
            public void onDisableScrollWithZoomLevelEvent(float zoom, TMapPoint centerPoint) {
                //Toast.makeText(getApplicationContext(), "zoomLevel=" + zoom + "\nlon=" + centerPoint.getLongitude() + "\nlat=" + centerPoint.getLatitude(), Toast.LENGTH_SHORT).show();
            }
        });
        tMapView.setOnMarkerClickEvent(new TMapView.OnCalloutMarker2ClickCallback() {
            @Override
            public void onCalloutMarker2ClickEvent(String s, TMapMarkerItem2 tMapMarkerItem2) {
                Toast.makeText(getApplicationContext(), "marker", Toast.LENGTH_SHORT).show();
                MarkerOverlay marker = (MarkerOverlay) tMapMarkerItem2;

                Intent myintent = new Intent(Intent.ACTION_VIEW, Uri.parse(marker.balloonView.youtubeItems[0].URL));
                startActivity(myintent);
            }
        });

        //화면 설정
        Button settingButton = findViewById(R.id.setting_button);
        Button mylocationButton = findViewById(R.id.my_location_button);
        final EditText locationInput = findViewById(R.id.location_input);
        Button locationInputButton = findViewById(R.id.location_input_button);

        Button buttonZoomIn = findViewById(R.id.button_zoom_in);
        Button buttonZoomOut = findViewById(R.id.button_zoom_out);

        //layoutSearchButton = findViewById(R.id.layout_search_button);
        //Button searchButton = findViewById(R.id.search_button);
        youtuberButton = findViewById(R.id.youtuber_button);
        refreshButton = findViewById(R.id.refresh_button);
        Button worldcupButton = findViewById(R.id.worldcup_button);
        Button likeButton = findViewById(R.id.like_button);

        //갱신 버튼
        refreshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                refreshMarker();
            }
        });
        //아래 toolbar 버튼 설정
        youtuberButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(getApplicationContext(), YoutuberActivity.class);
                //intent.addFlags(intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("point", centerPointList);
                startActivityForResult(intent, 202);
            }
        });
        worldcupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        likeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        // 위치 검색, 이동
        locationInputButton.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View view) {
                String address = locationInput.getText().toString();
                Geocoder geocoder = new Geocoder(getApplicationContext());

                ArrayList<GeoLocation> resultList = new ArrayList<>();

                try {
                    List<Address> list = geocoder.getFromLocationName(address, 10);

                    for (Address addr : list) {
                        resultList.add(new GeoLocation(addr.getLatitude(), addr.getLongitude()));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                tMapView.setCenterPoint(resultList.get(0).longitude, resultList.get(0).latitude, true);
            }
        });

        // "확대" 버튼 클릭
        buttonZoomIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tMapView.MapZoomIn();
            }
        });

        // "축소" 버튼 클릭
        buttonZoomOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tMapView.MapZoomOut();
            }
        });

        // db에 유튜브 리스트 요청
        jsonObjectArrayList = new ArrayList<>();
        SearchDB searchDB = new SearchDB();
        searchDB.returnData(jsonObjectArrayList,centerPoint);   // 전체 음식점 정보 json으로 받아오기


        // 잠시 시간 필요함
        new Handler().postDelayed(new Runnable()
        {
            @Override
            public void run()
            {
                Log.e("TAG", "check");

                if ( makeMarker(jsonObjectArrayList) ) {    // 마커 생성
                    Toast.makeText(getApplicationContext(), "makeMarker통과", Toast.LENGTH_SHORT).show();
                    TMapMarkerItem2 markerItem = null;
                    for (int i = 0; i < markerList.size(); i++) {
                        markerItem = markerList.get(i);
                        Log.d("TAG", "markerPoint : " + markerItem.getTMapPoint());
                        tMapView.addMarkerItem2(markerItem.getID(), markerItem);    // 지도에 추가
                    }
                }
            }
        }, 3000);

    }

    public boolean makeMarker(ArrayList<JSONObject> jsonObjectArrayList) {

        if (jsonObjectArrayList.isEmpty())
            Log.e("TAG", "어레이 비어있음");

        JSONObject jsonObject;
        for (int i = 0; i < jsonObjectArrayList.size(); i++) {
            jsonObject = jsonObjectArrayList.get(i);
            String response = jsonObject.toString();
            Log.e("TAG", response);

            // 마커 생성
            MarkerOverlay markerItem = new MarkerOverlay(this, response);
            Log.d("point", "point : " + markerItem.markerPoint);
            markerItem.setTMapPoint( markerItem.markerPoint ); // 마커의 좌표 지정
            String sID = "markerItem" + i;

            Bitmap bitmap = BitmapFactory.decodeResource(this.getResources(), R.drawable.marker_icon_blue);
            markerItem.setIcon(resizeBitmap(bitmap)); // 마커 아이콘 지정
            markerItem.setPosition(0.5f, 0.75f); // 마커의 중심점을 중앙, 하단으로 설정
            markerItem.setID(sID); // 마커의 id 지정

            markerList.add(markerItem);
        }

        if (markerList.isEmpty()) {
            return false;
        }
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Toast.makeText(getApplicationContext(), "search", Toast.LENGTH_SHORT).show();
    }

    public Bitmap resizeBitmap(Bitmap original) {
        int resizeWidth = 200;

        double aspectRatio = (double) original.getHeight() / (double) original.getWidth();
        int targetHeight = (int) (resizeWidth * aspectRatio);

        Bitmap result = Bitmap.createScaledBitmap(original, resizeWidth, targetHeight, false);
        if( result != original) {
            original.recycle();
        }
        return result;
    }

    public void deleteMarker(){
        jsonObjectArrayList.clear();
        for(int i=0;i<markerList.size();i++) {
            tMapView.removeMarkerItem2(markerList.get(i).getID());
        }
        markerList.clear();
        Log.d("삭제","삭제~");
    }

    public void refreshMarker(){
        deleteMarker();
        SearchDB searchDB = new SearchDB();
        searchDB.returnData(jsonObjectArrayList,tMapView.getCenterPoint());
        new Handler().postDelayed(new Runnable()
        {
            @Override
            public void run()
            {
                Log.e("TAG", "check");

                if ( makeMarker(jsonObjectArrayList) ) {    // 마커 생성
                    Toast.makeText(getApplicationContext(), "makeMarker통과", Toast.LENGTH_SHORT).show();
                    TMapMarkerItem2 markerItem = null;
                    for (int i = 0; i < markerList.size(); i++) {
                        markerItem = markerList.get(i);
                        Log.d("TAG", "markerPoint : " + markerItem.getTMapPoint());
                        tMapView.addMarkerItem2(markerItem.getID(), markerItem);    // 지도에 추가
                    }
                }
            }
        }, 3000);

    }
}
