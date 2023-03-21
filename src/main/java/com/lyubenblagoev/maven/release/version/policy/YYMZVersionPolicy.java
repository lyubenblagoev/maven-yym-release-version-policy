package com.lyubenblagoev.maven.release.version.policy;

import javax.inject.Named;
import javax.inject.Singleton;

import com.lyubenblagoev.maven.release.version.DateUtils;
import com.lyubenblagoev.maven.release.version.UnsupportedVersionException;
import com.lyubenblagoev.maven.release.version.YYMZVersion;
import org.apache.maven.shared.release.policy.version.VersionPolicy;
import org.apache.maven.shared.release.policy.version.VersionPolicyRequest;
import org.apache.maven.shared.release.policy.version.VersionPolicyResult;
import org.eclipse.sisu.Description;

@Singleton
@Named("YYMZVersionPolicy")
@Description("A version policy following the X.Y.Z format, where X is the 2-digit year, "
        + "Y is the current month (no leading zero) and Z is an optional increment, "
        + "showing the subsequent release for the month and year depicted by Y and X")
public class YYMZVersionPolicy implements VersionPolicy {

    private static final String DEVELOPMENT_VERSION_POSTFIX = "-SNAPSHOT";

    private final int currentYear;
    private final int currentMonth;

    public YYMZVersionPolicy() {
        this.currentYear = DateUtils.getCurrentYearTwoDigits();
        this.currentMonth = DateUtils.getCurrentMonth();
    }

    @Override
    public VersionPolicyResult getReleaseVersion(VersionPolicyRequest versionPolicyRequest) {
        String currentVersion = versionPolicyRequest.getVersion();
        return calculateNextVersion(currentVersion, true);
    }

    @Override
    public VersionPolicyResult getDevelopmentVersion(VersionPolicyRequest versionPolicyRequest) {
        String currentVersion = versionPolicyRequest.getVersion();
        return calculateNextVersion(currentVersion, false);
    }

    private VersionPolicyResult calculateNextVersion(String currentVersion, boolean isProductionRelease) {
        String currentWithoutPostfix = currentVersion.replaceAll(DEVELOPMENT_VERSION_POSTFIX, "");
        String[] parts = currentWithoutPostfix.split("\\.");
        if (parts.length < 2) {
            throw new UnsupportedVersionException("Unsupported version: " + currentVersion);
        }

        int patch = 0;
        if (Integer.parseInt(parts[0]) == currentYear && Integer.parseInt(parts[1]) == currentMonth) {
            boolean currentIsSnapshot = currentVersion.contains(DEVELOPMENT_VERSION_POSTFIX);
            if (parts.length == 3) {
                if (currentIsSnapshot && isProductionRelease) {
                    patch = Integer.parseInt(parts[2]);
                } else {
                    patch = Integer.parseInt(parts[2]) + 1;
                }
            } else {
                if (!currentIsSnapshot) {
                    patch = 1;
                }
            }
        }

        YYMZVersion version = patch > 0
                ? new YYMZVersion(currentYear, currentMonth, patch)
                : new YYMZVersion(currentYear, currentMonth);

        String postfix = isProductionRelease ? "" : DEVELOPMENT_VERSION_POSTFIX;
        String nextVersion = version + postfix;

        return new VersionPolicyResult().setVersion(nextVersion);
    }
}
