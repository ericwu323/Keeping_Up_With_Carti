package com.example.keepingupwithcarti;

import android.os.Build;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;

import android.view.Menu;
import android.view.MenuItem;

import java.util.Random;

import static com.example.keepingupwithcarti.R.drawable.ic_baseline_add_24;

public class MainActivity extends AppCompatActivity {

    private RecyclerView taskRecyclerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();

        taskRecyclerView = findViewById(R.id.tasksRecycler);
        taskRecyclerView.setLayoutManager(new LinearLayoutManager(this));


        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private String inputToCarti(String input){
        String output = "";

        input = input.toLowerCase();
        Random random = new Random();
        int num = 0;

        for(int i = 0; i < input.length(); i++){
            if(Character.isAlphabetic(input.charAt(i))){
                num = random.nextInt(3);

                if(input.charAt(i) == 'e' && num <= 1)
                    output += "3";
                else if(num == 2 && i != 0){
                    output += input.substring(i-1, i).toUpperCase();
                }

            }
            else if(Character.isSpaceChar(input.charAt(i))){
                num = random.nextInt(10);
                if(num == 0)
                    output += "... ";
                else if(num == 1)
                    output += " !";
                else if(num == 2)
                    output += "*+:) ";
                else if(num == 3)
                    output += "++ ";
                else if(num == 4)
                    output += "* + ";
                else if(num == 5)
                    output += "**!! ";
                else if(num == 6)
                    output += "+ :) + ";
                else if(num == 7)
                    output +=  "** ";
                else if(num == 8)
                    output += " !! ";
                else if(num == 9)
                    output += " slatt ! :) ** ";
                else
                    output += " ";
            }
        }


        return output;
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
}