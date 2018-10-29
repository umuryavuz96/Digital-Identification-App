package fragments;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.murat.m_onboarding.R;

import activities.IDScanActivity;
import activities.InstructionsActivity;

@SuppressLint("ValidFragment")
public class Instruction3_Fragment extends Fragment {

    private TextView done;
    private Context context;
    private Activity activity;

    @SuppressLint("ValidFragment")
    public Instruction3_Fragment(Context context, Activity activity) {
        this.context = context;
        this.activity = activity;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_instruction3,container,false);
        done = (TextView) view.findViewById(R.id.done);

        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent goToIDScan = new Intent(context, IDScanActivity.class);
                startActivity(goToIDScan);
                activity.finish();
            }
        });

        return view;


    }

}
