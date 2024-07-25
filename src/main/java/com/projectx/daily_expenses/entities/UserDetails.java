package com.projectx.daily_expenses.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "user_details")
public class UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;
    private String userName;
    private String userEmail;
    private Long userMobile;
    private String userPassword;
    private Boolean userStatus;
    @Lob
    private byte[] photo;
    @Lob
    private byte[] signature;
    private Date insertedTime;
}
