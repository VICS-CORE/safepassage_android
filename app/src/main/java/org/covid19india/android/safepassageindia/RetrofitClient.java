package org.covid19india.android.safepassageindia;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    private static Retrofit retrofit = null;

    public static Retrofit getClient(String baseUrl) {
        if (retrofit == null) {

//            CookieManager cookieManager = new CookieManager();
//            cookieManager.setCookiePolicy(CookiePolicy.ACCEPT_ALL);
//
//            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
//            logging.setLevel(HttpLoggingInterceptor.Level.BODY);
//            long connection_time_in_min = 0;
//            long write_time_in_min = 0;
//            long read_time_in_min = 0;
//            OkHttpClient.Builder oktHttpClient = new OkHttpClient.Builder()
//                    .connectTimeout(connection_time_in_min, TimeUnit.MINUTES)
//                    .writeTimeout(write_time_in_min, TimeUnit.MINUTES)
//                    .readTimeout(read_time_in_min, TimeUnit.MINUTES)
//                    .cookieJar(new JavaNetCookieJar(cookieManager));
//            oktHttpClient.addInterceptor(logging);

            //.client(oktHttpClient.build())
            retrofit = new Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}
