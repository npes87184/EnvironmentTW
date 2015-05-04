package com.npes87184.enviromenttw;

import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.widget.Toast;

/**
 * Created by npes87184 on 2015/4/26.
 */
public class RadiationMap  extends Fragment implements FetchTask.OnFetchListener, LocationListener {

    private View v;

    private static final String MAP_URL = "file:///android_asset/map.html";
    private WebView webView;
    private String[] inputStrings = new String[45];
    Double longitude;
    Double latitude;

    @JavascriptInterface
    public String getData(int i) {
        return inputStrings[i];
    }


    @JavascriptInterface
    public String getLat() {
        //get system location service
        LocationManager status = (LocationManager) (getActivity().getSystemService(Context.LOCATION_SERVICE));
        if (status.isProviderEnabled(LocationManager.GPS_PROVIDER) || status.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            //if we can use location service, call locationServiceInitial() update location
            locationServiceInitial();
            return String.valueOf(latitude);
        } else {
            return "23.5000";
        }
    }


    @JavascriptInterface
    public String getLng() {
        //get system location service
        LocationManager status = (LocationManager) (getActivity().getSystemService(Context.LOCATION_SERVICE));
        if (status.isProviderEnabled(LocationManager.GPS_PROVIDER) || status.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            //if we can use location service, call locationServiceInitial() update location
            locationServiceInitial();
            return String.valueOf(longitude);
        } else {
            return "120.7800";
        }
    }


    private LocationManager lms;
    private String bestProvider = LocationManager.GPS_PROVIDER;    //best location provider

    private void locationServiceInitial() {
        lms = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);    //get system location service
        Criteria criteria = new Criteria();    // choose location provider
        bestProvider = lms.getBestProvider(criteria, true);    //choose  location provider
        Location location = lms.getLastKnownLocation(bestProvider);
        getLocation(location);
    }

    private void getLocation(Location location) {    //show location
        if (location != null) {
            longitude = location.getLongitude();
            latitude = location.getLatitude();
        } else {
            Toast.makeText(getActivity(), getString(R.string.gps_fail), Toast.LENGTH_LONG).show();
        }
    }

    public static RadiationMap newInstance(int index) {
        RadiationMap radiationMap = new RadiationMap();

        // Supply index input as an argument.
        Bundle args = new Bundle();
        args.putInt("home", index);
        radiationMap.setArguments(args);

        return radiationMap;
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
        v = inflater.inflate(R.layout.radiation_map, container, false);

        ConnectivityManager CM = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = CM.getActiveNetworkInfo();
        if ((info != null) && info.isConnected()) {
            FetchTask radiation = new FetchTask();
            radiation.setOnFetchListener(this);
            radiation.execute(DataType.Radiation);
        } else {
            Toast.makeText(getActivity(), getString(R.string.internet_detail), Toast.LENGTH_LONG).show();
        }

        return v;
    }

    @Override
    public void OnRadiationFetchFinished() {
        for(int i=0;i<DataFetcher.getInstance().getRadiations().size();i++) {
            inputStrings[i] = DataFetcher.getInstance().getRadiations().get(i).getLocation() + "：" +DataFetcher.getInstance().getRadiations().get(i).getValue();
        }
        setupWebView();
    }

    @Override
    public void OnWaterFetchFinished() {

    }

    @Override
    public void OnAirFinished() {

    }

    @Override
    public void OnUVFinished() {

    }

    private void setupWebView() {
        webView = (WebView) v.findViewById(R.id.webView);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.addJavascriptInterface(RadiationMap.this , "AndroidFunction");
        webView.loadUrl(MAP_URL);
    }


    @Override
    public void onLocationChanged(Location arg0) {	//when location change
        // TODO Auto-generated method stub

    }

    @Override
    public void onProviderDisabled(String arg0) {	//when gps close
        // TODO Auto-generated method stub

    }

    @Override
    public void onProviderEnabled(String arg0) {	//when gps om
        // TODO Auto-generated method stub

    }

    @Override
    public void onStatusChanged(String arg0, int arg1, Bundle arg2) {	//when gps status change
        // TODO Auto-generated method stub

    }

}
