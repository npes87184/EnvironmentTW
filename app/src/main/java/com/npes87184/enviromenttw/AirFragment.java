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

import com.npes87184.enviromenttw.model.AirAdapter;

import java.util.ArrayList;

/**
 * Created by npes87184 on 2015/4/26.
 */
public class AirFragment extends Fragment {

    private View v;
    private ListView listV;

    private SharedPreferences prefs;
    private AirAdapter adapter;
    private final String KEY_AIR = "air";
    private ArrayList<Boolean> star =  new ArrayList<Boolean>();

    public static AirFragment newInstance(int index) {
        AirFragment airFragment = new AirFragment();

        // Supply index input as an argument.
        Bundle args = new Bundle();
        args.putInt("home", index);
        airFragment.setArguments(args);

        return airFragment;
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
                boolean temp = prefs.getBoolean(KEY_AIR + DataFetcher.getInstance().getAir().get(i).getValue().split(":")[0], false);
                if(temp) {
                    prefs.edit().putBoolean(KEY_AIR + DataFetcher.getInstance().getAir().get(i).getValue().split(":")[0], false).commit();
                } else {
                    prefs.edit().putBoolean(KEY_AIR + DataFetcher.getInstance().getAir().get(i).getValue().split(":")[0], true).commit();
                }
                adapter.setSelectItem(i, !temp);
                adapter.notifyDataSetInvalidated();
            }
        });

        ConnectivityManager CM = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = CM.getActiveNetworkInfo();
        if((info != null) && info.isConnected()) {
            OnAirFinished();
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
/*
        AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
        alert.setTitle(getString((R.string.data_source)));
        alert.setMessage(getString((R.string.data_source_detail)));
        alert.setNegativeButton("Close", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        });
        alert.show();
*/
        return v;
    }

    private void OnAirFinished() {
        if(DataFetcher.getInstance().getAir().size()==0) {
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
            for(int i=0;i<DataFetcher.getInstance().getAir().size();i++) {
                star.add(prefs.getBoolean(KEY_AIR + DataFetcher.getInstance().getAir().get(i).getValue().split(":")[0], false));
            }
            adapter = new AirAdapter(getActivity(), DataFetcher.getInstance().getAir());
            adapter.init(star);
            listV.setAdapter(adapter);
        }
    }

}
