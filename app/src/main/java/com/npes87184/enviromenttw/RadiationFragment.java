package com.npes87184.enviromenttw;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.baoyz.widget.PullRefreshLayout;
import com.dexafree.materialList.cards.SmallImageCard;
import com.dexafree.materialList.view.MaterialListView;

import java.util.ArrayList;

/**
 * Created by npes87184 on 2015/4/26.
 */
public class RadiationFragment extends Fragment implements FetchTask.OnFetchListener {

    private View v;
    MaterialListView mListView;
    PullRefreshLayout layout;

    public static RadiationFragment newInstance(int index) {
        RadiationFragment radiationFragment = new RadiationFragment();

        // Supply index input as an argument.
        Bundle args = new Bundle();
        args.putInt("home", index);
        radiationFragment.setArguments(args);

        return radiationFragment;
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
        layout = (PullRefreshLayout) v.findViewById(R.id.swipeRefreshLayout);
        mListView = (MaterialListView) v.findViewById(R.id.material_listview);

        ConnectivityManager CM = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = CM.getActiveNetworkInfo();
        if((info != null) && info.isConnected()) {
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
                if((info != null) && info.isConnected()) {
                    FetchTask radiation = new FetchTask();
                    radiation.setOnFetchListener(RadiationFragment.this);
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
            SmallImageCard card = new SmallImageCard(getActivity());
            card.setDescription(DataFetcher.getInstance().getRadiations().get(i).getValue());
            card.setTitle(DataFetcher.getInstance().getRadiations().get(i).getLocation());
            card.setDrawable(R.drawable.ic_launcher);
            mListView.add(card);
        }
        layout.setRefreshing(false);
    }
}
