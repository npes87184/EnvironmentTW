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

import com.baoyz.widget.PullRefreshLayout;
import com.npes87184.enviromenttw.model.RadiationAdapter;
import com.npes87184.enviromenttw.model.UVAdapter;

import java.util.ArrayList;

/**
 * Created by npes87184 on 2015/5/3.
 */
public class UVFragment extends Fragment implements FetchTask.OnFetchListener {

    private View v;
    private ListView listV;

    private SharedPreferences prefs;
    private UVAdapter adapter;
    private final String KEY_UV = "UV";
    private ArrayList<Boolean> star =  new ArrayList<Boolean>();

    public static UVFragment newInstance(int index) {
        UVFragment uvFragment = new UVFragment();

        // Supply index input as an argument.
        Bundle args = new Bundle();
        args.putInt("home", index);
        uvFragment.setArguments(args);

        return uvFragment;
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
            public void onItemClick(AdapterView parent,View v,int id,long arg3) {
                //save star or not
                boolean temp = prefs.getBoolean(KEY_UV + DataFetcher.getInstance().getUV().get(id).getLocation(), false);
                if(temp) {
                    prefs.edit().putBoolean(KEY_UV + DataFetcher.getInstance().getUV().get(id).getLocation(), false).commit();
                } else {
                    prefs.edit().putBoolean(KEY_UV + DataFetcher.getInstance().getUV().get(id).getLocation(), true).commit();
                }
                adapter.setSelectItem(id, !temp);
                adapter.notifyDataSetInvalidated();
            }
        });

        ConnectivityManager CM = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = CM.getActiveNetworkInfo();
        if((info != null) && info.isConnected()) {
            OnUVFinished();
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
        alert.show();*/

        return v;
    }

    @Override
    public void OnRadiationFetchFinished() {

    }

    @Override
    public void OnWaterFetchFinished() {

    }

    @Override
    public void OnAirFinished() {

    }

    @Override
    public void OnUVFinished() {
        for(int i=0;i<DataFetcher.getInstance().getUV().size();i++) {
            star.add(prefs.getBoolean(KEY_UV + DataFetcher.getInstance().getUV().get(i).getLocation(), false));
        }
        adapter = new UVAdapter(getActivity(), DataFetcher.getInstance().getUV());
        adapter.init(star);
        listV.setAdapter(adapter);
    }
}
