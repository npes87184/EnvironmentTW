package com.npes87184.enviromenttw;

import android.os.Bundle;

import it.neokree.materialnavigationdrawer.MaterialNavigationDrawer;
import it.neokree.materialnavigationdrawer.elements.MaterialAccount;


public class NavigationDrawer extends MaterialNavigationDrawer {
    @Override
    public void init(Bundle savedInstanceState) {

        MaterialAccount account = new MaterialAccount(this.getResources(),"npes87184","npes87184@gmail.com",R.drawable.npes, R.drawable.bamboo);
        this.addAccount(account);

        this.addSection(newSection(getString(R.string.star), R.drawable.ic_menu_star, StarFragment.newInstance(0)));
        this.addSection(newSection(getString(R.string.radiation), R.drawable.radiation, RadiationFragment.newInstance(0)));
        this.addSection(newSection(getString(R.string.radiation_map), R.drawable.ic_perm_group_location, RadiationMap.newInstance(1)));
        this.addSection(newSection(getString(R.string.water_Reservoir), R.drawable.water, WaterFragment.newInstance(2)));
        this.addSection(newSection(getString(R.string.air), R.drawable.air, AirFragment.newInstance(3)));
   /*     this.addSection(newSection("Section 3", R.drawable.ic_mic_white_24dp,new FragmentButton()).setSectionColor(Color.parseColor("#9c27b0")));
        this.addSection(newSection("Section",R.drawable.ic_hotel_grey600_24dp,new FragmentButton()).setSectionColor(Color.parseColor("#03a9f4")));
*/
        // create bottom section
        this.addBottomSection(newSection(getString(R.string.about),R.drawable.ic_menu_info_details, AboutFragment.newInstance(4)));

        // add pattern
        this.setBackPattern(MaterialNavigationDrawer.BACKPATTERN_BACK_ANYWHERE);
    }
}
