package es.upm.miw.lovecalculator;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import es.upm.miw.lovecalculator.models.LoveCalculator;
import es.upm.miw.lovecalculator.view.LoveHistoricAdapter;

public class ActividadLoveHistoric extends Activity {

    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mLoveCalculatorDatabaseReference;
    private List<LoveCalculator> loveCalculators;

    private ListView lvLoveHistoric;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_love_historic);
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mLoveCalculatorDatabaseReference = mFirebaseDatabase.getReference().child("lovecalculator");
        loveCalculators = new ArrayList<>();
        mLoveCalculatorDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    loveCalculators.add(ds.getValue(LoveCalculator.class));
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
        lvLoveHistoric = findViewById(R.id.lvLoveHistoric);
        LoveHistoricAdapter loveHistoricAdapter = new LoveHistoricAdapter(this, R.layout.item_love_historic, loveCalculators);
        lvLoveHistoric.setAdapter(loveHistoricAdapter);
    }
}
