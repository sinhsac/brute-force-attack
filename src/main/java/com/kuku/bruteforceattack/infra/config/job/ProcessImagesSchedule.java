package com.kuku.bruteforceattack.infra.config.job;

import com.kuku.bruteforceattack.components.host.domain.HostInfo;
import com.kuku.bruteforceattack.components.images.domain.Image;
import com.kuku.bruteforceattack.components.images.domain.ImageRepo;
import com.kuku.bruteforceattack.utils.XUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class ProcessImagesSchedule {
    private static Logger log = LogManager.getLogger(ProcessImagesSchedule.class);
    private static boolean flag = true;

    @Qualifier("getAsyncExecutor")
    @Autowired
    ThreadPoolTaskExecutor executor;

    @Autowired
    ImageRepo imageRepo;

    @Scheduled(initialDelay = 5000, fixedDelay = 60 * 60000)
    public void run() {
        if (!flag) {
            log.debug("job is running");
            return;
        }
        flag = false;
        try {
            log.debug("job starting");
            List<HostInfo> hostInfos = Arrays.asList(
                    new HostInfo("greenupload.com", "https://greenupload.com/images/"),
                    new HostInfo("anh4.com", "https://anh4.com/images/")
            );

            LocalDate localDate = LocalDate.of(2019, 4, 17);
            LocalDate endWhen = LocalDate.now();
            for (HostInfo hostInfo : hostInfos) {
                log.debug("process for {}", hostInfo.domain);
                processForOnlyHost(hostInfo, localDate, endWhen);
            }
        } catch (Exception ex) {
            log.error("has error ", ex);
        } finally {
            log.debug("job end");
            flag = true;
        }
    }

    private void processForOnlyHost(HostInfo hostInfo, LocalDate localDate, LocalDate endWhen) {
        while (localDate.isBefore(endWhen)) {
            List<Image> images = new ArrayList<>();
            for (int i = 0; i < 10000; i++) {
                Image image = Image.fromDateWithIndex(localDate, hostInfo, i);
                if (image != null) {
                    images.add(image);
                }
            }
            LocalDate tmp = localDate;
            localDate = localDate.plusDays(1L);
            if (images == null || images.isEmpty()) {
                continue;
            }
            List<Image.Key> keys = images.stream().map(img -> img.getKey()).collect(Collectors.toList());
            if (keys == null || keys.isEmpty()) {
                continue;
            }
            List<Image> imagesInMySql = imageRepo.findAllById(keys);
            if (imagesInMySql != null && !imagesInMySql.isEmpty()) {
                images.removeIf(img -> imagesInMySql.contains(img));
            }
            processForListImages(images, tmp);
        }
    }

    private void processForListImages(List<Image> images, LocalDate localDate) {
        log.debug("process for {} images on {}", images.size(), localDate);
        images.forEach(image -> {
            executor.execute(() -> {
                if (XUtils.isOk(image.thumbnail)) {
                    log.info("found {} image in host {}", image.fileName, image.domain);
                    imageRepo.save(image);
                }
            });
        });
    }
}
