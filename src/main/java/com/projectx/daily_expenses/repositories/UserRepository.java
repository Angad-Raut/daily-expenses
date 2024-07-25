package com.projectx.daily_expenses.repositories;

import com.projectx.daily_expenses.entities.UserDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface UserRepository extends JpaRepository<UserDetails,Long> {
    @Query(value = "select * from user_details where user_id=:userId",nativeQuery = true)
    UserDetails getUserDetailsById(@Param("userId")Long userId);
    @Query(value = "select * from user_details where user_mobile=:mobile",nativeQuery = true)
    UserDetails getUserDetailsByMobile(@Param("mobile")Long mobile);
    @Query(value = "select * from user_details where user_email=:email",nativeQuery = true)
    UserDetails getUserDetailsByEmail(@Param("email")String email);
    Boolean existsUserDetailsByUserMobile(Long mobile);
    Boolean existsUserDetailsByUserEmail(String email);
    @Modifying
    @Transactional
    @Query(value = "update user_details set user_mobile=:mobile and user_email=:email where user_id=:userId",nativeQuery = true)
    Integer updateMobileAndEmail(@Param("userId")Long userId,@Param("mobile")Long mobile,@Param("email")String email);

    @Modifying
    @Transactional
    @Query(value = "update user_details set user_mobile=:mobile where user_id=:userId",nativeQuery = true)
    Integer updateMobile(@Param("userId")Long userId,@Param("mobile")Long mobile);

    @Modifying
    @Transactional
    @Query(value = "update user_details set user_email=:email where user_id=:userId",nativeQuery = true)
    Integer updateEmail(@Param("userId")Long userId,@Param("email")String email);
    @Transactional
    @Modifying
    @Query(value = "update user_details set user_password=:password where user_id=:userId",nativeQuery = true)
    Integer updatePassword(@Param("userId")Long userId,@Param("password")String password);
    @Query(value = "select user_password from user_details where user_id=:userId",nativeQuery = true)
    String getPassword(@Param("userId")Long userId);
}
