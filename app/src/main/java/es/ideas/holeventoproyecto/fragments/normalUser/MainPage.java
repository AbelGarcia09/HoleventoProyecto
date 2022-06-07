package es.ideas.holeventoproyecto.fragments.normalUser;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import es.ideas.holeventoproyecto.R;


public class MainPage extends Fragment {

    public MainPage() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_normal_user_main_page, container, false);
    }
}