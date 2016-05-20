package com.example.jake_.cp3307_a1;

import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {

    private PictureWorker worker;
    private int[] drawablesPipes = {R.drawable.pipe1, R.drawable.pipe2, R.drawable.pipe3, R.drawable.pipe4, R.drawable.pipe5, R.drawable.pipe6};
    private int[] drawablesShapes = {R.drawable.shape1, R.drawable.shape2, R.drawable.shape3, R.drawable.shape4, R.drawable.shape5};
    private int[] drawablesPatterns = {R.drawable.patterns1, R.drawable.patterns2, R.drawable.patterns3, R.drawable.patterns4,
            R.drawable.patterns5, R.drawable.patterns6};

    private static DatabaseAccess database;
    private String theme;

    public MainActivity() {
        worker = new PictureWorker(this);
        worker.start();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        //pass this entire array into controller
        ImageView[] imgViews = {(ImageView) findViewById(R.id.preview), (ImageView) findViewById(R.id.topLeft),
                (ImageView) findViewById(R.id.topRight), (ImageView) findViewById(R.id.bottomLeft), (ImageView) findViewById(R.id.bottomRight)};

        database = new DatabaseAccess(this);


        Handler handler = new Handler();
        ImageViewController.setViews(imgViews);

        Bundle settingsData = getIntent().getExtras();
        if (settingsData != null) {
            theme = settingsData.getString("theme");
        }

        int length;
        int[] drawables;
        switch (theme) {
            case "pipes":
                length = drawablesPipes.length;
                drawables = drawablesPipes;
                break;
            case "shapes":
                length = drawablesShapes.length;
                drawables = drawablesShapes;
                break;
            default:
                length = drawablesPatterns.length;
                drawables = drawablesPatterns;
                break;
        }

        worker.totalImages = length;
        for (int drawable : drawables) {
            worker.loadResource(drawable, handler);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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

    public void Randomize(View view) {
        ImageViewController.reset();
    }

    public void nextImage(View view) {

        /**
         Cursor cursor = database.getAllCursor();
         int count = cursor.getCount();
         System.out.println(count);
         while (cursor.moveToNext()) {
         int id = cursor.getInt(0);
         int comp = cursor.getInt(1);
         int touch = cursor.getInt(2);
         System.out.println(String.format("%s %s %s",id , comp, touch));
         }
         cursor.close();
         */


        switch (view.getId()) {
            case R.id.topLeft:
                ImageViewController.nextImage(1);
                break;
            case R.id.topRight:
                ImageViewController.nextImage(2);
                break;
            case R.id.bottomLeft:
                ImageViewController.nextImage(3);
                break;
            case R.id.bottomRight:
                ImageViewController.nextImage(4);
                break;
        }

        if (ImageViewController.isComplete()) {
            System.out.println("complete");
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        worker.quit();

        this.finish();
        Runtime.getRuntime().gc();
    }

}

