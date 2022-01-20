package com.example.project;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CreateGroup#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CreateGroup extends Fragment implements View.OnClickListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    EditText groupName;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private DatabaseReference mDatabase;
    public CreateGroup() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CreateGroup.
     */
    // TODO: Rename and change types and number of parameters
    public static CreateGroup newInstance(String param1, String param2) {
        CreateGroup fragment = new CreateGroup();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View myView = inflater.inflate(R.layout.fragment_create_group, container, false);
        Button btn = myView.findViewById(R.id.create_group_button);

        groupName = myView.findViewById(R.id.create_group_name);
        mDatabase = FirebaseDatabase.getInstance().getReference();

        btn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                //generate a random group id from 3 letters
                final boolean[] found = new boolean[1];
                String finalId;
                do {
                    String id = "";
                    found[0] = false;
                    for (int i = 0; i < 3; i++)
                        id += (char) ('A' + (int) (Math.random() * 26));
                    for (int i = 0; i < 3; i++)
                        id += (char) ('0' + (int) (Math.random() * 10));
                    finalId = id;
                    String finalId1 = finalId;
                    mDatabase.child("groups").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DataSnapshot> task) {
                            if (!task.isSuccessful()) {
                                Log.e("firebase", "Error getting data", task.getException());
                            } else {
                                Object result = task.getResult().getValue();
                                    for (int i = 0; i < (((HashMap) result).keySet()).size(); i++) {
                                        if (finalId1.equals(((HashMap) result).keySet().toArray()[i])) {
                                            found[0] = true;
                                        }
                                    }
                            }
                        }
                    });
                }
                while(found[0]);
                Group group = new Group(groupName.getText().toString(), MainActivity.currentUser.getUid());
                mDatabase.child("groups").child(finalId).setValue(group);
                MyAppData.genreNames.add(groupName.getText().toString());
                MyAppData.genreDurations.add(finalId);
                mDatabase.child("groups").child(finalId).child("members").child(MainActivity.currentUser.getUid()).setValue(MainActivity.currentUser.getDisplayName());
                mDatabase.child("users").child(MainActivity.currentUser.getUid()).child("groups").child(finalId).setValue(groupName.getText().toString());
            }
        });
//        AlertDialog alertDialog = new AlertDialog.Builder(myView.getContext()).create();
//        alertDialog.setTitle("Alert");
//        alertDialog.setMessage("Alert message to be shown");
//        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
//                new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int which) {
//                        dialog.dismiss();
//                    }
//                });
//        alertDialog.show();
//        String groupNameText = groupName.getText().toString();
//        Group group = new Group(groupNameText, MainActivity.currentUser.getUid());
//        mDatabase = FirebaseDatabase.getInstance().getReference();
//        mDatabase.child("hell man").child("oh_yeah").child("idwhat").setValue("123");
//        Uri xx = FirebaseAuth.getInstance().getCurrentUser().getPhotoUrl();
//        Log.d("Photo",String.valueOf(xx));
//        mDatabase.child("hell man").child("oh_yeah").child("idwhat").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<DataSnapshot> task) {
//                if (!task.isSuccessful()) {
//                    Log.e("firebase", "Error getting data", task.getException());
//                }
//                else {
//                    Log.d("firebase", String.valueOf(task.getResult().getValue()));
//                    //store the result in a variable
//                    String result = task.getResult().getValue().toString();
//                    //do something with the result
//                    int i = 0;
//                }
//            }
//        });
        return myView;
    }
    @Override
    public void onClick(View v) {

    }
}