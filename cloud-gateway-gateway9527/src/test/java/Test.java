import java.time.ZonedDateTime;

public class Test {
    @org.junit.Test
    public void test1() {
        ZonedDateTime zdt = ZonedDateTime.now();
        System.out.println(zdt);    // 2020-03-26T20:34:13.609+08:00[Asia/Shanghai]
    }
}
