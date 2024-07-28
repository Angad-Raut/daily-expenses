package com.projectx.daily_expenses.repositories;

import com.projectx.daily_expenses.entities.UserProfileDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserProfileRepository extends JpaRepository<UserProfileDetails,Long> {
    @Query(value = "select * from user_profile where id=:id",nativeQuery = true)
    UserProfileDetails getById(@Param("id")Long id);

    @Query(value = "select * from user_profile where user_id=:userId",nativeQuery = true)
    UserProfileDetails getByUserId(@Param("userId")Long userId);

    Boolean existsUserProfileDetailsByPanNumber(String pan);
    Boolean existsUserProfileDetailsByAadharNumber(String aadhar);
}
