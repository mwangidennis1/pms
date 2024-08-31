package org.mwangi.maya.notification;

import org.mwangi.maya.entities.Parcel;
import org.springframework.context.ApplicationEvent;

public class ParcelStatusChangedEvent extends ApplicationEvent {
    private final Parcel parcel;

    public ParcelStatusChangedEvent(Object source,Parcel parcel) {
        super(source);
        this.parcel=parcel;
    }

    public Parcel getParcel() {
        return parcel;
    }
}
