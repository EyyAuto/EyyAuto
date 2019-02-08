package in.ac.nitc.eyyauto.handlers;

import android.support.annotation.NonNull;

import com.google.firebase.database.DatabaseError;

import in.ac.nitc.eyyauto.models.User;

public final class UserHandler extends DatabaseHandler {
    private static final String USER_ROOT_PATH = "EyyAuto/Users/";
    private static final Class<?> type = User.class;

    public interface Event extends BaseEvent {
        void onReceive(User user);
    }

    private Event event;

    public void get(@NonNull String uid, @NonNull Event event) {
        String path = USER_ROOT_PATH + uid;
        this.event = event;
        getDataOnce(path, type);
    }

    @Override
    protected void onReceive(Object data) {
        User user = (User) data;
        event.onReceive(user);
    }

    @Override
    protected void onFailed(DatabaseError error) {
        event.onFailed(error);
    }
}