package com.openclassrooms.realestatemanager.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Philippe on 21/02/2018.
 */

public class Utils {

    /**
     * Conversion d'un prix d'un bien immobilier (Dollars vers Euros)
     * NOTE : NE PAS SUPPRIMER, A MONTRER DURANT LA SOUTENANCE
     *
     * @param dollars integer to convert
     * @return euro double value
     */
    public static double convertDollarToEuro(int dollars) {
        return (double) Math.round(dollars * 0.85226);
    }

    /**
     * Convert euro to dollar
     *
     * @param euro integer to convert
     * @return dollar double value
     */
    public static double convertEuroToDollar(int euro) {
        return (double) Math.round(euro * 1.1733);
    }


    /**
     * Conversion de la date d'aujourd'hui en un format plus approprié
     * NOTE : NE PAS SUPPRIMER, A MONTRER DURANT LA SOUTENANCE
     *
     * @return formatted date "dd/MM/yyyy"
     */
    public static String getTodayDate() {
        @SuppressLint("SimpleDateFormat") DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        return dateFormat.format(new Date());
    }

    /**
     * Vérification de la connexion réseau
     * NOTE : NE PAS SUPPRIMER, A MONTRER DURANT LA SOUTENANCE
     *
     * @param context context
     * @return boolean
     */
    public static Boolean isInternetAvailable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED;
    }

    public static long getLongFormatDate() {
        Calendar calendar = Calendar.getInstance();
        return calendar.getTime().getTime();
    }

    public static long getLongFormatDate(int years, int month, int day) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(years, month, day);
        return calendar.getTime().getTime();
    }

    public static String getLongToString(long longToConvert) {
        Date date = new Date(longToConvert);
        @SuppressLint("SimpleDateFormat") SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        return format.format(date);
    }
}
