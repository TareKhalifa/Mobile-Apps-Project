package com.example.project.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.project.GoogleLogin;
import com.example.project.MainActivity;
import com.example.project.MyAppData;
import com.example.project.databinding.FragmentHomeBinding;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.events.Publisher;

public class HomeFragment extends Fragment implements View.OnClickListener  {

    private FragmentHomeBinding binding;
    private DatabaseReference mDatabase;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        final TextView textView = binding.textHome;
        homeViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        Button button = binding.signInOutButton;
        button.setText("Sign Out");
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GoogleLogin.isLoggedOut = true;
                Intent intent = new Intent(getActivity(), GoogleLogin.class);
                try {
                    getActivity().finish();
                }
                catch (Exception e){
                    Log.d("HomeFragment", "finishing: " + e.getMessage());
                }
                startActivity(intent);
            }
        });
        MyAppData.getMyGroups();
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onClick(View v) {

    }
}