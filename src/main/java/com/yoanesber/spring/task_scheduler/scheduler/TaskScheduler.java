package com.yoanesber.spring.task_scheduler.scheduler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Component;

import com.yoanesber.spring.task_scheduler.service.UserService;

@Component
@SuppressWarnings("unused")
public class TaskScheduler {

    private final ThreadPoolTaskScheduler threadPoolTaskScheduler;

    private final UserService userService;
    
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    public TaskScheduler(@Qualifier("threadPoolTaskScheduler") ThreadPoolTaskScheduler threadPoolTaskScheduler,
        UserService userService) {
        this.threadPoolTaskScheduler = threadPoolTaskScheduler;
        this.userService = userService;
    }

    // Task to set the account expiration date because the user has not logged in within the specified time
    @Async("threadPoolTaskScheduler")
    @Scheduled(cron = "#{@environment.getProperty('scheduler.daily-task.set-account-expiration-date')}")
    public void setAccountExpirationDate() {
        logger.info("Cron task runs on thread " + Thread.currentThread().getName() + " for setAccountExpirationDate");
        try {
            userService.setAccountExpirationDate();
        } catch (Exception e) {
            logger.error("Exception on thread " + Thread.currentThread().getName() + " for setAccountExpirationDate with message: " + e.getMessage());
        }
        logger.info("Cron task on thread " + Thread.currentThread().getName() + " for setAccountExpirationDate has been completed");
    }

    // Task to change isAccountNonExpired to false based on the account expiration date
    @Async("threadPoolTaskScheduler")
    @Scheduled(cron = "#{@environment.getProperty('scheduler.daily-task.set-accounts-to-expired')}")
    public void setAccountsToExpired() {
        logger.info("Cron task runs on thread " + Thread.currentThread().getName() + " for setAccountsToExpired");
        try {
            userService.setAccountsToExpired();
        } catch (Exception e) {
            logger.error("Exception on thread " + Thread.currentThread().getName() + " for setAccountsToExpired with message: " + e.getMessage());
        }
        logger.info("Cron task on thread " + Thread.currentThread().getName() + " for setAccountsToExpired has been completed");
    }

    // Task to change isCredentialsNonExpired to false based on the credentials expiration date
    @Async("threadPoolTaskScheduler")
    @Scheduled(cron = "#{@environment.getProperty('scheduler.daily-task.set-credentials-to-expired')}")
    public void setCredentialsToExpired() {
        logger.info("Cron task runs on thread " + Thread.currentThread().getName() + " for setCredentialsToExpired");
        try {
            userService.setCredentialsToExpired();
        } catch (Exception e) {
            logger.error("Exception on thread " + Thread.currentThread().getName() + " for setCredentialsToExpired with message: " + e.getMessage());
        }
        logger.info("Cron task on thread " + Thread.currentThread().getName() + " for setCredentialsToExpired has been completed");
    }

    // Task to send an email to users whose accounts have expired (isAccountNonExpired = false) to notify them that their accounts have expired
    @Async("threadPoolTaskScheduler")
    @Scheduled(cron = "#{@environment.getProperty('scheduler.weekly-task.send-email-to-users-with-expired-accounts')}")
    public void sendEmailToUsersWithExpiredAccounts() {
        logger.info("Cron task runs on thread " + Thread.currentThread().getName() + " for sendEmailToUsersWithExpiredAccounts");
        try {
            userService.sendEmailToUsersWithExpiredAccounts();
        } catch (Exception e) {
            logger.error("Exception on thread " + Thread.currentThread().getName() + " for sendEmailToUsersWithExpiredAccounts with message: " + e.getMessage());
        }
        logger.info("Cron task on thread " + Thread.currentThread().getName() + " for sendEmailToUsersWithExpiredAccounts has been completed");
    }

    // Task to send an email to users whose credentials have expired (isCredentialsNonExpired = false) to notify them that their credentials have expired
    @Async("threadPoolTaskScheduler")
    @Scheduled(cron = "#{@environment.getProperty('scheduler.weekly-task.send-email-to-users-with-expired-credentials')}")
    public void sendEmailToUsersWithExpiredCredentials() {
        logger.info("Cron task runs on thread " + Thread.currentThread().getName() + " for sendEmailToUsersWithExpiredCredentials");
        try {
            userService.sendEmailToUsersWithExpiredCredentials();
        } catch (Exception e) {
            logger.error("Exception on thread " + Thread.currentThread().getName() + " for sendEmailToUsersWithExpiredCredentials with message: " + e.getMessage());  
        }
        logger.info("Cron task on thread " + Thread.currentThread().getName() + " for sendEmailToUsersWithExpiredCredentials has been completed");
    }

    // Task to clean up users whose accounts have expired and are older than the specified number of days
    @Async("threadPoolTaskScheduler")
    @Scheduled(cron = "#{@environment.getProperty('scheduler.monthly-task.cleanup-expired-accounts')}")
    public void cleanUpExpiredAccounts() {
        logger.info("Cron task runs on thread " + Thread.currentThread().getName() + " for cleanUpExpiredAccounts");
        try {
            userService.cleanUpExpiredAccounts();
        } catch (Exception e) {
            logger.error("Exception on thread " + Thread.currentThread().getName() + " for cleanUpExpiredAccounts with message: " + e.getMessage());
        }
        logger.info("Cron task on thread " + Thread.currentThread().getName() + " for cleanUpExpiredAccounts has been completed");
    }

    // Task to clean up users whose credentials have expired and are older than the specified number of days
    @Async("threadPoolTaskScheduler")
    @Scheduled(cron = "#{@environment.getProperty('scheduler.monthly-task.cleanup-expired-credentials')}")
    public void cleanUpExpiredCredentials() {
        logger.info("Cron task runs on thread " + Thread.currentThread().getName() + " for cleanUpExpiredCredentials");
        try {
            userService.cleanUpExpiredCredentials();
        } catch (Exception e) {
            logger.error("Exception on thread " + Thread.currentThread().getName() + " for cleanUpExpiredCredentials with message: " + e.getMessage());
        }
        logger.info("Cron task on thread " + Thread.currentThread().getName() + " for cleanUpExpiredCredentials has been completed");
    }
}