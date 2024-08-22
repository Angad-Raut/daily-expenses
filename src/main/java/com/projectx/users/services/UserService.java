package com.projectx.users.services;

import com.projectx.common.exceptions.AlreadyExistsException;
import com.projectx.common.exceptions.InvalidDataException;
import com.projectx.common.exceptions.InvalidUserException;
import com.projectx.common.exceptions.ResourceNotFoundException;
import com.projectx.common.payloads.EntityIdDto;
import com.projectx.users.entities.UserProfileDetails;
import com.projectx.users.payloads.*;

public interface UserService {
    Boolean registration(UserDto userDto)throws AlreadyExistsException, InvalidDataException;
    LoginResponseDto getLogin(LoginDto loginDto)throws InvalidUserException;
    Boolean changePassword(ChangePasswordDto dto)throws ResourceNotFoundException,InvalidDataException;
    Boolean updateUserSetting(AccountSettingDto dto)throws ResourceNotFoundException,AlreadyExistsException;
    UserProfileDetails addUpdateUserProfile(UserProfileDto dto)throws ResourceNotFoundException,AlreadyExistsException;
    ViewUserProfileDto getUserProfileDetails(EntityIdDto dto)throws ResourceNotFoundException;
    AccountInfoDto getAccountInfo(EntityIdDto dto)throws ResourceNotFoundException;
}
