package com.projectx.daily_expenses.services;

import com.projectx.daily_expenses.commons.AlreadyExistsException;
import com.projectx.daily_expenses.commons.Constants;
import com.projectx.daily_expenses.commons.InvalidDataException;
import com.projectx.daily_expenses.commons.InvalidUserException;
import com.projectx.daily_expenses.dtos.LoginDto;
import com.projectx.daily_expenses.dtos.LoginResponseDto;
import com.projectx.daily_expenses.dtos.UserDto;
import com.projectx.daily_expenses.entities.UserDetails;
import com.projectx.daily_expenses.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class UserServiceImpl implements UserService{

    @Autowired
    private UserRepository userRepository;

    @Override
    public Boolean registration(UserDto userDto) throws AlreadyExistsException, InvalidDataException {
        try {
            isMobileExist(userDto.getUserMobile());
            isEmailExist(userDto.getUserEmail());
            UserDetails userDetails = UserDetails.builder()
                    .userName(userDto.getUserName())
                    .userMobile(userDto.getUserMobile())
                    .userEmail(userDto.getUserEmail())
                    .userPassword(userDto.getUserPassword())
                    .userStatus(true)
                    .insertedTime(new Date())
                    .build();
            return userRepository.save(userDetails)!=null?true:false;
        } catch (AlreadyExistsException e) {
            throw new AlreadyExistsException(e.getMessage());
        } catch (Exception e) {
            throw new InvalidDataException(e.getMessage());
        }
    }

    @Override
    public LoginResponseDto getLogin(LoginDto loginDto) throws InvalidUserException {
        try {
            UserDetails userDetails = getAuthentication(loginDto);
            if (userDetails==null) {
                throw new InvalidUserException(Constants.USER_NOT_EXISTS);
            }
            return LoginResponseDto.builder()
                    .userId(userDetails.getUserId())
                    .userEmail(userDetails.getUserEmail())
                    .userMobile(userDetails.getUserMobile())
                    .userName(userDetails.getUserName())
                    .build();
        } catch (InvalidUserException e) {
            throw new InvalidUserException(e.getMessage());
        }
    }

    private void isMobileExist(Long mobile) {
        if (userRepository.existsUserDetailsByUserMobile(mobile)){
            throw new AlreadyExistsException(Constants.MOBILE_NUMBER_EXISTS);
        }
    }

    private void isEmailExist(String email) {
        if (userRepository.existsUserDetailsByUserEmail(email)){
            throw new AlreadyExistsException(Constants.EMAIL_ID_EXISTS);
        }
    }
    private UserDetails getAuthentication(LoginDto loginDto) {
        if (loginDto.getIsMobile()) {
            Long mobile = Long.parseLong(loginDto.getUserName());
            UserDetails userDetails = userRepository.getUserDetailsByMobile(mobile);
            if (userDetails.getUserMobile().equals(mobile) && userDetails.getUserPassword().equals(loginDto.getPassword())) {
                return userDetails;
            } else {
                throw new InvalidUserException(Constants.INVALID_CREDENTIALS);
            }
        } else {
            UserDetails userDetails = userRepository.getUserDetailsByEmail(loginDto.getUserName());
            if (userDetails.getUserEmail().equals(loginDto.getUserName()) && userDetails.getUserPassword().equals(loginDto.getPassword())) {
                return userDetails;
            } else {
                throw new InvalidUserException(Constants.INVALID_CREDENTIALS);
            }
        }
    }
}
