package com.example.TheFuture.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.developer.kalert.KAlertDialog;
import com.example.TheFuture.R;
import com.example.TheFuture.model.ModelNotes;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import static com.example.TheFuture.R.string.Are;
import static com.example.TheFuture.R.string.delete;
import static com.example.TheFuture.R.string.deleteDone;
import static com.example.TheFuture.R.string.edit;
import static com.example.TheFuture.R.string.edit1;
import static com.example.TheFuture.R.string.edit2;


public class AdpterNotes extends  RecyclerView.Adapter<AdpterNotes.ViewHolder> {

    Context context;
    LayoutInflater layoutInflater;
    List<ModelNotes> list;
    FirebaseFirestore db = FirebaseFirestore.getInstance();


    public AdpterNotes(Context context, List<ModelNotes> list) {
        this.context = context;
        this.list = list;
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.tempnotes, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ModelNotes model = list.get(position);

        holder.notes.setText(model.getNotes());
        String TimeFinal = new SimpleDateFormat("yyyy/MM/dd  hh:mm a").format(model.getTime().toDate());

        holder.Time.setText(""+TimeFinal);
    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

    TextView notes,Time;


    public ViewHolder(View itemView) {
        super(itemView);

        notes = itemView.findViewById(R.id.tvxnotes);
        Time = itemView.findViewById(R.id.tvxnotesTime);
        itemView.setOnClickListener(view -> new KAlertDialog(context, KAlertDialog.WARNING_TYPE)
                .setTitleText(context.getString(Are))
                .setContentText(context.getString(edit2))
                .setConfirmClickListener(context.getString(delete), new KAlertDialog.KAlertClickListener() {
                            @Override
                            public void onClick(KAlertDialog kAlertDialog) {
                                delete();

                                kAlertDialog.dismissWithAnimation();


                            }
                        }
                )

                .setCancelClickListener(context.getString(R.string.Cancel),KAlertDialog::cancel)

                .show());


    }

        private void delete() {
            ModelNotes model = list.get(getAdapterPosition());
            ;
            new KAlertDialog(context, KAlertDialog.PROGRESS_TYPE)
                    .getProgressHelper().getBarColor();


            db.collection("Notes")
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

//        private void editDataWithFirebase() {
//            ModelNotes model = list.get(getAdapterPosition());
//            LinearLayout linearLayoutupdata;
//            EditText edtAddNotsinUpdata;
//
//            AlertDialog builder = new AlertDialog.Builder(context).create();
//            View view = LayoutInflater.from(context).inflate(R.layout.tempnotes, null);
//            linearLayoutupdata = view.findViewById(R.id.linearUpdata);
//
//
//
//            builder.setView(view);
//            builder.setCancelable(false);
//
//
//            edtAddNotsinUpdata = view.findViewById(R.id.edtAddNotsinUpdata);
//            notes.setVisibility(View.GONE);
//            linearLayoutupdata.setVisibility(View.VISIBLE);
//
//
//
//            edtAddNotsinUpdata.setText(model.getNotes());
//
////            view.findViewById(R.id.btnCancelData).setOnClickListener(v -> {
////                builder.dismiss();
////            });
//
//            view.findViewById(R.id.SaveNotsinUpdata).setOnClickListener(v -> {
//
//                if (edtAddNotsinUpdata.getText().toString().isEmpty()) {
//                    edtAddNotsinUpdata.requestFocus();
//                    edtAddNotsinUpdata.setError("please Enter Notes");
//                }else {
//                    HashMap<String, Object> user = new HashMap<>();
//                    user.put("Notes", edtAddNotsinUpdata.getText().toString());
//                    db.collection("Notes")
//                            .document(model.getID())
//                            .update(user)
//                            .addOnSuccessListener(new OnSuccessListener<Void>() {
//                                @Override
//                                public void onSuccess(Void documentReference) {
//
//                                    builder.dismiss();
//
//                                }
//                            })
//                            .addOnFailureListener(e -> Toast.makeText(context, "Error + \\n" + e, Toast.LENGTH_SHORT).show());
//                    builder.show();
//                }
//                });
//
//
//
//
//
//
//
//
//        }

}
}
