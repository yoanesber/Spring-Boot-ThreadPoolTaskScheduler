package com.yoanesber.spring.task_scheduler.service.impl;

import java.time.Instant;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yoanesber.spring.task_scheduler.entity.User;
import com.yoanesber.spring.task_scheduler.repository.UserRepository;
import com.yoanesber.spring.task_scheduler.service.UserService;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Value("${business.policy.allowed-inactive-days}")
    private int allowedInactiveDays;

    @Value("${business.policy.expired-account-retention-days}")
    private int expiredAccountRetentionDays;

    @Value("${business.policy.expired-credentials-retention-days}")
    private int expiredCredentialsRetentionDays;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public void setAccountExpirationDate() {
        try {
            // Find users who have not logged in (inactive) for a certain period of time
            userRepository.findUsersInactiveForDays(allowedInactiveDays).forEach(userId -> {
                User user = userRepository.findById(userId).orElse(null);

                // Set the account expiration date to the current date
                user.setAccountExpirationDate(Instant.now());
                user.setUpdatedBy("System");
                user.setUpdatedDate(Instant.now());
                userRepository.save(user);
            });
        } catch (Exception e) {
            throw new RuntimeException("Error setting account expiration date", e);
        }
    }

    @Override
    @Transactional
    public void setAccountsToExpired() {
        try {
            // Find users whose account expiration date is less than the current date
            userRepository.findAccountExpirationDateLessThanNow().forEach(userId -> {
                User user = userRepository.findById(userId).orElse(null);

                // Set the account to expire
                user.setAccountNonExpired(false);
                user.setUpdatedBy("System");
                user.setUpdatedDate(Instant.now());
                userRepository.save(user);
            });
        } catch (Exception e) {
            throw new RuntimeException("Error setting accounts to expire", e);
        }
    }

    @Override
    @Transactional
    public void setCredentialsToExpired() {
        try {
            // Find users whose credentials expiration date is less than the current date
            userRepository.findCredentialsExpirationDateLessThanNow().forEach(userId -> {
                User user = userRepository.findById(userId).orElse(null);

                // Set the credentials to expire
                user.setCredentialsNonExpired(false);
                user.setUpdatedBy("System");
                user.setUpdatedDate(Instant.now());
                userRepository.save(user);
            });
        } catch (Exception e) {
            throw new RuntimeException("Error setting credentials to expire", e);
        }
    }

    @Override
    @Transactional
    public void sendEmailToUsersWithExpiredAccounts() {
        try {
            // Find users whose accounts have expired
            userRepository.findExpiredAccount().forEach(userId -> {
                User user = userRepository.findById(userId).orElse(null);

                if (!user.getEmail().isEmpty()) {
                    // Send an email to the user
                    System.out.println("Sending email to user with expired account: " + user.getEmail());

                    // Process the email sending here ...
                    // You can use JavaMailSender or other email sending libraries
                    // Async email sending is recommended to avoid blocking the main thread
                }
            });
        } catch (Exception e) {
            throw new RuntimeException("Error sending email to users with expired accounts", e);
        }
    }

    @Override
    @Transactional
    public void sendEmailToUsersWithExpiredCredentials() {
        try {
            // Find users whose credentials have expired
            userRepository.findExpiredCredentials().forEach(userId -> {
                User user = userRepository.findById(userId).orElse(null);

                if (!user.getEmail().isEmpty()) {
                    // Send an email to the user
                    System.out.println("Sending email to user with expired credentials: " + user.getEmail());

                    // Process the email sending here ...
                    // You can use JavaMailSender or other email sending libraries
                    // Async email sending is recommended to avoid blocking the main thread
                }
            });
        } catch (Exception e) {
            throw new RuntimeException("Error sending email to users with expired credentials", e);
        }
    }

    @Override
    @Transactional
    public void cleanUpExpiredAccounts() {
        try {
            // Find users whose accounts have expired
            userRepository.findUsersWithExpiredAccountsOlderThan(expiredAccountRetentionDays).forEach(userId -> {
                User user = userRepository.findById(userId).orElse(null);

                // Archive the user records if needed before deleting ..

                // Delete the user
                user.setDeleted(true);
                user.setUpdatedBy("System");
                user.setUpdatedDate(Instant.now());
                userRepository.save(user);
            });
        } catch (Exception e) {
            throw new RuntimeException("Error cleaning up expired accounts", e);
        }
    }

    @Override
    @Transactional
    public void cleanUpExpiredCredentials() {
        try {
            // Find users whose credentials have expired
            userRepository.findUsersWithExpiredCredentialsOlderThan(expiredCredentialsRetentionDays).forEach(userId -> {
                User user = userRepository.findById(userId).orElse(null);

                // Archive the user records if needed before deleting ..

                // Delete the user
                user.setDeleted(true);
                user.setUpdatedBy("System");
                user.setUpdatedDate(Instant.now());
                userRepository.save(user);
            });
        } catch (Exception e) {
            throw new RuntimeException("Error cleaning up expired credentials", e);
        }
    }
}
