package com.admission.security;

import com.admission.security.constant.RedisConstant;
import com.admission.security.utils.IdUtils;
import com.admission.security.utils.ShortUrlGenerate;
import org.springframework.util.StopWatch;

import java.util.UUID;

public class ShortUrlTest {

    public static void test() {

        int size = 1000000;
        String[] urls = new String[size];

        for (int i = 0; i < size; i++) {
            urls[i] = "https://time.geekbang.org/column/article/80850" + UUID.randomUUID().toString();
        }
        System.out.println("数据填充完成");

//        long l = System.currentTimeMillis();
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        for (String s : urls) {
            String shortUrl = ShortUrlGenerate.generate(s);
        }
        stopWatch.stop();
        System.out.println("共耗时：" + stopWatch.getTotalTimeSeconds());
//        System.out.println(System.currentTimeMillis() - l);
    }

    public static void main(String[] args) {
//        test();
        String str = "中山大学:" + "user2:" + "13417778470";
        System.out.println(ShortUrlGenerate.generate(str));

        String temp = String.format(RedisConstant.USER_SHORT_URL, "http://view.dev.5gimos.com/colleges-universities/hg", IdUtils.randomUUID(), "13417778470");
        System.out.println(temp);
        String shortUrl = ShortUrlGenerate.generate(temp);
        System.out.println(shortUrl);
    }
}
