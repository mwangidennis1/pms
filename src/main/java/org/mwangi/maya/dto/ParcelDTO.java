package org.mwangi.maya.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.mwangi.maya.entities.Categories;
import org.mwangi.maya.entities.ParcelStatus;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@Builder
public class ParcelDTO {
    private String destination;
    private LocalDateTime estimatedDeliveryDate;
    private String approxWeight;
    private Categories categories;
    private ParcelStatus parcelStatus;
    private  String trackingNumber;
    private ReceiverDTO receiver;
    private SenderDTO sender;
    public ParcelDTO(){

    }
}
