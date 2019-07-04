package com.wizag.mobileapprovals.network;

import com.wizag.mobileapprovals.models.AdminDocsModel;
import com.wizag.mobileapprovals.models.UserModel;

import retrofit2.Call;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface APiInterface {

    @GET("documents")
    Call<AdminDocsModel> getAdminDocs();

    @GET("groupDocs")
    Call<UserModel> getUserDocs();

}
