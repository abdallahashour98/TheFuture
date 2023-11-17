package com.example.TheFuture;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.TheFuture.adapter.AdpterNotes;
import com.example.TheFuture.model.ModelData;
import com.example.TheFuture.model.ModelNotes;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AddNotesActivity extends AppCompatActivity {

    EditText edtAddNots;
    ImageView SaveNots;
    RecyclerView rc ;
    FirebaseFirestore db;
    List<ModelNotes> List;
    ModelNotes model ;
    AdpterNotes adpterNotes;
    SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_nots);

        edtAddNots = findViewById(R.id.edtAddNots);
        SaveNots = findViewById(R.id.SaveNots);
        rc = findViewById(R.id.rcnots);
        swipeRefreshLayout = findViewById(R.id.SwiperefreshLayout);

        List = new ArrayList<>();
        model = new ModelNotes();
        adpterNotes = new AdpterNotes(this, List);


        SaveNots.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (edtAddNots.getText().toString().isEmpty()) {
                    edtAddNots.requestFocus();
                    edtAddNots.setError(getString(R.string.Notes));
                }else {
                    AddDataInFirestore();
                }



            }
        });
        getDataWithFirestore();
        swipeRefreshLayout.setOnRefreshListener(() -> {

            List.clear();
            adpterNotes.notifyDataSetChanged();
            getDataWithFirestore();
            edtAddNots.setText("");

            swipeRefreshLayout.setRefreshing(false);
        });



    }

    private void getDataWithFirestore() {
        db  = FirebaseFirestore.getInstance();

        db.collection("Notes")
                .orderBy("Time", Query.Direction.ASCENDING)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {

                    if (!queryDocumentSnapshots.isEmpty()) {

                        List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                        for (DocumentSnapshot documentSnapshot : list) {

                            String note = documentSnapshot.getString("Notes");
                            com.google.firebase.Timestamp Time = documentSnapshot.getTimestamp("Time");
                            String ID = documentSnapshot.getId();



                                ModelNotes modelNotes = new ModelNotes(Time,note,ID);


                            rc.setHasFixedSize(true);
                                rc.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
                                rc.setAdapter(adpterNotes);


                                List.add(modelNotes);
                                adpterNotes.notifyDataSetChanged();



                        }
                        adpterNotes.notifyDataSetChanged();




                    } else {
                        // if the snapshot is empty we are displaying a toast message.

                    }
                });


    }


    private void AddDataInFirestore() {




            HashMap<String, Object> user = new HashMap<>();

            user.put("Notes", edtAddNots.getText().toString());
            user.put( "Time", FieldValue.serverTimestamp());



        db.collection("Notes")
                    .add(user)
                    .addOnSuccessListener(documentReference -> {
                        Toast.makeText(this,(R.string.saved), Toast.LENGTH_SHORT).show();
                        edtAddNots.setText("");
                        List.clear();
                        getDataWithFirestore();


                    })
                    .addOnFailureListener(e -> Toast.makeText(this, "Error + \\n" + e, Toast.LENGTH_SHORT).show());
        }


}