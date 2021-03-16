package com.cursor.onlineshop.repositories;

import com.cursor.onlineshop.entities.FileData;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FileRepo extends JpaRepository<FileData, String> {
}
