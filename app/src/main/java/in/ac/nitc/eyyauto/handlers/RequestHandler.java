package in.ac.nitc.eyyauto.handlers;

import android.support.annotation.NonNull;

import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.annotations.Nullable;

import in.ac.nitc.eyyauto.models.Request;

import static in.ac.nitc.eyyauto.Constants.REQUEST_ROOT_PATH;

public final class RequestHandler extends DatabaseHandler<Request> {

    public String pushRequest(Request request, Event<Request> event) {
        String key = getPushKey(REQUEST_ROOT_PATH);
        putValue(key, request);
        ValueEventListener l = addListener(REQUEST_ROOT_PATH + key, event);
        listenerMap.put(key, l);
        return key;
    }

    public void removeRequest(String key) {
        putValue(key, null);
        if (listenerMap.get(key) != null)
            removeListener(REQUEST_ROOT_PATH + key, listenerMap.get(key));
    }

    @Override
    public void readOnce(@NonNull String uid, @NonNull Event<Request> event) {

    }

    @Override
    public void putValue(@NonNull String uid, @Nullable Request data) {
        putData(REQUEST_ROOT_PATH + uid, data);
    }
}
