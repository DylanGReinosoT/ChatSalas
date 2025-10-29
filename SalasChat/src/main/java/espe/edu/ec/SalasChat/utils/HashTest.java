package espe.edu.ec.SalasChat.utils;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class HashTest {
    public static void main(String[] args) {
        String rawPassword = "1234";
        String encoded = new BCryptPasswordEncoder().encode(rawPassword);
        System.out.println("Hash generado: " + encoded);
    }
}
