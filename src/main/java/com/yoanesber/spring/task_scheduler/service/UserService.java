package com.yoanesber.spring.task_scheduler.service;

public interface UserService {

    // Method to set the account expiration date because the user has not logged in within the specified time
    void setAccountExpirationDate();

    // Method to change isAccountNonExpired to false based on the account expiration date
    void setAccountsToExpired();

    // Method to change isCredentialsNonExpired to false based on the credentials expiration date
    // The CredentialsNonExpired field is used to determine whether the user's credentials (password) have expired  
    // The credentials expiration date is set when the user is created (forcing a password change), and it can be updated by the user regularly
    void setCredentialsToExpired();

    // Method to send an email to users whose accounts have expired (isAccountNonExpired = false) to notify them that their accounts have expired
    void sendEmailToUsersWithExpiredAccounts();

    // Method to send an email to users whose credentials have expired (isCredentialsNonExpired = false) to notify them that their credentials have expired
    void sendEmailToUsersWithExpiredCredentials();

    // Method to clean up users whose accounts have expired and are older than the specified number of days 
    void cleanUpExpiredAccounts();
    
    // Method to clean up users whose credentials have expired and are older than the specified number of days
    void cleanUpExpiredCredentials();
}
