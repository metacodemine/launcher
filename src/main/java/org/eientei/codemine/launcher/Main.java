package org.eientei.codemine.launcher;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.felixroske.jfxsupport.AbstractJavaFxApplicationSupport;
import de.felixroske.jfxsupport.SplashScreen;
import org.eientei.codemine.launcher.ui.RootView;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;

@SpringBootApplication
public class Main extends AbstractJavaFxApplicationSupport {
    public static void main(String[] args) {
        SplashScreen splashScreen = new SplashScreen() {
            @Override
            public String getImagePath() {
                return "/org/eientei/codemine/launcher/ui/splash.jpg";
            }
        };
        launchApp(Main.class, RootView.class, splashScreen, args);
    }

    @Bean
    public RestTemplate restTemplate() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(JsonGenerator.Feature.IGNORE_UNKNOWN, true);
        objectMapper.configure(JsonParser.Feature.IGNORE_UNDEFINED, true);
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES).disable(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES);

        RestTemplate restTemplate = new RestTemplate();
        MappingJackson2HttpMessageConverter messageConverter = new MappingJackson2HttpMessageConverter();
        messageConverter.setSupportedMediaTypes(Arrays.asList(MediaType.APPLICATION_JSON, MediaType.APPLICATION_OCTET_STREAM));
        messageConverter.setPrettyPrint(false);
        messageConverter.setObjectMapper(objectMapper);
        restTemplate.getMessageConverters().removeIf(m -> m.getClass().getName().equals(MappingJackson2HttpMessageConverter.class.getName()));
        restTemplate.getMessageConverters().add(messageConverter);
        return restTemplate;
    }
}
