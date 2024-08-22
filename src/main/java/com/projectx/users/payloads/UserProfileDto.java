package com.projectx.users.payloads;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserProfileDto {
    private Long id;
    @NotNull(message = "UserId cannot be null!!")
    private Long userId;
    @NotNull(message = "Enter qualification!!")
    private String qualification;
    @NotNull(message = "Enter profession!!")
    private String profession;
    @NotNull(message = "Enter gender!!")
    private String gender;
    @NotNull(message = "Select date of birth!!")
    private String dateOfBirth;
    @NotNull(message = "Enter blood group!!")
    private String bloodGroup;
    @NotNull(message = "Enter pan number!!")
    private String panNumber;
    @NotNull(message = "Enter Aadhar Number!!")
    private String aadharNumber;
    @NotNull(message = "Enter user address street!!")
    private String street;
    @NotNull(message = "Enter user address city!!")
    private String city;
    @NotNull(message = "Enter user address state!!")
    private String state;
    @NotNull(message = "Enter user address country!!")
    private String country;
    @NotNull(message = "Enter user address pinCode!!")
    private Integer pinCode;
}
