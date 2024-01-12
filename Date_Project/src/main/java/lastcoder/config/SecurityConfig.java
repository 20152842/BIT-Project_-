package lastcoder.config;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.header.writers.frameoptions.XFrameOptionsHeaderWriter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import lastcoder.service.UserSecurityService;


@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true,prePostEnabled = true)
public class SecurityConfig { 

	private UserSecurityService userSecurityService;


    // 암호화에 필요한 PasswordEncoder 를 Bean 등록합니다.
    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }
//	
//	@Bean
//    public BCryptPasswordEncoder passwordEncoder() {
//        return new BCryptPasswordEncoder();
//    }


	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration)
			throws Exception {
		return authenticationConfiguration.getAuthenticationManager();
	}
	
	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http.authorizeRequests().antMatchers("/main", "/issue", "/login", "/login_social", "/board").permitAll()
								.antMatchers("/map", "/board/**").hasAnyRole("ADMIN","USER")
								
								.and().csrf().disable()
								.headers().addHeaderWriter(new XFrameOptionsHeaderWriter(XFrameOptionsHeaderWriter.XFrameOptionsMode.SAMEORIGIN))
								
								.and().formLogin()
								.loginPage("/login_form")
								
								.loginProcessingUrl("/login_process")
								.usernameParameter("username")//아이디 파라미터명 설정
								.passwordParameter("password")//패스워드 파라미터명 설정
								
								.defaultSuccessUrl("/main")
								.failureUrl("/login?error=true")
								
								.successHandler(new AuthenticationSuccessHandler() {
								    @Override
								    public void onAuthenticationSuccess(HttpServletRequest httpServletRequest, HttpServletResponse response, Authentication authentication)
								    									throws IOException, ServletException {
								        System.out.println("authentication:: "+ authentication.getName());
								        response.sendRedirect("/");
								    }
								})//로그인 성공 후 핸들러
								.failureHandler(new AuthenticationFailureHandler() {
								    @Override
								    public void onAuthenticationFailure(HttpServletRequest httpServletRequest, HttpServletResponse response, AuthenticationException e)
								    									throws IOException, ServletException {
								        System.out.println("exception:: " + e.getMessage());
								        response.sendRedirect("/login");
								    }
								})//로그인 실패 후 핸들러
								.and().logout()
								.logoutRequestMatcher(new AntPathRequestMatcher("/user/logout"))
								.logoutSuccessUrl("/main")
								.invalidateHttpSession(true);
//		.loginProcessingUrl("/user/login")
		return http.build();
	}


	
}