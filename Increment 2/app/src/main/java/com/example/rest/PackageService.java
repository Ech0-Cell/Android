package com.example.rest;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface PackageService {
    @GET("api/package")
    Call<List<Package>> getPackages();
}
