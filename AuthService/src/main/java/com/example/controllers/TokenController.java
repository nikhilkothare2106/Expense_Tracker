package com.example.controllers;

import com.example.entities.RefreshToken;
import com.example.request.AuthRequest;
import com.example.request.RefreshTokenRequest;
import com.example.response.JwtResponse;
import com.example.services.JwtService;
import com.example.services.RefreshTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
public class TokenController {
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private RefreshTokenService refreshTokenService;

    @Autowired
    private JwtService jwtService;

    @PostMapping("auth/login")
    public ResponseEntity<JwtResponse> authenticateAndGetToken(@RequestBody AuthRequest authRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            authRequest.getUsername(),
                            authRequest.getPassword()
                    )
            );

            if (authentication.isAuthenticated()) {
                RefreshToken refreshToken = refreshTokenService.createRefreshToken(authRequest.getUsername());

                JwtResponse response = JwtResponse.builder()
                        .accessToken(jwtService.generateToken(authRequest.getUsername()))
                        .token(refreshToken.getToken())
                        .build();

                return ResponseEntity.ok(response);
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(null);  // or return custom error DTO
        }
    }


    @PostMapping("/auth/refreshToken")
    public ResponseEntity refreshToken(@RequestBody RefreshTokenRequest request) {
        try{
            String token = request.getToken();

            RefreshToken refreshToken = refreshTokenService.findByToken(token)
                    .orElseThrow(() -> new RuntimeException("Refresh Token not found in DB"));

            refreshTokenService.verifyExpiration(refreshToken);

            String username = refreshToken.getUser().getUsername();
            String accessToken = jwtService.generateToken(username);

            JwtResponse build = JwtResponse.builder()
                    .accessToken(accessToken)
                    .token(token)
                    .build();
            return ResponseEntity.ok().body(build);
        }
        catch (Exception e){
            e.printStackTrace();
            throw new RuntimeException("Refresh Token not found in DB");
        }

    }

    @GetMapping("/test")
    public ResponseEntity<?> helper(){
        return ResponseEntity.ok().build();
    }

}
