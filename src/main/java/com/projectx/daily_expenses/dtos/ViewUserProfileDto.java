package com.projectx.daily_expenses.dtos;

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
