package com.market.server.utils;

import lombok.extern.log4j.Log4j2;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.text.ParseException;

@Log4j2
public class DateUtil {

    /**
     * 파일 처리 시간을 yyyyMMddHHmm 형태로 반환 한다.
     *
     * @param fileType 파일 타입 jpg, png
     * @return 처리 시간의 문자열 202009040159.jpg
     * @author topojs8
     */
    private static ThreadLocal<SimpleDateFormat> getNowTimeToyyyyMMddHHmm = new ThreadLocal<SimpleDateFormat>() {
        @Override
        protected SimpleDateFormat initialValue() {
            return new SimpleDateFormat("yyyyMMddHHmm");
        }
    };
    public static String getNowTimeToyyyyMMddHHmm(Date date, String fileType) throws ParseException {
        return getNowTimeToyyyyMMddHHmm.get().format(date) + fileType;
    }

    /**
     * 파일 처리 문자열 yyyyMMddHHmm 형태 인지 확인 한다.
     *
     * @param date 처리 시간 문자열
     * @return void
     * @author topojs8
     */
    private static void parseDate(String dateStr) {
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmm");
            Date date = simpleDateFormat.parse(dateStr);
        } catch (ParseException e) {
            log.error("ParseError", e);
        }
    }

}
