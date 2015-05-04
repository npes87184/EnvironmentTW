package com.npes87184.enviromenttw;

import android.os.AsyncTask;

/**
 * Created by npes87184 on 2015/4/26.
 */

enum DataType {
    Radiation, Water, Air, UV
}

public class FetchTask extends AsyncTask<DataType, Void, DataType> {

    interface OnFetchListener {
        public void OnRadiationFetchFinished();
        public void OnWaterFetchFinished();
        public void OnAirFinished();
        public void OnUVFinished();
    }

    private OnFetchListener onFetchListener;

    @Override
    protected DataType doInBackground(DataType[] params) {
        switch (params[0]) {
            case Radiation:
                DataFetcher.getInstance().fetchRadiation();
                break;
            case Water:
                DataFetcher.getInstance().fetchWater();
                break;
            case Air:
                DataFetcher.getInstance().fetchAir();
                break;
            case UV:
                DataFetcher.getInstance().fetchUV();
                break;
        }
        return params[0];
    }

    @Override
    protected void onPostExecute(DataType param) {
        switch (param) {
            case Radiation:
                onFetchListener.OnRadiationFetchFinished();
                break;
            case Water:
                onFetchListener.OnWaterFetchFinished();
                break;
            case Air:
                onFetchListener.OnAirFinished();
                break;
            case UV:
                onFetchListener.OnUVFinished();
                break;
        }
    }

    public void setOnFetchListener(OnFetchListener listener){
        onFetchListener = listener;
    }

}
