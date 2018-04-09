package com.example.chanellehojberg.test;

import android.app.DownloadManager;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toolbar;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static android.app.PendingIntent.getActivity;

public class Main2Activity extends AppCompatActivity {

    private String baseServerURL = "http://169.234.31.186:5000/";
    private RequestQueue queue;
    ArrayAdapter<String> adapter;

    private ListView mListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTitle("EventEater");

        // Instantiate the RequestQueue.
        queue = Volley.newRequestQueue(this);

        query("the");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        mListView = (ListView) findViewById(R.id.listView);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                TextView tv = (TextView) view.findViewById(R.id.cardURL);
                String url = tv.getText().toString();
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(browserIntent);
            }
        });

    }

    public void updateEvents(JSONObject events) {
        System.out.println("CALLED UPDATE!");



        ArrayList<Card> list =  new ArrayList<>();

        try {

            for (int i = 0; i < 5; i++) {
                events.getString("event" + Integer.toString(i));
                JSONObject event_ = events.getJSONObject("event" + Integer.toString(i));
                String description = event_.getString("description");
                String name = event_.getString("name");
                String url  = event_.getString("url");

                JSONObject location = event_.getJSONObject("location");
                String address = location.getString("address");

                Card toAdd = new Card("drawable://" + R.drawable.sports_logo,
                        name,
                        description,
                        address,
                        url);

                list.add(toAdd);

            }
        }
        catch (Exception e) {

        }



        CustomListAdapter adapter = new CustomListAdapter(this, R.layout.card_layout_main, list);
        mListView.setAdapter(adapter);
    }

    public void query(String toQuery) {
        String url = baseServerURL + toQuery;

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        System.out.print("The response123 IS:" + response);
                        try {
                            JSONObject events = new JSONObject(response);
                            updateEvents(events);
                        }
                        catch(Exception e) {

                        }

                        //mTextView.setText("Response is: "+ response.substring(0,500));
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //mTextView.setText("That didn't work!");
            }
        });

        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu){

        MenuInflater inflater  = getMenuInflater();
        inflater.inflate(R.menu.menu_search, menu);
        MenuItem item = menu.findItem(R.id.menuSearch);
        SearchView searchView = (SearchView) item.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener(){

            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                query(s);
                //adapter.getFilter().filter(s);
                return false;
            }




        });





        return super.onCreateOptionsMenu(menu);

    }



}
