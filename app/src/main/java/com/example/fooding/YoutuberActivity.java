package com.example.fooding;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PointF;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.skt.Tmap.TMapCircle;
import com.skt.Tmap.TMapMarkerItem;
import com.skt.Tmap.TMapPoint;
import com.skt.Tmap.TMapView;

import java.util.ArrayList;

public class YoutuberActivity extends AppCompatActivity {
    TMapView tMapView;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_youtuber);

        LinearLayout layoutTmap = findViewById(R.id.layout_tmap);
        tMapView = new TMapView(this);
        tMapView.setSKTMapApiKey("80e66504-97df-4d02-bc81-57c796cd67a1");   //API key setting
        layoutTmap.addView( tMapView );

        Intent getIntent = getIntent();
        final double[] centerPointList = getIntent.getDoubleArrayExtra("point");
        TMapPoint centerPoint = new TMapPoint(centerPointList[0], centerPointList[1]);

        tMapView.setCenterPoint(centerPointList[1], centerPointList[0], true);
        TMapCircle tMapCircle = new TMapCircle();
        tMapCircle.setCenterPoint( centerPoint );
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

        //화면 설정
        Button settingButton = findViewById(R.id.setting_button);

        Button buttonZoomIn = findViewById(R.id.button_zoom_in);
        Button buttonZoomOut = findViewById(R.id.button_zoom_out);

        Button searchButton = findViewById(R.id.search_button);
        //Button youtuberButton = findViewById(R.id.youtuber_button);
        Button worldcupButton = findViewById(R.id.worldcup_button);
        Button likeButton = findViewById(R.id.like_button);

        //아래 toolbar 버튼 설정
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(getApplicationContext(), Search2Activity.class);
                //intent.addFlags(intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("point", centerPointList);
                startActivityForResult(intent, 201);
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

        TMapMarkerItem markerItem1 = new TMapMarkerItem();
        TMapPoint tMapPoint1 = tMapView.getCenterPoint();

// 마커

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Toast.makeText(getApplicationContext(), "channel", Toast.LENGTH_SHORT).show();
    }
}
