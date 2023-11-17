package com.example.TheFuture.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.developer.kalert.KAlertDialog;
import com.example.TheFuture.R;
import com.example.TheFuture.model.ModelData;
import com.example.TheFuture.model.ModelDiary;
import com.example.TheFuture.model.ModelNotes;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;

import static com.example.TheFuture.R.string.Are;
import static com.example.TheFuture.R.string.delete;
import static com.example.TheFuture.R.string.deleteDone;
import static com.example.TheFuture.R.string.edit2;

public class AdapterDiary extends RecyclerView.Adapter<AdapterDiary.ViewHolder> {


    Context context;
    LayoutInflater layoutInflater;
    List<ModelDiary> list;
    List<ModelData> listModel;
    FirebaseFirestore db = FirebaseFirestore.getInstance();


    public AdapterDiary(Context context, List<ModelDiary> list) {
        this.context = context;
        this.list = list;
        layoutInflater = LayoutInflater.from(context);
    }


    @Override
    public AdapterDiary.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.adddiary, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(AdapterDiary.ViewHolder holder, int position) {
        ModelDiary model = list.get(position);

        holder.nameUser.setText(model.getName());

        String checktrueorfales = model.getCheck();


        if (checktrueorfales.equals("x")) {
            holder.checkBox.setChecked(true);

        } else if (checktrueorfales.equals("")|checktrueorfales.equals("")){
            holder.checkBox.setChecked(false);

        }

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView nameUser, totalDiary, existingDiary, remainingDiary;
        CheckBox checkBox;


        public ViewHolder(View itemView) {
            super(itemView);
            nameUser = itemView.findViewById(R.id.nameDiary);
            totalDiary = itemView.findViewById(R.id.totaldiary);
            existingDiary = itemView.findViewById(R.id.existingdiary);
            remainingDiary = itemView.findViewById(R.id.remainingdiary);
            checkBox = itemView.findViewById(R.id.itemCheck);
            checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    if (b) {

                        editUser();
                        checkBox.setChecked(true);


                    } else {
                        editUser();
                        checkBox.setChecked(false);

                    }
                }
            });
        }
            void editUser() {
                ModelData model = listModel.get(getAdapterPosition());
                ModelDiary model2 = list.get(getAdapterPosition());

                HashMap<String, Object> user = new HashMap<>();
                user.put("DiaryTeacherID", model.getID());



                db.collection("Diary")
                        .document(model2.getID())
                        .update(user)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void documentReference) {

                                Toast.makeText(context, "UpdateDone", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .addOnFailureListener(e -> Toast.makeText(context, "Error + \\n" + e, Toast.LENGTH_SHORT).show());

            }

        }

    }


