package com.kuku.bruteforceattack.infra.config.job;

import com.kuku.bruteforceattack.utils.XUtils;
import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.time.LocalDate;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

@Component
public class ProcessImagesSchedule {
    private static Logger log = LogManager.getLogger(ProcessImagesSchedule.class);
    public static final String baseUrl = "https://greenupload.com/images/";
    private static boolean flag = true;

    @Qualifier("getAsyncExecutor")
    @Autowired
    ThreadPoolTaskExecutor executor;

    @Scheduled(initialDelay = 5000, fixedDelay = 1 * 60000)
    public void run() {
        if (!flag) {
            log.debug("job is running");
            return;
        }
        flag = false;
        try {
            LocalDate localDate = LocalDate.of(2019, 1, 17);
            LocalDate endWhen = LocalDate.now();
            AtomicInteger totalFound = new AtomicInteger();
            AtomicLong total = new AtomicLong();
            while (localDate.isBefore(endWhen)) {
                for (int i = 0; i < 10000; i++) {
                    LocalDate finalLocalDate = localDate;
                    int finalI = i;
                    executor.execute(() -> {
                        String fileName = String.format("%04d", new Object[] { Integer.valueOf(finalI) });
                        String imagePath = String.format("%s/%02d/%02d/IMG_%s", new Object[] { Integer.valueOf(finalLocalDate.getYear()), Integer.valueOf(finalLocalDate.getMonthValue()), Integer.valueOf(finalLocalDate.getDayOfMonth()), fileName });
                        String pathFileThumb = imagePath + ".th.jpg";
                        String pathFile = imagePath + ".jpg";
                        String thumbUrl = "https://greenupload.com/images/" + pathFileThumb;
                        String url = "https://greenupload.com/images/" + pathFile;
                        if (XUtils.isOk(thumbUrl)) {
                            try {
                                FileUtils.writeStringToFile(new File("./urls.txt"), url + "\n", Charset.forName("utf-8").name());
                                totalFound.getAndIncrement();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                        total.getAndIncrement();
                        if (total.get() % 1000L == 0L) {
                            System.out.println("processed " + total + " - date : " + finalLocalDate + " index : " + finalI + " - found : " + totalFound);
                        }
                    });
                }
                localDate = localDate.plusDays(1L);
            }
        } catch (Exception ex) {
            log.error("has error ", ex);
        } finally {
            flag = true;
        }
    }
}
