package com.test;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@Service
public class PrintLoggerTest {
    private static Logger logger = LoggerFactory.getLogger(PrintLoggerTest.class);

    @PostConstruct
    public void printLogger() throws Exception{
        Executors.newSingleThreadExecutor().submit(() -> {
            while (true) {
                logger.info("我是info级别日志,test");
                logger.error("我是error级别日志,test");
                logger.warn("我是warn级别日志,test");
                logger.debug("我是debug级别日志,test");
                TimeUnit.SECONDS.sleep(5);
            }
        });
    }
}
