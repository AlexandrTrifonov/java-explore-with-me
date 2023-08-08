package ru.practicum.mainservice.constants;

import java.time.format.DateTimeFormatter;

public class Constants {
    public static final int MIN_SIZE_NAME_OF_CATEGORY = 1;
    public static final int MAX_SIZE_NAME_OF_CATEGORY = 50;
    public static final int MIN_LENGTH_EMAIL = 6;
    public static final int MAX_LENGTH_EMAIL = 254;
    public static final int MIN_SIZE_NAME_OF_USER = 2;
    public static final int MAX_SIZE_NAME_OF_USER = 250;
    public static final String DATETIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
    public static final DateTimeFormatter DATETIME_FORMATTER = DateTimeFormatter.ofPattern(DATETIME_FORMAT);
    public static final String RANGE_START = "2000-01-01 00:00:00";
    public static final String RANGE_END = "2046-01-01 00:00:00";
    public static final String PAGE_DEFAULT_FROM = "0";
    public static final String PAGE_DEFAULT_SIZE = "10";
    public static final int MIN_LENGTH_ANNOTATION = 20;
    public static final int MAX_LENGTH_ANNOTATION = 2000;
    public static final int MIN_LENGTH_DESCRIPTION = 20;
    public static final int MAX_LENGTH_DESCRIPTION = 7000;
    public static final int MIN_LENGTH_TITLE = 3;
    public static final int MAX_LENGTH_TITLE = 120;
    public static final int MIN_SIZE_TITLE_OF_COMPILATION = 1;
    public static final int MAX_SIZE_TITLE_OF_COMPILATION = 50;

}
