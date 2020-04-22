package org.covid19india.android.safepassageindia;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface UserApi {
    String BASE_URL = "http://34.69.21.192";

    @GET("/user/")
    Call<UserList> getUsers(@Query("format") String format, @Query("user_phonenumber") String number, @Query("usertype") String type);

    @GET("/user/")
    Call<UserPassList> getUserPasses(@Query("format") String format, @Query("user_phonenumber") String number, @Query("usertype") String type);

    @GET("/user/")
    Call<PassList> getPasses(@Query("format") String format, @Query("user_phonenumber") String number, @Query("usertype") String type);
}