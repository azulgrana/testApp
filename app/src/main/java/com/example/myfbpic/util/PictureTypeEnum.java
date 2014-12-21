package com.example.myfbpic.util;

public enum PictureTypeEnum {
    Original(0), Blurred(1);

    private final int value;

    private PictureTypeEnum(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}