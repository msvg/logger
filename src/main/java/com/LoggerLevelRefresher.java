package com;

import com.ctrip.framework.apollo.Config;
import com.ctrip.framework.apollo.model.ConfigChangeEvent;
import com.ctrip.framework.apollo.spring.annotation.ApolloConfig;
import com.ctrip.framework.apollo.spring.annotation.ApolloConfigChangeListener;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.logging.LoggingInitializationContext;
import org.springframework.boot.logging.LoggingSystem;
import org.springframework.cloud.context.environment.EnvironmentChangeEvent;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import javax.annotation.PostConstruct;
import java.util.Set;

@Service
public class LoggerLevelRefresher implements ApplicationContextAware, EnvironmentAware {

  private ApplicationContext applicationContext;
  private ConfigurableEnvironment environment;

  @Autowired
  private LoggingSystem loggingSystem;

  @ApolloConfig
  private Config config;

  @PostConstruct
  private void initialize() {
    refreshLoggingLevels(config.getPropertyNames());
  }

  @ApolloConfigChangeListener
  private void onChange(ConfigChangeEvent changeEvent) {
    refreshLoggingLevels(changeEvent.changedKeys());
  }

  @ApolloConfigChangeListener(interestedKeyPrefixes = {"log.test."})
  private void onChangeLog(ConfigChangeEvent changeEvent) {
    LoggingInitializationContext initializationContext = new LoggingInitializationContext(environment);
    String logConfig = environment.getProperty("logging.config");
    try{
      ResourceUtils.getURL(logConfig).openStream().close();
      loggingSystem.cleanUp();
      loggingSystem.initialize(initializationContext, logConfig, null);
    }catch (Exception e){
    }
  }

  private void refreshLoggingLevels(Set<String> changedKeys) {
    System.out.println("Refreshing logging levels");

    /**
     * refresh logging levels
     * @see org.springframework.cloud.logging.LoggingRebinder#onApplicationEvent
     */
    this.applicationContext.publishEvent(new EnvironmentChangeEvent(changedKeys));

    System.out.println("Logging levels refreshed");
  }

  @Override
  public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
    this.applicationContext = applicationContext;
  }

  @Override
  public void setEnvironment(Environment environment) {
    this.environment = (ConfigurableEnvironment) environment;
  }
}
