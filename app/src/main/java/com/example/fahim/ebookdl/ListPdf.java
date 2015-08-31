package com.example.fahim.ebookdl;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.io.File;
import java.util.ArrayList;

public class ListPdf extends Activity {

    File path;
    ListView list;
    static ArrayList<String> pdf_paths=new ArrayList<String>();
    static ArrayList<String> pdf_names=new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.showfiles);

        list=(ListView)findViewById(R.id.listView1);
        pdf_paths.clear();
        pdf_names.clear();

        //Access External storage
        path = new File(Environment.getExternalStorageDirectory() + "/it-ebooks");
        searchFolderRecursive1(path);

        Log.i("path is : ", path.toString());

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, android.R.id.text1, pdf_names);
        Log.i("files", "" + pdf_names);

        list.setAdapter(adapter);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {

                String path = pdf_paths.get(arg2);
                File file = new File(path);

                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setDataAndType(Uri.fromFile(file), "application/pdf");
                intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);

                startActivity(intent);
            }
        });
    }

    private static void searchFolderRecursive1(File folder)
    {
        if (folder != null)
        {
            if (folder.listFiles() != null)
            {
                for (File file : folder.listFiles())
                {
                    if (file.isFile())
                    {
                        //.pdf files
                        if(file.getName().contains(".pdf"))
                        {
                            Log.i("files found :", "path__="+file.getName());
                            file.getPath();
                            pdf_names.add(file.getName());
                            pdf_paths.add(file.getPath());
                            Log.e("pdf_paths", ""+pdf_names);
                        }
                    }
                    else
                    {
                        searchFolderRecursive1(file);
                    }
                }
            }
        }
    }
}
