package com.npes87184.enviromenttw;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import lecho.lib.hellocharts.listener.ColumnChartOnValueSelectListener;
import lecho.lib.hellocharts.model.Column;
import lecho.lib.hellocharts.model.ColumnChartData;
import lecho.lib.hellocharts.model.PieChartData;
import lecho.lib.hellocharts.model.SliceValue;
import lecho.lib.hellocharts.model.SubcolumnValue;
import lecho.lib.hellocharts.util.ChartUtils;
import lecho.lib.hellocharts.view.ColumnChartView;
import lecho.lib.hellocharts.view.PieChartView;

/**
 * Created by npes87184 on 2015/4/26.
 */
public class WaterFragment extends Fragment implements FetchTask.OnFetchListener {

    private View v;
    private ColumnChartView chart;
    private ColumnChartData data;
    private TextView tv;

    public static WaterFragment newInstance(int index) {
        WaterFragment waterFragment = new WaterFragment();

        // Supply index input as an argument.
        Bundle args = new Bundle();
        args.putInt("home", index);
        waterFragment.setArguments(args);

        return waterFragment;
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
        v = inflater.inflate(R.layout.fragment_water, container, false);
        chart = (ColumnChartView)v.findViewById(R.id.chart);
        ConnectivityManager CM = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = CM.getActiveNetworkInfo();
        if((info != null) && info.isConnected()) {
            FetchTask water = new FetchTask();
            water.setOnFetchListener(this);
            water.execute(DataType.Water);
        } else {
            Toast.makeText(getActivity(), getString(R.string.internet_detail), Toast.LENGTH_LONG).show();
        }

        return v;
    }

    @Override
    public void OnRadiationFetchFinished() {

    }

    @Override
    public void OnAirFinished() {

    }

    @Override
    public void OnWaterFetchFinished() {
        chart.setOnValueTouchListener(new ValueTouchListener());
        chart.setValueSelectionEnabled(false);
        chart.setZoomEnabled(false);
        data = new ColumnChartData(generateColumnData());
        chart.setColumnChartData(data);
    }

    private ColumnChartData generateColumnData() {

        int numColumns = DataFetcher.getInstance().getWater().size();
        // Column can have many subcolumns, here by default I use 1 subcolumn in each of 8 columns.
        List<Column> columns = new ArrayList<Column>();
        List<SubcolumnValue> values;
        for (int i = 0; i < numColumns; ++i) {
            values = new ArrayList<SubcolumnValue>();

            float tt = Float.valueOf(DataFetcher.getInstance().getWater().get(i).getValue());
            if(tt<=30) {
                values.add(new SubcolumnValue(tt, ChartUtils.COLOR_RED));
            } else if(30<tt && tt <=60) {
                values.add(new SubcolumnValue(tt, ChartUtils.COLOR_ORANGE));
            } else {
                values.add(new SubcolumnValue(tt, ChartUtils.COLOR_GREEN));
            }
            Column column = new Column(values);
            column.setHasLabels(true);
            column.setHasLabelsOnlyForSelected(false);
            columns.add(column);
        }

        ColumnChartData columnChartData = new ColumnChartData(columns);
        return columnChartData;
    }

    private class ValueTouchListener implements ColumnChartOnValueSelectListener {

        @Override
        public void onValueSelected(int columnIndex, int subcolumnIndex, SubcolumnValue value) {
            View view = View.inflate(getActivity(), R.layout.water_detail, null);

            PieChartView piechart;
            PieChartData piedata;

            piechart = (PieChartView)view.findViewById(R.id.chart);

            List<SliceValue> values = new ArrayList<SliceValue>();

            float tt = Float.valueOf(DataFetcher.getInstance().getWater().get(columnIndex).getValue());
            if(tt<=30) {
                SliceValue sliceValue = new SliceValue(tt, ChartUtils.COLOR_RED);
                values.add(sliceValue);
            } else if(30<tt && tt <=60) {
                SliceValue sliceValue = new SliceValue(tt, ChartUtils.COLOR_ORANGE);
                values.add(sliceValue);
            } else {
                SliceValue sliceValue = new SliceValue(tt, ChartUtils.COLOR_GREEN);
                values.add(sliceValue);
            }

            SliceValue sliceValue1 = new SliceValue(100-tt, Color.parseColor("#9E9E9E"));
            values.add(sliceValue1);

            piedata = new PieChartData(values);
            piedata.setHasLabels(true);
            piedata.setHasCenterCircle(true);
            piedata.setCenterText1(DataFetcher.getInstance().getWater().get(columnIndex).getLocation());
            piechart.setValueSelectionEnabled(false);
            piechart.setPieChartData(piedata);

            AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
            dialog.setTitle(getResources().getString(R.string.detail)).setView(view).setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface arg0, int arg1) {
                    // TODO Auto-generated method stub
                }
            }).show();
        }

        @Override
        public void onValueDeselected() {
            // TODO Auto-generated method stub
        }

    }
}
