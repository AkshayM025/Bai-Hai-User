package com.techno.baihai.api;

import com.techno.baihai.model.CoinHistoryModel;
import com.techno.baihai.model.GetBaiHaiTransactionModel;
import com.techno.baihai.model.GetCategoryModel;
import com.techno.baihai.model.GetUserFoundnTransModel;

import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface APIInterface {


    @GET("insert_chat?")
    Call<ResponseBody> insertChat(@Query("receiver_id") String receiver_id,
                                  @Query("sender_id") String sender_id,
                                  @Query("product_id") String product_id,
                                  @Query("chat_message") String chat_message);//,
//                                  @Query("chat_image") String chat_image);


    @GET("get_chat?")
    Call<ResponseBody> getChat(@Query("receiver_id") String receiver_id,
                               @Query("sender_id") String sender_id,
                               @Query("product_id") String product_id);



    @GET("social_login?")
    Call<ResponseBody> socialLogin(@Query("name") String name,
                                   @Query("email") String email,
                                   @Query("mobile") String mobile,
                                   @Query("social_id") String social_id,
                                   @Query("register_id") String register_id,
                                   @Query("image") String image,
                                   @Query("lat") String lat,
                                   @Query("lon") String lon);


    @GET("stripe_payment?")
    Call<ResponseBody> stripe_payment(@Query("user_id") String user_id,
                                      @Query("trans_id") String trans_id,
                                      @Query("amount") String amount,
                                      @Query("status") String status,
                                      @Query("token") String token,
                                      @Query("currency") String currency);


    @GET("get_foundation_user_donation?")
    Call<GetUserFoundnTransModel> get_foundation_user_donation(@Query("user_id") String user_id);


    @GET("get_baihai_user_donation?")
    Call<GetBaiHaiTransactionModel> get_baihai_user_donation(@Query("user_id") String user_id);


    @FormUrlEncoded
    @POST("get_category?")
    Call<GetCategoryModel> get_category(@FieldMap Map<String, String> params);

    @GET("get_coin_history?")
    Call<CoinHistoryModel> get_coin_history(@Query("user_id") String user_id);


}


