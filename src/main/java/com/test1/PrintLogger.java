package com.test1;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@Service
public class PrintLogger {
    private static Logger logger = LoggerFactory.getLogger(PrintLogger.class);

    @PostConstruct
    public void printLogger() throws Exception{
        Executors.newSingleThreadExecutor().submit(() -> {
            while (true) {
                logger.info("我是info级别日志,test1");
                logger.error("我是error级别日志,test1");
                logger.warn("我是warn级别日志,test1");
                logger.debug("我是debug级别日志,test1");
                TimeUnit.SECONDS.sleep(5);
            }
        });
    }
}
