package com.npes87184.enviromenttw;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.baoyz.widget.PullRefreshLayout;
import com.dexafree.materialList.cards.SmallImageCard;
import com.dexafree.materialList.view.MaterialListView;

/**
 * Created by npes87184 on 2015/5/2.
 */
public class StarFragment extends Fragment implements FetchTask.OnFetchListener {
    private View v;
    MaterialListView mListView;
    PullRefreshLayout layout;
    private SharedPreferences prefs;
    private final String KEY_RADIATION = "radiation";
    private final String KEY_AIR = "air";
    private final String KEY_UV = "UV";
    private ProgressDialog dialog;

    public static StarFragment newInstance(int index) {
        StarFragment starFragment = new StarFragment();

        // Supply index input as an argument.
        Bundle args = new Bundle();
        args.putInt("home", index);
        starFragment.setArguments(args);

        return starFragment;
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
        v = inflater.inflate(R.layout.fragment_star, container, false);
        layout = (PullRefreshLayout) v.findViewById(R.id.swipeRefreshLayout);
        mListView = (MaterialListView) v.findViewById(R.id.material_listview);
        prefs = getActivity().getPreferences(1);

        ConnectivityManager CM = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = CM.getActiveNetworkInfo();
        if((info != null) && info.isConnected()) {
            dialog = ProgressDialog.show(getActivity(),
                    getString(R.string.load), getString(R.string.load_detail), true);
            FetchTask radiation = new FetchTask();
            radiation.setOnFetchListener(this);
            radiation.execute(DataType.Radiation);
        } else {
            mListView.clear();
            SmallImageCard card = new SmallImageCard(getActivity());
            card.setDescription(getString(R.string.internet_detail));
            card.setTitle(getString(R.string.internet));
            //card.setDrawable(R.drawable.ic_launcher);
            mListView.add(card);
        }

        // listen refresh event
        layout.setOnRefreshListener(new PullRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // start refresh
                ConnectivityManager CM = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo info = CM.getActiveNetworkInfo();
                if ((info != null) && info.isConnected()) {
                    FetchTask radiation = new FetchTask();
                    radiation.setOnFetchListener(StarFragment.this);
                    radiation.execute(DataType.Radiation);
                } else {
                    mListView.clear();
                    SmallImageCard card = new SmallImageCard(getActivity());
                    card.setDescription(getString(R.string.internet_detail));
                    card.setTitle(getString(R.string.internet));
                    //card.setDrawable(R.drawable.ic_launcher);
                    layout.setRefreshing(false);
                    mListView.add(card);
                }
            }
        });

        return v;
    }

    @Override
    public void OnRadiationFetchFinished() {
        mListView.clear();
        for(int i=0;i<DataFetcher.getInstance().getRadiations().size();i++) {
            boolean temp = prefs.getBoolean(KEY_RADIATION + String.valueOf(i), false);
            if(temp) {
                SmallImageCard card = new SmallImageCard(getActivity());
                card.setDescription(DataFetcher.getInstance().getRadiations().get(i).getLocation() + "：" + DataFetcher.getInstance().getRadiations().get(i).getValue());
                if (Float.parseFloat(DataFetcher.getInstance().getRadiations().get(i).getValue()) > 0.2) {
                    card.setDrawable(R.drawable.normal);
                } else if (Float.parseFloat(DataFetcher.getInstance().getRadiations().get(i).getValue()) > 20) {
                    card.setDrawable(R.drawable.bad);
                } else {
                    card.setDrawable(R.drawable.good);
                }
                card.setTitle(getString(R.string.radiation));
                mListView.add(card);
            }
        }
        FetchTask air = new FetchTask();
        air.setOnFetchListener(StarFragment.this);
        air.execute(DataType.Air);
    }

    @Override
    public void OnWaterFetchFinished() {

    }

    @Override
    public void OnAirFinished() {
        for(int i=0;i<DataFetcher.getInstance().getAir().size();i++) {
            boolean temp = prefs.getBoolean(KEY_AIR + DataFetcher.getInstance().getAir().get(i).getValue().split(":")[0], false);
            if(temp) {
                SmallImageCard card = new SmallImageCard(getActivity());
                card.setDescription(DataFetcher.getInstance().getAir().get(i).getLocation() + "：" + DataFetcher.getInstance().getAir().get(i).getValue());
                if (Float.parseFloat(DataFetcher.getInstance().getAir().get(i).getValue().split(":")[1]) < 50) {
                    card.setDrawable(R.drawable.good);
                } else if (Float.parseFloat(DataFetcher.getInstance().getAir().get(i).getValue().split(":")[1]) < 100) {
                    card.setDrawable(R.drawable.normal);
                } else {
                    card.setDrawable(R.drawable.bad);
                }
                card.setTitle(getString(R.string.air));
                mListView.add(card);
            }
        }
        FetchTask uv = new FetchTask();
        uv.setOnFetchListener(StarFragment.this);
        uv.execute(DataType.UV);
    }

    @Override
    public void OnUVFinished() {
        for(int i=0;i<DataFetcher.getInstance().getUV().size();i++) {
            boolean temp = prefs.getBoolean(KEY_UV + String.valueOf(i), false);
            if(temp) {
                SmallImageCard card = new SmallImageCard(getActivity());
                card.setDescription(DataFetcher.getInstance().getUV().get(i).getLocation() + "：" + DataFetcher.getInstance().getUV().get(i).getValue());
                if (Float.parseFloat(DataFetcher.getInstance().getUV().get(i).getValue()) < 3) {
                    card.setDrawable(R.drawable.good);
                } else if (Float.parseFloat(DataFetcher.getInstance().getUV().get(i).getValue()) < 6) {
                    card.setDrawable(R.drawable.normal);
                } else {
                    card.setDrawable(R.drawable.bad);
                }
                card.setTitle(getString(R.string.UV));
                mListView.add(card);
            }
        }
        dialog.dismiss();
        layout.setRefreshing(false);
    }

}
