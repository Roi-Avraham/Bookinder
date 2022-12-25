package com.example.bookinder.MyProfile.ui.Profile;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.bookinder.MainActivity;
import com.example.bookinder.Profile.Profile_activity;
import com.example.bookinder.R;
import com.example.bookinder.UploadBook.UplodingBook;
import com.example.bookinder.databinding.FragmentProfileBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class ProfileFragment extends Fragment implements BottomNavigationView.OnNavigationItemSelectedListener{

    private FragmentProfileBinding binding;
    BottomNavigationView bottomNavigationView;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentProfileBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        bottomNavigationView = binding.buttonNavigation;
        bottomNavigationView.setOnNavigationItemSelectedListener(this);
        bottomNavigationView.setSelectedItemId(R.id.nav_person);
        bottomNavigationView.getMenu().findItem(R.id.nav_person).setChecked(true);

//        final TextView textView = binding.textHome;
//        homeViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Intent intent;
        switch (item.getItemId()) {
            case R.id.nav_home:
                intent = new  Intent(getActivity(), MainActivity.class);
                ProfileFragment.this.startActivity(intent);
                return true;
            case R.id.nav_cart:
                intent = new  Intent(getActivity(), UplodingBook.class);
                ProfileFragment.this.startActivity(intent);
                return true;
        }
        return false;
    }
}