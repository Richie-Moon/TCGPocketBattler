package com.tcgpocket.server;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Google sign-in. Every page and {@code /play} stay open to everyone; signing in
 * only says who you are, and the first sign-in creates your {@code users} row.
 *
 * <p>Without the {@code db} profile there is nowhere to keep users, so there is
 * no sign-in at all and the server plays exactly as before.
 *
 * <p>CSRF tokens are off: the session cookie is {@code SameSite=Lax}
 * ({@code application.properties}), so a cross-site POST arrives signed out.
 */
@Configuration
class Accounts {

    @Bean
    @Profile("!db")
    SecurityFilterChain open(HttpSecurity http) {
        return http.authorizeHttpRequests(requests -> requests.anyRequest().permitAll()).build();
    }

    @Bean
    @Profile("db")
    SecurityFilterChain google(HttpSecurity http, JdbcClient db) {
        OidcUserService google = new OidcUserService();
        return http
                .authorizeHttpRequests(requests -> requests.anyRequest().permitAll())
                .csrf(AbstractHttpConfigurer::disable)
                .oauth2Login(login -> login
                        .userInfoEndpoint(info -> info.oidcUserService(request -> {
                            OidcUser user = google.loadUser(request);
                            remember(db, user);
                            return user;
                        }))
                        .defaultSuccessUrl("/", true))
                .logout(logout -> logout.logoutSuccessUrl("/"))
                .build();
    }

    /** Creates the user on first sign-in. Later sign-ins keep the stored name, so it can be renamed. */
    private static void remember(JdbcClient db, OidcUser user) {
        db.sql("""
                MERGE INTO users u
                USING (SELECT 'google' provider, ? subject, ? display_name) s
                ON (u.provider = s.provider AND u.subject = s.subject)
                WHEN NOT MATCHED THEN INSERT (provider, subject, display_name)
                    VALUES (s.provider, s.subject, s.display_name)""")
                .params(user.getSubject(), displayName(user))
                .update();
    }

    private static String displayName(OidcUser user) {
        String name = user.getGivenName() != null ? user.getGivenName() : user.getFullName();
        return name != null ? name : "Trainer";
    }

    @RestController
    @Profile("db")
    static class Me {

        private final JdbcClient db;

        Me(JdbcClient db) {
            this.db = db;
        }

        record Account(String name) {
        }

        /** 401 when signed out, which is how the web client knows to offer "Sign in with Google". */
        @GetMapping("/api/me")
        ResponseEntity<Account> me(@AuthenticationPrincipal OidcUser user) {
            if (user == null) {
                return ResponseEntity.status(401).build();
            }
            return ResponseEntity.ok(db.sql("SELECT display_name FROM users WHERE provider = 'google' AND subject = ?")
                    .param(user.getSubject())
                    .query((row, n) -> new Account(row.getString(1)))
                    .single());
        }
    }
}
