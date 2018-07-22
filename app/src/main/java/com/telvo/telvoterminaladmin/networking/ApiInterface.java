package com.telvo.telvoterminaladmin.networking;

import com.telvo.telvoterminaladmin.model.admin.action.DepositAgentRequest;
import com.telvo.telvoterminaladmin.model.admin.action.DepositAgentResponse;
import com.telvo.telvoterminaladmin.model.admin.action.WithdrawAgentRequest;
import com.telvo.telvoterminaladmin.model.admin.action.WithdrawAgentResponse;
import com.telvo.telvoterminaladmin.model.admin.action.WithdrawShopRequest;
import com.telvo.telvoterminaladmin.model.admin.action.WithdrawShopResponse;
import com.telvo.telvoterminaladmin.model.admin.action.CreateUserResponse;
import com.telvo.telvoterminaladmin.model.admin.history.home.HomeWithdrawHistoryResponse;
import com.telvo.telvoterminaladmin.model.admin.history.deposit.AdminDepositHistoryResponse;
import com.telvo.telvoterminaladmin.model.admin.history.withdraw.AdminWithdrawHistoryResponse;
import com.telvo.telvoterminaladmin.model.agent.action.NonSysWithdrawRequest;
import com.telvo.telvoterminaladmin.model.agent.action.NonSysWithdrawResponse;
import com.telvo.telvoterminaladmin.model.login.LoginRequest;
import com.telvo.telvoterminaladmin.model.agent.action.ConfirmWithdrawRequest;
import com.telvo.telvoterminaladmin.model.agent.action.ConfirmWithdrawResponse;
import com.telvo.telvoterminaladmin.model.agent.action.DepositRequest;
import com.telvo.telvoterminaladmin.model.agent.action.DepositResponse;
import com.telvo.telvoterminaladmin.model.agent.action.WithdrawRequest;
import com.telvo.telvoterminaladmin.model.agent.action.WithdrawResponse;
import com.telvo.telvoterminaladmin.model.agent.history.AgentHistoryResponse;
import com.telvo.telvoterminaladmin.model.agent.login.LoginResponseAgent;
import com.telvo.telvoterminaladmin.model.shop.action.QrRequest;
import com.telvo.telvoterminaladmin.model.shop.action.QrResponse;
import com.telvo.telvoterminaladmin.model.shop.action.ShopBalanceResponse;
import com.telvo.telvoterminaladmin.model.shop.history.ShopHistoryResponse;
import com.telvo.telvoterminaladmin.model.shop.login.LoginResponseShop;
import com.telvo.telvoterminaladmin.model.admin.action.HomeWithdrawRequest;
import com.telvo.telvoterminaladmin.model.admin.action.HomeWithdrawResponse;
import com.telvo.telvoterminaladmin.model.admin.login.LoginResponseAdmin;

import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by invariant on 10/29/17.
 */

public interface ApiInterface {

    @POST("auth/adminLogin")
    Call<LoginResponseAgent> loginAsAgent(@Body LoginRequest loginRequest);

    @POST("auth/adminLogin")
    Call<LoginResponseShop> loginAsShop(@Body LoginRequest loginRequest);

    @POST("auth/adminLogin")
    Call<LoginResponseAdmin> loginAsAdmin(@Body LoginRequest loginRequest);

    @POST("agent/deposit")
    Call<DepositResponse> depositUser(@Header("Authorization") String authorization, @Body DepositRequest depositRequest);

    @POST("agent/withdraw")
    Call<WithdrawResponse> withdrawUser(@Header("Authorization") String authorization, @Body WithdrawRequest withdrawRequest);

    @POST("agent/nonSystemWithdraw")
    Call<NonSysWithdrawResponse> withdrawNonSysUser(@Header("Authorization") String authorization, @Body NonSysWithdrawRequest nonSysWithdrawRequest);

    @POST("agent/systemWithdraw")
    Call<ConfirmWithdrawResponse> confirmWithdraw(@Header("Authorization") String authorization, @Body ConfirmWithdrawRequest confirmWithdrawRequest);

    @POST("shop/generateQR")
    Call<QrResponse> generateQR(@Header("Authorization") String authorization, @Body QrRequest qrRequest);

    @POST("admin/homeWithdraws")
    Call<HomeWithdrawResponse> verifyHomeWithdraw(@Header("Authorization") String authorization, @Body HomeWithdrawRequest homeWithdrawRequest);

    @GET("agent/transactions")
    Call<AgentHistoryResponse> getDefaultAgentHistory(@Header("Authorization") String authorization);

    @GET("agent/transactions")
    Call<AgentHistoryResponse> getPreciseAgentHistory(@Header("Authorization") String authorization, @Query("start") String start, @Query("end") String end);

    @GET("shop/{id}")
    Call<ShopBalanceResponse> getShopBalance(@Header("Authorization") String authorization, @Path("id") String id);

    @GET("shop/payments/history")
    Call<ShopHistoryResponse> getDefaultShopHistory(@Header("Authorization") String authorization);

    @GET("shop/payments/history")
    Call<ShopHistoryResponse> getPreciseShopHistory(@Header("Authorization") String authorization, @Query("start") String start, @Query("end") String end);

    @GET("admin/homeWithdraws")
    Call<HomeWithdrawHistoryResponse> getHomeWithdraws(@Header("Authorization") String authorization, @Query("state") String state);

    @GET("admin/homeWithdraws")
    Call<HomeWithdrawHistoryResponse> getPreciseHomeWithdraws(@Header("Authorization") String authorization, @Query("state") String state, @Query("start") String start, @Query("end") String end);

    @POST("admin/agents/deposit")
    Call<DepositAgentResponse> depositAgent(@Header("Authorization") String authorization, @Body DepositAgentRequest depositAgentRequest);

    @POST("admin/agents/withdraw")
    Call<WithdrawAgentResponse> withdrawAgent(@Header("Authorization") String authorization, @Body WithdrawAgentRequest withdrawAgentRequest);

    @POST("admin/shops/withdraw")
    Call<WithdrawShopResponse> withdrawShop(@Header("Authorization") String authorization, @Body WithdrawShopRequest withdrawShopRequest);

    @GET("admin/deposits")
    Call<AdminDepositHistoryResponse> getDefaultAdminDepositHistory(@Header("Authorization") String authorization);

    @GET("admin/deposits")
    Call<AdminDepositHistoryResponse> getPreciseAdminDepositHistory(@Header("Authorization") String authorization, @Query("start") String start, @Query("end") String end);

    @GET("admin/withdraws")
    Call<AdminWithdrawHistoryResponse> getDefaultAdminWithdrawHistory(@Header("Authorization") String authorization);

    @GET("admin/withdraws")
    Call<AdminWithdrawHistoryResponse> getPreciseAdminWithdrawHistory(@Header("Authorization") String authorization, @Query("start") String start, @Query("end") String end);

    @Multipart
    @POST("admin/users")
    Call<CreateUserResponse> createUser(@Header("Authorization") String authorization,
                                        @PartMap Map<String, RequestBody> params,
                                        @Part MultipartBody.Part image,
                                        @Part MultipartBody.Part signImage);
}
