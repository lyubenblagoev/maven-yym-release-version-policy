package com.lyubenblagoev.maven.release.version;

import java.time.LocalDateTime;

public class DateUtils {

    public static int getCurrentYearTwoDigits() {
        LocalDateTime now = LocalDateTime.now();
        return now.getYear() % (now.getYear() / 1000 * 1000);
    }

    public static int getCurrentMonth() {
        return LocalDateTime.now().getMonthValue();
    }
}
