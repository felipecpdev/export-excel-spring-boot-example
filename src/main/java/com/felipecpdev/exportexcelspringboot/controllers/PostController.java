package com.felipecpdev.exportexcelspringboot.controllers;

import com.felipecpdev.exportexcelspringboot.services.ExcelService;

import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/posts")
public class PostController {
    private final ExcelService excelService;

    public PostController(ExcelService excelService) {
        this.excelService = excelService;
    }

    @GetMapping(path = "/export-excel-posts", produces = {MediaType.APPLICATION_OCTET_STREAM_VALUE})
    public ResponseEntity<Resource> exportExcelPosts() {
        return excelService.exportExcelPosts();
    }

}
