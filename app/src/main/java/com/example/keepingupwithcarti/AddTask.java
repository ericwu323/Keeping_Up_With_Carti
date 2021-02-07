package com.example.keepingupwithcarti;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;


import com.example.keepingupwithcarti.Model.ToDoModel;
import com.example.keepingupwithcarti.Util.Database;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.sql.Time;
import java.util.Calendar;
import java.util.Objects;
import java.util.Random;


import static java.security.AccessController.getContext;

public class AddTask extends BottomSheetDialogFragment {
    public static final String TAG = "ActionBottomDialog";
    private EditText newTaskText;
    private Button newTaskSaveButton, newTaskAddTimeButton;
    private Context context;
    private Database db;
    TimePickerDialog timePicker;

    public static AddTask newInstance(){
        return new AddTask();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NORMAL, R.style.DialogStyle);
        context = getContext();

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.new_task, container, false);
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        return view;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        newTaskText = Objects.requireNonNull(getView()).findViewById(R.id.textForNewTask);
        newTaskSaveButton = getView().findViewById(R.id.addTaskButton);
        newTaskAddTimeButton = getView().findViewById(R.id.addTimeButton);

        boolean isUpdate = false;

        final Bundle bundle = getArguments();
        if(bundle != null){
            isUpdate = true;
            String task = bundle.getString("task");
            newTaskText.setText(task);
            assert task != null;
            if(task.length() > 0)
                newTaskSaveButton.setTextColor(ContextCompat.getColor(Objects.requireNonNull(getContext()), R.color.colorPrimaryDark));
        }

        db = new Database(getActivity());
        db.openDatabase();

        newTaskText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.toString().equals("")){
                    newTaskSaveButton.setEnabled(false);
                    newTaskSaveButton.setTextColor(Color.GRAY);
                }
                else{
                    newTaskSaveButton.setEnabled(true);
                    newTaskSaveButton.setTextColor(ContextCompat.getColor(Objects.requireNonNull(getContext()), R.color.colorPrimaryDark));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        newTaskAddTimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar calendar = Calendar.getInstance();
                int hour = calendar.get(Calendar.HOUR_OF_DAY), min = calendar.get(Calendar.MINUTE);

                timePicker = new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int i, int i1) {

                    }
                }, hour, min, true);
                timePicker.show();
                setAlarm(calendar.getTimeInMillis());
            }
        });

        final boolean finalIsUpdate = isUpdate;
        final MediaPlayer mp = MediaPlayer.create(context, R.raw.slime_sound);
        newTaskSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mp.start();
                String text = newTaskText.getText().toString().trim();
                text = inputToCarti(text);
                if(finalIsUpdate){
                    db.updateTask(bundle.getInt("id"), text);
                }
                else {
                    ToDoModel task = new ToDoModel();
                    task.setTask(text);
                    task.setStatus(0);
                    db.insertTask(task);
                }
                dismiss();
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public static String inputToCarti(String input){
        String output = "";

        input = input.toLowerCase();
        Random random = new Random();
        int num = 0;

        for(int i = 0; i < input.length(); i++){
            if(Character.isAlphabetic(input.charAt(i))){
                num = random.nextInt(3);

                if(input.charAt(i) == 'e')
                    output += "3";
                else if(num == 2 && i + 1 != input.length()){
                    output += input.substring(i, i+1).toUpperCase();
                }
                else
                    output += input.charAt(i);

            }
            if(Character.isSpaceChar(input.charAt(i)) || (i+1 == input.length())){
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
    public void onDismiss(DialogInterface dialog){
        Activity activity = getActivity();

        if(activity instanceof DialogCloseListener){
            ((DialogCloseListener) activity).handleDialogClose(dialog);
        }

}

    private void setAlarm(long time) {
        //getting the alarm manager
        AlarmManager am = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);

        //creating a new intent specifying the broadcast receiver
        Intent i = new Intent(getContext(), CartiAlarm.class);

        //creating a pending intent using the intent
        PendingIntent pi = PendingIntent.getBroadcast(getContext(), 0, i, 0);

        //setting the repeating alarm that will be fired every day
        am.setRepeating(AlarmManager.RTC, time, AlarmManager.INTERVAL_DAY, pi);
        // Toast.makeText(this, "Alarm is set", Toast.LENGTH_SHORT).show();
    }

}
