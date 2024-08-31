package org.mwangi.maya.repositories;

import org.mwangi.maya.entities.Parcel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface ParcelRepository extends JpaRepository<Parcel,Long> {
    @Query("""
      SELECT p FROM Parcel p WHERE p.trackingNumber =?1
""")
    Parcel findParcelByTrackingNumber(String number);
    @Query("""
  SELECT  p FROM Parcel p WHERE p.estimatedDeliveryDate BETWEEN :from AND :to
""")
    List<Parcel> findParcelByEstimatedDeliveryDateIsBetweenDate(LocalDateTime from,LocalDateTime to);
}
