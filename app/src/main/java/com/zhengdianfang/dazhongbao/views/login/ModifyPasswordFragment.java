package com.zhengdianfang.dazhongbao.views.login;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zhengdianfang.dazhongbao.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class ModifyPasswordFragment extends Fragment {


    public ModifyPasswordFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_password_modify, container, false);
    }

}
