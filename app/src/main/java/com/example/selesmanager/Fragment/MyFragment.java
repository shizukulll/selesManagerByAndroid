package com.example.selesmanager.Fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.selesmanager.Activity.INFOActivity;
import com.example.selesmanager.Activity.LoginActivity;
import com.example.selesmanager.R;
import com.example.selesmanager.pojo.User;
import com.example.selesmanager.util.Get;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MyFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MyFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    User user;
    private TextView nameView, idView, phoneView;
    FloatingActionButton fab ;
    private Button update,quit;

    public MyFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MyFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MyFragment newInstance(String param1, String param2) {
        MyFragment fragment = new MyFragment();
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
        View view = inflater.inflate(R.layout.fragment_my, container, false);
        nameView = view.findViewById(R.id.nameView);
        idView = view.findViewById(R.id.idView);
        phoneView = view.findViewById(R.id.phoneView);
        update = view.findViewById(R.id.button);
        quit = view.findViewById(R.id.button2);
        fab = view.findViewById(R.id.floatingActionButton);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), INFOActivity.class);
                startActivity(intent);
                getActivity().finish();
            }
        });

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "敬请期待", Toast.LENGTH_SHORT).show();
            }
        });

        quit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);
                getActivity().finish();
            }
        });

        SharedPreferences prefs = getActivity().getSharedPreferences("USER", Context.MODE_PRIVATE);
        String id = prefs.getString("id", "null");
        SharedPreferences pref = getActivity().getSharedPreferences("IPCFG", Context.MODE_PRIVATE);
        String apiUrl = "http://" + pref.getString("ip", "49.232.17.181") + ":" + pref.getString("port", "8080") + "/mobile/user/findById";

        new Thread(new Runnable() {
            @Override
            public void run() {
                Get get = new Get();
                User user = get.getUserById(apiUrl, id);

                // Update UI on the main thread
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (user != null) {
                            nameView.setText(user.getName());
                            idView.setText(user.getId());
                            phoneView.setText(user.getMobile());
                        } else {
                            // Handle the case where user is null
                            nameView.setText("User not found");
                            idView.setText("N/A");
                            phoneView.setText("N/A");
                        }
                    }
                });
            }
        }).start();

        return view;
    }
}
