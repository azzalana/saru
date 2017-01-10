package su.mboh.asu.service;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import su.mboh.asu.entity.Category;
import su.mboh.asu.entity.New;

/**
 * Created by smktiufa on 17/12/16.
 */

public interface CategoryService {
    //menampilkan
    @GET("category")
    Call<List<Category>>getCategories();

    //mengambil data berdasarkan id
    @GET("category/{title}")
    Call<List<Category>>getCategories(@Path("title") int id);


    //mengirimdata
    @POST("category")
    Call<Category> postCategory(@Body Category category);


    //menghapusdata
    @DELETE("category/{id}")
    Call<Category> deleteCategory(@Path("id") int id);


    //mengeditdata
    @PUT("category/{id}")
    Call<Category> putCategory(@Path("id") int id, @Body Category category);

}
