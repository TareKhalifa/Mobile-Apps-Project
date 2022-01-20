package com.example.project;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.project.databinding.ActivityMainBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    public ActivityMainBinding binding;
    static public FirebaseUser currentUser;
    static public boolean isMainMenuLaunched = false;
    private DatabaseReference mDatabase;
    public static ImageView i;
    public static LoggedInUser user;
    //remove title bar


    @Override
    //TODO: create a loggedin user class

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        currentUser = GoogleLogin.currentUser;
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.appBarMain.toolbar);
        //getSupportActionBar().hide();
        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.create_group, R.id.join_group)
                .setOpenableLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
        if (currentUser != null) {
            user = new LoggedInUser();
            user.setEmail(currentUser.getEmail());
            user.setDisplayName(currentUser.getDisplayName());
            user.setUid(currentUser.getUid());
            user.setPhotoUrl(currentUser.getPhotoUrl().toString());
            mDatabase = FirebaseDatabase.getInstance().getReference();
            mDatabase.keepSynced(true);
            mDatabase.child("users").child(currentUser.getUid()).child("name").setValue(currentUser.getDisplayName());
            mDatabase.child("users").child(currentUser.getUid()).child("email").setValue(currentUser.getEmail());
            mDatabase.child("users").child(currentUser.getUid()).child("photo").setValue(currentUser.getPhotoUrl().toString());
            ArrayList<Group> groups = new ArrayList<>();
            ArrayList<String> groupIds = new ArrayList<>();
            mDatabase.child("users").child(user.getUid()).child("groups").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DataSnapshot> task) {
                    if (!task.isSuccessful()) {
                        Log.e("firebase", "Error getting data", task.getException());
                    } else {
                        if(task.getResult().getChildrenCount()!=0) {
                            DataSnapshot dataSnapshot = task.getResult();
                            for (DataSnapshot ds : dataSnapshot.getChildren()) {
                                groupIds.add(ds.getKey());
                            }
                            for (String id : groupIds) {
                                mDatabase.child("groups").child(id).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                                        if (!task.isSuccessful()) {
                                            Log.e("firebase", "Error getting data", task.getException());
                                        } else {
                                            DataSnapshot ds = task.getResult();
                                            Group group = new Group(ds.child("name").getValue().toString(),user.getUid());
                                            group.setId(ds.getKey());
                                            ArrayList<String> members = new ArrayList<>();
                                            ArrayList<String> transactionsIDs = new ArrayList<>();
                                            HashMap<String, String> transactions = new HashMap<>();
                                            for (DataSnapshot ds2 : ds.child("members").getChildren()) {
                                                members.add(ds2.getKey());
                                            }
                                            for (DataSnapshot ds2 : ds.child("Transactions").getChildren()) {
                                                transactionsIDs.add(ds2.getKey());
                                            }
                                            group.setMembers(members.toArray(new String[members.size()]));
                                            for(String member : members){
                                                mDatabase.child("users").child(member).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                                                        if (!task.isSuccessful()) {
                                                            Log.e("firebase", "Error getting data", task.getException());
                                                        } else {
                                                            DataSnapshot dataSnapshot = task.getResult();
                                                            Log.e("firebase", "photo: " + dataSnapshot.getValue().toString()+"  " + member);
                                                            group.photos.put(member,dataSnapshot.child("photo").getValue().toString());
                                                            group.names.put(member,dataSnapshot.child("name").getValue().toString());

                                                        }
                                                    }
                                                });
                                            }
                                            for(String trans : transactionsIDs){
                                                mDatabase.child("groups").child(id).child("Transactions").child(trans).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                                                        if (!task.isSuccessful()) {
                                                            Log.e("firebase", "Error getting data", task.getException());
                                                        } else {
                                                            group.transactions.put(task.getResult().getKey(),task.getResult().getValue(Transaction.class));
                                                        }
                                                    }
                                                });
                                            }
                                            groups.add(group);
                                            user.setGroups(groups.toArray(new Group[groups.size()]));
                                        }
                                    }
                                });
                            }
                        }
                    }
                }
            });
        }
        //Toast.makeText(this, "Welcome " + currentUser.getDisplayName(), Toast.LENGTH_LONG).show();
        TextView t = binding.navView.getHeaderView(0).findViewById(R.id.LoggedInUserName);
        t.setText(currentUser.getDisplayName());
        TextView t1 = binding.navView.getHeaderView(0).findViewById(R.id.LoggedInUserEmail);
        t1.setText(currentUser.getEmail());
        i = binding.navView.getHeaderView(0).findViewById(R.id.userImg);
        new AsyncTasks(currentUser.getPhotoUrl().toString(), i).execute();
        isMainMenuLaunched = true;


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }
}