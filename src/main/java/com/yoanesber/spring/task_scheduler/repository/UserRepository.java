package com.yoanesber.spring.task_scheduler.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.yoanesber.spring.task_scheduler.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    // Query to find users who have not logged in for a certain period of time
    @Query(value = """
    select
        id
    from
        users
    where
        last_login is not null
        and EXTRACT(DAY FROM (NOW() - last_login)) > :allowedInactiveDays
        and is_account_non_expired
        and lower(username) <> 'superadmin' 
    """, nativeQuery = true)
    List<Long> findUsersInactiveForDays(@Param("allowedInactiveDays") int allowedInactiveDays);

    // Query to find users whose account expiration date is less than the current date
    @Query(value = """
    select
        id
    from
        users
    where
        is_account_non_expired
        and account_expiration_date is not null
        and account_expiration_date < NOW()
        and lower(username) <> 'superadmin' 
    """, nativeQuery = true)
    List<Long> findAccountExpirationDateLessThanNow();

    // Query to find users whose credentials expiration date is less than the current date
    @Query(value = """
    select
        id
    from
        users
    where
        is_credentials_non_expired
        and credentials_expiration_date is not null
        and credentials_expiration_date < NOW()
        and lower(username) <> 'superadmin'
    """, nativeQuery = true)
    List<Long> findCredentialsExpirationDateLessThanNow();


    // Query to find users whose accounts have expired
    @Query(value = """
    select
        id
    from
        users
    where
        is_account_non_expired = false
        and lower(username) <> 'superadmin'
    """, nativeQuery = true)
    List<Long> findExpiredAccount();

    // Query to find users whose credentials have expired
    @Query(value = """
    select
        id
    from
        users
    where
        is_credentials_non_expired = false
        and lower(username) <> 'superadmin'
    """, nativeQuery = true)
    List<Long> findExpiredCredentials();

    // Query to find users whose accounts have expired and are older than the specified number of days
    @Query(value = """
    select
        id
    from
        users
    where
        is_account_non_expired = false
        and EXTRACT(DAY FROM (NOW() - account_expiration_date)) > :expiredAccountRetentionDays
        and lower(username) <> 'superadmin'
    """, nativeQuery = true)
    List<Long> findUsersWithExpiredAccountsOlderThan(@Param("expiredAccountRetentionDays") int expiredAccountRetentionDays);

    // Query to find users whose credentials have expired and are older than the specified number of days
    @Query(value = """
    select
        id
    from
        users
    where
        is_credentials_non_expired = false
        and EXTRACT(DAY FROM (NOW() - credentials_expiration_date)) > :expiredCredentialsRetentionDays
        and lower(username) <> 'superadmin'
    """, nativeQuery = true)
    List<Long> findUsersWithExpiredCredentialsOlderThan(@Param("expiredCredentialsRetentionDays") int expiredCredentialsRetentionDays);
}
