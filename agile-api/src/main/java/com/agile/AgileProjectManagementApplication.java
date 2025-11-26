package com.agile;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Properties;

//import com.agile.storage.StorageProperties;
import com.agile.storage.StorageProperties;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.http.MediaType;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.client.RestTemplate;

//@ServletComponentScan
@ComponentScan({"com.agile.*"})
@EnableConfigurationProperties(StorageProperties.class)
@EnableScheduling
@SpringBootApplication
public class AgileProjectManagementApplication extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(AgileProjectManagementApplication.class, args);
    }

//    @Bean
//    CommandLineRunner init(StorageService storageService) {
//        return (args) -> {
//            storageService.deleteAll();
//            storageService.init();
//        };
//    }

//    @Bean
//    public RestTemplate getRestTemplate() {
//        RestTemplate restTemplate = new RestTemplate();
//        restTemplate.getMessageConverters().add(0, new StringHttpMessageConverter(StandardCharsets.UTF_8));
//        return restTemplate;
//    }

//    @Bean
//    public RestTemplate getRestTemplate() {
//        final RestTemplate restTemplate = new RestTemplate();
//        //restTemplate.getMessageConverters().add(0, new StringHttpMessageConverter(StandardCharsets.UTF_8));
//        restTemplate.getMessageConverters().add(jacksonSupportsMoreTypes());
//        return restTemplate;
//    }

//    private HttpMessageConverter jacksonSupportsMoreTypes() {//eg. Gitlab returns JSON as plain text
//        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
//        converter.setSupportedMediaTypes(Arrays.asList(MediaType.parseMediaType("text/plain;charset=utf-8"), MediaType.APPLICATION_OCTET_STREAM));
//        return converter;
//    }

    // set spring.config.location here if we want to deploy the application as a war
    // on tomcat

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder
                                                         springApplicationBuilder) {

        return springApplicationBuilder
                .sources(AgileProjectManagementApplication.class)
                .properties(getProperties());

    }

    static Properties getProperties() {

        Properties props = new Properties();

        props.put("spring.config.location", "file:////home/core/agile/");

        return props;

    }
    private static final int READ_TIMEOUT = 30000;
    private static final int CONNECTION_TIMEOUT = 30000;

    private HttpMessageConverter jacksonSupportsMoreTypes() {//eg. Gitlab returns JSON as plain text
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        converter.setSupportedMediaTypes(Arrays.asList(MediaType.parseMediaType("text/plain;charset=utf-8"), MediaType.APPLICATION_OCTET_STREAM, MediaType.APPLICATION_XML));
        return converter;
    }

//    @Bean
//    public RestTemplate getRestTemplate() {
//        RestTemplate restTemplate = new RestTemplate();
//        try {
//            HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();
//            factory.setReadTimeout(READ_TIMEOUT);
//            factory.setConnectTimeout(CONNECTION_TIMEOUT);
//
//            restTemplate = new RestTemplate(factory);
//
//            restTemplate.getMessageConverters().add(jacksonSupportsMoreTypes());
//        } catch (Exception exception) {
////			logger.debug("ERROR", exception);
//            restTemplate = new RestTemplate();
//        }
//        restTemplate.getMessageConverters()
//                .add(0, new StringHttpMessageConverter(StandardCharsets.UTF_8));
//        return restTemplate;
//
//    }


}
