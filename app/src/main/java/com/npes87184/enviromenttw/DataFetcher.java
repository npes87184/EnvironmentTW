package com.npes87184.enviromenttw;

import com.npes87184.enviromenttw.model.Radiation;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;

/**
 * Created by npes87184 on 2015/4/26.
 */
public class DataFetcher {

    private ArrayList<Radiation> radiations = new ArrayList<Radiation>();

    private DataFetcher() {
    }

    private static DataFetcher mFetcher;

    public static DataFetcher getInstance() {
        if (null == mFetcher) {
            mFetcher = new DataFetcher();
        }
        return mFetcher;
    }

    public void fetchRadiation() {
        radiations.clear();
        try {
            boolean first = true;
            DefaultHttpClient client = new DefaultHttpClient();
            HttpGet method = new HttpGet(new URI("http://www.aec.gov.tw/open/gammamonitor.csv"));
            HttpResponse res = client.execute(method);
            BufferedReader reader = new BufferedReader(new InputStreamReader(res.getEntity().getContent(),"BIG5"));
            String line;
            while((line = reader.readLine())!=null) {
                String [] data = line.split(",");
                if(first) {
                    first = false;
                    continue;
                }
                Radiation temp = new Radiation(data[0], data[2]);
                radiations.add(temp);
            }
            first = true;
        } catch (IOException e) {

        } catch (URISyntaxException e) {

        }
    }

    public ArrayList<Radiation> getRadiations() {
        return radiations;
    }
}

