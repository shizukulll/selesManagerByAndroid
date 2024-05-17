package com.example.selesmanager.Fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.selesmanager.R;
import com.example.selesmanager.pojo.Product;
import com.example.selesmanager.util.Get;

import java.io.IOException;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AddFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddFragment extends Fragment {
    private EditText nameEditText, priceEditText, countEditText, manufacturerEditText;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public AddFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AddFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AddFragment newInstance(String param1, String param2) {
        AddFragment fragment = new AddFragment();
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
        View view = inflater.inflate(R.layout.fragment_add, container, false);

        // 初始化输入框
        nameEditText = view.findViewById(R.id.nameAdd);
        priceEditText = view.findViewById(R.id.priceAdd);
        countEditText = view.findViewById(R.id.countAdd);
        manufacturerEditText = view.findViewById(R.id.editTextText5);

        Button submitButton = view.findViewById(R.id.button5);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = nameEditText.getText().toString();
                String priceString = priceEditText.getText().toString();
                String countString = countEditText.getText().toString();
                String brand = manufacturerEditText.getText().toString();

                try {
                    double price = Double.parseDouble(priceString);
                    int count = Integer.parseInt(countString);
                    if(price < 0 || count < 0){
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getContext(), "价格不可小于0，数量不可小于0", Toast.LENGTH_SHORT).show();
                            }
                        });
                        return ;
                    }

                    SharedPreferences prefs = getActivity().getSharedPreferences("IPCFG", Context.MODE_PRIVATE);
                    String apiUrl = "http://" + prefs.getString("ip","49.232.17.181") + ":" + prefs.getString("port","8080") + "/mobile/product/insert";

                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            Get get = new Get();
                            // 创建一个新的 Product 对象并设置属性
                            Product product = new Product(name, price, count, brand);
                            try {
                                boolean isSuccess = get.insert(apiUrl, product);
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(getContext(), "添加成功", Toast.LENGTH_SHORT).show();
                                }
                            });

                        }
                    }).start();
                } catch (NumberFormatException e) {
                    Toast.makeText(getContext(), "请输入正确的价格和数量", Toast.LENGTH_SHORT).show();
                    return; // 不执行提交操作
                }
            }
        });

        return view;
    }
}