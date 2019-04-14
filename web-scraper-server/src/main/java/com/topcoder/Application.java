package com.topcoder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan("com.topcoder.common.util") //Hack: workaround for unable to component-scan "com.topcoder.common.util.SpringTool"
public class Application {

  private static Logger logger = LoggerFactory.getLogger(Application.class.getName());

  public static void main(String[] args) {

    boolean isRestMode = false;
    for (String arg : args) {
      logger.info("arg = " + arg);
      if ("--rest".equalsIgnoreCase(arg)) {
        isRestMode = true;
      }
    }
    new SpringApplicationBuilder(Application.class).web(isRestMode).run(args);


  }

}
