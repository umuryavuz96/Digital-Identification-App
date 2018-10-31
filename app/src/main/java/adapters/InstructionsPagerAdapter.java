package adapters;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import fragments.Instruction1_Fragment;
import fragments.Instruction2_Fragment;
import fragments.Instruction3_Fragment;
import fragments.Instruction4_Fragment;
import fragments.Instruction5_Fragment;


public class InstructionsPagerAdapter extends FragmentPagerAdapter {

    final   int    PAGE_COUNT  = 5;
    private String tabTitles[] = new String[]{"", "", "", "", ""};
    private Context context;
    private Activity activity;



    public InstructionsPagerAdapter(FragmentManager fm, Context context, Activity activity) {
        super(fm);
        this.context = context;
        this.activity = activity;
    }

    @Override
    public int getCount() {
        return PAGE_COUNT;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new Instruction1_Fragment();
            case 1:
                return new Instruction2_Fragment();
            case 2:
                return new Instruction3_Fragment(context,activity);
            case 3:
                return new Instruction4_Fragment();
            case 4:
                return new Instruction5_Fragment();
            default:
                return new Instruction2_Fragment();
        }

    }

    @Override
    public CharSequence getPageTitle(int position) {
        // Generate title based on item position
        return tabTitles[position];
    }
}
