package com.example.min.weatherfore.database;

public class WeatherDbSchema {
    public static final class WeatherTable {
        public static final String NAME = "weathers";
        public static final class Cols {
            public static final String ID = "id";
            public static final String DATE = "date";
            public static final String CODE = "code";
            public static final String LOW = "low";
            public static final String HIGH = "high";
            public static final String STATE = "state";
            public static final String HUM = "hun";
            public static final String PRE = "pre";
            public static final String WIN = "win";
        }
    }
}