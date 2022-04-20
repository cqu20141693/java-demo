package com.gow.runner;

import com.gow.cache.service.BookRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * @author gow
 * @date 2021/7/3 0003
 */
@Component
public class AppRunner implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(AppRunner.class);

    private final BookRepository bookRepository;

    public AppRunner(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        logger.info(".... Fetching books");
        logger.info("isbn-1234 -->" + bookRepository.getByIsbn("isbn-1234"));
        logger.info("isbn-4567 -->" + bookRepository.getByIsbn("isbn-4567"));
        logger.info("isbn-7890 -->" + bookRepository.getByIsbn("isbn-7890"));
        logger.info("isbn-7890 -->" + bookRepository.getByIsbn("isbn-7890"));
        logger.info("start to test cacheTwo");
        logger.info("isbn-1234 -->" + bookRepository.getBooks("isbn-1234"));
        logger.info("isbn-4567 -->" + bookRepository.getBooks("isbn-4567"));
        logger.info("isbn-7890 -->" + bookRepository.getBooks("isbn-7890"));
        logger.info("isbn-7890 -->" + bookRepository.getBooks("isbn-7890"));
    }

}
