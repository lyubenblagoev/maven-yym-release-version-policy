package com.lyubenblagoev.maven.release.version;

import org.codehaus.plexus.util.StringUtils;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class YYMZVersion {

    public static final int MIN_YEAR = 20;
    public static final int MAX_YEAR = 99;
    public static final int MIN_MONTH = 1;
    public static final int MAX_MONTH = 12;

    private final int major;
    private final int minor;
    private int patch;

    public YYMZVersion(int major, int minor) {
        this(major, minor, -1);
    }

    public YYMZVersion(int major, int minor, int patch) {
        if (major < MIN_YEAR || major > MAX_YEAR) {
            throw new IllegalArgumentException("Major version must be a year between 20 an 99");
        }
        if (minor < MIN_MONTH || minor > MAX_MONTH) {
            throw new IllegalArgumentException("Minor version must be a month between 1 an 12");
        }
        this.major = major;
        this.minor = minor;
        if (patch > 0) {
            this.patch = patch;
        }
    }

    @Override
    public String toString() {
        return String.join(".", versionStringList());
    }

    private List<String> versionStringList() {
        String[] parts = new String[]{
                String.valueOf(major),
                String.valueOf(minor),
                patch > 0 ? String.valueOf(patch) : ""
        };
        return Stream.of(parts)
                .filter(StringUtils::isNotBlank)
                .collect(Collectors.toList());
    }
}
