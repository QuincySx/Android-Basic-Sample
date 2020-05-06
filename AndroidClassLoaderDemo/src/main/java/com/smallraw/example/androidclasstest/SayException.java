package com.smallraw.example.androidclasstest;

public class SayException implements ISay {
    @Override
    public String saySomething() {
        return "something wrong here";
    }
}
