package com.checkpoint.vaiol.mobileNerwork.packages;

public class Package {
    private String name; //unique id
    private double feePerDay;
    private double feePerMonth;
    private double feePerMinuteOnline;
    private double feePerMinuteOffline;
    private double feeMessage;
    private double feeInternetUsage; //per Minute
    private double feeOutgoingCallConnect;
    private int bonusMinutePerMonth;
    private int bonusMessagePerMonth;

    public Package(String name, double feePerDay, double feePerMonth, double feePerMinuteOnline, double feePerMinuteOffline, double feeMessage, double feeInternetUsage, double feeOutgoingCallConnect) {
        this.name = name;
        this.feePerDay = feePerDay; //hrn
        this.feePerMonth = feePerMonth; //hrn
        this.feePerMinuteOnline = feePerMinuteOnline; //hrn
        this.feePerMinuteOffline = feePerMinuteOffline; //hrn
        this.feeMessage = feeMessage; //hrn
        this.feeInternetUsage = feeInternetUsage; //hrn
        this.feeOutgoingCallConnect = feeOutgoingCallConnect; //hrn
    }

    public String getName() {
        return name;
    }

    public double getFeePerDay() {
        return feePerDay;
    }

    public double getFeePerMonth() {
        return feePerMonth;
    }

    public double getFeePerMinuteOnline() {
        return feePerMinuteOnline;
    }

    public double getFeePerMinuteOffline() {
        return feePerMinuteOffline;
    }

    public double getFeeMessage() {
        return feeMessage;
    }

    public double getFeeInternetUsage() {
        return feeInternetUsage;
    }

    public double getFeeOutgoingCallConnect() {
        return feeOutgoingCallConnect;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Package aPackage = (Package) o;

        if (Double.compare(aPackage.feeInternetUsage, feeInternetUsage) != 0) return false;
        if (Double.compare(aPackage.feeMessage, feeMessage) != 0) return false;
        if (Double.compare(aPackage.feePerDay, feePerDay) != 0) return false;
        if (Double.compare(aPackage.feePerMinuteOffline, feePerMinuteOffline) != 0) return false;
        if (Double.compare(aPackage.feePerMinuteOnline, feePerMinuteOnline) != 0) return false;
        if (Double.compare(aPackage.feePerMonth, feePerMonth) != 0) return false;
        if (name != null ? !name.equals(aPackage.name) : aPackage.name != null) return false;

        return true;
    }

    @Override
    public String toString() {
        return "Package{" +
                "name='" + name + '\'' +
                ", feePerDay=" + feePerDay +
                ", feePerMonth=" + feePerMonth +
                ", feePerMinuteOnline=" + feePerMinuteOnline +
                ", feePerMinuteOffline=" + feePerMinuteOffline +
                ", feeMessage=" + feeMessage +
                ", feeInternetUsage=" + feeInternetUsage +
                '}';
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = name.hashCode();
        temp = Double.doubleToLongBits(feePerDay);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(feePerMonth);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(feePerMinuteOnline);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(feePerMinuteOffline);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(feeMessage);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(feeInternetUsage);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(feeOutgoingCallConnect);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }

}
