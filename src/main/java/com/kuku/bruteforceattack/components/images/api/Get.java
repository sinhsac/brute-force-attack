package com.kuku.bruteforceattack.components.images.api;

import com.kuku.bruteforceattack.common.domain.PageInfo;
import com.kuku.bruteforceattack.components.images.domain.Image;
import com.kuku.bruteforceattack.components.images.domain.ImageRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController("getAllImage")
@RequestMapping("/api/v1/images")
public class Get {
    @Autowired
    ImageRepo imageRepo;

    @GetMapping
    public List<Image> getImages(@RequestParam(name = "page", defaultValue = "1") Integer page,
                                 @RequestParam(name = "size", defaultValue = "10") Integer size,
                                 @RequestParam(name = "q", defaultValue = "") String searchQuery) {
        PageInfo pageInfo = new PageInfo(page, size, searchQuery);
        Page<Image> images = imageRepo.findAll(pageInfo.toPageRequest(Sort.by("createdAt").descending()));
        return images.getContent();
    }
}
