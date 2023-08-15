package com.malkinfo.janataaapp.managers.utils

import android.content.Context
import android.content.ContextWrapper
import android.util.Log
import com.malkinfo.janataaapp.R
import com.google.gson.GsonBuilder
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.malkinfo.janataaapp.constants.MyCommunityAppConstants
import com.malkinfo.janataaapp.constants.MyCommunityAppEnv
import com.malkinfo.janataaapp.managers.retrofit.*
import com.malkinfo.janataaapp.models.response.*
import com.malkinfo.janataaapp.models.response.Base.BaseApiResponse
import com.malkinfo.janataaapp.models.response.BloodGroup.BloodGroupRequestApiResponse
import com.malkinfo.janataaapp.models.response.BloodGroup.BloodRequestApiResponse
import com.malkinfo.janataaapp.models.response.Community.*
import com.malkinfo.janataaapp.models.response.Matrimonys.*
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class RetrofitManager(context: Context) : ContextWrapper(context) {

    companion object {
        private var INSTANCE: RetrofitManager? = null
        private lateinit var defaultRetrofit: Retrofit
        private var userRetrofit: Retrofit? = null

        fun getInstance(context: Context): RetrofitManager {
            if (INSTANCE == null) {
                initManager(context)
            }
            return INSTANCE!!;
        }

        private fun initManager(context: Context) {
            INSTANCE = RetrofitManager(context)

            val interceptor = Interceptor { chain ->
                val request = chain.request().newBuilder()
                    .build()

                chain.proceed(request)
            }

            val httpClient: OkHttpClient.Builder =
                OkHttpClient.Builder().addInterceptor(interceptor)
                    .addInterceptor(ConnectivityInterceptor(context))
                    .connectTimeout(100, TimeUnit.SECONDS)
                    .readTimeout(200, TimeUnit.SECONDS);

            if (!MyCommunityAppEnv.PROD_MODE.equals(context.getString(R.string.environment))) {
                val logInterceptor = HttpLoggingInterceptor()
                logInterceptor.level = HttpLoggingInterceptor.Level.BODY
                httpClient.addInterceptor(logInterceptor)
            }


            val gson = GsonBuilder()
                .registerTypeAdapter(LoginApiResponse::class.java, LoginApiResponse.LoginApiDeserializer())
                .registerTypeAdapter(RenewAccessApiResponse::class.java, RenewAccessApiResponse.RenewAccessApiDeserializer())
                .registerTypeAdapter(OtpApiResponse::class.java, OtpApiResponse.OtpApiDeserializer())
                .create()



            defaultRetrofit = Retrofit.Builder()
                .baseUrl(context.getString(R.string.base_url))
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(CoroutineCallAdapterFactory())
                .client(httpClient.build())
                .build()

        }
    }

    private fun getUserRetrofit(): Retrofit {

        if (userRetrofit == null) {
            val interceptor = Interceptor { chain ->
                val request = chain.request().newBuilder()
                    .header("Accept", "*/*")
                    .header("Content-Type" ,"application/json")
                    .header("Authorization" ,"Bearer " +getToken())
                    .build()
                chain.proceed(request)
            }
            val gson = GsonBuilder()
                .registerTypeAdapter(StateApiResponse::class.java, StateApiResponse.StateApiDeserializer())
                .registerTypeAdapter(DistrictApiResponse::class.java, DistrictApiResponse.DistrictApiDeserializer())
                .registerTypeAdapter(VillageApiResponse::class.java, VillageApiResponse.VillageApiDeserializer())
                .registerTypeAdapter(CasteApiResponse::class.java, CasteApiResponse.CasteApiDeserializer())
                .registerTypeAdapter(FileUploadApiResponse::class.java, FileUploadApiResponse.FileUploadApiDeserializer())
                .registerTypeAdapter(SignUpApiResponse::class.java, SignUpApiResponse.SignUpApiDeserializer())
                .registerTypeAdapter(IdentityApiResponse::class.java, IdentityApiResponse.IdentityCardApiDeserializer())
                .registerTypeAdapter(FeatureEnableApiResponse::class.java, FeatureEnableApiResponse.FeatureEnableApiDeserializer())
                .registerTypeAdapter(GroupApiResponse::class.java, GroupApiResponse.GroupApiDeserializer())
                .registerTypeAdapter(JoinGroupApiResponse::class.java, JoinGroupApiResponse.JoinGroupApiDeserializer())
                .registerTypeAdapter(GroupPostApiResponse::class.java, GroupPostApiResponse.GroupPostApiDeserializer())
                .registerTypeAdapter(BaseApiResponse::class.java, BaseApiResponse.BaseApiDeserializer())
                .registerTypeAdapter(GroupMembersApiResponse::class.java, GroupMembersApiResponse.AboutGroupApiDeserializer())
                .registerTypeAdapter(ViewPostApiResponse::class.java, ViewPostApiResponse.ViewPostApiDeserializer())
                .registerTypeAdapter(CommentApiResponse::class.java, CommentApiResponse.CommentApiDeserializer())
                .registerTypeAdapter(AddCommentApiResponse::class.java, AddCommentApiResponse.AddCommentApiDeserializer())
                .registerTypeAdapter(ReportPostApiResponse::class.java, ReportPostApiResponse.ReportPostApiDeserializer())
                .registerTypeAdapter(WritePostApiResponse::class.java, WritePostApiResponse.writePostApiDeserializer())
                .registerTypeAdapter(UserDetailsApiResponse::class.java, UserDetailsApiResponse.userDetailsApiDeserializer())
                .registerTypeAdapter(FollowerApiResponse::class.java, FollowerApiResponse.FollowerApiDeserializer())
                .registerTypeAdapter(ProfilePostApiResponse::class.java, ProfilePostApiResponse.ProfilePostApiDeserializer())
                .registerTypeAdapter(MeetingApiResponse::class.java, MeetingApiResponse.MeetingApiDeserializer())
                .registerTypeAdapter(BloodGroupRequestApiResponse::class.java, BloodGroupRequestApiResponse.BloodGroupApiDeserializer())
                .registerTypeAdapter(MatrimonyRegistrationDetailsApiResponse::class.java, MatrimonyRegistrationDetailsApiResponse.MatrimonyRegistrationApiDeserializer())
                .registerTypeAdapter(CityApiResponse::class.java, CityApiResponse.CityApiDeserializer())
                .registerTypeAdapter(MatchProfileApiResponse::class.java, MatchProfileApiResponse.MatchProfileApiDeserializer())
                .registerTypeAdapter(MatchProfileDetailsApiResponse::class.java, MatchProfileDetailsApiResponse.MatchProfileDetailsApiDeserializer())
                .registerTypeAdapter(ShortlistProfileApiResponse::class.java, ShortlistProfileApiResponse.ShortlistProfileApiDeserializer())
                .registerTypeAdapter(BlockedProfileApiResponse::class.java, BlockedProfileApiResponse.BlockedProfileApiDeserializer())
                .registerTypeAdapter(MatrimonyProfileApiResponse::class.java, MatrimonyProfileApiResponse.MatrimonyProfileDetailsApiDeserializer())
                .registerTypeAdapter(MatrimonyUserApiResponse::class.java, MatrimonyUserApiResponse.MatrimonyUserDetailsApiDeserializer())
                .registerTypeAdapter(BloodRequestApiResponse::class.java, BloodRequestApiResponse.BloodRequestApiDeserializer())
                .registerTypeAdapter(CountryApiResponse::class.java, CountryApiResponse.CountryApiDeserializer())
                .registerTypeAdapter(MandalApiResponse::class.java, MandalApiResponse.MandalApiDeserializer())
                .registerTypeAdapter(PartnerPreferenceApiResponse::class.java, PartnerPreferenceApiResponse.PartnerPreferenceApiDeserializer())
                .registerTypeAdapter(MessageApiResponse::class.java, MessageApiResponse.MessageApiDeserializer())
                .registerTypeAdapter(MessageListApiResponse::class.java, MessageListApiResponse.MessageListApiDeserializer())
                .registerTypeAdapter(ChatListResponse::class.java, ChatListResponse.ChatListApiDeserializer())
                .registerTypeAdapter(HomeGroupPostApiResponse::class.java, HomeGroupPostApiResponse.homeAllPostDeserializer())
                .registerTypeAdapter(HomeAllGroupApiResponse::class.java, HomeAllGroupApiResponse.HomeAllGroupApiDeserializer())
                .registerTypeAdapter(UserApiResponse::class.java,
                    UserApiResponse.UserApiDeserializer())
                .create()

            val httpClient: OkHttpClient.Builder =
                OkHttpClient.Builder().addInterceptor(interceptor)
                    .addInterceptor(ConnectivityInterceptor(baseContext))
                    .connectTimeout(100, TimeUnit.SECONDS)
                    .readTimeout(200, TimeUnit.SECONDS);

            if (!MyCommunityAppEnv.PROD_MODE.equals(baseContext.getString(R.string.environment))) {
                val logInterceptor = HttpLoggingInterceptor()
                logInterceptor.level = HttpLoggingInterceptor.Level.BODY
                httpClient.addInterceptor(logInterceptor)
            }

            userRetrofit = Retrofit.Builder()
                .baseUrl(baseContext.getString(R.string.base_url)) //+ context.getString(R.string.url_version))
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(CoroutineCallAdapterFactory())
                .client(httpClient.build())
                .build()
        }
        Log.d("mTag","getToken = ${getToken()}")

        return userRetrofit!!
    }

    fun getToken() : String ? {
        return SharedPrefManager.getInstance(baseContext).getPreference(MyCommunityAppConstants.AUTH_TOKEN)
    }
    fun getLaunchApi() : LaunchApi {
        return defaultRetrofit.create(LaunchApi::class.java)
    }

    fun getBaseApi(): BaseApi{
        return getUserRetrofit().create(BaseApi::class.java)
    }

    fun getUserApi() : UserApi {
        return getUserRetrofit().create(UserApi::class.java)
    }

    fun getCommunityApi() : CommunityApi {
        return getUserRetrofit().create(CommunityApi::class.java)
    }

    fun getBloodGroupApi() : BloodGroupApi {
        return getUserRetrofit().create(BloodGroupApi::class.java)
    }

    fun getMatrimonyApi() : MatrimonyApi {
        return getUserRetrofit().create(MatrimonyApi::class.java)
    }

    fun clearAll() {
        INSTANCE = null
        userRetrofit == null
    }

    fun logout() {
        userRetrofit = null
    }

}