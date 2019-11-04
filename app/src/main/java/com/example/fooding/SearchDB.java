package com.example.fooding;

import android.location.Location;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.skt.Tmap.TMapPoint;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import androidx.annotation.NonNull;


public class SearchDB {
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    public SearchDB() {
    }

    public void returnData(final ArrayList<JSONObject> jsonObjectArrayList,final TMapPoint centerPoint) {
        db.collection("Crawling")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                double distance = checkDistance(document,centerPoint);
                                if(distance>1000) {
                                    continue;
                                }
                                else {
                                    JSONObject jsonObject = new JSONObject();
                                    Log.i("returnData", document.getId() + " => " + document.getData().get("name"));
                                    returnYoutube(jsonObjectArrayList, jsonObject, document.getId(), document.getData().get("name"), document.getData().get("lat"), document.getData().get("lng"));
                                }
                            }
                        } else {
                            Log.e("returnData", "Error getting documents: ", task.getException());
                        }
                    }
                });
    }

    public void returnYoutube(final ArrayList<JSONObject> jsonObjectArrayList,final JSONObject jsonObject ,final Object id,final Object name,final Object lat,final Object lng){
        db.collection("Crawling").document(id.toString()).collection("youtube")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            ArrayList<String> videoList = new ArrayList<>();
                            for (QueryDocumentSnapshot document : task.getResult()) {

                                JSONObject youtubeitem = new JSONObject();
                                try {
                                    youtubeitem.put("thumbnail",document.getData().get("thumbnail"));
                                    youtubeitem.put("channel",document.getData().get("channel"));
                                    youtubeitem.put("description",document.getData().get("description"));
                                    youtubeitem.put("title",document.getData().get("title"));
                                    youtubeitem.put("URL",document.getData().get("URL"));
                                    videoList.add(youtubeitem.toString());
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }
                            try {
                                jsonObject.put("name",name);
                                jsonObject.put("lat",lat);
                                jsonObject.put("lng",lng);
                                jsonObject.put("youtube",videoList);
                                Log.i("returnYoutube",jsonObject.toString());
                                jsonObjectArrayList.add(jsonObject);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        } else {
                            Log.e("returnYoutube", "Error getting documents: ", task.getException());
                        }
                    }
                });
    }

    public double checkDistance(QueryDocumentSnapshot document, TMapPoint point){
        Location locationA = new Location("point A");
        Location locationB = new Location("point B");

        locationA.setLatitude(point.getLatitude());
        locationA.setLongitude(point.getLongitude());

        locationB.setLatitude(Double.parseDouble(document.getData().get("lat").toString()));
        locationB.setLongitude(Double.parseDouble(document.getData().get("lng").toString()));

        double distance = locationA.distanceTo(locationB);

        Log.i("checkDistance",Double.toString(distance));
        return distance;
    }


}