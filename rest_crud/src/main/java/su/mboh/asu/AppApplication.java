package su.mboh.asu;

import android.app.Application;

import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;
import android.util.Log;

import com.path.android.jobqueue.JobManager;
import com.path.android.jobqueue.config.Configuration;
import com.path.android.jobqueue.log.CustomLogger;
import static android.support.design.R.id.*;
import retrofit2.Retrofit;
/**
 * Created by smktiufa on 17/12/16.
 */

public class AppApplication extends Application {


    private String BASE_URL = "http://192.168.4.104:8080/";

    private static AppApplication instance;
    private Retrofit retrofit;
    private JobManager jobManager;


    @Override
    public void onCreate() {
        super.onCreate();

        instance = this;

        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(JacksonConverterFactory.create())
                .build();
        Configuration configuration = new Configuration.Builder(this)
                .customLogger(new CustomLogger() {
                    @Override
                    public boolean isDebugEnabled() {
                        return true;
                    }

                    @Override
                    public void d(String text, Object... args) {
                        Log.d("LOG JOB",String.format(text,args));
                    }

                    @Override
                    public void e(Throwable t, String text, Object... args) {
                        Log.e("LOG JOB",String.format(text,args));
                    }

                    @Override
                    public void e(String text, Object... args) {
                        Log.e("LOG JOB",String.format(text,args));
                    }
                })
                .maxConsumerCount(3)
                .minConsumerCount(1)
                .loadFactor(3)
                .consumerKeepAlive(120)
                .build();
        jobManager= new JobManager(this, configuration);

                }


    public static AppApplication getInstance() {
        return instance;
    }

    public Retrofit getRetrofit() {
        return retrofit;
    }
    public JobManager getJobManager(){
        return jobManager;
    }
}
