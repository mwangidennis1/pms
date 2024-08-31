package org.mwangi.maya.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class ReceiverDTO {
    private String receiverPhoneNo;
    private  String receiverEmail;
    public  ReceiverDTO(){

    }
}
