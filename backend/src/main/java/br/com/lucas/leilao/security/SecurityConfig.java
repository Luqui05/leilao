package br.com.lucas.leilao.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

  @Autowired
  private JwtAuthFilter jwtAuthFilter;

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    return http
        .csrf(csrf -> csrf.disable())
        .cors(Customizer.withDefaults())
        .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .authorizeHttpRequests(auth -> auth
            // Rotas públicas
            .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
            .requestMatchers("/auth/**").permitAll()
            .requestMatchers(HttpMethod.POST, "/api/pessoas").permitAll()
            
            // Categorias - autenticados podem ver, apenas ADMIN/VENDEDOR podem criar/editar/excluir
            .requestMatchers(HttpMethod.GET, "/api/categorias/**").authenticated()
            .requestMatchers(HttpMethod.POST, "/api/categorias").hasAnyRole("ADMIN", "VENDEDOR")
            .requestMatchers(HttpMethod.PATCH, "/api/categorias/**").hasAnyRole("ADMIN", "VENDEDOR")
            .requestMatchers(HttpMethod.DELETE, "/api/categorias/**").hasRole("ADMIN")
            
            // Leilões - autenticados podem ver, VENDEDOR pode criar seus próprios, ADMIN gerencia todos
            .requestMatchers(HttpMethod.GET, "/api/leiloes/**").authenticated()
            .requestMatchers(HttpMethod.POST, "/api/leiloes").hasAnyRole("VENDEDOR", "ADMIN")
            .requestMatchers(HttpMethod.PATCH, "/api/leiloes/**").hasAnyRole("VENDEDOR", "ADMIN")
            .requestMatchers(HttpMethod.DELETE, "/api/leiloes/**").hasAnyRole("VENDEDOR", "ADMIN")
            
            // Pessoas - GET autenticado (com lógica no service), ADMIN gerencia
            .requestMatchers(HttpMethod.GET, "/api/pessoas/**").authenticated()
            .requestMatchers(HttpMethod.PATCH, "/api/pessoas/**").authenticated() // Verificação adicional no service
            .requestMatchers(HttpMethod.DELETE, "/api/pessoas/**").hasRole("ADMIN")
            
            // Perfis - apenas ADMIN
            .requestMatchers("/api/perfis/**").hasRole("ADMIN")
            
            // Pessoa-Perfil - apenas ADMIN
            .requestMatchers(HttpMethod.GET, "/api/pessoas-perfis").authenticated()
            .requestMatchers(HttpMethod.POST, "/api/pessoas-perfis").hasRole("ADMIN")
            .requestMatchers(HttpMethod.PATCH, "/api/pessoas-perfis/**").hasRole("ADMIN")
            .requestMatchers(HttpMethod.DELETE, "/api/pessoas-perfis/**").hasRole("ADMIN")
            
            // Imagens - autenticados podem ver, VENDEDOR/ADMIN podem gerenciar
            .requestMatchers(HttpMethod.GET, "/api/imagens/**").authenticated()
            .requestMatchers(HttpMethod.POST, "/api/imagens/**").hasAnyRole("VENDEDOR", "ADMIN")
            .requestMatchers(HttpMethod.DELETE, "/api/imagens/**").hasAnyRole("VENDEDOR", "ADMIN")
            
            // Lances - autenticados podem dar lance, ver seus próprios
            .requestMatchers(HttpMethod.GET, "/api/lances/**").authenticated()
            .requestMatchers(HttpMethod.POST, "/api/lances").hasAnyRole("COMPRADOR", "ADMIN")
            
            // Feedbacks - autenticados podem criar/ver
            .requestMatchers(HttpMethod.GET, "/api/feedbacks/**").authenticated()
            .requestMatchers(HttpMethod.POST, "/api/feedbacks").authenticated()
            .requestMatchers(HttpMethod.DELETE, "/api/feedbacks/**").hasRole("ADMIN")
            
            // Pagamentos - autenticados podem ver os próprios
            .requestMatchers(HttpMethod.GET, "/api/pagamentos/**").authenticated()
            .requestMatchers(HttpMethod.POST, "/api/pagamentos").hasAnyRole("COMPRADOR", "ADMIN")
            
            .anyRequest().authenticated())
        .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
        .build();
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
    return config.getAuthenticationManager();
  }

  @Bean
  public CorsConfigurationSource corsConfigurationSource() {
    CorsConfiguration configuration = new CorsConfiguration();
    configuration.setAllowedOrigins(List.of("http://localhost:3000"));
    configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
    configuration.setAllowedHeaders(List.of("*"));
    configuration.setExposedHeaders(List.of("Authorization"));
    configuration.setAllowCredentials(false);

    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", configuration);
    return source;
  }
}
