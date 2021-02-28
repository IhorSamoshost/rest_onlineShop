package com.cursor.onlineshop.dtos;

import lombok.AllArgsConstructor;
import org.springframework.core.io.Resource;

@AllArgsConstructor
public class GetFileDto {
    private Resource file;
    private String fileName;
}
