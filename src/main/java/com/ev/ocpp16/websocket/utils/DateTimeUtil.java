package com.ev.ocpp16.websocket.utils;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class DateTimeUtil {

    public static String currentDateTimeToISO8601() {
        ZonedDateTime now = ZonedDateTime.now(ZoneOffset.UTC);
        DateTimeFormatter isoFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        return now.format(isoFormatter);
    }

    public static LocalDateTime iso8601ToBasicDateTime(String isoDate) throws DateTimeParseException {
        try {
            return LocalDateTime.parse(isoDate);
        } catch (DateTimeParseException e1) {
            try {
                // 시간대 정보가 있는 경우 (UTC 또는 오프셋)
                ZonedDateTime zdt = ZonedDateTime.parse(isoDate);
                return zdt.toLocalDateTime();
            } catch (DateTimeParseException e2) {
                // OffsetDateTime 처리
                OffsetDateTime odt = OffsetDateTime.parse(isoDate);
                return odt.toLocalDateTime();
            }
        }
    }
}
