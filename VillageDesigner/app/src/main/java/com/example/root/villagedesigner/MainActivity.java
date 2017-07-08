package com.example.root.villagedesigner;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;

import com.example.root.villagedesigner.db.DatabaseHelper;

public class MainActivity extends AppCompatActivity {

    ImageView house_1;
    ImageView house_2;
    ImageView house_3;
    ImageView house_4;
    ImageView road_1;
    ImageView road_2;
    ImageView road_3;
    ImageView road_4;
    ImageView road_5;
    ImageView road_6;
    ImageView road_7;
    ImageView road_8;
    ImageView road_9;
    ImageView road_10;
    ImageView trees_1;
    ImageView water;
    Button clearDBBtn;

    DatabaseHelper myDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.myDb = new DatabaseHelper(this);

        // Houses

        house_1 = (ImageView) findViewById(R.id.house_1);

        house_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startVillageActivity(R.drawable.house_1, "house");
            }
        });

        house_2 = (ImageView) findViewById(R.id.house_2);

        house_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startVillageActivity(R.drawable.house_2, "house");
            }
        });

        house_3 = (ImageView) findViewById(R.id.house_3);

        house_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startVillageActivity(R.drawable.house_3, "house");
            }
        });

        house_4 = (ImageView) findViewById(R.id.house_4);

        house_4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startVillageActivity(R.drawable.house_4, "house");
            }
        });

        // Roads

        road_1 = (ImageView) findViewById(R.id.road_1);

        road_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startVillageActivity(R.drawable.road_1, "road");
            }
        });

        road_2 = (ImageView) findViewById(R.id.road_2);

        road_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startVillageActivity(R.drawable.road_2, "road");
            }
        });

        road_3 = (ImageView) findViewById(R.id.road_3);

        road_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startVillageActivity(R.drawable.road_3, "road");
            }
        });

        road_4 = (ImageView) findViewById(R.id.road_4);

        road_4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startVillageActivity(R.drawable.road_4, "road");
            }
        });

        road_5 = (ImageView) findViewById(R.id.road_5);

        road_5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startVillageActivity(R.drawable.road_5, "road");
            }
        });

        road_6 = (ImageView) findViewById(R.id.road_6);

        road_6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startVillageActivity(R.drawable.road_6, "road");
            }
        });

        road_7 = (ImageView) findViewById(R.id.road_7);

        road_7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startVillageActivity(R.drawable.road_7, "road");
            }
        });

        road_8 = (ImageView) findViewById(R.id.road_8);

        road_8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startVillageActivity(R.drawable.road_8, "road");
            }
        });

        road_9 = (ImageView) findViewById(R.id.road_9);

        road_9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startVillageActivity(R.drawable.road_9, "road");
            }
        });

        road_10 = (ImageView) findViewById(R.id.road_10);

        road_10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startVillageActivity(R.drawable.road_10, "road");
            }
        });

        // Trees

        trees_1 = (ImageView) findViewById(R.id.trees_1);

        trees_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startVillageActivity(R.drawable.trees_1, "trees");
            }
        });

        // Water

        water = (ImageView) findViewById(R.id.water);

        water.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startVillageActivity(R.drawable.water, "water");
            }
        });


        clearDBBtn = (Button) findViewById(R.id.clearDBBtn);

        clearDBBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearDatabase();
            }
        });
    }

    void startVillageActivity(int drawable_id, String type) {
        Intent intent = new Intent(MainActivity.this, VillageActivity.class);
        intent.putExtra("drawable_id", Integer.toString(drawable_id));
        intent.putExtra("type", type);
        startActivity(intent);
    }

    void clearDatabase() {
        this.myDb.deleteAllData();
    }
}
