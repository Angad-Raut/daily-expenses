package com.projectx.daily_expenses.services;

import com.projectx.daily_expenses.commons.*;
import com.projectx.daily_expenses.dtos.*;
import com.projectx.daily_expenses.entities.UserDetails;
import com.projectx.daily_expenses.entities.UserProfileDetails;
import com.projectx.daily_expenses.repositories.UserProfileRepository;
import com.projectx.daily_expenses.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.text.ParseException;
import java.util.Date;

@Component
public class UserServiceImpl implements UserService{

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserProfileRepository userProfileRepository;

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

    @Override
    public Boolean changePassword(ChangePasswordDto dto) throws ResourceNotFoundException, InvalidDataException {
        try {
            UserDetails details = userRepository.getUserDetailsById(dto.getUserId());
            validatePassword(details,dto);
            return userRepository.updatePassword(dto.getUserId(),dto.getNewPassword())==1?true:false;
        } catch (ResourceNotFoundException e) {
            throw new ResourceNotFoundException(e.getMessage());
        } catch (InvalidDataException e) {
            throw new InvalidDataException(e.getMessage());
        }
    }

    @Override
    public Boolean updateUserSetting(AccountSettingDto dto) throws ResourceNotFoundException, AlreadyExistsException {
        Boolean result = false;
        try {
             UserDetails details = userRepository.getUserDetailsById(dto.getUserId());
             if (details==null) {
                 throw new ResourceNotFoundException(Constants.USER_NOT_EXISTS);
             }
             if (dto.getUserMobile()!=null) {
                 isMobileExist(dto.getUserMobile());
                 details.setUserMobile(dto.getUserMobile());
                 result = true;
             }
             if (dto.getUserEmail()!=null) {
                 isEmailExist(dto.getUserEmail());
                 details.setUserEmail(dto.getUserEmail());
                 result = true;
             }
             if (dto.getSignature().getName()!=null) {
                 details.setSignature(dto.getSignature().getBytes());
                 result = true;
             }
             if (dto.getPhoto().getName()!=null) {
                 details.setPhoto(dto.getPhoto().getBytes());
                 result = true;
             }
             if (result) {
                 result = userRepository.save(details)!=null?true:false;
             }
        } catch (ResourceNotFoundException e) {
            throw new ResourceNotFoundException(e.getMessage());
        } catch (AlreadyExistsException e) {
            throw new ResourceNotFoundException(e.getMessage());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return result;
    }

    @Override
    public UserProfileDetails addUpdateUserProfile(UserProfileDto dto) throws ResourceNotFoundException, AlreadyExistsException {
        try {
            UserProfileDetails profileDetails = null;
            UserDetails details = userRepository.getUserDetailsById(dto.getUserId());
            if (details==null) {
                throw new ResourceNotFoundException(Constants.USER_NOT_EXISTS);
            }
            if (dto.getId()==null) {
                isPanExist(dto.getPanNumber());
                isAadharExist(dto.getAadharNumber());
                profileDetails = UserProfileDetails.builder()
                        .userId(details.getUserId())
                        .qualification(dto.getQualification())
                        .profession(dto.getProfession())
                        .gender(dto.getGender())
                        .dateOfBirth(Constants.getISODate(dto.getDateOfBirth()))
                        .bloodGroup(dto.getBloodGroup())
                        .panNumber(dto.getPanNumber())
                        .aadharNumber(dto.getAadharNumber())
                        .street(dto.getStreet())
                        .city(dto.getCity())
                        .state(dto.getState())
                        .country(dto.getCountry())
                        .pinCode(dto.getPinCode())
                        .build();
            } else {
                profileDetails = userProfileRepository.getById(dto.getId());
                if (profileDetails==null) {
                    throw new ResourceNotFoundException(Constants.USER_PROFILE_NOT_EXISTS);
                }
                if (!dto.getQualification().equals(profileDetails.getQualification())) {
                    profileDetails.setQualification(dto.getQualification());
                }
                if (!dto.getProfession().equals(profileDetails.getProfession())) {
                    profileDetails.setProfession(dto.getProfession());
                }
                if (!dto.getGender().equals(profileDetails.getGender())) {
                    profileDetails.setGender(dto.getGender());
                }
                if (!dto.getBloodGroup().equals(profileDetails.getBloodGroup())) {
                    profileDetails.setBloodGroup(dto.getBloodGroup());
                }
                if (!dto.getPanNumber().equals(profileDetails.getPanNumber())) {
                    isPanExist(dto.getPanNumber());
                    profileDetails.setPanNumber(dto.getPanNumber());
                }
                if (!dto.getAadharNumber().equals(profileDetails.getAadharNumber())) {
                    isAadharExist(dto.getAadharNumber());
                    profileDetails.setAadharNumber(dto.getAadharNumber());
                }
                if (!dto.getStreet().equals(profileDetails.getStreet())) {
                    profileDetails.setStreet(dto.getStreet());
                }
                if (!dto.getCity().equals(profileDetails.getCity())) {
                    profileDetails.setCity(dto.getCity());
                }
                if (!dto.getState().equals(profileDetails.getState())) {
                    profileDetails.setState(dto.getState());
                }
                if (!dto.getCountry().equals(profileDetails.getCountry())) {
                    profileDetails.setCountry(dto.getCountry());
                }
                if (!dto.getPinCode().equals(profileDetails.getPinCode())) {
                    profileDetails.setPinCode(dto.getPinCode());
                }
            }
            return userProfileRepository.save(profileDetails);
        } catch (ResourceNotFoundException e) {
            throw new ResourceNotFoundException(e.getMessage());
        } catch (AlreadyExistsException e) {
            throw new AlreadyExistsException(e.getMessage());
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public ViewUserProfileDto getUserProfileDetails(EntityIdDto dto) throws ResourceNotFoundException {
        try {
            UserProfileDetails details = userProfileRepository.getById(dto.getEntityId());
            if (details==null) {
                throw new ResourceNotFoundException(Constants.USER_PROFILE_NOT_EXISTS);
            }
            return ViewUserProfileDto.builder()
                    .id(details.getId())
                    .userId(details.getUserId())
                    .qualification(details.getQualification())
                    .profession(details.getProfession())
                    .gender(details.getGender())
                    .dateOfBirth(Constants.toExpenseDate(details.getDateOfBirth()))
                    .bloodGroup(details.getBloodGroup())
                    .panNumber(details.getPanNumber())
                    .aadharNumber(details.getAadharNumber())
                    .street(details.getStreet())
                    .city(details.getCity())
                    .state(details.getState())
                    .country(details.getCountry())
                    .pinCode(details.getPinCode())
                    .build();
        } catch (ResourceNotFoundException e) {
            throw new ResourceNotFoundException(e.getMessage());
        }
    }

    private void validatePassword(UserDetails details,ChangePasswordDto dto) {
        if (details==null) {
            throw new ResourceNotFoundException(Constants.USER_NOT_EXISTS);
        }
        if (dto.getOldPassword().equals(dto.getNewPassword())) {
            throw new InvalidDataException(Constants.NEW_PASSWORD_SAME);
        }
        if (!dto.getOldPassword().equals(details.getUserPassword())) {
            throw new InvalidDataException(Constants.OLD_PASSWORD_NOT_MATCH);
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
    private void isPanExist(String pan) {
        if (userProfileRepository.existsUserProfileDetailsByPanNumber(pan)){
            throw new AlreadyExistsException(Constants.PAN_NUMBER_ALREADY_EXISTS);
        }
    }
    private void isAadharExist(String aadhar) {
        if (userProfileRepository.existsUserProfileDetailsByAadharNumber(aadhar)){
            throw new AlreadyExistsException(Constants.AADHAR_NUMBER_ALREADY_EXISTS);
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
