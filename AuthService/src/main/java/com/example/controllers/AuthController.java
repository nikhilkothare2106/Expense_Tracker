package com.example.controllers;

import com.example.entities.RefreshToken;
import com.example.request.SignupRequest;
import com.example.response.JwtResponse;
import com.example.services.JwtService;
import com.example.services.RefreshTokenService;
import com.example.services.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController {
    @Autowired
    private JwtService jwtService;

    @Autowired
    private RefreshTokenService refreshTokenService;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @PostMapping("auth/signup")
    public ResponseEntity SignUp(@RequestBody SignupRequest signupRequest){
        try{
            Boolean isSignUpped = userDetailsService.signupUser(signupRequest);
            if(!isSignUpped){
                return new ResponseEntity<>("Already Exist", HttpStatus.BAD_REQUEST);
            }
            RefreshToken refreshToken = refreshTokenService.createRefreshToken(signupRequest.getUsername());
            String jwtToken = jwtService.generateToken(signupRequest.getUsername());
            return new ResponseEntity<>(JwtResponse.builder().accessToken(jwtToken).
                    token(refreshToken.getToken()).build(), HttpStatus.OK);
        }catch (Exception ex){
            ex.printStackTrace();
            return new ResponseEntity<>("Exception in User Service", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
