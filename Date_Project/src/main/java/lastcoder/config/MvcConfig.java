package lastcoder.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.WebRequestInterceptor;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import lastcoder.service.AuthHandlerInterceptor;

@Configuration
@EnableWebMvc
public class MvcConfig implements WebMvcConfigurer {
    
    @Bean
	public AuthHandlerInterceptor authHandlerInterceptor() {
		return new AuthHandlerInterceptor();
	}
    @Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addWebRequestInterceptor((WebRequestInterceptor) authHandlerInterceptor())
			.addPathPatterns("/**");
                     // .excludePathPatterns("/product/order/review/**")
	}
}