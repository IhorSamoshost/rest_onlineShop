package com.cursor.onlineshop.controllers;

import com.cursor.onlineshop.dtos.GetFileDto;
import com.cursor.onlineshop.entities.FileData;
import com.cursor.onlineshop.services.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.net.URI;

@RestController
@RequestMapping("/media")
@RequiredArgsConstructor
public class FileController {

    private final FileService fileService;

    @PostMapping()
    public ResponseEntity<Void> create(@RequestParam("file") MultipartFile file) throws Exception {
        final FileData newFileData = fileService.createFile(file);
        return ResponseEntity.created(
                URI.create("media/" + newFileData.getFileId())
        ).build();
    }

    @GetMapping(value = "/{mediaId}")
    public ResponseEntity<GetFileDto> getFile(@PathVariable("mediaId") String mediaId) throws FileNotFoundException {
        return ResponseEntity.ok(fileService.getFile(mediaId));
    }

    @DeleteMapping(value = "/{mediaId}")
    public ResponseEntity<Resource> deleteFile(@PathVariable("mediaId") String mediaId) throws FileNotFoundException {
        fileService.deleteFile(mediaId);
        return ResponseEntity.ok().build();
    }
}
