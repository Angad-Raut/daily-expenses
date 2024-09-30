package com.projectx.users.payloads;

import javax.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDto {
    @NotNull(message = "Please enter your full name!!")
    private String userName;
    @NotNull(message = "Please enter your email-Id!!")
    @Email(message = "Please enter valid email-Id!!")
    private String userEmail;
    @NotNull(message = "Please enter your mobile!!")
    //@Size(min = 10,max = 10,message = "Please enter valid mobile number!!")
    private Long userMobile;
    @NotNull(message = "Please enter your password")
    @Size(min = 7,max = 15,message = "Password should be minimum 7 and maximum 15 characters!!")
    private String userPassword;
}
