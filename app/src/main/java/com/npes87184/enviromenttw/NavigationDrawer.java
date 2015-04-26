package com.npes87184.enviromenttw;

import android.os.Bundle;

import it.neokree.materialnavigationdrawer.MaterialNavigationDrawer;
import it.neokree.materialnavigationdrawer.elements.MaterialAccount;


public class NavigationDrawer extends MaterialNavigationDrawer {
    @Override
    public void init(Bundle savedInstanceState) {

        MaterialAccount account = new MaterialAccount(this.getResources(),"npes87184","npes87184@gmail.com",R.drawable.npes, R.drawable.bamboo);
        this.addAccount(account);

        this.addSection(newSection("Section 1", RadiationFragment.newInstance(0)));
        this.addSection(newSection("Section 2", RadiationMap.newInstance(1)));
   /*     this.addSection(newSection("Section 3", R.drawable.ic_mic_white_24dp,new FragmentButton()).setSectionColor(Color.parseColor("#9c27b0")));
        this.addSection(newSection("Section",R.drawable.ic_hotel_grey600_24dp,new FragmentButton()).setSectionColor(Color.parseColor("#03a9f4")));

        // create bottom section
        this.addBottomSection(newSection("Bottom Section",R.drawable.ic_settings_black_24dp,new Intent(this,Settings.class)));
*/
        // add pattern
        this.setBackPattern(MaterialNavigationDrawer.BACKPATTERN_BACK_ANYWHERE);
    }
}
