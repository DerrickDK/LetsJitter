package edu.uga.cs.letsjitter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;


public class SettingsFragment extends Fragment {
    private FirebaseAuth authentication;
    private Button logoutButton;
    private View view;
    public SettingsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        authentication = FirebaseAuth.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_settings, container, false);
        logoutButton = view.findViewById(R.id.logOutButton);
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                authentication.signOut();
                Intent loginScreen = new Intent(getActivity(), LoginActivity.class);
                startActivity(loginScreen);
            }
        });

        // Inflate the layout for this fragment
        return view;

    }

}
