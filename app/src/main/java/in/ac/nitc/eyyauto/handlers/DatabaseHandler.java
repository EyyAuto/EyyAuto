package in.ac.nitc.eyyauto.handlers;

import android.support.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.annotations.Nullable;

import java.lang.reflect.ParameterizedType;
import java.util.HashMap;
import java.util.Map;

public abstract class DatabaseHandler<T> {
    private static final FirebaseDatabase database = FirebaseDatabase.getInstance();
    @SuppressWarnings("unchecked")
    private final Class<T> classType = (Class<T>)((ParameterizedType)getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    protected Map<String, ValueEventListener> listenerMap = new HashMap<>();

    private void processResponse(@NonNull DataSnapshot dataSnapshot, @NonNull Event<T> event) {
        T data = dataSnapshot.getValue(classType);
        event.onReceive(data);
    }

    protected void getDataOnce(@NonNull String path, @NonNull final Event<T> event) {
        database.getReference(path).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                processResponse(dataSnapshot, event);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                event.onFailed(databaseError);
            }
        });
    }

    protected ValueEventListener addListener(@NonNull String path, @NonNull final Event<T> listener) {
        return database.getReference(path).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                processResponse(dataSnapshot, listener);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                listener.onFailed(databaseError);
            }
        });
    }

    protected String getPushKey(@NonNull String path) {
        return database.getReference(path).push().getKey();
    }

    protected void removeListener(@NonNull String path, @NonNull ValueEventListener listener) {
        database.getReference(path).removeEventListener(listener);
    }

    protected void putData(@NonNull String path, @Nullable T data) {
        database.getReference(path).setValue(data);
    }

    abstract public void readOnce(@NonNull String uid, @NonNull Event<T> event);

    abstract public void putValue(@NonNull String uid, @Nullable T data);
}
