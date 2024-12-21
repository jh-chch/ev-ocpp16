package com.ev.ocpp16.global.utils;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class DateTimeUtil {

    private static final ZoneId KOREA_ZONE = ZoneId.of("Asia/Seoul");

    // iso8601 문자열을 ZonedDateTime으로 변환
    public static ZonedDateTime iso8601ToZonedDateTime(String isoDate) {
        return ZonedDateTime.parse(isoDate);
    }

    // ZonedDateTime을 한국 시간 LocalDateTime으로 변환
    public static LocalDateTime zonedDateTimeToKoreanLocalDateTime(ZonedDateTime zonedDateTime) {
        return zonedDateTime
                .withZoneSameInstant(KOREA_ZONE)
                .toLocalDateTime();
    }

    // iso8601 문자열을 한국 시간 LocalDateTime으로 변환
    public static LocalDateTime iso8601ToKoreanLocalDateTime(String isoDate) {
        return zonedDateTimeToKoreanLocalDateTime(iso8601ToZonedDateTime(isoDate));
    }

    // 현재 시간(서울)을 iso8601 문자열로 반환
    public static String currentKoreanLocalDateTimeToISO8601() {
        return ZonedDateTime.now(KOREA_ZONE).format(DateTimeFormatter.ISO_OFFSET_DATE_TIME);
    }

    // 현재 시간(서울)을 LocalDateTime으로 반환
    public static LocalDateTime currentKoreanLocalDateTime() {
        return LocalDateTime.now(KOREA_ZONE);
    }

    // LocalDateTime(서울)를 Date로 반환
    public static Date koreanLocalDateTimeToDate(LocalDateTime localDateTime) {
        return Date.from(localDateTime.atZone(KOREA_ZONE).toInstant());
    }
}
