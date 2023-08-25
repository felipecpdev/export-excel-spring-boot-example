package com.felipecpdev.exportexcelspringboot.services;

import com.felipecpdev.exportexcelspringboot.controllers.PostController;
import com.felipecpdev.exportexcelspringboot.entity.Post;
import com.felipecpdev.exportexcelspringboot.xls.XlsBuilder;
import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Service
public class ExcelService {
    private static final Logger logger = LoggerFactory.getLogger(PostController.class);
    private final XlsBuilder xlsBuilder;
    private final PostService postService;

    public ExcelService(XlsBuilder xlsBuilder, PostService postService) {
        this.xlsBuilder = xlsBuilder;
        this.postService = postService;
    }

    public ResponseEntity<Resource> exportExcelPosts() {
        try {
            String folderPath = xlsBuilder.folderPath();
            String fileName = xlsBuilder.buildFilename("posts");
            String pathTemplate = folderPath + fileName;

            HttpHeaders httpHeaders = xlsBuilder.getHttpHeaders(fileName);

            List<Post> postList = postService.findAll();
            List<String> postHeaders = List.of("Id", "Titulo", "Autor", "Contenido", "Fecha de Creación");

            Workbook workbook;
            workbook = xlsBuilder.buildExcelPost(postHeaders, postList);

            FileOutputStream fileOut = new FileOutputStream(pathTemplate);
            workbook.write(fileOut);
            workbook.close();
            fileOut.close();

            Path path = Paths.get(pathTemplate);
            ByteArrayResource resource = new ByteArrayResource(Files.readAllBytes(path));
            logger.info("{Excel exportado} -> " + fileName);
            return ResponseEntity.ok().headers(httpHeaders).contentLength(new File(pathTemplate).length())
                    .contentType(MediaType
                            .parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                    .body(resource);
        } catch (Exception e) {
            logger.error("{0}", e);
            return null;
        }
    }
}
