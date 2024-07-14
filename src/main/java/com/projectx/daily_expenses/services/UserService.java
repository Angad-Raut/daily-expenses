package com.projectx.daily_expenses.services;

import com.projectx.daily_expenses.commons.AlreadyExistsException;
import com.projectx.daily_expenses.commons.InvalidDataException;
import com.projectx.daily_expenses.commons.InvalidUserException;
import com.projectx.daily_expenses.dtos.LoginDto;
import com.projectx.daily_expenses.dtos.LoginResponseDto;
import com.projectx.daily_expenses.dtos.UserDto;

public interface UserService {
    Boolean registration(UserDto userDto)throws AlreadyExistsException, InvalidDataException;
    LoginResponseDto getLogin(LoginDto loginDto)throws InvalidUserException;
}
