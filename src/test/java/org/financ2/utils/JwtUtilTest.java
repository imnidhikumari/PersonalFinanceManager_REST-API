package org.financ2.utils;

import io.jsonwebtoken.Claims;
import org.finance2.utils.JwtUtil;

public class JwtUtilTest {
    public static void main(String[] args){

        String token = JwtUtil.generateToken("test@gmail.com");
        System.out.println("Generated Token" + token);

        Claims claims = JwtUtil.validateToken(token);
        String email = claims.getSubject(); //In claims, subject contains email
        System.out.println("Extracted Email: " + email);
    }
}
