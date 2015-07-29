package com.example.fahim.ebookdl;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by fahim on 6/8/15.
 */
public class BookAdapter extends ArrayAdapter<BookDataModel> {

    ArrayList<BookDataModel> booklist;
    LayoutInflater vi;
    int Resource;
    public ViewHolder holder;

    private Context context;

    public static class ViewHolder{
        public ImageView imageView;
        public TextView textView;
    }

    public BookAdapter(Context context, int resource, ArrayList<BookDataModel> booklist) {
        super(context, resource, booklist);

        this.context = context;

        vi = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        Resource = resource;
        this.booklist = booklist;
    }

    // General Image downloading with Asynctask -- SLow

//    @Override
//    public View getView(int position, View convertView, ViewGroup parent){
//        View v = convertView;
//        ImageView view = (ImageView) convertView;
//        if (v == null) {
//            holder = new ViewHolder();
//            v = vi.inflate(Resource, null);
//            holder.imageView = (ImageView) v.findViewById(R.id.bookImg);
//            view = new ImageView(context);
//            holder.textView = (TextView) v.findViewById(R.id.bookTxt);
//            v.setTag(holder);
//
//        }else{
//            holder = (ViewHolder) v.getTag();
//        }
//
//        Picasso.with(context).load(url).into(view);
//
//        new DownloadImageTask(holder.imageView).execute(booklist.get(position).getImage());
//        holder.textView.setText(booklist.get(position).getBookTitle());
//
//        return v;
//    }

    // With Picasso much better performance and smooth

    @Override
    public View getView(int position, View convertView, ViewGroup parent){

        View v = convertView;

        if (v == null) {
            holder = new ViewHolder();
            v = vi.inflate(Resource, null);
            holder.imageView = (ImageView) v.findViewById(R.id.bookImg);
            holder.textView = (TextView) v.findViewById(R.id.bookTxt);
            v.setTag(holder);
        }else{
            holder = (ViewHolder) v.getTag();
        }

        Picasso.with(context).load(booklist.get(position).getImage()).placeholder(R.drawable.loading).resize(150, 220).into(holder.imageView);

        holder.textView.setText(booklist.get(position).getBookTitle());

        return v;
    }



//    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
//        ImageView bmImage;
//
//        public DownloadImageTask(ImageView bmImage) {
//            this.bmImage = bmImage;
//        }
//
//        protected Bitmap doInBackground(String... urls) {
//            String urldisplay = urls[0];
//            Bitmap mIcon11 = null;
//            try {
//                InputStream in = new java.net.URL(urldisplay).openStream();
//                mIcon11 = BitmapFactory.decodeStream(in);
//            } catch (Exception e) {
//                Log.e("Error", e.getMessage());
//                e.printStackTrace();
//            }
//            return mIcon11;
//        }
//
//        protected void onPostExecute(Bitmap result) {
//            bmImage.setImageBitmap(result);
//        }
//
//    }
}
