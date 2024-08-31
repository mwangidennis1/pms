package org.mwangi.maya.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Data
@Builder
@AllArgsConstructor
public class Parcel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String destination;
    @Column(name = "estimated_date")
    private LocalDateTime estimatedDeliveryDate;
    @Column(name = "approx_weight")
    private String approxWeight;
    @Enumerated(EnumType.STRING)
    private Categories categories;
    @Enumerated(EnumType.STRING)
    private  ParcelStatus parcelStatus;
    @Column(name = "track_number")
    private  String trackingNumber;
    @OneToOne(cascade = CascadeType.ALL)
    private Receiver receiver;
    @OneToOne(cascade = CascadeType.ALL)
    private  Sender sender;
    private BigDecimal amount;

    public Parcel() {

    }
}
