package com.example.u1arlyncotradoejercicio2tema5;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.SphericalUtil;

import java.text.NumberFormat;

public class Util {
    public static String formatDistanceBetween(LatLng point1, LatLng point2) {
        if (point1 == null || point2 == null) {//si no tiene valores
            return null;
        }
        NumberFormat numberFormat = NumberFormat.getNumberInstance();//configura el formato del numero
        double distance = Math.round(SphericalUtil.computeDistanceBetween(point1, point2));
        if (distance >= 1000) {
            numberFormat.setMaximumFractionDigits(1);
            return numberFormat.format(distance / 1000) + " Km";
        }
        return numberFormat.format(distance) + " m";
    }
}