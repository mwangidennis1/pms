package org.mwangi.maya.repositories;

import org.mwangi.maya.entities.Employee;
import org.mwangi.maya.entities.Receiver;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ReceiverRepository extends JpaRepository<Receiver,Long> {
    @Query("""
   SELECT r FROM Receiver r WHERE r.receiverEmail=?1
""")
    Receiver findByEmail(String email);}
