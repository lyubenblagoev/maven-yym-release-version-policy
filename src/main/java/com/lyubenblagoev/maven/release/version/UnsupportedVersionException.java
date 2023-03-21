package com.lyubenblagoev.maven.release.version;

public class UnsupportedVersionException extends RuntimeException {

    public UnsupportedVersionException(String message) {
        super(message);
    }
}
