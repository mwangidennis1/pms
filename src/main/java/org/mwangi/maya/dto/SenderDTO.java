package org.mwangi.maya.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class SenderDTO {
    private String senderName;
    private String senderPhoneNo;
    private  String senderEmail;
    public SenderDTO(){

    }
}
