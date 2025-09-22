package com.github.gomestkd.startup.repository;

import com.github.gomestkd.startup.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<Book, Long> {
}
