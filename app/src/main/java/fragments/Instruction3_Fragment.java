package fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.murat.m_onboarding.R;

import activities.IDScanActivity;
import activities.InstructionsActivity;


public class Instruction3_Fragment extends Fragment {





    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        return inflater.inflate(R.layout.fragment_instruction3,container,false);


    }

}
