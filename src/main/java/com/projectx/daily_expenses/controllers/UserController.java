package com.projectx.daily_expenses.controllers;

import com.projectx.daily_expenses.commons.*;
import com.projectx.daily_expenses.dtos.*;
import com.projectx.daily_expenses.entities.UserProfileDetails;
import com.projectx.daily_expenses.services.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/userDetails")
public class UserController {

    @Autowired
    private UserService userService;

    @RequestMapping(value = "/userRegister")
    public ResponseEntity<ResponseDto<Boolean>> userRegister(@Valid @RequestBody UserDto userDto) {
        try {
            Boolean data = userService.registration(userDto);
            return new ResponseEntity<>(new ResponseDto<>(data,null,null),HttpStatus.CREATED);
        } catch (AlreadyExistsException | InvalidDataException e) {
            return new ResponseEntity<>(new ResponseDto<>(null,e.getMessage(),null), HttpStatus.OK);
        }
    }

    @RequestMapping(value = "/getLogin")
    public ResponseEntity<ResponseDto<LoginResponseDto>> getLogin(@Valid @RequestBody LoginDto loginDto) {
        try {
            LoginResponseDto data = userService.getLogin(loginDto);
            return new ResponseEntity<>(new ResponseDto<>(data,null,null),HttpStatus.OK);
        } catch (InvalidUserException e) {
            return new ResponseEntity<>(new ResponseDto<>(null,e.getMessage(),null), HttpStatus.OK);
        }
    }
    @RequestMapping(value = "/getAccountInfo")
    public ResponseEntity<ResponseDto<AccountInfoDto>> getAccountInfo(@Valid @RequestBody EntityIdDto dto) {
        try {
            AccountInfoDto data = userService.getAccountInfo(dto);
            return new ResponseEntity<>(new ResponseDto<>(data,null,null),HttpStatus.OK);
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity<>(new ResponseDto<>(null,e.getMessage(),null), HttpStatus.OK);
        }
    }
    @GetMapping(value = "/logout")
    public ResponseEntity<ResponseDto<Boolean>> logout() {
        return new ResponseEntity<>(new ResponseDto<>(true,null,null),HttpStatus.OK);
    }

    @PostMapping(value = "/changePassword")
    public ResponseEntity<ResponseDto<Boolean>> changePassword(@Valid @RequestBody ChangePasswordDto dto) {
        try {
            Boolean data = userService.changePassword(dto);
            return new ResponseEntity<>(new ResponseDto<>(data,null,null),HttpStatus.OK);
        } catch (ResourceNotFoundException | InvalidDataException e) {
            return new ResponseEntity<>(new ResponseDto<>(null,e.getMessage(),null),HttpStatus.OK);
        }
    }

    @PostMapping(value = "/updateAccountSetting",consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
    public ResponseEntity<ResponseDto<Boolean>> updateAccountSetting(@ModelAttribute @Valid AccountSettingDto dto) {
        try {
            Boolean data = userService.updateUserSetting(dto);
            return new ResponseEntity<>(new ResponseDto<>(data,null,null),HttpStatus.OK);
        } catch (ResourceNotFoundException | AlreadyExistsException e) {
            return new ResponseEntity<>(new ResponseDto<>(null,e.getMessage(),null),HttpStatus.OK);
        }
    }

    @PostMapping(value = "/addUpdateUserProfile")
    public ResponseEntity<ResponseDto<UserProfileDetails>> addUpdateUserProfile(@Valid @RequestBody UserProfileDto dto) {
        try {
            UserProfileDetails data = userService.addUpdateUserProfile(dto);
            return new ResponseEntity<>(new ResponseDto<>(data,null,null),HttpStatus.OK);
        } catch (ResourceNotFoundException | AlreadyExistsException e) {
            return new ResponseEntity<>(new ResponseDto<>(null,e.getMessage(),null),HttpStatus.OK);
        }
    }

    @PostMapping(value = "/getUserProfileDetails")
    public ResponseEntity<ResponseDto<ViewUserProfileDto>> getUserProfileDetails(@Valid @RequestBody EntityIdDto dto) {
        try {
            ViewUserProfileDto data = userService.getUserProfileDetails(dto);
            return new ResponseEntity<>(new ResponseDto<>(data,null,null),HttpStatus.OK);
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity<>(new ResponseDto<>(null,e.getMessage(),null),HttpStatus.OK);
        }
    }
}
