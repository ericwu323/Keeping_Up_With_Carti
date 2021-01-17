package com.example.keepingupwithcarti;

import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;

import com.example.keepingupwithcarti.Adapter.ListAdapter;
import com.example.keepingupwithcarti.Model.ToDoModel;
import com.example.keepingupwithcarti.Util.Database;
import com.google.android.material.floatingactionbutton.FloatingActionButton;


import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.ItemTouchHelper;

import android.view.View;

import android.view.Menu;
import android.view.MenuItem;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Random;


public class MainActivity extends AppCompatActivity implements DialogCloseListener{

    private RecyclerView taskRecyclerView;
    private Database db;
    private List<ToDoModel> taskList;
    private FloatingActionButton fab;
    private ListAdapter tasksAdapter;
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Objects.requireNonNull(getSupportActionBar()).hide();
        db = new Database(this);
        db.openDatabase();
        taskRecyclerView = findViewById(R.id.tasksRecycler);
        taskRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        tasksAdapter = new ListAdapter(db,MainActivity.this);
        taskRecyclerView.setAdapter(tasksAdapter);

        ItemTouchHelper itemTouchHelper = new
                ItemTouchHelper(new RItemTouchHelper(tasksAdapter));
        itemTouchHelper.attachToRecyclerView(taskRecyclerView);


        fab = findViewById(R.id.fab);
        taskList = db.getAllTasks();
        Collections.reverse(taskList);
        tasksAdapter.setTasks(taskList);


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddTask.newInstance().show(getSupportFragmentManager(), AddTask.TAG);
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
    @Override
    public void handleDialogClose(DialogInterface dialog){
        taskList = db.getAllTasks();
        Collections.reverse(taskList);
        tasksAdapter.setTasks(taskList);
        tasksAdapter.notifyDataSetChanged();
    }
}