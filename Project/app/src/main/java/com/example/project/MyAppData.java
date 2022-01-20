package com.example.project;

import android.provider.ContactsContract;
import android.util.Log;
import android.widget.ListView;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;

public class MyAppData {
    private static DatabaseReference mDatabase;
    public static ArrayList<String> genreNames = new ArrayList<>();
    public static ArrayList<String> genreDurations = new ArrayList<>();
    static public void getMyGroups()
    {
        genreNames = new ArrayList<>();
        genreDurations = new ArrayList<>();
        ArrayList<String> tempList = new ArrayList<>();
        final Object[] result = new Object[1];
        HashMap<String, String> tempMap = new HashMap<>();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("users").child(MainActivity.currentUser.getUid()).child("groups").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e("firebase", "Error getting data", task.getException());
                } else {
                    if(task.getResult().getChildrenCount()!=0) {
                        DataSnapshot dataSnapshot = task.getResult();
                        for (DataSnapshot ds : dataSnapshot.getChildren()) {
                            tempList.add(ds.getKey());
                        }
                        mDatabase.child("groups").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DataSnapshot> task) {
                                if (!task.isSuccessful()) {
                                    Log.e("firebase", "Error getting data", task.getException());
                                } else {
                                    if(task.getResult().getChildrenCount()!=0) {
                                        DataSnapshot dataSnapshot = task.getResult();
                                        for (DataSnapshot ds : dataSnapshot.getChildren()) {
                                            if(tempList.contains(ds.getKey())) {
                                                tempMap.put(ds.getKey(), ds.child("name").getValue().toString());
                                            }
                                        }
                                        for(String key : tempMap.keySet())
                                        {
                                            genreNames.add(tempMap.get(key));
                                            genreDurations.add(key);
                                        }
                                    }
                                }
                            }
                        });
                    }
                }
            }
        });
    }
}
