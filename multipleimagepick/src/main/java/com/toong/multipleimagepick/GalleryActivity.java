package com.toong.multipleimagepick;

import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.StringDef;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import com.toong.multipleimagepick.adapter.MyRecyclerViewAdapter;
import com.toong.multipleimagepick.helper.GridSpacingItemDecoration;
import java.util.ArrayList;
import java.util.Collections;

public class GalleryActivity extends AppCompatActivity {
    @StringDef({ Action.ACTION_SINGLE_PICK_IMAGE, Action.ACTION_MULTIPLE_PICK_IMAGE })
    public @interface Action {
        String ACTION_SINGLE_PICK_IMAGE = "multipleimagepick.ACTION_SINGLE_PICK_IMAGE";
        String ACTION_MULTIPLE_PICK_IMAGE = "multipleimagepick.ACTION_MULTIPLE_PICK_IMAGE";
    }

    private RecyclerView gridGallery;
    private Handler handler;
    private MyRecyclerViewAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);

        getSupportActionBar().setTitle(getString(R.string.select_image));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        init();
        new GetImageAsync().execute();
    }

    private void init() {
        handler = new Handler();
        gridGallery = (RecyclerView) findViewById(R.id.recycler_gallery);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 3);
        gridGallery.addItemDecoration(new GridSpacingItemDecoration(3, 5, true));
        gridGallery.setLayoutManager(gridLayoutManager);
        adapter = new MyRecyclerViewAdapter(getApplicationContext(), getIntent().getAction());

        gridGallery.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.gallery_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and return to preview activity (if there is any)
        }
        if (item.getItemId() == R.id.action_done) {
            returnAllSelectedImageAndFinish();
        }
        return super.onOptionsItemSelected(item);
    }

    private void returnAllSelectedImageAndFinish() {
        ArrayList<GalleryImage> selected = adapter.getSelected();
        String[] allPath = new String[selected.size()];
        for (int i = 0; i < allPath.length; i++) {
            allPath[i] = selected.get(i).sdcardPath;
        }
        Intent data = new Intent().putExtra("all_path", allPath);
        setResult(RESULT_OK, data);
        finish();
    }

    private class GetImageAsync extends AsyncTask<Void, Void, ArrayList<GalleryImage>> {
        @Override
        protected ArrayList<GalleryImage> doInBackground(Void... params) {
            return getGalleryPhotos();
        }

        @Override
        protected void onPostExecute(ArrayList<GalleryImage> galleryImages) {
            super.onPostExecute(galleryImages);
            adapter.addAll(galleryImages);
        }
    }

    private ArrayList<GalleryImage> getGalleryPhotos() {
        ArrayList<GalleryImage> galleryList = new ArrayList<GalleryImage>();

        try {
            final String[] columns = {
                    MediaStore.Images.Media.DATA, MediaStore.Images.Media._ID
            };
            final String orderBy = MediaStore.Images.Media._ID;

            Cursor imageCursor =
                    managedQuery(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, columns, null, null,
                            orderBy);

            if (imageCursor != null && imageCursor.getCount() > 0) {

                while (imageCursor.moveToNext()) {
                    GalleryImage item = new GalleryImage();

                    int dataColumnIndex = imageCursor.getColumnIndex(MediaStore.Images.Media.DATA);

                    item.sdcardPath = imageCursor.getString(dataColumnIndex);

                    galleryList.add(item);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // show newest photo at beginning of the list
        Collections.reverse(galleryList);
        return galleryList;
    }
}
