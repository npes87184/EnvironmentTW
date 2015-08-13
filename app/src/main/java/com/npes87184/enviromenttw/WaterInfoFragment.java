package com.npes87184.enviromenttw;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.npes87184.enviromenttw.model.WaterInfoAdapter;

import java.util.ArrayList;

/**
 * Created by npes87184 on 2015/8/13.
 */
public class WaterInfoFragment extends Fragment {


    private View v;
    private ListView listV;

    private SharedPreferences prefs;
    private WaterInfoAdapter adapter;
    private final String KEY_WATER = "water";
    private ArrayList<Boolean> star =  new ArrayList<Boolean>();

    public static WaterInfoFragment newInstance(int index) {
        WaterInfoFragment waterInfoFragment = new WaterInfoFragment();

        // Supply index input as an argument.
        Bundle args = new Bundle();
        args.putInt("home", index);
        waterInfoFragment.setArguments(args);

        return waterInfoFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        v = inflater.inflate(R.layout.fragment_radiation, container, false);
        listV = (ListView)v.findViewById(R.id.listview1);
        prefs = getActivity().getPreferences(1);

        listV.setOnItemClickListener(new ListView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView parent,View v,int i,long arg3) {
                //save star or not
                boolean temp = prefs.getBoolean(KEY_WATER + DataFetcher.getInstance().getWater().get(i).getLocation(), false);
                if(temp) {
                    prefs.edit().putBoolean(KEY_WATER + DataFetcher.getInstance().getWater().get(i).getLocation(), false).commit();
                } else {
                    prefs.edit().putBoolean(KEY_WATER + DataFetcher.getInstance().getWater().get(i).getLocation(), true).commit();
                }
                adapter.setSelectItem(i, !temp);
                adapter.notifyDataSetInvalidated();
            }
        });

        ConnectivityManager CM = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = CM.getActiveNetworkInfo();
        if((info != null) && info.isConnected()) {
            OnWaterInfoFetchFinished();
        } else {
            AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
            alert.setTitle(getString((R.string.internet)));
            alert.setMessage(getString((R.string.internet_detail)));
            alert.setNegativeButton("Close", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int id) {
                    dialog.dismiss();
                }
            });
            alert.show();
        }

        return v;
    }

    private void OnWaterInfoFetchFinished() {
        if(DataFetcher.getInstance().getWater().size()==0) {
            AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
            alert.setTitle(getString((R.string.internet)));
            alert.setMessage(getString((R.string.internet_stauts)));
            alert.setNegativeButton("Close", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int id) {
                    dialog.dismiss();
                }
            });
            alert.show();
        } else {
            for(int i=0;i<DataFetcher.getInstance().getWater().size();i++) {
                star.add(prefs.getBoolean(KEY_WATER + DataFetcher.getInstance().getWater().get(i).getLocation(), false));
            }
            adapter = new WaterInfoAdapter(getActivity(), DataFetcher.getInstance().getWater());
            adapter.init(star);
            listV.setAdapter(adapter);
        }
    }

}
