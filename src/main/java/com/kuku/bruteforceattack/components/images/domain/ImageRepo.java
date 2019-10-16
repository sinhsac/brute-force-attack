package com.kuku.bruteforceattack.components.images.domain;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ImageRepo extends JpaRepository<Image, Image.Key>, JpaSpecificationExecutor<Image> {
    class Spec {

        public static Specification<Image> hasInFileNames(List<String> fileNames) {
            return (root, query, cb) -> root.get("fileName").in(fileNames);
        }
    }
}
