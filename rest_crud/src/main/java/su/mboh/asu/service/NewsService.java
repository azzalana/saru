package su.mboh.asu.service;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import su.mboh.asu.entity.New;

/**
 * Created by smktiufa on 17/12/16.
 */

public interface NewsService {
    //menampilkan
    @GET("news")
    Call<List<New>>getNews();


    //mengambil data berdasarkan id
    @GET("news/{id}")
    Call<List<New>>getNews(@Path("id")int id);


    //mengirimdata
    @POST("news")
    Call<New> postNews (@Body New news);


    //menghapusdata
    @DELETE("news/{id}")
    Call<New> deleteNews (@Path("id") long id);


    //mengeditdata
    @PUT("news/{id}")
    Call<New> putNews (@Path("id") int id, @Body New news);

}
