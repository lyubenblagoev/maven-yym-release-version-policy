package com.lyubenblagoev.maven.release.version.policy;

import com.lyubenblagoev.maven.release.version.DateUtils;
import org.apache.maven.shared.release.policy.version.VersionPolicyRequest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public final class YYMZVersionPolicyTest {

    private int currentYear;
    private int currentMonth;
    private YYMZVersionPolicy versionPolicy;

    @BeforeEach
    public void setup() {
        this.currentYear = DateUtils.getCurrentYearTwoDigits();
        this.currentMonth = DateUtils.getCurrentMonth();
        this.versionPolicy = new YYMZVersionPolicy();
    }

    @Test
    public void getReleaseVersion_whenOlderVersionIsPassed_initialVersionIsReturned() {
        VersionPolicyRequest versionPolicyRequest = versionPolicyRequest(versionOlder());
        String version = getNextReleaseVersion(versionPolicyRequest);
        Assertions.assertEquals(versionCurrentInitial(), version);
    }

    @Test
    public void getReleaseVersion_whenOlderSnapshotVersionIsPassed_initialVersionIsReturned() {
        VersionPolicyRequest versionPolicyRequest = versionPolicyRequest(asSnapshot(versionOlder()));
        String version = getNextReleaseVersion(versionPolicyRequest);
        Assertions.assertEquals(versionCurrentInitial(), version);
    }

    @Test
    public void getReleaseVersion_whenInitialVersion_firstIncrementIsReturned() {
        VersionPolicyRequest versionPolicyRequest = versionPolicyRequest(versionCurrentInitial());
        String version = getNextReleaseVersion(versionPolicyRequest);
        Assertions.assertEquals(versionCurrentFirstIncrement(), version);
    }

    @Test
    public void getReleaseVersion_whenFirstIncrement_secondIncrementIsReturned() {
        VersionPolicyRequest versionPolicyRequest = versionPolicyRequest(versionCurrentFirstIncrement());
        String version = getNextReleaseVersion(versionPolicyRequest);
        Assertions.assertEquals(versionCurrentSecondIncrement(), version);
    }

    @Test
    public void getReleaseVersion_whenCurrentSnapshot_currentWithoutPostfixIsReturned() {
        VersionPolicyRequest versionPolicyRequest = versionPolicyRequest(asSnapshot(versionCurrentInitial()));
        String version = getNextReleaseVersion(versionPolicyRequest);
        Assertions.assertEquals(versionCurrentInitial(), version);
    }

    @Test
    public void getReleaseVersion_whenFirstIncrementSnapshot_firstIncrementIsReturned() {
        VersionPolicyRequest versionPolicyRequest = versionPolicyRequest(asSnapshot(versionCurrentFirstIncrement()));
        String version = getNextReleaseVersion(versionPolicyRequest);
        Assertions.assertEquals(versionCurrentFirstIncrement(), version);
    }

    @Test
    public void getDevelopmentVersion_whenCurrentInitial_firstIncrementSnapshotIsReturned() {
        VersionPolicyRequest versionPolicyRequest = versionPolicyRequest(versionCurrentInitial());
        String version = getNextDevelopmentVersion(versionPolicyRequest);
        Assertions.assertEquals(asSnapshot(versionCurrentFirstIncrement()), version);
    }

    @Test
    public void getDevelopmentVersion_whenFirstIncrement_secondIncrementSnapshotIsReturned() {
        VersionPolicyRequest versionPolicyRequest = versionPolicyRequest(versionCurrentFirstIncrement());
        String version = getNextDevelopmentVersion(versionPolicyRequest);
        Assertions.assertEquals(asSnapshot(versionCurrentSecondIncrement()), version);
    }

    @Test
    public void getDevelopmentVersion_whenFirstIncrementSnapshot_secondIncrementSnapshotIsReturned() {
        VersionPolicyRequest versionPolicyRequest = versionPolicyRequest(asSnapshot(versionCurrentFirstIncrement()));
        String version = getNextDevelopmentVersion(versionPolicyRequest);
        Assertions.assertEquals(asSnapshot(versionCurrentSecondIncrement()), version);
    }

    @Test
    public void getDevelopmentVersion_whenOlder_currentInitialSnapshotIsReturned() {
        VersionPolicyRequest versionPolicyRequest = versionPolicyRequest(versionOlder());
        String version = getNextDevelopmentVersion(versionPolicyRequest);
        Assertions.assertEquals(asSnapshot(versionCurrentInitial()), version);
    }

    private String getNextReleaseVersion(VersionPolicyRequest versionPolicyRequest) {
        return versionPolicy.getReleaseVersion(versionPolicyRequest).getVersion();
    }

    private String getNextDevelopmentVersion(VersionPolicyRequest versionPolicyRequest) {
        return versionPolicy.getDevelopmentVersion(versionPolicyRequest).getVersion();
    }

    private VersionPolicyRequest versionPolicyRequest(String version) {
        return new VersionPolicyRequest().setVersion(version);
    }

    private String versionCurrentInitial() {
        return String.format("%d.%d", currentYear, currentMonth);
    }

    private String versionCurrentFirstIncrement() {
        return String.format("%d.%d.%d", currentYear, currentMonth, 1);
    }

    private String versionCurrentSecondIncrement() {
        return String.format("%d.%d.%d", currentYear, currentMonth, 2);
    }

    private String versionOlder() {
        return String.format("%d.%d.%d", currentYear, currentMonth - 1, 1);
    }

    private String asSnapshot(String versionWithoutPostfix) {
        return String.format("%s-SNAPSHOT", versionWithoutPostfix);
    }
}
