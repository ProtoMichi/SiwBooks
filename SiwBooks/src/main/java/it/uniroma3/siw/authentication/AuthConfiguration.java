package it.uniroma3.siw.authentication;

import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import it.uniroma3.siw.service.CustomOAuth2UserService;

/**
 * Classe di configurazione per la sicurezza dell'applicazione.
 * Definisce le regole di autenticazione, autorizzazione e gestione login/logout.
 */
@Configuration
@EnableWebSecurity
public class AuthConfiguration {

    @Autowired
    private DataSource dataSource;

    @Autowired
    private CustomOAuth2UserService customOAuth2UserService;

    /**
     * Configura l'autenticazione JDBC basata sul database.
     * Viene utilizzata per recuperare le credenziali e i ruoli degli utenti.
     *
     * @param auth l'oggetto AuthenticationManagerBuilder per la configurazione
     * @throws Exception in caso di errore di configurazione
     */
    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.jdbcAuthentication()
            .dataSource(dataSource)
            .authoritiesByUsernameQuery("SELECT username, ruolo FROM credentials WHERE username=?")
            .usersByUsernameQuery("SELECT username, password, 1 as enabled FROM credentials WHERE username=?");
    }

    /**
     * Bean per la codifica delle password usando BCrypt.
     *
     * @return un'istanza di PasswordEncoder
     */
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    /**
     * Bean per ottenere l'AuthenticationManager da AuthenticationConfiguration.
     *
     * @param authConfig la configurazione dell'autenticazione
     * @return l'AuthenticationManager da utilizzare
     * @throws Exception se non è possibile ottenere l'AuthenticationManager
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    /**
     * Configura la catena di filtri di sicurezza per la gestione delle richieste HTTP.
     * Imposta le regole di accesso, login form e login OAuth2, oltre alla configurazione del logout.
     *
     * @param http l'oggetto HttpSecurity da configurare
     * @return il SecurityFilterChain configurato
     * @throws Exception in caso di errore nella configurazione
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .cors(cors -> cors.disable())
            .authorizeHttpRequests(auth -> auth
                // Rotte accessibili a tutti (senza autenticazione)
                .requestMatchers(HttpMethod.GET,
                    "/", "/login", "/login=?error=true", "/registrati",
                    "/css/**", "/js/**", "/libri/**", "/immagine/**",
                    "/autore/foto/**", "/autori/**", "/ricercaLibro/**",
                    "/ricercaAutore/**", "/login/oauth2/**").permitAll()
                .requestMatchers(HttpMethod.POST, "/registrati").permitAll()

                // Accesso riservato al ruolo ADMIN
                .requestMatchers("/admin/**").hasAuthority("ADMIN")

                // Accesso riservato al ruolo USER
                .requestMatchers("/utente/**").hasAuthority("USER")

                // Tutte le altre richieste richiedono autenticazione
                .anyRequest().authenticated()
            )
            // Login form personalizzato
            .formLogin(form -> form
                .loginPage("/login")
                .defaultSuccessUrl("/", true)
                .failureUrl("/login?error=true")
                .permitAll()
            )
            // Login con OAuth2 (es. Google)
            .oauth2Login(oauth -> oauth
                .loginPage("/login")
                .userInfoEndpoint(userInfo -> userInfo
                    .oidcUserService(customOAuth2UserService) // utilizza un servizio personalizzato OIDC
                )
                .defaultSuccessUrl("/", true)
            )
            // Logout personalizzato
            .logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/")
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID")
                .clearAuthentication(true)
                .permitAll()
            );

        return http.build();
    }
}
