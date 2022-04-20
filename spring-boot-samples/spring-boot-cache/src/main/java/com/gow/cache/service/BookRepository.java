package com.gow.cache.service;

import com.gow.cache.model.Book;
import java.util.List;

/**
 * @author gow
 * @date 2021/7/3 0003
 */
public interface BookRepository {
    Book getByIsbn(String isbn);

    List<Book> getBooks(String isbn);
}
