package com.dajudge.buql.postgis.types;

public class Point {
    private double x;
    private double y;

    public Point() {
    }

    public Point(final double x, final double y) {
        this.x = x;
        this.y = y;
    }

    public void setX(final double x) {
        this.x = x;
    }

    public void setY(final double y) {
        this.y = y;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }
}
