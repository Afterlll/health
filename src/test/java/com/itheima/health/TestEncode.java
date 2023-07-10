package com.itheima.health;

import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class TestEncode {
    // 使用BCrypt算法进行加密
    @Test
    void test01() {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        for (int i = 0; i < 10; i++) {
            String encode = encoder.encode("123456");
            System.out.println(encode);
            /*
            $2a$10$w0q8hmcma59ohtsIJZKsn.xj6AAruMrxyHjsuAZk3NbxeNRy5/4cm
            $2a$10$/okup6hfdQ47siQyuLQoteTYizbs94zsZ1Oe2u8.7YzFqWz/EJYke
            $2a$10$VPx06fuI4BJ2t4W5W7nrCeHyymG5AFHWvRU6vffJbud6JsrZZWF0e
            $2a$10$YXetNEftRlTZ0vzpVdDquuUD11NheccIEXMluOdCFVqB26CyNPu0K
            $2a$10$f1D.7hkiFeotFDijNElY9uBfV0d65NfHzqTVpiS6UNgYrs0RLYywq
            $2a$10$oK9mamv7i3G4N0s/pBI4Z.wFGuLeOdFzwqjfvhzA9X7pu99gYprDy
            $2a$10$v7NMaEE1lxYs5wSKclucQeKXY3MmWBzYG24Itme8u1laZ1t/AH7U2
            $2a$10$YgwK2zrewh/KRbi.ubibOeV045mm3cYtVbg60Z.UB.qFjEd1zxRIa
            $2a$10$nDDicQ.GIxvAhiKrLAS9u.rJ2swNtQyEcw/hNI4shktyhKfvzGr3C
            $2a$10$rZp/g8K6MiMYugYWCG9tieaEXPMA3jn7TSArt1Lpsf0dwe2NzSawG
             */
        }
    }

    // 使用BCrypt进行密码对比
    @Test
    void test02() {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        System.out.println(encoder.matches("123456", "$2a$10$w0q8hmcma59ohtsIJZKsn.xj6AAruMrxyHjsuAZk3NbxeNRy5/4cm"));
    }
}
