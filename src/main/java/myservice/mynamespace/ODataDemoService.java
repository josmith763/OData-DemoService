package myservice.mynamespace;

import myservice.mynamespace.web.DemoServlet;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class ODataDemoService {

    public static void main(String[] args) {
        SpringApplication.run(ODataDemoService.class, args);
    }

    @Bean
    public ServletRegistrationBean servletRegistrationBean() {
        ServletRegistrationBean bean = new ServletRegistrationBean(
                new DemoServlet(), "/demoservice/DemoService.svc/*");
        return bean;
    }

}