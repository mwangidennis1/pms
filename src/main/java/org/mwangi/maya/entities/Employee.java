package org.mwangi.maya.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;


@Data
@Builder
@AllArgsConstructor
@Entity
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private  String username;
    private  String password;
    private String email;
    @Enumerated(EnumType.STRING)
    private Role role;
    @Column(name="password_token")
    private String resetPasswordToken;



    public Employee() {

    }
}
