package org.mwangi.maya.notification;

import org.mwangi.maya.entities.Parcel;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
public class ParcelStatusChangedListener implements ApplicationListener<ParcelStatusChangedEvent> {
    @Override
    public void onApplicationEvent(ParcelStatusChangedEvent event) {
        Parcel parcel=event.getParcel();
    }
}
