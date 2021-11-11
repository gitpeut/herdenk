package org.peut.herdenk.security;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.sql.DataSource;

import static org.springframework.http.HttpMethod.*;
import static org.springframework.http.HttpMethod.GET;


@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity( prePostEnabled = true)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
    private final DataSource dataSource;
    private JwtRequestFilter jwtRequestFilter;

    @Autowired
    SecurityConfiguration(DataSource dataSource, JwtRequestFilter jwtRequestFilter) {
        this.dataSource = dataSource;
        this.jwtRequestFilter = jwtRequestFilter;

    }


    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.jdbcAuthentication()
                .dataSource(dataSource)
                .usersByUsernameQuery("SELECT email, password, enabled FROM users WHERE email=?")
                .authoritiesByUsernameQuery("SELECT email, role FROM users where email = ?");

        jwtRequestFilter.setUserDetailsService( userDetailsService());
    }

    @Bean
    public static PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    @Override
    public UserDetailsService userDetailsServiceBean() throws Exception {
        return super.userDetailsServiceBean();
    }



    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http
                .authorizeRequests()
                .antMatchers(       "/api/v1/authorities**").fullyAuthenticated()
                .antMatchers(       "/api/v1/graves**").fullyAuthenticated()
                .antMatchers(       "/api/v1/users**").fullyAuthenticated()
                .antMatchers(       "/api/v1/reactions**").fullyAuthenticated()
                .antMatchers(       "/media**").fullyAuthenticated()
                .antMatchers(       "/api/v1/login").permitAll()
                .antMatchers(       "/api/v1/register").permitAll()
                .antMatchers(OPTIONS, "/**").permitAll()
                .antMatchers(GET,   "/api/v1/users/all").hasRole("ADMIN")
                .antMatchers(GET, "/api/v1/users/me").fullyAuthenticated()
                .antMatchers(DELETE,   "/api/v1/users/{userId}").hasRole("ADMIN")
                .antMatchers(GET,   "/api/v1/users/{userId}").access("@AccessBeans.isSelfOrIsAdmin( #userId )")
                .antMatchers(PUT,   "/api/v1/users/{userId}").access("@AccessBeans.isSelfOrIsAdmin( #userId )")
                .antMatchers(GET,   "/api/v1/graves/all").hasRole( "ADMIN" )
                .antMatchers(GET,   "/api/v1/graves/summary").permitAll()
                .antMatchers(GET,   "/api/v1/graves/{graveId}").access( "@AccessBeans.hasGraveAccessOrIsAdmin( #graveId )")
                .antMatchers(PUT,   "/api/v1/graves/{graveId}").access( "@AccessBeans.hasAtLeastOwnerAccess( #graveId )")
                .antMatchers(DELETE,"/api/v1/graves/{graveId}").access( "@AccessBeans.hasAtLeastOwnerAccess( #graveId )")
                .antMatchers(GET,   "/api/v1/authorities/all").hasRole("ADMIN")
                .antMatchers(GET,   "/api/v1/authorities/user/{userId}").access("@AccessBeans.isSelfOrIsAdmin( #userId )")
                .antMatchers(GET,   "/api/v1/authorities/grave/{graveId}").access("@AccessBeans.hasAtLeastOwnerAccess( #graveId )")
                .antMatchers(DELETE,"/api/v1/authorities/{UserId}/{graveId}").access("@AccessBeans.hasAtLeastOwnerAccess( #graveId )")
                .antMatchers(POST,  "/api/v1/authorities/grave/{graveId}/**").access("@AccessBeans.hasAtLeastOwnerAccess( #graveId )")
                .antMatchers(PUT,   "/api/v1/authorities/grave/{graveId}/**").access("@AccessBeans.hasAtLeastOwnerAccess( #graveId )")
                .antMatchers(GET,   "/api/v1/reactions/all").hasRole("ADMIN")
                .antMatchers(GET,   "/api/v1/reactions/grave/{graveId}").access("@AccessBeans.hasGraveAccessOrIsAdmin( #graveId )")
                .antMatchers(GET,   "/api/v1/reactions/user/{userId}").access("@AccessBeans.isSelfOrIsAdmin( #userId )")
                .antMatchers(POST,  "/api/v1/reactions/grave/{graveId}").access("@AccessBeans.hasAtLeastWriteAccess( #graveId )")
                .antMatchers(PUT,   "/api/v1/reactions/{reactionId}").access("@AccessBeans.isGraveOwnerOrAuthor( #reactionId )")
                .antMatchers(GET,   "/api/v1/reactions/permission/{graveId}").access("@AccessBeans.hasAtLeastOwnerAccess( #graveId )")
                .antMatchers(POST,  "/api/v1/reactions/permission/{graveId}/{permission}").permitAll()
                .antMatchers(GET,   "/api/v1/reactions/token/{graveId}/{token}").access("@AccessBeans.hasGraveAccessOrIsAdmin( #graveId )")
                .antMatchers(POST,  "/api/v1/reactions/token/{graveId}/{token}").access("@AccessBeans.hasGraveAccessOrIsAdmin( #graveId )")
                .antMatchers(DELETE,"/api/v1/reactions/{reactionId}").access("@AccessBeans.isGraveOwnerOrAuthor( #reactionId )")
                .antMatchers(       "/media/{graveId}/**").access("@AccessBeans.hasGraveAccessOrIsAdmin( #graveId )")
                .anyRequest().denyAll()
                .and()
                .csrf(csrf -> csrf.disable() )
                .formLogin().disable()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);


        http.addFilterBefore( jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
    }

}
