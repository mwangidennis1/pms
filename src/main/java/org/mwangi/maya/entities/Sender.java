package org.mwangi.maya.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Data
@Builder
@AllArgsConstructor
public class Sender {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "sender_name")
    private String senderName;
    @Column(name = "sender_number")
    private String senderPhoneNo;
    @Column(name = "sender_email")
    private  String senderEmail;


    public Sender() {

    }
}
