package com.thehecklers.ssecressvr;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@SpringBootApplication
public class SsecResSvrApplication {

    public static void main(String[] args) {
        SpringApplication.run(SsecResSvrApplication.class, args);
    }

}

@EnableWebSecurity
class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .mvcMatchers("/resources/claims/**").hasAuthority("SCOPE_openid")
                .mvcMatchers("/resources/email/**").hasAuthority("SCOPE_email")
                .and().oauth2ResourceServer().jwt();
    }
}


@RestController
@RequestMapping("/resources")
class ResourceController {
    @GetMapping("/something")
    String getSomething() {
        return "This is really something!";
    }

    @GetMapping("/claims")
    Map<String, Object> getClaims(@AuthenticationPrincipal Jwt jwt) {
        return jwt.getClaims();
    }

    @GetMapping("/email")
    String getSubject(@AuthenticationPrincipal Jwt jwt) {
        return jwt.getSubject();
    }
}