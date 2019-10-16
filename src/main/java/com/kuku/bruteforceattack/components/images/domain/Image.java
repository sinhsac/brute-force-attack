package com.kuku.bruteforceattack.components.images.domain;

import com.kuku.bruteforceattack.components.host.domain.HostInfo;
import org.apache.catalina.Host;
import org.apache.commons.lang3.StringUtils;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Date;
import java.util.Objects;

@Entity
@IdClass(Image.Key.class)
@Table(name = "images")
public class Image {
    @Id
    @Column(name = "domain")
    public String domain;

    @Id
    @Column(name = "file_name")
    public String fileName;

    @Column(name = "thumbnail")
    public String thumbnail;

    @Column(name = "file_url")
    public String fileUrl;

    @Column(name = "created_at")
    public Date createdAt;

    @Transient
    public String imagePath;

    @Transient
    private String pathFileThumb;

    public Image() {}

    @Override
    public int hashCode() {
        return Objects.hashCode(fileName);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }

        if (obj instanceof Image) {
            String tmpFileName = ((Image) obj).fileName;
            String tmpDomain = ((Image) obj).domain;
            if (StringUtils.isAnyBlank(tmpDomain, tmpFileName)) {
                return false;
            }

            return tmpFileName.equals(fileName) && tmpDomain.equals(domain);
        }

        return false;
    }

    public static Image fromDateWithIndex(LocalDate localDate, HostInfo hostInfo, Integer index) {
        if (localDate == null || StringUtils.isBlank(hostInfo.baseUrl) || index == null || index < 1) {
            return null;
        }
        Image image = new Image();
        image.domain = hostInfo.domain;
        image.fileName = String.format("%04d", new Object[] { Integer.valueOf(index)});
        image.imagePath = String.format("%s/%02d/%02d/IMG_%s", new Object[]{Integer.valueOf(localDate.getYear()),
                Integer.valueOf(localDate.getMonthValue()),
                Integer.valueOf(localDate.getDayOfMonth()),
                image.fileName});
        image.pathFileThumb = image.imagePath + ".th.jpg";
        image.fileName = image.imagePath + ".jpg";
        image.thumbnail = hostInfo.baseUrl + image.pathFileThumb;
        image.fileUrl = hostInfo.baseUrl + image.fileName;
        if (StringUtils.isNotBlank(image.fileName)) {
            return image;
        }
        return null;
    }

    public Key getKey() {
        return new Key(domain, fileName);
    }

    public static class Key implements Serializable {
        public String domain;
        public String fileName;

        public Key() {}

        public Key(String domain, String fileName) {
            this.domain = domain;
            this.fileName = fileName;
        }
    }
}
