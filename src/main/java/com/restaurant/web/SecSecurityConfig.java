package com.restaurant.web;

/*@Configuration
@EnableWebSecurity*/
public class SecSecurityConfig /*extends WebSecurityConfigurerAdapter */ {
/*    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    protected void configure(final AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication()
                .withUser("user").password(passwordEncoder().encode("user")).roles("USER")
                .and()
                .withUser("user2").password(passwordEncoder().encode("user2Pass")).roles("USER")
                .and()
                .withUser("admin").password(passwordEncoder().encode("adminPass")).roles("ADMIN");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.formLogin()
                .loginPage("/login-main.html")
                .loginProcessingUrl("/controller?command=loginMain")
                .defaultSuccessUrl("/list-food.html",true)
                .failureUrl("/login.html?error=true");
    }*/
}