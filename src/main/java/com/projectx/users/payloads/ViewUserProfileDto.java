package com.projectx.users.payloads;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ViewUserProfileDto {
    private Long id;
    private Long userId;
    private String fullName;
    private String email;
    private Long mobile;
    private String qualification;
    private String profession;
    private String gender;
    private String dateOfBirth;
    private String bloodGroup;
    private String panNumber;
    private String aadharNumber;
    private String street;
    private String city;
    private String state;
    private String country;
    private Integer pinCode;
}
