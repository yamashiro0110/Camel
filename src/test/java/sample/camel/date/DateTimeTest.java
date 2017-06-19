package sample.camel.date;

import org.junit.Test;

import java.time.LocalDateTime;

/**
 * Created by yamashiro-r on 2017/06/19.
 */
public class DateTimeTest {

    @Test
    public void 月末を判定する() {
        LocalDateTime now = LocalDateTime.now();
        System.out.println(now);
        System.out.println("lengthOfMonth:" + now.toLocalDate().lengthOfMonth());
        System.out.println("getMonthValue:" + now.toLocalDate().getMonthValue());
        System.out.println("getDayOfMonth:" + now.toLocalDate().getDayOfMonth());
    }
}
