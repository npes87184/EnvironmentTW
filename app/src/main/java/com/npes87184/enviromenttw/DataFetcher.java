package com.npes87184.enviromenttw;

import com.npes87184.enviromenttw.model.DataContainer;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;

/**
 * Created by npes87184 on 2015/4/26.
 */
public class DataFetcher {

    private ArrayList<DataContainer> radiations = new ArrayList<DataContainer>();
    private ArrayList<DataContainer> water = new ArrayList<DataContainer>();

    private DataFetcher() {
    }

    private static DataFetcher mFetcher;

    public static DataFetcher getInstance() {
        if (null == mFetcher) {
            mFetcher = new DataFetcher();
        }
        return mFetcher;
    }

    public void fetchWater() {
        water.clear();
        try {
            Document doc = Jsoup.connect("http://fhy.wra.gov.tw/ReservoirPage_2011/StorageCapacity.aspx").get();
            Elements tableTags = doc.getElementsByAttributeValue("class", "list nowrap").select("table");
            Elements tdTags = tableTags.select("td").not("td[colspan]");

            for (int i = 0; i < tdTags.size(); i += 12) {
                if (i + 12 <= tdTags.size() - 1) {
                    if ((i + 11) % 12 == 11 && tdTags.get(i + 11).text().equals("--")) {
                        continue;
                    } else {
                        DataContainer temp = new DataContainer(tdTags.get(i).text(), tdTags.get(i + 11).text().replace(" %", ""));
                        water.add(temp);
                    }
                }
            }

        } catch(Exception e) {

        }
    }

    public ArrayList<DataContainer> getWater() {
        return water;
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
                DataContainer temp = new DataContainer(data[0], data[2]);
                radiations.add(temp);
            }
            first = true;
        } catch (IOException e) {

        } catch (URISyntaxException e) {

        }
    }

    public ArrayList<DataContainer> getRadiations() {
        return radiations;
    }
}

