package com.kuku.bruteforceattack.utils;

import com.kuku.bruteforceattack.infra.config.job.ProcessImagesSchedule;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

public class XUtils {
    private static Logger log = LogManager.getLogger(XUtils.class);
    public static int getResponseCode(String urlString) {
        try {
            HttpURLConnection huc = initRequestInfo(urlString);
            huc.connect();
            return huc.getResponseCode();
        } catch (Exception ignore) {
            return -1;
        }
    }

    public static boolean isOk(String urlString) {
        try {
            HttpURLConnection huc = initRequestInfo(urlString);
            huc.connect();
            return (huc.getResponseCode() == 200);
        } catch (Exception ex) {
            log.error("has error when check status", ex);
            return false;
        }
    }

    private static HttpURLConnection initRequestInfo(String urlString) throws IOException {
        URL u = new URL(urlString);
        HttpURLConnection huc = (HttpURLConnection) u.openConnection();
        huc.setRequestMethod("HEAD");
        huc.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows; U; Windows NT 6.0; en-US; rv:1.9.1.2) Gecko/20090729 Firefox/3.5.2 (.NET CLR 3.5.30729)");
        return huc;
    }
}