package com.projectx.daily_expenses.services;

import com.projectx.daily_expenses.commons.*;
import com.projectx.daily_expenses.dtos.*;
import com.projectx.daily_expenses.entities.UserProfileDetails;

public interface UserService {
    Boolean registration(UserDto userDto)throws AlreadyExistsException, InvalidDataException;
    LoginResponseDto getLogin(LoginDto loginDto)throws InvalidUserException;
    Boolean changePassword(ChangePasswordDto dto)throws ResourceNotFoundException,InvalidDataException;
    Boolean updateUserSetting(AccountSettingDto dto)throws ResourceNotFoundException,AlreadyExistsException;
    UserProfileDetails addUpdateUserProfile(UserProfileDto dto)throws ResourceNotFoundException,AlreadyExistsException;
    ViewUserProfileDto getUserProfileDetails(EntityIdDto dto)throws ResourceNotFoundException;
}
