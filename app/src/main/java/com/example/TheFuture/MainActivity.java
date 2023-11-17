package com.example.TheFuture;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.TheFuture.adapter.AdapterData;
import com.example.TheFuture.model.ModelData;
import com.google.android.material.checkbox.MaterialCheckBox;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.nex3z.togglebuttongroup.SingleSelectToggleGroup;
import com.nex3z.togglebuttongroup.button.CircularToggle;


import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    RadioGroup RadioGroupDayOfWeak;
    RadioButton Saturday,Sunday,Monday,Tuesday,Wednesday,Thursday,Friday;
    int Hour , mints ;

    //    RadioButton radioButtonOtherStudy, radioButtonStudy3, radioButtonStudy2, radioButtonStudy1, radioButtonRoom, radioButtonRoom2, radioButtonRoom3, radioButtonRoom4, radioButton1, radioButton2, radioButton3,
//            radioButton4, radioButton5, radioButton6, radioButton7, radioButton8, radioButton9, radioButton10,
//            radioButton11, radioButton12, radioButton13, radioButton14, radioButton15, radioButton16, radioButton17, radioButton18;
    public RecyclerView rc, rc2, rc3, rc4;
    List<ModelData> dataList, dataList2, dataList3, dataList4;
    AdapterData adapterData, adapterData2, adapterData3, adapterData4;
    FirebaseFirestore db, firestore;
    ModelData model, model2, model3, model4;
    ImageView addSubject, language , addNots ;
    MaterialCheckBox ramadan;
    public String Id = "", Room = "", Day = "Saturday", StudyYear = "", StudyRoom = "", StudySubject = "", checkValue = "";
    SwipeRefreshLayout swipeRefreshLayout;
    private boolean first = true;
    RelativeLayout layout;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        RadioGroupDayOfWeak = findViewById(R.id.RadioGroupDayOfWeak);

        ramadan = findViewById(R.id.ramadanMonth);
        rc = findViewById(R.id.rc);
        rc2 = findViewById(R.id.rc2);
        rc3 = findViewById(R.id.rc3);
        rc4 = findViewById(R.id.rc4);
        addSubject = findViewById(R.id.addSubject);
        language = findViewById(R.id.language);
        addNots = findViewById(R.id.addNots);
        swipeRefreshLayout = findViewById(R.id.SwipeRefreshLayout);
        dataList = new ArrayList<>();
        dataList2 = new ArrayList<>();
        dataList3 = new ArrayList<>();
        dataList4 = new ArrayList<>();
        model = new ModelData();
        model2 = new ModelData();
        model3 = new ModelData();
        model4 = new ModelData();
        adapterData = new AdapterData(this, dataList);
        adapterData2 = new AdapterData(this, dataList2);
        adapterData3 = new AdapterData(this, dataList3);
        adapterData4 = new AdapterData(this, dataList4);
        firestore = FirebaseFirestore.getInstance();

        clearData();
        getCurrentDay();
        getDataInRoom();
        goToAddNotes();

        swipeRefreshLayout.setOnRefreshListener(() -> {
            first = false;
            clearData();
            getDataInRoom();
            swipeRefreshLayout.setRefreshing(false);
        });
        addSubject.setOnClickListener(view -> MainActivity.this.ImageViewAdd());
        ramadan.setOnCheckedChangeListener((compoundButton, b) -> {
            if (b == true) {
                clearData();
                Intent goToRamadanMonth = new Intent(MainActivity.this, RamadanMonthActivity.class);
                startActivity(goToRamadanMonth);
                ramadan.setChecked(false);
                finish();
            }

        });
        language.setOnClickListener(view -> showChangeLanguageDialog());
    }

    private void goToAddNotes(){
        addNots.setOnClickListener(view -> {
            Intent goToAddNots = new Intent(MainActivity.this, AddNotesActivity.class);
            startActivity(goToAddNots);
        });

    }
    private void getCurrentDay(){
        Saturday = findViewById(R.id.radioButtonDaySat);
        Sunday = findViewById(R.id.radioButtonDaySun);
        Monday = findViewById(R.id.radioButtonDayMon);
        Tuesday = findViewById(R.id.radioButtonDayTues);
        Wednesday = findViewById(R.id.radioButtonDayWed);
        Thursday = findViewById(R.id.radioButtonDayThu);
        Friday = findViewById(R.id.radioButtonDayFri);

        SimpleDateFormat sdf = new SimpleDateFormat("EEEE");
        Date d = new Date();
        String dayOfTheWeek = sdf.format(d);
        if (dayOfTheWeek.equals("Saturday")){
            Saturday.setChecked(true);
            Day = "Saturday";
        }else if (dayOfTheWeek.equals("Sunday")){
            Sunday.setChecked(true);
            Day = "Sunday";
        }else if (dayOfTheWeek.equals("Monday")){
            Monday.setChecked(true);
            Day = "Monday";
        }else if (dayOfTheWeek.equals("Tuesday")){
            Tuesday.setChecked(true);
            Day = "Tuesday";
        }else if (dayOfTheWeek.equals("Wednesday")){
            Wednesday.setChecked(true);
            Day = "Wednesday";
        }else if (dayOfTheWeek.equals("Thursday")){
            Thursday.setChecked(true);
            Day = "Thursday";
        }else if (dayOfTheWeek.equals("Friday")){
            Friday.setChecked(true);
            Day = "Friday";
        }
        RadioGroupDayOfWeak.setOnCheckedChangeListener((radioGroup, i) -> {
            switch (i) {

                case R.id.radioButtonDaySat:
                    Day = "Saturday";
                    MainActivity.this.clearData();
                    MainActivity.this.getDataInRoom();
                    break;
                case R.id.radioButtonDaySun:
                    Day = "Sunday";
                    MainActivity.this.clearData();
                    MainActivity.this.getDataInRoom();
                    break;
                case R.id.radioButtonDayMon:
                    Day = "Monday";
                    MainActivity.this.clearData();
                    MainActivity.this.getDataInRoom();
                    break;
                case R.id.radioButtonDayTues:
                    Day = "Tuesday";
                    MainActivity.this.clearData();
                    MainActivity.this.getDataInRoom();
                    break;
                case R.id.radioButtonDayWed:
                    Day = "Wednesday";
                    MainActivity.this.clearData();
                    MainActivity.this.getDataInRoom();
                    break;
                case R.id.radioButtonDayThu:
                    Day = "Thursday";
                    MainActivity.this.clearData();
                    MainActivity.this.getDataInRoom();
                    break;
                case R.id.radioButtonDayFri:
                    Day = "Friday";
                    MainActivity.this.clearData();
                    MainActivity.this.getDataInRoom();
                    break;
            }

        });


    }

    private void showChangeLanguageDialog() {

        androidx.appcompat.app.AlertDialog builder = new androidx.appcompat.app.AlertDialog.Builder(this).create();
        View view = LayoutInflater.from(this).inflate(R.layout.language, null);
        builder.setView(view);
        ImageView cancel_alert = view.findViewById(R.id.cancel_alert);
        cancel_alert.setOnClickListener(v -> {
            builder.dismiss();
        });
        RadioButton radio_en = view.findViewById(R.id.radio_english);
        RadioButton radio_ar = view.findViewById(R.id.radio_arabic);
        view.findViewById(R.id.save_language).setOnClickListener(v -> {
            if (radio_en.isChecked()) {
                setLanguage("En");
            } else if (radio_ar.isChecked()) {
                setLanguage("Ar");
            }
        });
        SharedPreferences get =getSharedPreferences("language", Activity.MODE_PRIVATE);
        String language =get.getString("my_language","");
        if (language.equals("En")){
            radio_en.setChecked(true);
            radio_ar.setChecked(false);

        }else if (language.equals("Ar")){
            radio_ar.setChecked(true);
            radio_en.setChecked(false);

        }
        builder.show();
    }
    private void LoadChangeLanguage() {

            SharedPreferences get =getSharedPreferences("language", Activity.MODE_PRIVATE);
            String language =get.getString("my_language","");
            Locale l = new Locale(language);
            Locale.setDefault(l);
            Configuration configuration = new Configuration();
            configuration.locale = l;
            getResources().updateConfiguration(configuration, getResources().getDisplayMetrics());


    }

    private void setLanguage(String lang) {
        SharedPreferences.Editor editor = this.getSharedPreferences("language", MODE_PRIVATE).edit();
        editor.putString("my_language", lang);
        editor.apply();
        Locale l = new Locale(lang);
        Locale.setDefault(l);
        Configuration configuration = new Configuration();
        configuration.locale = l;
        getResources().updateConfiguration(configuration, getResources().getDisplayMetrics());
        Intent intent = new Intent(this, SplachActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    private void getDataInRoom() {
        db = FirebaseFirestore.getInstance();
        Log.d("TAre", "getDataInRoom1: " + Day);
        db.collection(Day)
                .orderBy("Time", Query.Direction.ASCENDING)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (!first) {
                        dataList.clear();
                        dataList2.clear();
                        dataList3.clear();
                        dataList4.clear();
                    }
                    if (!queryDocumentSnapshots.isEmpty()) {

                        List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                        for (DocumentSnapshot documentSnapshot : list) {

                            com.google.firebase.Timestamp Time = documentSnapshot.getTimestamp("Time");
                            String studyYear = documentSnapshot.getString("studyYear");
                            String subjectName = documentSnapshot.getString("subjectName");
                            String teacherName = documentSnapshot.getString("teacherName");
                            String Day = documentSnapshot.getString("Day");
                            String Room = documentSnapshot.getString("Room");
                            String dayOF = documentSnapshot.getString("dayOF");
                            String onlyDayOfWeek = documentSnapshot.getString("onlyDayOfWeek");
                            String ID = documentSnapshot.getId();
                            assert Room != null;

                            if (Room.equals("قاعة 1")) {
                                Room = "room 1";
                            } else if (Room.equals("قاعة 2")) {
                                Room = "room 2";

                            } else if (Room.equals("قاعة 3")) {
                                Room = "room 3";

                            } else if (Room.equals("قاعة 4")) {
                                Room = "room 4";

                            }

                            if (Room.equals("room 1")) {
                                ModelData modelDat = new ModelData(ID, teacherName, subjectName, studyYear, Time, Room, Day,dayOF,onlyDayOfWeek);
//                                ModelDay modelDay = new ModelDay(Day);

                                rc.setHasFixedSize(true);
                                rc.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
                                rc.setAdapter(adapterData);
                                Log.d("room :", ": " + modelDat.getID());

                                Id = ID;
                                dataList.add(modelDat);
                                adapterData.notifyDataSetChanged();
                            } else if (Room.equals("room 2")) {
                                ModelData modelDat2 = new ModelData(ID, teacherName, subjectName, studyYear, Time, Room, Day,dayOF,onlyDayOfWeek);
//                                ModelDay modelDay = new ModelDay(Day);

                                rc2.setHasFixedSize(true);
                                rc2.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
                                rc2.setAdapter(adapterData2);

                                Id = ID;
                                dataList2.add(modelDat2);
                                adapterData2.notifyDataSetChanged();
                            } else if (Room.equals("room 3")) {
                                rc3.setHasFixedSize(true);
                                rc3.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
                                rc3.setAdapter(adapterData3);

                                ModelData modelDat3 = new ModelData(ID, teacherName, subjectName, studyYear, Time, Room, Day,dayOF,onlyDayOfWeek);
//                                ModelDay modelDay = new ModelDay(Day);

                                Id = ID;
                                dataList3.add(modelDat3);
                                adapterData3.notifyDataSetChanged();
                            } else if (Room.equals("room 4")) {
                                rc4.setHasFixedSize(true);
                                rc4.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
                                rc4.setAdapter(adapterData4);

                                ModelData modelDat4 = new ModelData(ID, teacherName, subjectName, studyYear, Time, Room, Day,dayOF,onlyDayOfWeek);
//                                ModelDay modelDay = new ModelDay(Day);
                                Id = ID;
                                dataList4.add(modelDat4);
                                adapterData4.notifyDataSetChanged();
                            }

                        }
                        adapterData.notifyDataSetChanged();
                        adapterData2.notifyDataSetChanged();
                        adapterData3.notifyDataSetChanged();
                        adapterData4.notifyDataSetChanged();

                    } else {
                        // if the snapshot is empty we are displaying a toast message.

                    }
                });


    }

    private void ImageViewAdd() {
        RadioGroup RadioGroupStudyYear, RadioGroupStudyRoom, RadioGroupSubject;
        EditText AddTeacher;
        Button AddTime;
        CheckBox checkBox;



        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.addsubject);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

//        AlertDialog builder = new AlertDialog.Builder(this).create();
//        View view = LayoutInflater.from(this).inflate(R.layout.addsubject, null);
//        builder.setView(view);
//        builder.setCancelable(false);


        AddTeacher = dialog.findViewById(R.id.edtAddTeacherName);
        checkBox = dialog.findViewById(R.id.checkboxSubjectTime);
        AddTime = dialog.findViewById(R.id.AddTime);
        RadioGroupStudyYear = dialog.findViewById(R.id.RadioGroupStudyYear);
        RadioGroupStudyRoom = dialog.findViewById(R.id.RadioGroupStudyRoom);
        RadioGroupSubject = dialog.findViewById(R.id.RadioGroupSubject);


        if (checkBox.isChecked()){
            checkValue = "true";
        }else
        {
            checkValue = "false";
        }
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b){
                    checkValue = "true";
                    checkBox.setChecked(true);
                }else if (!b)
                {
                    checkValue = "false";
                    checkBox.setChecked(false);

                }
            }
        });
        RadioGroupStudyYear.setOnCheckedChangeListener((radioGroup, s) -> {
            switch (s) {
                case R.id.radioButtonOtherStudy:
                    StudyYear = "سنه دراسية اخري";

                    break;
                case R.id.radioButtonStudy3:
                    StudyYear = "3 ث";

                    break;
                case R.id.radioButtonStudy2:
                    StudyYear = "2 ث";

                    break;
                case R.id.radioButtonStudy1:
                    StudyYear = "1 ث";

                    break;

            }

        });
        RadioGroupStudyRoom.setOnCheckedChangeListener((radioGroup, x) -> {
            switch (x) {

                case R.id.radioButtonRoom4:
                    StudyRoom = "قاعة 4";
                    break;
                case R.id.radioButtonRoom3:
                    StudyRoom = "قاعة 3";
                    break;
                case R.id.radioButtonRoom2:
                    StudyRoom = "قاعة 2";
                    break;
                case R.id.radioButtonRoom:
                    StudyRoom = "قاعة 1";
                    break;

            }
        });
        RadioGroupSubject.setOnCheckedChangeListener((radioGroup, y) -> {
            switch (y) {

                case R.id.radioButton1:
                    StudySubject = "عربي";
                    break;
                case R.id.radioButton2:
                    StudySubject = "رياضه";
                    break;
                case R.id.radioButton3:
                    StudySubject = "فيزياء";
                    break;
                case R.id.radioButton4:
                    StudySubject = "كيمياء";
                    break;
                case R.id.radioButton5:
                    StudySubject = "فلسفه";
                    break;

                case R.id.radioButton7:
                    StudySubject = "علم نفس";
                    break;
                case R.id.radioButton8:
                    StudySubject = "احياء";
                    break;
                case R.id.radioButton9:
                    StudySubject = "جولوجيا";
                    break;
                case R.id.radioButton10:
                    StudySubject = "جعرافيا";
                    break;
                case R.id.radioButton11:
                    StudySubject = "تاريخ";
                    break;
                case R.id.radioButton12:
                    StudySubject = "إحصاء";
                    break;
                case R.id.radioButton13:
                    StudySubject = "تربية وطنية";
                    break;
                case R.id.radioButton14:
                    StudySubject = "إنجلش";
                    break;
                case R.id.radioButton15:
                    StudySubject = "فرنساوي";
                    break;
                case R.id.radioButton16:
                    StudySubject = "الماني";
                    break;
                case R.id.radioButton17:
                    StudySubject = "إيطالي";
                    break;
                case R.id.radioButton18:
                    StudySubject = "اسباني";
                    break;

            }

        });

        dialog.findViewById(R.id.btnCancelData).setOnClickListener(v -> {
            dialog.dismiss();
        });
        AddTime.setOnClickListener(v -> {
            TimePickerDialog.OnTimeSetListener timeSetListener = new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker view1, int hourOfDay, int minute) {
                    String am_pm = "";

                    Calendar datetime = Calendar.getInstance();
                    datetime.set(Calendar.HOUR_OF_DAY, hourOfDay);
                    datetime.set(Calendar.MINUTE, minute);

                    if (datetime.get(Calendar.AM_PM) == Calendar.AM)
                        am_pm = "AM";
                    else if (datetime.get(Calendar.AM_PM) == Calendar.PM)
                        am_pm = "PM";

                    String strHrsToShow = (datetime.get(Calendar.HOUR) == 0) ? "12" : datetime.get(Calendar.HOUR) + "";
//                    String strMinToShow = (datetime.get(Calendar.MINUTE) == 0) + "";

                    AddTime.setText(strHrsToShow + ":" + datetime.get(Calendar.MINUTE) + " " + am_pm);
//                    AddTime.setText(strHrsToShow + ":" +strMinToShow + " " + am_pm);


//
//                        Hour = hourOfDay;
//                        mints = minute;
//                        AddTime.setText(String.format(Locale.getDefault(), "%02d:%02d", Hour, mints));

                }
            };
            TimePickerDialog timePickerDialog = new TimePickerDialog(this, timeSetListener, Hour, mints, false);

            timePickerDialog.setTitle("select Time");
            timePickerDialog.show();

        });


//        String finalCheckvalue = checkValue;
        dialog.findViewById(R.id.btnSaveData).setOnClickListener(v -> {

            if (AddTeacher.toString().isEmpty()) {
                AddTeacher.requestFocus();
                AddTeacher.setError(getString(R.string.pleaseEnterTeacherName));
                return;
            }
            if (StudyYear.isEmpty()) {
                Toast.makeText(this, getString(R.string.pleaseEnterTeacherNam), Toast.LENGTH_SHORT).show();
                return;
            }
            if (StudyRoom.isEmpty()) {
                Toast.makeText(this, getString(R.string.Pleaseselectroom), Toast.LENGTH_SHORT).show();
                return;
            }
            if (StudySubject.isEmpty()) {
                Toast.makeText(this, getString(R.string.SubjectName), Toast.LENGTH_SHORT).show();
                return;
            }

            String addTime = AddTime.getText().toString();

            try {
                DateFormat formatter = new SimpleDateFormat("hh:mm a");

                Date date = null;

                date = formatter.parse(addTime);
                Timestamp timestamp = new Timestamp(date.getTime());
                HashMap<String, Object> user = new HashMap<>();
                user.put("Time", timestamp);
                user.put("teacherName", AddTeacher.getText().toString());
                user.put("studyYear", StudyYear);
                user.put("subjectName", StudySubject);
                user.put("Room", StudyRoom);
                user.put("Day", Day);
                user.put("onlyDayOfWeek", checkValue);


                db.collection(Day)
                        .add(user)
                        .addOnSuccessListener(documentReference -> {
                            clearData();
                            getDataInRoom();
                            dialog.dismiss();
                        })
                        .addOnFailureListener(e -> Toast.makeText(this, "Error + \\n" + e, Toast.LENGTH_SHORT).show());
            } catch (ParseException e) {
                e.printStackTrace();
            }

        });


        dialog.show();

    }

    private void clearData() {
        dataList.clear();
        dataList2.clear();
        dataList3.clear();
        dataList4.clear();
        adapterData.notifyDataSetChanged();
        adapterData2.notifyDataSetChanged();
        adapterData3.notifyDataSetChanged();
        adapterData4.notifyDataSetChanged();

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();


    }

    @Override
    protected void onPause() {
        super.onPause();
        LoadChangeLanguage();


    }

    @Override
    protected void onResume() {
        super.onResume();

        LoadChangeLanguage();





    }
}





