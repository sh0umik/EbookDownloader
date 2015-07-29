package com.example.fahim.ebookdl;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.ParseException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {

    GridView bookGridView;

    String searchStr;

    Integer pageNumber = 1;
    Integer totalFound;

    Integer limit;

    public ArrayList<BookDataModel> booklist;
    public BookAdapter bookAdapter;

    // Actionbar Variables
    private ListView mDrawerList;
    private DrawerLayout mDrawerLayout;
    private ArrayAdapter<String> mAdapter;
    private ActionBarDrawerToggle mDrawerToggle;
    private String mActivityTitle;

    // Pagination Buttons

    Button nextButton;
    Button previousButton;

    TextView total;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.books_grid);

        mDrawerList = (ListView)findViewById(R.id.navList);
        mDrawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
        mActivityTitle = getTitle().toString();

        addDrawerItems();
        setupDrawer();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);


        if(isOnline()){

            searchStr = "2014";
            getResutlFromServer(searchStr, null);

//            nextButton = (Button) findViewById(R.id.next);
//            nextButton.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//
//                    getResutlFromServer(searchStr, "next");
//
//                }
//            });
//
//            previousButton = (Button) findViewById(R.id.previous);
//            previousButton.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//
//                    getResutlFromServer(searchStr, "back");
//                }
//            });
        }

    }

    // Serch for Books form the server
    public void getResutlFromServer(String string, String status){

        setTitle(searchStr+" books");

        Log.i("MyActivity", " limit is : " + limit);

        if(status == "next"){

            if(pageNumber < limit){
                pageNumber++;
                previousButton.setEnabled(true);
            }else{
                previousButton.setEnabled(true);
            }
        }
        else if(status == "back"){

            if(pageNumber > 1){
                pageNumber--;
                nextButton.setEnabled(true);
            }else{
                previousButton.setEnabled(false);
            }
        }

        booklist = new ArrayList<BookDataModel>();
        bookGridView = (GridView) findViewById(R.id.gridView);

        // Click event for each grid view content
        bookGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Intent obj = new Intent(getApplicationContext(), BookDetailsView.class);
                obj.putExtra("ID", booklist.get(i).getBookID());
                obj.putExtra("ImageURL", booklist.get(i).getImage());
                startActivity(obj);
                overridePendingTransition(R.anim.right_in, R.anim.left_out);
            }
        });

        bookAdapter = new BookAdapter(getApplicationContext(), R.layout.grid_view_content, booklist);
        bookGridView.setAdapter(bookAdapter);
        Log.i("MyActivity", "Page Number is  " + pageNumber + " searcchin for : " + searchStr);
        new JSONAsyncTask().execute("http://it-ebooks-api.info/v1/search/" + string + "/page/" + pageNumber);

    }

    // Funciton to check if we are connected to online or not
    public boolean isOnline() {
        ConnectivityManager conMgr = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = conMgr.getActiveNetworkInfo();
        if(netInfo == null || !netInfo.isConnected() || !netInfo.isAvailable())
        {
            return false;
        }
        return true;
    }

    // Navigation Drawer Functions
    private void addDrawerItems() {
        String[] osArray = { "Latest", "Android", "iOS", "Windows", "OS X", "Linux", "PHP", "MySql", "Java", "MongoDb" };
        mAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, osArray);
        mDrawerList.setAdapter(mAdapter);

        mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String value = parent.getItemAtPosition(position).toString();
                Toast.makeText(MainActivity.this, "Searching for " + value + " books", Toast.LENGTH_SHORT).show();
                searchStr = value.replaceAll(" ", "%20");
                getResutlFromServer(searchStr, null);
            }
        });
    }

    private void setupDrawer() {
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.drawer_open, R.string.drawer_close) {

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                getSupportActionBar().setTitle("Categories");
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                getSupportActionBar().setTitle(mActivityTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };

        mDrawerToggle.setDrawerIndicatorEnabled(true);
        mDrawerLayout.setDrawerListener(mDrawerToggle);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);

        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setSubmitButtonEnabled(true);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchStr = query;

                pageNumber = 1; // Set page number to 1 for new search
                //nextButton.setEnabled(true);
                //previousButton.setEnabled(false);

                getResutlFromServer(searchStr, null);
                Log.i("MyActivity", "searching for : " + query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });


        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        // Activate the navigation drawer toggle
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    // AsyncTask Class for JSON handeling

    class JSONAsyncTask extends AsyncTask<String, Void, Boolean> {

        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(MainActivity.this);
            dialog.setMessage("Loading ...");
            dialog.show();
            dialog.setCancelable(false);
        }

        @Override
        protected Boolean doInBackground(String... urls) {
            try {

                //------------------>>
                HttpGet httppost = new HttpGet(urls[0]);
                HttpClient httpclient = new DefaultHttpClient();
                HttpResponse response = httpclient.execute(httppost);

                // StatusLine stat = response.getStatusLine();
                int status = response.getStatusLine().getStatusCode();

                if (status == 200) {
                    HttpEntity entity = response.getEntity();
                    String data = EntityUtils.toString(entity);

                    // Make a new JSON object with the response data
                    JSONObject jsono = new JSONObject(data);

                    totalFound = Integer.parseInt(jsono.getString("Total"));  // Get total result
                    limit = totalFound / 10; // set the limit for pagination

                    JSONArray jarray = jsono.getJSONArray("Books");

                    for (int i = 0; i < jarray.length(); i++) {
                        JSONObject object = jarray.getJSONObject(i);

                        BookDataModel bookData = new BookDataModel();

                        bookData.setBookID(object.getString("ID"));
                        bookData.setBookTitle(object.getString("Title"));
                        bookData.setImage(object.getString("Image"));

                        booklist.add(bookData);
                    }
                    return true;
                }

                //------------------>>

            } catch (ParseException e1) {
                e1.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return false;
        }

        protected void onPostExecute(Boolean result) {
            dialog.cancel();
            bookAdapter.notifyDataSetChanged();
            if(result == false)
                Toast.makeText(getApplicationContext(), "Unable to fetch data from server", Toast.LENGTH_LONG).show();

            //total = (TextView) findViewById(R.id.total);
            //total.setText(String.valueOf(totalFound + " books found"));

        }
    }
}

