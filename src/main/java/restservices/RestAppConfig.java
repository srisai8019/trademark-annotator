package restservices;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class RestAppConfig implements WebMvcConfigurer {
    @Autowired
    private AnnotateRequestInterceptor annotateRequestInterceptor;

    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(annotateRequestInterceptor).addPathPatterns("/annotate");
    }
}
