package in.ac.nitc.eyyauto.models;

import com.google.android.gms.maps.model.LatLng;

public class Request {
    private String uid;
    private String did;
    private LatLng pickup;
    private LatLng dropoff;

    public Request() {
    }

    public String getDid() {
        return did;
    }

    public void setDid(String did) {
        this.did = did;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public LatLng getDropoff() {
        return dropoff;
    }

    public void setDropoff(LatLng dropoff) {
        this.dropoff = dropoff;
    }

    public LatLng getPickup() {
        return pickup;
    }

    public void setPickup(LatLng pickup) {
        this.pickup = pickup;
    }
}
