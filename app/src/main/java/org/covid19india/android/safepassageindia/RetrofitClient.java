package org.covid19india.android.safepassageindia;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;

import java.util.Dictionary;
import java.util.Hashtable;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    private static Retrofit retrofit = null;

    public static void storeCookie(Context context, String cookie) {
        SharedPreferences sf = context.getSharedPreferences("session_cookie", Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = sf.edit();
        edit.clear();
        edit.putString("Set-Cookie", cookie);
        edit.putString("Expiry", getDate(cookie));
        edit.apply();
    }

    public static void removeCookie(Context context) {
        SharedPreferences sf = context.getSharedPreferences("session_cookie", Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = sf.edit();
        edit.clear();
        edit.apply();
    }

    private static String getDate(String cookie) {
        Dictionary<String, Integer> months = new Hashtable();
        months.put("January", 1);
        months.put("February", 2);
        months.put("March", 3);
        months.put("April", 4);
        months.put("May", 5);
        months.put("June", 6);
        months.put("July", 7);
        months.put("August", 8);
        months.put("September", 9);
        months.put("October", 10);
        months.put("November", 11);
        months.put("December", 12);

        cookie = cookie.substring(cookie.indexOf("expires="));
        cookie = cookie.substring(cookie.indexOf(",") + 2, cookie.indexOf(";") - 4);
        int day = Integer.parseInt(cookie.substring(0, cookie.indexOf(" ")));
        cookie = cookie.substring(cookie.indexOf(" ") + 1);
        int month = months.get(cookie.substring(0, cookie.indexOf(" ")));
        cookie = cookie.substring(cookie.indexOf(" ") + 1);
        int year = Integer.parseInt(cookie.substring(0, cookie.indexOf(" ")));
        cookie = cookie.substring(cookie.indexOf(" ") + 1);
        int hour = Integer.parseInt(cookie.substring(0, cookie.indexOf(":")));
        cookie = cookie.substring(cookie.indexOf(":") + 1);
        int min = Integer.parseInt(cookie.substring(0, cookie.indexOf(":")));
        cookie = cookie.substring(cookie.indexOf(":") + 1);
        int sec = Integer.parseInt(cookie.substring(0));
        return day + "/" + month + "/" + year + " " + hour + ":" + min + ":" + sec;
    }

    public static boolean isEmpty(Context context) {
        SharedPreferences sf = context.getSharedPreferences("session_cookie", Context.MODE_PRIVATE);
        String cookie = sf.getString("Set-Cookie", "NA");
        return cookie.equals("NA") || cookie.isEmpty();
    }

    public static boolean isExpired(Context context) {
        String expiry = getExpiry(context);
        if (expiry.equals("NA")) {
            return false;
        } else {
            return checkDate(expiry);
        }
    }

    public static String getExpiry(Context context) {
        SharedPreferences sf = context.getSharedPreferences("session_cookie", Context.MODE_PRIVATE);
        return sf.getString("Expiry", "NA");
    }

    private static boolean checkDate(String date) {
        int day, month, year, hour, min, sec;
        day = Integer.parseInt(date.substring(0, date.indexOf("/")));
        date = date.substring(date.indexOf("/") + 1);
        //Subtract month by 1 as Calendar.JANUARY starts with 0
        month = Integer.parseInt(date.substring(0, date.indexOf("/"))) - 1;
        date = date.substring(date.indexOf("/") + 1);

        year = Integer.parseInt(date.substring(0, date.indexOf(" ")));
        date = date.substring(date.indexOf(" ") + 1);

        hour = Integer.parseInt(date.substring(0, date.indexOf(":")));
        date = date.substring(date.indexOf(":") + 1);

        min = Integer.parseInt(date.substring(0, date.indexOf(":")));
        date = date.substring(date.indexOf(":") + 1);

        sec = Integer.parseInt(date);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            android.icu.util.Calendar current = android.icu.util.Calendar.getInstance();
            android.icu.util.Calendar previous = android.icu.util.Calendar.getInstance();
            previous.set(year, month, day, hour, min, sec);
            return current.compareTo(previous) >= 0;

        } else {
            java.util.Calendar current = java.util.Calendar.getInstance();
            java.util.Calendar previous = java.util.Calendar.getInstance();
            previous.set(year, month, day, hour, min, sec);
            return current.compareTo(previous) >= 0;
        }
    }


    public static Retrofit getClient(String baseUrl) {
        if (retrofit == null) {
            //.client(oktHttpClient.build())
//            OkHttpClient.Builder builder = new OkHttpClient.Builder();
//            builder.addInterceptor(new Interceptor() {
//                @Override
//                public Response intercept(Chain chain) throws IOException {
//
//                    Request request =chain.request();
//                    Request newRequest = request.newBuilder().header("Set-Cookie","");
//
//                    return null;
//                }
//            });
            retrofit = new Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .client(new OkHttpClient())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}
