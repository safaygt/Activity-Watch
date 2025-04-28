package com.smartict.activitywatch.service;

import com.smartict.activitywatch.dto.UsrActivityDTO;
import com.smartict.activitywatch.entity.Activity;
import com.smartict.activitywatch.entity.Usr;
import com.smartict.activitywatch.entity.UsrActivity;
import com.smartict.activitywatch.repository.ActivityRepository;
import com.smartict.activitywatch.repository.UsrActivityRepository;
import com.smartict.activitywatch.repository.UsrRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MainService {

    private final UsrRepository usrRepository;
    private final ActivityRepository activityRepository;
    private final UsrActivityRepository usrActivityRepository;

    public void logActivity(UsrActivityDTO dto) throws Throwable {
        Optional<Usr> userOpt = usrRepository.findById(dto.getFkUserId());
        Optional<Activity> activityOpt = activityRepository.findById(dto.getFkActivityId());

        if (userOpt.isEmpty() || activityOpt.isEmpty()) {
            throw new RuntimeException("User or Activity not found.");
        }

        UsrActivity entity = new UsrActivity();
        entity.setUsr(userOpt.get());
        entity.setActivity(activityOpt.get());

        // date'i DTO'dan alacaksan bunu yaz, yoksa otomatik olarak @CreationTimestamp zaten set eder
        if (dto.getDate() != null) {
            entity.setDate(dto.getDate());
        }

        entity.setAfk(dto.isAfk());

        usrActivityRepository.save(entity);
    }


}
