package com.projectx.daily_expenses.controllers;

import com.projectx.daily_expenses.commons.AlreadyExistsException;
import com.projectx.daily_expenses.commons.InvalidDataException;
import com.projectx.daily_expenses.commons.InvalidUserException;
import com.projectx.daily_expenses.commons.ResponseDto;
import com.projectx.daily_expenses.dtos.LoginDto;
import com.projectx.daily_expenses.dtos.LoginResponseDto;
import com.projectx.daily_expenses.dtos.UserDto;
import com.projectx.daily_expenses.services.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    @GetMapping(value = "/logout")
    public ResponseEntity<ResponseDto<Boolean>> logout() {
        return new ResponseEntity<>(new ResponseDto<>(true,null,null),HttpStatus.OK);
    }
}
