package com.kuku.bruteforceattack.infra.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.lang.management.ManagementFactory;
import java.lang.management.MemoryPoolMXBean;
import java.lang.management.MemoryType;
import java.lang.management.MemoryUsage;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/healthcheck")
public class HealthCheck {
    private static final DecimalFormat formatter = new DecimalFormat("#,###");

    @Autowired
    JdbcTemplate jdbc;

    @GetMapping
    public Map healthCheck(){
        try {
            sqlCheck();

            // Returns memory data
            Map<String, Object> output = new HashMap<>();
            memoryCheck(output);

            return output;
        } catch (Throwable e) {
            e.printStackTrace(); // to be pick up by marathon too.
            throw e;
        }
    }

    private void memoryCheck( Map<String, Object> output) {
        long memoryUsed = 0;
        long memoryMax = 0;
        long committed = 0;
        long init = 0;

        for (MemoryPoolMXBean pool : ManagementFactory.getMemoryPoolMXBeans()) {
            if (pool.getType() == MemoryType.HEAP) {
                MemoryUsage usage = pool.getCollectionUsage();
                memoryUsed += usage.getUsed();
                memoryMax += usage.getMax();
                committed += usage.getCommitted();
                init += usage.getInit();
            }
        }
        output.put("usedMemory", Math.round(memoryUsed / 1e6));
        output.put("maxMemory", Math.round(memoryMax / 1e6));
        output.put("committedMemofy", Math.round(committed / 1e6));
        output.put("initMemofy", Math.round(init / 1e6));

    }

    private void sqlCheck() {
        jdbc.execute("select 1");
    }
}