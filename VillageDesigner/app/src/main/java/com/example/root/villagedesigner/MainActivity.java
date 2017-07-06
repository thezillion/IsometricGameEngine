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
    Button clearDBBtn;

    DatabaseHelper myDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.myDb = new DatabaseHelper(this);

        house_1 = (ImageView) findViewById(R.id.house_1);

        house_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, VillageActivity.class);
                intent.putExtra("drawable_id", Integer.toString(R.drawable.house_1));
                startActivity(intent);
            }
        });

        house_2 = (ImageView) findViewById(R.id.house_2);

        house_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, VillageActivity.class);
                intent.putExtra("drawable_id", Integer.toString(R.drawable.house_2));
                startActivity(intent);
            }
        });

        house_3 = (ImageView) findViewById(R.id.house_3);

        house_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, VillageActivity.class);
                intent.putExtra("drawable_id", Integer.toString(R.drawable.house_3));
                startActivity(intent);
            }
        });

        house_4 = (ImageView) findViewById(R.id.house_4);

        house_4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, VillageActivity.class);
                intent.putExtra("drawable_id", Integer.toString(R.drawable.house_4));
                startActivity(intent);
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

    void clearDatabase() {
        this.myDb.deleteAllData();
    }
}
