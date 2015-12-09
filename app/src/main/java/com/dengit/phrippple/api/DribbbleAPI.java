package com.dengit.phrippple.api;

import com.dengit.phrippple.data.RequestTokenBody;
import com.dengit.phrippple.data.Shot;
import com.dengit.phrippple.data.TokenInfo;

import java.util.List;

import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Query;
import rx.Observable;

/**
 * Created by dengit on 15/12/7.
 */
public interface DribbbleAPI {

    String API_BASE_URL = "https://api.dribbble.com";
    String CLIENT_ID = "c5c94fd5886dbfddacd4bc3e62cd14b3456ebe17d8de208b27ce91224d951aec";
    String CLIENT_SECRET = "f6dba6bd84efd90a1fe29728e06f83d0bb8ad7841bd7ef9333f7271bc7f3d863";
    String AUTHORIZE_URL = String.format("https://dribbble.com/oauth/authorize?client_id=%s", CLIENT_ID);
    String TOKEN_URL = "https://dribbble.com/oauth/token";
    String CALLBACK_SCHEME = "phrippple";
    String CALLBACK_HOST = "phone-callback";


    @GET("/v1/shots")
    Observable<List<Shot>> getShots(@Query("access_token") String accessToken, @Query("page") int page);

    @POST(TOKEN_URL)
    Observable<TokenInfo> getToken(@Body RequestTokenBody body);
}