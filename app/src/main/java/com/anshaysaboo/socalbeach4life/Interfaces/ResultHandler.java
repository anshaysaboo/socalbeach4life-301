package com.anshaysaboo.socalbeach4life.Interfaces;

public interface ResultHandler<T> {
    void onSuccess(T data);
    void onFailure(Exception e);
}
