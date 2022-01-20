package com.example.project;

import static androidx.databinding.DataBindingUtil.setContentView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * A fragment representing a list of Items.
 */
public class MyGroupsFragment extends Fragment implements AdapterView.OnItemClickListener {
    String[] genreUrls;
    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 1;
    private DatabaseReference mDatabase;
    ArrayList<String> genreNames2;
    ArrayList<String> genreDurations2;
    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public MyGroupsFragment() {

    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static MyGroupsFragment newInstance(int columnCount) {
        MyGroupsFragment fragment = new MyGroupsFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //hide actionbar
        //((AppCompatActivity) getActivity()).getSupportActionBar().hide();


        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
        MainActivity main = (MainActivity) getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.my_groups_fragment, container, false);
        Context context = view.getContext();
        ListView lv = (ListView) view.findViewById(R.id.listtt);
        Object [] genreNames =  MyAppData.genreNames.toArray();
        Object [] genreDurations = MyAppData.genreDurations.toArray();
        ArrayList<MyGroupsRowData> videos = new ArrayList<>();
        for (int ii = 0; ii < MyAppData.genreNames.size(); ii++) {
            videos.add(new MyGroupsRowData(genreNames[ii].toString(), genreDurations[ii].toString()));
        }
        MyGroupsListAdapter adapter = new MyGroupsListAdapter(getActivity(), videos);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view11, int position, long id) {
                Intent intent = new Intent(getActivity(), TransactionsList.class);
                intent.putExtra("groupName", genreNames[position].toString());
                intent.putExtra("groupID", genreDurations[position].toString());
                startActivity(intent);
            }
        });
        lv.setAdapter(adapter);
        return view;
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        MainActivity main = (MainActivity) getActivity();
    }
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }
}
