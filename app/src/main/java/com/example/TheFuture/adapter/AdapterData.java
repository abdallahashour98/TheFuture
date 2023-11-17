package com.example.TheFuture.adapter;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.developer.kalert.KAlertDialog;
import com.example.TheFuture.DiaryActivity;
import com.example.TheFuture.MainActivity;
import com.example.TheFuture.R;
import com.example.TheFuture.model.ModelData;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;


import org.checkerframework.checker.nullness.qual.NonNull;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import static com.example.TheFuture.R.*;
import static com.example.TheFuture.R.string.*;


public class AdapterData extends RecyclerView.Adapter<AdapterData.ViewHolder> {
    Context context;
    LayoutInflater layoutInflater;
    List<ModelData> list;
    MainActivity mainActivity = new MainActivity();
    String StudyYear = "", StudyRoom = "", StudySubject = "", checkValue = "";
    int Hour, mints;

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    public AdapterData() {
    }

    public AdapterData(Context context, List<ModelData> list) {
        this.context = context;
        this.list = list;
        layoutInflater = LayoutInflater.from(context);

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(layout.tempdata, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ModelData model = list.get(position);

        holder.teacherName.setText("أ / " + model.getTeacherName());
        holder.subjectName.setText("المادة / " + model.getSubjectName());
        holder.studyYear.setText("السنه / " + model.getStudyYear());
        holder.DayOf.setText(model.getDayOF());
        String TimeFinal = new SimpleDateFormat("hh:mm a").format(model.getTime().toDate());
        holder.Time.setText("الوقت / " + TimeFinal);



            if (model.getOnlyDayOfWeek().equals("true")){

//                holder.dayOnly.setVisibility(View.VISIBLE);
                Picasso.with(context)
                .load(model.getOnlyDayOfWeek())
                .placeholder(drawable.logotime)
                .into(holder.dayOnly);


            }else if (model.getOnlyDayOfWeek().equals("false")| (model.getOnlyDayOfWeek().equals(""))){
                holder.dayOnly.setVisibility(View.GONE);

            }


    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView teacherName, subjectName, studyYear, Time, DayOf;
        ImageView dayOnly;



        public ViewHolder(View itemView) {
            super(itemView);

            teacherName = itemView.findViewById(R.id.teacherName);
            subjectName = itemView.findViewById(R.id.subjectName);
            studyYear = itemView.findViewById(R.id.studyYear);
            Time = itemView.findViewById(R.id.Time);
            DayOf = itemView.findViewById(R.id.studyTrueOrFalse);
            dayOnly = itemView.findViewById(R.id.onlyDayOfWeek);
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    ModelData model = list.get(getAdapterPosition());

                    Button edit , delelt , cancel;
                    Dialog dialog = new Dialog(context);
                    dialog.setContentView(layout.alerteditanddelet);
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    edit = dialog.findViewById(R.id.btnEditSubject);
                    delelt = dialog.findViewById(R.id.btnDeletSubject);
                    cancel = dialog.findViewById(R.id.btnCancelSubject);
                    edit.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            editDataWithFirebase();
                            dialog.dismiss();

                        }
                    });
                    delelt.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            delete();
                            dialog.dismiss();
                        }
                    });
                    cancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent i = new Intent(context, DiaryActivity.class);
                            i.putExtra("id",model.getID());
                            context.startActivity(i);
                        }
                    });

                    dialog.show();

                    return false;
                }
            });
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (DayOf.getText() == "X") {

                        DayOf.setText("");
                        editSubjectDayOFf();


                    } else {
                        DayOf.setText("X");
                        editSubjectDayOFf();

                    }
                }
            });


        }

        private void delete() {
            ModelData model = list.get(getAdapterPosition());
            ;
            new KAlertDialog(context, KAlertDialog.PROGRESS_TYPE)
                    .getProgressHelper().getBarColor();


            db.collection(model.getDay())
                    .document(model.getID())
                    .delete()
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void documentReference) {
                            Toast.makeText(context, deleteDone, Toast.LENGTH_SHORT).show();

                        }
                    })
                    .addOnFailureListener(e -> Toast.makeText(context, "Error + \\n" + e, Toast.LENGTH_SHORT).show());


        }

        private void editDataWithFirebase() {
            ModelData model = list.get(getAdapterPosition());


            RadioButton radioButtonOtherStudy, radioButtonStudy3, radioButtonStudy2, radioButtonStudy1, radioButtonRoom, radioButtonRoom2, radioButtonRoom3, radioButtonRoom4, radioButton1, radioButton2, radioButton3,
                    radioButton4, radioButton5, radioButton6, radioButton7, radioButton8, radioButton9, radioButton10,
                    radioButton11, radioButton12, radioButton13, radioButton14, radioButton15, radioButton16, radioButton17, radioButton18;

            RadioGroup RadioGroupStudyYear, RadioGroupStudyRoom, RadioGroupSubject;
            EditText AddTeacher;
            Button AddTime;
            CheckBox checkBox;



            Dialog dialog = new Dialog(context);
            dialog.setContentView(R.layout.addsubject);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.setCancelable(false);

            AddTeacher = dialog.findViewById(id.edtAddTeacherName);
            checkBox = dialog.findViewById(id.checkboxSubjectTime);


            AddTime = dialog.findViewById(R.id.AddTime);


            RadioGroupStudyYear = dialog.findViewById(R.id.RadioGroupStudyYear);
            RadioGroupStudyRoom = dialog.findViewById(R.id.RadioGroupStudyRoom);
            RadioGroupSubject = dialog.findViewById(R.id.RadioGroupSubject);

            radioButtonOtherStudy = dialog.findViewById(R.id.radioButtonOtherStudy);
            radioButtonStudy3 = dialog.findViewById(R.id.radioButtonStudy3);
            radioButtonStudy2 = dialog.findViewById(R.id.radioButtonStudy2);
            radioButtonStudy1 = dialog.findViewById(R.id.radioButtonStudy1);

            radioButtonRoom = dialog.findViewById(R.id.radioButtonRoom);
            radioButtonRoom2 = dialog.findViewById(R.id.radioButtonRoom2);
            radioButtonRoom3 = dialog.findViewById(R.id.radioButtonRoom3);
            radioButtonRoom4 = dialog.findViewById(R.id.radioButtonRoom4);

            radioButton1 = dialog.findViewById(R.id.radioButton1);
            radioButton2 = dialog.findViewById(R.id.radioButton2);
            radioButton3 = dialog.findViewById(R.id.radioButton3);
            radioButton4 = dialog.findViewById(R.id.radioButton4);
            radioButton5 = dialog.findViewById(R.id.radioButton5);
            radioButton7 = dialog.findViewById(R.id.radioButton7);
            radioButton8 = dialog.findViewById(R.id.radioButton8);
            radioButton9 = dialog.findViewById(R.id.radioButton9);
            radioButton10 = dialog.findViewById(R.id.radioButton10);
            radioButton11 = dialog.findViewById(R.id.radioButton11);
            radioButton12 = dialog.findViewById(R.id.radioButton12);
            radioButton13 = dialog.findViewById(R.id.radioButton13);
            radioButton14 = dialog.findViewById(R.id.radioButton14);
            radioButton15 = dialog.findViewById(R.id.radioButton15);
            radioButton16 = dialog.findViewById(R.id.radioButton16);
            radioButton17 = dialog.findViewById(R.id.radioButton17);
            radioButton18 = dialog.findViewById(R.id.radioButton18);

            AddTeacher.setText(model.getTeacherName());
            String check ;

            if (model.getOnlyDayOfWeek().equals("true")){
                checkValue = "true";
                checkBox.setChecked(true);
            }else {
                checkValue = "false";
                checkBox.setChecked(false);

            }
            checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    if (b){
                        checkValue = "true";


                    }else if (!b){
                        checkValue = "false";

                    }
                }
            });




            if (model.getStudyYear().equals("سنه دراسية اخري")) {
                radioButtonOtherStudy.setChecked(true);
                StudyYear = "سنه دراسية اخري";
            } else if (model.getStudyYear().equals("3 ث")) {
                radioButtonStudy3.setChecked(true);
                StudyYear = "3 ث";
            } else if (model.getStudyYear().equals("2 ث")) {
                radioButtonStudy2.setChecked(true);
                StudyYear = "2 ث";
            } else if (model.getStudyYear().equals("1 ث")) {
                radioButtonStudy1.setChecked(true);
                StudyYear = "1 ث";
            }

            if (model.getRoom().equals("قاعة 4") | model.getRoom().equals("room 4")) {
                radioButtonRoom4.setChecked(true);
                StudyRoom = "room 4";
            } else if (model.getRoom().equals("قاعة 3") | model.getRoom().equals("room 3")) {
                radioButtonRoom3.setChecked(true);
                StudyRoom = "room 3";
            } else if (model.getRoom().equals("قاعة 2") | model.getRoom().equals("room 2")) {
                radioButtonRoom2.setChecked(true);
                StudyRoom = "room 2";
            } else if (model.getRoom().equals("قاعة 1") | model.getRoom().equals("room 1")) {
                radioButtonRoom.setChecked(true);
                StudyRoom = "room 1";
            }


            if (model.getSubjectName().equals("spanish") | model.getSubjectName().equals("اسباني")) {
                radioButton18.setChecked(true);
                StudySubject = "اسباني";
            } else if (model.getSubjectName().equals("Italiana") | model.getSubjectName().equals("ايطالي")) {
                radioButton17.setChecked(true);
                StudySubject = "ايطالي";

            } else if (model.getSubjectName().equals("Deutsch") | model.getSubjectName().equals("الماني")) {
                radioButton16.setChecked(true);
                StudySubject = "الماني";
            } else if (model.getSubjectName().equals("france") | model.getSubjectName().equals("فرنساوى")) {
                radioButton15.setChecked(true);
                StudySubject = "فرنساوى";
            } else if (model.getSubjectName().equals("english") | model.getSubjectName().equals("انجلش")) {
                radioButton14.setChecked(true);
                StudySubject = "انجلش";
            }  else if (model.getSubjectName().equals("تربية وطنية")) {
                radioButton13.setChecked(true);
                StudySubject = "تربية وطنية";
            } else if (model.getSubjectName().equals("إحصاء")) {
                radioButton12.setChecked(true);
                StudySubject = "إحصاء";
            } else if (model.getSubjectName().equals("تاريخ")) {
                radioButton11.setChecked(true);
                StudySubject = "تاريخ";
            } else if (model.getSubjectName().equals("جعرافيا")) {
                radioButton10.setChecked(true);
                StudySubject = "جعرافيا";
            } else if (model.getSubjectName().equals("جولوجيا")) {
                radioButton9.setChecked(true);
                StudySubject = "جولوجيا";
            } else if (model.getSubjectName().equals("احياء")) {
                radioButton8.setChecked(true);
                StudySubject = "احياء";
            } else if (model.getSubjectName().equals("علم نفس")) {
                radioButton7.setChecked(true);
                StudySubject = "علم نفس";
            } else if (model.getSubjectName().equals("فلسفه")) {
                radioButton5.setChecked(true);
                StudySubject = "فلسفه";
            } else if (model.getSubjectName().equals("كيمياء")) {
                radioButton4.setChecked(true);
                StudySubject = "كيمياء";
            } else if (model.getSubjectName().equals("فيزياء")) {
                radioButton3.setChecked(true);
                StudySubject = "فيزياء";
            } else if (model.getSubjectName().equals("رياضه")) {
                radioButton2.setChecked(true);
                StudySubject = "رياضه";
            } else if (model.getSubjectName().equals("عربي")) {
                radioButton1.setChecked(true);
                StudySubject = "عربي";
            }

            String time = new SimpleDateFormat("hh:mm a").format(model.getTime().toDate());
            AddTime.setText(time);


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

                    case R.id.radioButton18:
                        StudySubject = "اسباني";
                        break;
                    case R.id.radioButton17:
                        StudySubject = "ايطالي";
                        break;
                    case R.id.radioButton16:
                        StudySubject = "الماني";
                        break;
                    case R.id.radioButton15:
                        StudySubject = "فرنساوى";
                        break;
                    case R.id.radioButton14:
                        StudySubject = "انجلش";
                        break;

                    case R.id.radioButton13:
                        StudySubject = "تربية وطنية";
                        break;
                    case R.id.radioButton12:
                        StudySubject = "إحصاء";
                        break;
                    case R.id.radioButton11:
                        StudySubject = "تاريخ";
                        break;
                    case R.id.radioButton10:
                        StudySubject = "جعرافيا";
                        break;
                    case R.id.radioButton9:
                        StudySubject = "جولوجيا";
                        break;
                    case R.id.radioButton8:
                        StudySubject = "احياء";
                        break;
                    case R.id.radioButton7:
                        StudySubject = "علم نفس";
                        break;
                    case R.id.radioButton5:
                        StudySubject = "فلسفه";
                        break;
                    case R.id.radioButton4:
                        StudySubject = "كيمياء";
                        break;
                    case R.id.radioButton3:
                        StudySubject = "فيزياء";
                        break;
                    case R.id.radioButton2:
                        StudySubject = "رياضه";
                        break;
                    case R.id.radioButton1:
                        StudySubject = "عربي";
                        break;

                }

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

                        AddTime.setText(strHrsToShow + ":" + datetime.get(Calendar.MINUTE) + " " + am_pm);


//
//                        Hour = hourOfDay;
//                        mints = minute;
//                        AddTime.setText(String.format(Locale.getDefault(), "%02d:%02d", Hour, mints));

                    }
                };
                TimePickerDialog timePickerDialog = new TimePickerDialog(context, timeSetListener, Hour, mints, false);

                timePickerDialog.setTitle("select Time");
                timePickerDialog.show();


            });

            dialog.findViewById(id.btnCancelData).setOnClickListener(v -> {
                dialog.dismiss();
            });

//            String finalCheckValue = checkValue;
            dialog.findViewById(id.btnSaveData).setOnClickListener(v -> {

                if (AddTeacher.toString().isEmpty()) {
                    AddTeacher.requestFocus();
                    AddTeacher.setError(context.getString(R.string.pleaseEnterTeacherName));
                    return;
                }
                if (StudyYear.isEmpty()) {
                    Toast.makeText(context, (R.string.pleaseEnterTeacherNam), Toast.LENGTH_SHORT).show();
                    return;
                }
                if (StudyRoom.isEmpty()) {
                    Toast.makeText(context, (R.string.Pleaseselectroom), Toast.LENGTH_SHORT).show();
                    return;
                }
                if (StudySubject.isEmpty()) {
                    Toast.makeText(context, (R.string.SubjectName), Toast.LENGTH_SHORT).show();
                    return;
                }
//                if (AddMints.getSelectedItemPosition() == 0) {
//                    AddMints.requestFocus();
//                    Toast.makeText(context, (R.string.Mints), Toast.LENGTH_SHORT).show();
//                    return;
//                }
//                if (AddHours.getSelectedItemPosition() == 0) {
//                    AddHours.requestFocus();
//                    Toast.makeText(context, (R.string.Hours), Toast.LENGTH_SHORT).show();
//                    return;
//                }

//                String Hours = AddHours.getSelectedItem().toString();
//                String Mints = AddMints.getSelectedItem().toString();
//                if (AddTime.getText().equals("12")){
//                    Hours = "00" ;
//
//                }

                String addTime = AddTime.getText().toString();
                try {
                    DateFormat formatter = new SimpleDateFormat("hh:mm a");

                    Date date = null;

                    date = formatter.parse(addTime);
                    java.sql.Timestamp timestamp = new Timestamp(date.getTime());


                    HashMap<String, Object> user = new HashMap<>();
//            user.put("id", );
                    user.put("Time", timestamp);
                    user.put("teacherName", AddTeacher.getText().toString());
                    user.put("studyYear", StudyYear);
                    user.put("subjectName", StudySubject);
                    user.put("Room", StudyRoom);
                    user.put("onlyDayOfWeek", checkValue);


                    db.collection(model.getDay())
                            .document(model.getID())
                            .update(user)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void documentReference) {

                                    dialog.dismiss();

                                }
                            })
                            .addOnFailureListener(e -> Toast.makeText(context, "Error + \\n" + e, Toast.LENGTH_SHORT).show());
                } catch (ParseException e) {
                    e.printStackTrace();
                }

            });


            dialog.show();

        }

        private void editSubjectDayOFf() {
            ModelData model = list.get(getAdapterPosition());

            HashMap<String, Object> user = new HashMap<>();

            if (DayOf.getText() == "X") {

                user.put("dayOF", "X");

            } else {
                user.put("dayOF", "");

            }


            db.collection(model.getDay())
                    .document(model.getID())
                    .update(user)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void documentReference) {


                        }
                    })
                    .addOnFailureListener(e -> Toast.makeText(context, "Error + \\n" + e, Toast.LENGTH_SHORT).show());
        }


    }


}
