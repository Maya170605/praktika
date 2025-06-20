package com.example.customs.entity;

import jakarta.persistence.*;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "users")
public class User extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Column(name = "user_name", nullable = false)
    private String name;

    @ManyToOne
    @JoinColumn(name = "unp")
    private Unp unp;


    @Column
    private String email;

    @Column(name = "activity_type")
    private String activityType;

    private boolean verified;
}



