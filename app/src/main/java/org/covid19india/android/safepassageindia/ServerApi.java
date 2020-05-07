package org.covid19india.android.safepassageindia;

import org.covid19india.android.safepassageindia.model.PassList;
import org.covid19india.android.safepassageindia.model.UserList;
import org.covid19india.android.safepassageindia.model.UserPassList;

import java.util.List;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface ServerApi {
    String BASE_URL = "http://34.69.21.192";

    @GET("/user/")
    Call<UserList> getUsers(@Query("format") String format, @Query("user_phonenumber") String number, @Query("usertype") String type);

    @GET("/user/")
    Call<UserPassList> getUserPasses(@Query("format") String format, @Query("user_phonenumber") String number, @Query("usertype") String type);

    @GET("/user/")
    Call<PassList> getPasses(@Query("format") String format, @Query("user_phonenumber") String number, @Query("usertype") String type);

    @Multipart
    @POST("/sessionLogin/")
    Call<List<String>> sessionLogin(@Part("idToken") RequestBody idToken);
}