package com.checkpoint.vaiol.mobileNerwork.packages;

import java.util.HashSet;
import java.util.Set;

public abstract class PackageFactory {
    //each operator can add your own package, and than get him by name
    private static Set<Package> packages = new HashSet<Package>();

    //default packages:
    private static Package basicPack = null;
    private static Package prepaidPack = null;
    private static Package unlimitedPack = null;

    public static Package getBasicPackage() {
        if (basicPack == null) {
            String name = "Basic Package";
            double feePerDay = 0;
            double feePerMonth = 0;
            double feeOutgoingCallConnect = 0.10;
            double feePerMinuteOnline = 0.10;
            double feePerMinuteOffline = 0.50;
            double feeMessage = 0.50;
            double feeInternetUsage = 12.3;
            basicPack = new Package(name, feePerDay, feePerMonth, feePerMinuteOnline, feePerMinuteOffline, feeMessage, feeInternetUsage, feeOutgoingCallConnect);
        }
        return basicPack;
    }

    public static Package getPrepaidPackage() {
        if (prepaidPack == null) {
            String name = "Prepaid Package";
            double feePerDay = 5;
            double feePerMonth = 0;
            double feeOutgoingCallConnect = 0;
            double feePerMinuteOnline = 0;
            double feePerMinuteOffline = 0;
            double feeMessage = 0;
            double feeInternetUsage = 0.99;
            prepaidPack = new Package(name, feePerDay, feePerMonth, feePerMinuteOnline, feePerMinuteOffline, feeMessage, feeInternetUsage, feeOutgoingCallConnect);
        }
        return prepaidPack;
    }

    public static Package getUnlimitedPackage() {
        if (unlimitedPack == null) {
            String name = "Unlimited Package";
            double feePerDay = 0;
            double feePerMonth = 300;
            double feeOutgoingCallConnect = 0;
            double feePerMinuteOnline = 0;
            double feePerMinuteOffline = 0;
            double feeMessage = 0;
            double feeInternetUsage = 0;
            return new Package(name, feePerDay, feePerMonth, feePerMinuteOnline, feePerMinuteOffline, feeMessage, feeInternetUsage, feeOutgoingCallConnect);
        }
        return unlimitedPack;
    }

    public static void addPackage(Package pack) {
        packages.add(pack);
    }

    public static Package getPackage(String packageName) {
        for (Package pack : packages) {
            if (pack.getName().equals(packageName)) {
                return pack;
            }
        }
        return null;
    }
}
