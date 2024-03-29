package com.example.greg.challenge.injection

import com.example.greg.challenge.model.repository.GithubApiService
import com.example.greg.challenge.model.repository.ResultsRepository
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@Module
internal class AppModule {

    @Singleton
    @Provides
    fun provideRetrofit(): Retrofit {
        return Retrofit.Builder()
            .addCallAdapterFactory(
                RxJava2CallAdapterFactory.create())
            .addConverterFactory(
                MoshiConverterFactory.create())
            .baseUrl(GithubApiService.GITHUB_API_BASE_URL)
            .build()
    }

    @Singleton
    @Provides
    fun provideGithubApiService(retrofit: Retrofit) : GithubApiService {
        return retrofit.create(GithubApiService::class.java)
    }

    @Singleton
    @Provides
    fun provideResultsRepository(githubApiService: GithubApiService) : ResultsRepository {
        return ResultsRepository(githubApiService)
    }

}