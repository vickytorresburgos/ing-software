package edu.um.umbook.service;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
@Service
public class BirthdaySchedulerService {
    
    @Scheduled(cron = "0 0 8 * * ?") // Every day at 8 AM
    public void notifyBirthdays() {
        // Logic to notify birthdays. For PoC, just print.
        System.out.println("Running daily birthday check (Simulated Email)...");
    }
}
