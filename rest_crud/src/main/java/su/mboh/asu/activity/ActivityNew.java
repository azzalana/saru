package su.mboh.asu.activity;

import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.path.android.jobqueue.JobManager;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import su.mboh.asu.*;
import su.mboh.asu.adapter.NewAdapter;
import su.mboh.asu.entity.Category;
import su.mboh.asu.entity.New;
import su.mboh.asu.job.CategoryJob;
import su.mboh.asu.job.EventMessage;
import su.mboh.asu.job.NewsJob;


/**
 * Created by smktiufa on 16/12/16.
 */

public class ActivityNew extends AppCompatActivity {

    private int PROCCES_ID_GET_ALL_NEWS = 1;
    private int PROCCES_ID_POST_NEWS = 2;
    private int PROCCES_ID_PUT_NEWS = 3;
    private int PROCCES_ID_DELETE_NEWS = 4;

    private int PROCCES_ID_GET_ALL_CATEGORY = 20;

    private Spinner spinCategory;

    private RecyclerView recyclerNew;

    private NewAdapter newAdapter;

    private EditText editTitle;
    private List<Category>categories = new ArrayList<>();
    private EditText editContent;
    private New news = null;
    private ProgressDialog progresDialog;

    private JobManager jobManager;
    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new);

        recyclerNew = (RecyclerView) findViewById(R.id.recycler_menu);
        editTitle = (EditText) findViewById(R.id.edit_title);
        editContent = (EditText) findViewById(R.id.edit_content);

        spinCategory = (Spinner) findViewById(R.id.spin_category);
        newAdapter = new NewAdapter(this);

        jobManager = AppApplication.getInstance().getJobManager();

        recyclerNew.setLayoutManager(new LinearLayoutManager(this));
        recyclerNew.setItemAnimator(new DefaultItemAnimator());
        recyclerNew.setAdapter(newAdapter);

        progresDialog =new ProgressDialog(this);
        progresDialog.setTitle(R.string.app_name);
        progresDialog.setMessage(getString(R.string.wait));

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.menu_search).getActionView();

        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        searchView.setOnCloseListener(new SearchView.OnCloseListener(){
            @Override
            public boolean onClose(){

                newAdapter.clearNew();

                return false;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.menu_save){
            saveNew();

        } else if (id == R.id.menu_refresh) {
            jobManager.addJobInBackground(new NewsJob(PROCCES_ID_GET_ALL_NEWS, NewsJob.GET_ALL, null));

        }
        return true;
    }

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        jobManager.addJobInBackground(new CategoryJob(PROCCES_ID_GET_ALL_CATEGORY, CategoryJob.GET_ALL,null));
        jobManager.addJobInBackground(new NewsJob(PROCCES_ID_GET_ALL_NEWS, NewsJob.GET_ALL,null));
    }

    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(EventMessage message){

        if (message.getStatus() == EventMessage.ADD){
            if (message.getId() == PROCCES_ID_GET_ALL_NEWS){
                progresDialog.show();
            }
        }

        else if (message.getStatus() == EventMessage.SUCCESS){

            if (message.getId() == PROCCES_ID_GET_ALL_NEWS){
                progresDialog.dismiss();

                List<New> newsList = (List<New>) message.getContent();
                newAdapter.clearNew();
                newAdapter.addNewses(newsList);
            }

            else if (message.getId() == PROCCES_ID_POST_NEWS){
                Toast.makeText(this,"Posting news success", Toast.LENGTH_LONG).show();
                jobManager.addJobInBackground(new NewsJob(PROCCES_ID_GET_ALL_NEWS, NewsJob.GET_ALL, null));
            }

            else if (message.getId() == PROCCES_ID_PUT_NEWS){
                Toast.makeText(this,"Update news success", Toast.LENGTH_LONG).show();
                jobManager.addJobInBackground(new NewsJob(PROCCES_ID_GET_ALL_NEWS, NewsJob.GET_ALL, null));
            }

            else if (message.getId() == PROCCES_ID_DELETE_NEWS){
                Toast.makeText(this,"Delete news success", Toast.LENGTH_LONG).show();
                jobManager.addJobInBackground(new NewsJob(PROCCES_ID_GET_ALL_NEWS, NewsJob.GET_ALL, null));
            }

            else if (message.getId() == PROCCES_ID_GET_ALL_CATEGORY){

                categories = (List<Category>) message.getContent();

                ArrayAdapter<Category> arrayAdapter = new ArrayAdapter<Category>(this, android.R.layout.simple_dropdown_item_1line, categories);
                spinCategory.setAdapter(arrayAdapter);
            }

        }

        else if (message.getStatus() == EventMessage.ERROR){
            if (message.getId() == PROCCES_ID_GET_ALL_NEWS){
                progresDialog.dismiss();
            }

            else if (message.getId() == PROCCES_ID_POST_NEWS){
                progresDialog.dismiss();
                Toast.makeText(this,"Posting news failed", Toast.LENGTH_LONG).show();
            }

            else if (message.getId() == PROCCES_ID_PUT_NEWS){
                progresDialog.dismiss();
                Toast.makeText(this,"Update news failed", Toast.LENGTH_LONG).show();
            }

            else if (message.getId() == PROCCES_ID_DELETE_NEWS){
                progresDialog.dismiss();
                Toast.makeText(this,"Delete news failed", Toast.LENGTH_LONG).show();
            }
        }
    }


    /**
     * save news
     */
    private void saveNew(){

        String title = editTitle.getText().toString();
        String content = editContent.getText().toString();

        if (!title.equals("") && !content.equals("")) {

            if (news == null) {

                Category category = categories.get(spinCategory.getSelectedItemPosition());

                news = new New();
                news.setName(title);
                news.setContent(content);
                news.setCreateDate(new Date().getTime());
                news.setCategory(category);

                jobManager.addJobInBackground(new NewsJob(PROCCES_ID_POST_NEWS, NewsJob.POST, news));
                news = null;
                clearForm();

            }else {

                news.setName(title);
                news.setContent(content);
                news.setCreateDate(new Date().getTime());
                jobManager.addJobInBackground(new NewsJob(PROCCES_ID_PUT_NEWS, NewsJob.PUT, news));

                news = null;
                clearForm();

            }
        }
    }


    /**
     * edit news
     * @param news
     */
    public void editNew(New news){
        this.news = news;

        editTitle.setText(news.getName());
        editContent.setText(news.getContent());
    }


    /**
     * delete news
     * @param news
     */
    public void deleteNews(New news){
        jobManager.addJobInBackground(new NewsJob(PROCCES_ID_DELETE_NEWS, NewsJob.DELETE, news));
    }


    /**
     * clear form
     */
    private void clearForm(){
        editTitle.getText().clear();
        editContent.getText().clear();
    }

}