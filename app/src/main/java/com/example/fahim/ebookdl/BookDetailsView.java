package com.example.fahim.ebookdl;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ParseException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;


public class BookDetailsView extends Activity {

    String bookID;
    String bookImgUrl;

    ImageView  bookImg;
    TextView booktitle;
    TextView bookauthor;
    TextView bookYear;
    TextView bookPub;
    TextView bookDes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_details_view);

        // init

        bookImg = (ImageView) findViewById(R.id.bookImage);
        booktitle = (TextView) findViewById(R.id.bookTitle);
        bookauthor = (TextView) findViewById(R.id.bookAuthor);
        bookYear = (TextView) findViewById(R.id.BookYear);
        bookPub = (TextView) findViewById(R.id.BookPublishar);
        bookDes = (TextView) findViewById(R.id.BookDisription);


        //Get the bundle

        Bundle bundle = getIntent().getExtras();
        this.bookID = bundle.getString("ID");
        this.bookImgUrl = bundle.getString("ImageURL");



        new JSONAsyncTask().execute("http://it-ebooks-api.info/v1/book/" + this.bookID);
        new DownloadImageTask((ImageView) findViewById(R.id.bookImage))
                .execute(bookImgUrl);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_book_details_view, menu);
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

        return super.onOptionsItemSelected(item);
    }

    class JSONAsyncTask extends AsyncTask<String, Void, JSONObject> {

        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(BookDetailsView.this);
            dialog.setMessage("Loading...");
            dialog.show();
            dialog.setCancelable(false);
        }

        @Override
        protected JSONObject doInBackground(String... urls) {
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


                    JSONObject jsono = new JSONObject(data);


                    return jsono;
                }

                //------------------>>

            } catch (ParseException e1) {
                e1.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(JSONObject result) {
            dialog.cancel();
            try {
                booktitle.setText(result.getString("Title"));
                bookDes.setText(result.getString("Description"));
                bookauthor.setText(result.getString("Author"));
                bookYear.setText(result.getString("Year"));
                bookPub.setText(result.getString("Publisher"));

            }catch (JSONException e){
                e.printStackTrace();
            }

        }
    }

    private class DownloadImageTask extends AsyncTask<String , Void , Bitmap>{

        ImageView bookImgView;

        public DownloadImageTask(ImageView bookImgView){
            this.bookImgView = bookImgView;
        }

        @Override
        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap bookImg = null;

            try{
                InputStream in = new java.net.URL(urldisplay).openStream();
                bookImg = BitmapFactory.decodeStream(in);

            }catch (Exception e){
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }

            return bookImg;
        }

        protected void onPostExecute(Bitmap result){
            bookImgView.setImageBitmap(result);
        }
    }


}
