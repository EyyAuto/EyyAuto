package in.ac.nitc.eyyauto.handlers;

import android.support.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public abstract class DatabaseHandler {
    private static final FirebaseDatabase database = FirebaseDatabase.getInstance();

    protected void getDataOnce(String path, final Class<?> type) {
        database.getReference(path).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Object data = dataSnapshot.getValue(type);
                onReceive(data);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                onFailed(databaseError);
            }
        });
    }

    abstract protected void onReceive(Object data);

    abstract protected void onFailed(DatabaseError error);
}
