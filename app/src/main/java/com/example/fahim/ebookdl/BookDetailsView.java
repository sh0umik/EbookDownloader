package com.example.fahim.ebookdl;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.ParseException;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;


public class BookDetailsView extends AppCompatActivity {

    String bookID;
    String bookImgUrl;

    ImageView  bookImg;
    TextView booktitle;
    TextView bookauthor;
    TextView bookYear;
    TextView bookISBN;
    TextView bookDes;

    String navBookTitle;

    String downloadLink;

    Button downloadBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.details_view);
        setTitle("Book Details");

        // init

        bookImg = (ImageView) findViewById(R.id.bookImage);
        booktitle = (TextView) findViewById(R.id.bookTitle);
        bookauthor = (TextView) findViewById(R.id.bookAuthor);
        bookYear = (TextView) findViewById(R.id.bookYear);
        bookISBN = (TextView) findViewById(R.id.bookISBN);
        bookDes = (TextView) findViewById(R.id.bookDes);


        //Get the bundle

        Bundle bundle = getIntent().getExtras();
        this.bookID = bundle.getString("ID");
        this.bookImgUrl = bundle.getString("ImageURL");


        ImageView imageView = (ImageView) findViewById(R.id.bookImage);

        Picasso.with(this).load(bookImgUrl).resize(150, 220).into(imageView);

        new JSONAsyncTask().execute("http://it-ebooks-api.info/v1/book/" + this.bookID);

        //new DownloadImageTask((ImageView) findViewById(R.id.bookImage))
        //        .execute(bookImgUrl);

//        downloadBtn = (Button) findViewById(R.id.download);

//        downloadBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                goToUrl(downloadLink);
//            }
//        });

    }

    private void goToUrl (String url) {
        Uri uriUrl = Uri.parse(url);
        Intent launchBrowser = new Intent(Intent.ACTION_VIEW, uriUrl);
        startActivity(launchBrowser);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_book_details_view, menu);
        //MenuItem download = menu.findItem(R.id.buttonPanel);
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

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        overridePendingTransition(R.anim.right_out, R.anim.left_in);
        Log.i("some ", "back pressed");
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
                bookISBN.setText(result.getString("ISBN"));
                downloadLink = result.getString("Download");

                setTitle(result.getString("Title"));

            }catch (JSONException e){
                e.printStackTrace();
            }

        }
    }

//    private class DownloadImageTask extends AsyncTask<String , Void , Bitmap>{
//
//        ImageView bookImgView;
//
//        public DownloadImageTask(ImageView bookImgView){
//            this.bookImgView = bookImgView;
//        }
//
//        @Override
//        protected Bitmap doInBackground(String... urls) {
//            String urldisplay = urls[0];
//            Bitmap bookImg = null;
//
//            try{
//                InputStream in = new java.net.URL(urldisplay).openStream();
//                bookImg = BitmapFactory.decodeStream(in);
//
//            }catch (Exception e){
//                Log.e("Error", e.getMessage());
//                e.printStackTrace();
//            }
//
//            return bookImg;
//        }
//
//        protected void onPostExecute(Bitmap result){
//            bookImgView.setImageBitmap(result);
//        }
//    }


}
