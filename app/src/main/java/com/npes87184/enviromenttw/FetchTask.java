package com.npes87184.enviromenttw;

import android.os.AsyncTask;

/**
 * Created by npes87184 on 2015/4/26.
 */

enum DataType {
    Radiation, Capacity
}

public class FetchTask extends AsyncTask<DataType, Void, DataType> {

    interface OnFetchListener {
        public void OnRadiationFetchFinished();
    }

    private OnFetchListener onFetchListener;

    @Override
    protected DataType doInBackground(DataType[] params) {
        switch (params[0]) {
            case Radiation:
                DataFetcher.getInstance().fetchRadiation();
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
        }
    }

    public void setOnFetchListener(OnFetchListener listener){
        onFetchListener = listener;
    }

}
