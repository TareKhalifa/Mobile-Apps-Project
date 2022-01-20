package com.example.project.joingroup;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.project.MainActivity;
import com.example.project.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link JoinGroup#newInstance} factory method to
 * create an instance of this fragment.
 */
public class JoinGroup extends Fragment {
    private DatabaseReference mDatabase;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public JoinGroup() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment JoinGroup.
     */
    // TODO: Rename and change types and number of parameters
    public static JoinGroup newInstance(String param1, String param2) {
        JoinGroup fragment = new JoinGroup();
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
        // Inflate the layout for this fragment
        mDatabase = FirebaseDatabase.getInstance().getReference();
        View myView = inflater.inflate(R.layout.fragment_join_group, container, false);
        Button btn = myView.findViewById(R.id.join_group_button);

        EditText IDField = myView.findViewById(R.id.edit_text_join_group);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String groupID = IDField.getText().toString();
                //TODO: Check if group exists
                mDatabase.child("groups").child(groupID).child("members").child(MainActivity.currentUser.getUid()).setValue(MainActivity.currentUser.getDisplayName());
                mDatabase.child("users").child(MainActivity.currentUser.getUid()).child("groups").child(groupID).setValue(groupID);
                Intent intent = new Intent(getActivity(), MainActivity.class);
                startActivity(intent);
            }
        });
        //mDatabase.child("groups").child(finalId)
        return myView;
    }
}