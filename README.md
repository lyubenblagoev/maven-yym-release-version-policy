# Year and Month Based Version Policy for Maven Release Plugin

# Introduction

When using the [maven-release-plugin](https://maven.apache.org/maven-release/maven-release-plugin/) you can add custom rules on how the next version is determined. Such a "next version calculation" is called a Version Policy.

This is a version policy that enforces a format based on the current date in the format YY.M[.Z], where YY is the two-digit representation of the current year, M is the current month (without leading zero when the month is between 1 and 9) and an optional Z version, representing subsequent releases after the initial release for the month.

This is an example of a typical release sequence started with 4 releases in February 2023, followed by 3 releases in March and 2 in April: 

  * February 2023:
    * 23.2
    * 23.2.1
    * 23.2.2
    * 23.2.3
  * March 2023
    * 23.3
    * 23.3.1
    * 23.3.2
  * April 2023
    * 23.4
    * 23.4.1

After each release the repository is prepared for the next development iteration and changes the version in the POM to from the production version to a new X-SNAPSHOT version. The snapshot version is used and remains unchanged until the next release.

# Next version calculation

Next version is always calculated based on the current year and month. If the values match the values in the current version, the patch version (Z-version) is examined. Otherwise, the initial release for the current month is released (for example 23.2).

## Next production release

The version of the next production release is either the initial release for the month (as described in the above paragraph) or the version of the current snapshot release without the -SNAPSHOT postfix. 

For example consider the below releases: 

... 23.1.6-SNAPSHOT -> release 23.2 > 23.2.1-SNAPSHOT > release 23.2.1 > 23.2.2-SNAPSHOT > release 23.2.2 > 23.2.3-SNAPSHOT > release 23.3 ...    

# Requirements
This version policy requires version 3.0.0 or newer of the [maven-release-plugin](https://maven.apache.org/maven-release/maven-release-plugin/) to be used.

# Configuration

The default configuration assumes a version tag that is a combination of the project.artifactId and project.version: `@{project.artifactId}-@{project.version}`. If we want a version tag that is just the version we can change the `tagNameFormat` property to `@{project.version}`.

To configure the maven-release-plugin with the YYMZVersionPolicy and version tag that is just the version number, set the following configuration in pom.xml:

```xml

<plugin>
    <groupId>org.apache.maven.plugins</groupId>
    <artifactId>maven-release-plugin</artifactId>
    <version>3.0.0</version>
    <dependencies>
        <dependency>
            <groupId>com.lyubenblagoev</groupId>
            <artifactId>maven-yymz-release-version-policy</artifactId>
            <version>1.0.0</version>
        </dependency>
    </dependencies>
    <configuration>
        <tagNameFormat>@{project.version}</tagNameFormat>
        <projectVersionPolicyId>YYMZVersionPolicy</projectVersionPolicyId>
    </configuration>
</plugin>
```
