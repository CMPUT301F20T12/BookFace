package com.example.bookface.Notifications;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface APIService {

    @Headers({
            "Content-Type:application/json",
            "Authorization:key=AAAAHSpcXKM:APA91bGiZzNHxHomEvzuy-MbIDc4RVZ1TLea_36APEK6ZKmjgH5rQ3DcOXgs-SI19rpd-6xTCUPpeYSTMuawIgjl9zhF7hgrox8EYbPlkTsuSuvmrMGORjzf-cB4X9jYtL2e0ZzlgbHa"
    })

    @POST("fcm/send")
    Call<MyResponse> sendNotification(@Body Sender body);

}
