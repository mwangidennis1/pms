package org.mwangi.maya.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Entity
@Data
@Builder
@AllArgsConstructor
public class Receiver {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "receiver_number")
    private String receiverPhoneNo;
    @Column(name = "eceiver_email")
    private  String receiverEmail;

    public Receiver() {

    }
}
