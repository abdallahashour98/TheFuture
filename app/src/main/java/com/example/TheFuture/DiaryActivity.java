package com.example.TheFuture;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.TheFuture.adapter.AdapterData;
import com.example.TheFuture.adapter.AdapterDiary;
import com.example.TheFuture.model.ModelData;
import com.example.TheFuture.model.ModelDiary;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class DiaryActivity extends AppCompatActivity {
    RecyclerView rcName,rcCheckBox;
    EditText inputUserName;
    Button saveNameUser;
    List<ModelDiary> dataList;
    ModelData modelData;
    AdapterDiary adapterDiary;
    ModelDiary model;
    FirebaseFirestore db, firestore;
    String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diary);

        rcName = findViewById(R.id.tvxryname);
        rcCheckBox = findViewById(R.id.checkBoxryUser);
        inputUserName = findViewById(R.id.edtAddStudint);
        saveNameUser = findViewById(R.id.btnAddStudint);
        firestore = FirebaseFirestore.getInstance();

        dataList = new ArrayList<>();
        model = new ModelDiary();
        modelData = new ModelData();
        adapterDiary = new AdapterDiary(this, dataList);
        getDataFromStudent();
        getDataFromDiary();
//        getDataFromStudentAttend();



    }


    private void ImageViewAdd() {

                saveNameUser.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String input = inputUserName.getText().toString();
                        if (input.isEmpty()){
                            inputUserName.requestFocus();
                            inputUserName.setError("Please Enter DiaryName");
                        }else {
                            HashMap<String, Object> user = new HashMap<>();
                            user.put("SubjectNameTeacher", input);
                            user.put("DiaryTeacherID",modelData.getID());
                            db.collection("Diary")
                                    .add(user)
                                    .addOnSuccessListener(documentReference -> {
                                        Toast.makeText(DiaryActivity.this, "DiaryNotesSaved", Toast.LENGTH_SHORT).show();
                                    })
                                    .addOnFailureListener(e -> Toast.makeText(DiaryActivity.this, "Error + \\n" + e, Toast.LENGTH_SHORT).show());


                        }

                    }
                });






    }


    private void getDataFromStudent() {
        ModelData modelData = new ModelData();
        Intent intent = getIntent();
         id=intent.getStringExtra("id");
        db = FirebaseFirestore.getInstance();
        db.collection("Friday")
                .document("T5zrJZ1js2qJoYJOnjLq")
                .collection("studint")
                .orderBy("name")
                .get()

                .addOnSuccessListener(queryDocumentSnapshots -> {

                            if (!queryDocumentSnapshots.isEmpty()) {

                                List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                                for (DocumentSnapshot documentSnapshot : list) {
                                    ModelDiary modelDiary = new ModelDiary();

                                    String name = documentSnapshot.getString("name");
                                    String check = documentSnapshot.getString("check");
                                    String ID = documentSnapshot.getId();

                                    ModelDiary modelDat = new ModelDiary(ID, name,check);

                                    rcName.setHasFixedSize(true);
                                    rcName.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
                                    rcName.setAdapter(adapterDiary);
                                    Log.d("room :check", ": "+check);

                                    dataList.add(modelDat);
                                    adapterDiary.notifyDataSetChanged();
                                    Log.d("TAG-getName", ": "+modelDiary.getName());

                                }






                    } else {
                        // if the snapshot is empty we are displaying a toast message.

                    }
                });


    }
    private void getDataFromDiary() {

        db.collection("Diary")
                .orderBy("totalDiary")
                .get()

                .addOnSuccessListener(queryDocumentSnapshots -> {

                            if (!queryDocumentSnapshots.isEmpty()) {

                                List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                                for (DocumentSnapshot documentSnapshot : list) {
                                    ModelDiary modelDiary = new ModelDiary();

                                    Long totalDiary = documentSnapshot.getLong("totalDiary");
                                    Long existingDiary = documentSnapshot.getLong("existingDiary");
                                    Long remainingDiary = documentSnapshot.getLong("remainingDiary");

                                    String ID = documentSnapshot.getId();

                                    ModelDiary modelDat = new ModelDiary(ID, totalDiary,existingDiary,remainingDiary);

                                    rcName.setHasFixedSize(true);
                                    rcName.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
                                    rcName.setAdapter(adapterDiary);

                                    dataList.add(modelDat);
                                    adapterDiary.notifyDataSetChanged();
                                    Log.d("TAG-getName", ": "+modelDiary.getName());

                                }






                    } else {
                        // if the snapshot is empty we are displaying a toast message.

                    }
                });


    }
//    private void getDataFromStudentAttend() {
//        ModelData modelData = new ModelData();
//        ModelDiary modelDiary = new ModelDiary();
//        db = FirebaseFirestore.getInstance();
//        db.collection("Friday")
//                .document(id)
//                .collection("studint")
//                .document("T5zrJZ1js2qJoYJOnjlq")
//                .collection(modelDiary.getName())
//                .get()
//                .addOnSuccessListener(queryDocumentSnapshots -> {
//
//                            if (!queryDocumentSnapshots.isEmpty()) {
//
//                                List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
//                                for (DocumentSnapshot documentSnapshot : list) {
//
//                                    com.google.firebase.Timestamp Time = documentSnapshot.getTimestamp("Time");
//
//                                    Boolean attend = documentSnapshot.getBoolean("attend");
//                                    String ID = documentSnapshot.getId();
//
//                                    ModelDiary modelDat = new ModelDiary(ID,attend, Time);
//
//                                    rcCheckBox.setHasFixedSize(true);
//                                    rcCheckBox.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
//                                    rcCheckBox.setAdapter(adapterDiary);
//                                    Log.d("room :", ": " + modelDat.getID());
//
//                                    dataList.add(modelDat);
//                                    adapterDiary.notifyDataSetChanged();
//                                }
//
//
//
//
//
//
//                    } else {
//                        // if the snapshot is empty we are displaying a toast message.
//
//                    }
//                });
//
//
//    }

}