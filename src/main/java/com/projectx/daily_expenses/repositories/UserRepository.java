package com.projectx.daily_expenses.repositories;

import com.projectx.daily_expenses.entities.UserDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<UserDetails,Long> {
    @Query(value = "select * from user_details where user_mobile=:mobile",nativeQuery = true)
    UserDetails getUserDetailsByMobile(@Param("mobile")Long mobile);
    @Query(value = "select * from user_details where user_email=:email",nativeQuery = true)
    UserDetails getUserDetailsByEmail(@Param("email")String email);
    Boolean existsUserDetailsByUserMobile(Long mobile);
    Boolean existsUserDetailsByUserEmail(String email);
}
