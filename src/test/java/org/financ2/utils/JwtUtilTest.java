package org.financ2.utils;

import org.finance2.utils.JwtUtil;

public class JwtUtilTest {
    public static void main(String[] args){

        String token = JwtUtil.generateToken("test@gmail.com");
        System.out.println("Generated Token" + token);

        String email = JwtUtil.validateToken(token);
        System.out.println("Extracted Email: " + email);
    }
}
