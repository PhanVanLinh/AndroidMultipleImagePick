package com.luminous.pick;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ViewSwitcher;
import com.squareup.picasso.Picasso;
import com.toong.multipleimagepick.GalleryActivity;
import com.toong.multipleimagepick.GalleryImage;
import java.util.ArrayList;

public class MainActivity extends Activity {

    GridView gridGallery;
    Handler handler;

    ImageView imgSinglePick;
    Button btnGalleryPick;
    Button btnGalleryPickMul;

    String action;
    ViewSwitcher viewSwitcher;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.main);

        init();
    }

    private void init() {

        handler = new Handler();
        gridGallery = (GridView) findViewById(R.id.gridGallery);
        gridGallery.setFastScrollEnabled(true);

        viewSwitcher = (ViewSwitcher) findViewById(R.id.viewSwitcher);
        viewSwitcher.setDisplayedChild(1);

        imgSinglePick = (ImageView) findViewById(R.id.imgSinglePick);

        btnGalleryPick = (Button) findViewById(R.id.btnGalleryPick);
        btnGalleryPick.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Intent i = new Intent(GalleryActivity.Action.ACTION_SINGLE_PICK_IMAGE);
                startActivityForResult(i, 100);
            }
        });

        btnGalleryPickMul = (Button) findViewById(R.id.btnGalleryPickMul);
        btnGalleryPickMul.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent i = new Intent(GalleryActivity.Action.ACTION_MULTIPLE_PICK_IMAGE);
                startActivityForResult(i, 200);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 100 && resultCode == Activity.RESULT_OK) {

            viewSwitcher.setDisplayedChild(1);
            String single_path = data.getStringExtra("single_path");
            Picasso.with(this).load("file://" + single_path).into(imgSinglePick);
        } else if (requestCode == 200 && resultCode == Activity.RESULT_OK) {
            String[] all_path = data.getStringArrayExtra("all_path");

            ArrayList<GalleryImage> dataT = new ArrayList<GalleryImage>();

            for (String string : all_path) {
                GalleryImage item = new GalleryImage();
                item.sdcardPath = string;

                dataT.add(item);
            }

            viewSwitcher.setDisplayedChild(0);
        }
    }
}
