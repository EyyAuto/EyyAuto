package in.ac.nitc.eyyauto.handlers;

import com.google.firebase.database.DatabaseError;

public interface BaseEvent {
    void onFailed(DatabaseError databaseError);
}
