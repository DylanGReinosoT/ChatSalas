package espe.edu.ec.SalasChat.security;

import espe.edu.ec.SalasChat.model.Administrador;
import espe.edu.ec.SalasChat.repository.AdministradorRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {
    private final JwtUtil jwtUtil;
    private final AdministradorRepository adminRepo;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException{
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer")){
            String token = authHeader.substring(7);
            try{
                String username = jwtUtil.extractUsername(token);
                Optional<Administrador> adminOpt =  adminRepo.findByUsername(username);
                if (adminOpt.isPresent()) {
                    UsernamePasswordAuthenticationToken auth =
                            new UsernamePasswordAuthenticationToken(
                                    username, null, List.of(new SimpleGrantedAuthority("ADMIN"))
                            );
                    SecurityContextHolder.getContext().setAuthentication(auth);
                }
            }catch (Exception e){
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write(("Token invalido o expirado"));
                return;

            }
        }

        filterChain.doFilter(request, response);

    }


}
