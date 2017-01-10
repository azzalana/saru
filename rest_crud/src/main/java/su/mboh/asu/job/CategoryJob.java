package su.mboh.asu.job;

import android.util.Log;

import com.path.android.jobqueue.Job;
import com.path.android.jobqueue.Params;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import retrofit2.Response;
import su.mboh.asu.AppApplication;
import su.mboh.asu.entity.Category;
import su.mboh.asu.entity.New;
import su.mboh.asu.service.CategoryService;
import su.mboh.asu.service.NewsService;

/**
 * Created by smktiufa on 17/12/16.
 */

public class CategoryJob extends Job {

    public static final int GET_ALL = 100;
    public static final int GET_BY_TITLE = 200;
    public static final int POST = 300;
    public static final int PUT = 400;
    public static final int DELETE = 500;

    private int id;
    private int status;
    private Category category;

    public CategoryJob(int id, int status, Category category) {
        super(new Params(1).requireNetwork().persist());
        this.id = id;
        this.status = status;
        this.category = category;

    }



    @Override
    public void onAdded() {
        Log.d(getClass().getSimpleName(), "news job added");
        EventBus.getDefault().post(new EventMessage(id, EventMessage.ADD, null));
    }

    @Override
    public void onRun() throws Throwable {
        Log.d(getClass().getSimpleName(), "news job running");

        CategoryService service = AppApplication.getInstance()
                .getInstance()
                .getRetrofit()
                .create(CategoryService.class);


        Log.d(getClass().getSimpleName(), "job status " + status);

        switch (status) {
            case GET_ALL:
                Response<List<Category>> responseGetAll = service.getCategories().execute();
                Log.d(getClass().getSimpleName(), "job get All");
                if (responseGetAll.isSuccessful()) {
                    Log.d(getClass().getSimpleName(), "response news size " + responseGetAll.body());
                    EventBus.getDefault().post(new EventMessage(id, EventMessage.SUCCESS, responseGetAll.body()));
                } else {
                    Log.d(getClass().getSimpleName(), "News job erorr");
                    EventBus.getDefault().post(new EventMessage(id, EventMessage.ERROR, responseGetAll.body()));
                }
                break;
            case POST:
                Response<Category> responsePost = service.postCategory(category).execute();
                if (responsePost.isSuccessful()) {
                    Log.d(getClass().getSimpleName(), "category job succes " + responsePost.body());
                    EventBus.getDefault().post(new EventMessage(id, EventMessage.SUCCESS, responsePost.body()));
                } else {
                    Log.d(getClass().getSimpleName(), "category job error");
                    EventBus.getDefault().post(new EventMessage(id, EventMessage.ERROR, responsePost.body()));
                }
                break;
            case PUT:
                Response<Category> responsePut = service.putCategory(category.getId(), category).execute();
                if (responsePut.isSuccessful()) {
                    Log.d(getClass().getSimpleName(), "update succes"+category.getId());
                    EventBus.getDefault().post(new EventMessage(id, EventMessage.SUCCESS, responsePut.body()));
                } else {
                    Log.d(getClass().getSimpleName(), "update erorr"+category.getId());
                    EventBus.getDefault().post(new EventMessage(id, EventMessage.ERROR, responsePut.body()));
                }
                break;
            case DELETE:
                Response<Category> responseDelete = service.deleteCategory(category.getId()).execute();
                if (responseDelete.isSuccessful()) {
                    Log.d(getClass().getSimpleName(), "delete succes"+category.getId());
                    EventBus.getDefault().post(new EventMessage(id, EventMessage.SUCCESS, responseDelete.body()));
                } else {
                    Log.d(getClass().getSimpleName(), "delete erorr"+category.getId());
                    EventBus.getDefault().post(new EventMessage(id, EventMessage.ERROR, responseDelete.body()));
                }
                break;
        }


    }

    @Override
    protected void onCancel() {
        Log.d(getClass().getSimpleName(), "response canceled");
        EventBus.getDefault().post(new EventMessage(id, EventMessage.ERROR, null));
    }

    @Override
    protected boolean shouldReRunOnThrowable(Throwable throwable) {
        return false;
    }
}
