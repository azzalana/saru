package su.mboh.asu.activity;

import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.Date;

import su.mboh.asu.R;
import su.mboh.asu.adapter.NewAdapter;
import su.mboh.asu.adapter.NewDatabaseAdapter;
import su.mboh.asu.entity.New;

/**
 * Created by smktiufa on 16/12/16.
 */

public class ActivityNew extends AppCompatActivity {

    private RecyclerView recyclerNew;
    private NewAdapter newAdapter;
    private NewDatabaseAdapter newdatabaseadapter;

    private EditText editTitle;
    private EditText editContent;
    private New news;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new);

        recyclerNew = (RecyclerView) findViewById(R.id.recycler_menu);
        editTitle = (EditText) findViewById(R.id.edit_title);
        editContent = (EditText) findViewById(R.id.edit_content);

        newAdapter = new NewAdapter(ActivityNew.this);
        newdatabaseadapter = new NewDatabaseAdapter(this);

        recyclerNew.setLayoutManager(new LinearLayoutManager(this));
        recyclerNew.setItemAnimator(new DefaultItemAnimator());
        recyclerNew.setAdapter(newAdapter);

        newAdapter.addNewses(newdatabaseadapter.findNewsAll());


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
                newAdapter.clearNew();
                newAdapter.addNewses(newdatabaseadapter.findNewByTitle(query));
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
                newAdapter.addNewses(newdatabaseadapter.findNewsAll());
                return true;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.menu_save){
            saveNew();

            newAdapter.clearNew();
            newAdapter.addNewses(newdatabaseadapter.findNewsAll());
        } else if (id == R.id.menu_refresh) {
            newAdapter.clearNew();
            newAdapter.addNewses(newdatabaseadapter.findNewsAll());
        }
        return true;
    }

    public void saveNew() {

        String title = editTitle.getText().toString();
        String content = editContent.getText().toString();

        if (!title.equals("") && !content.equals("")) {
            if (news == null){
            news = new New();
            news.setTitle(title);
            news.setContent(content);
            news.setCreateDate(new Date().getTime());
            newdatabaseadapter.save(news);
            clearForm();
        }else {
                news.setTitle(title);
                news.setContent(content);
                news.setCreateDate(new Date().getTime());
                newdatabaseadapter.save(news);
                news = null;
                clearForm();
            }

    }
    }

    private void clearForm() {
        editTitle.getText().clear();
        editContent.getText().clear();
    }


    public void editNew(New news){
        this.news = news;
        editTitle.setText(news.getTitle());
        editContent.setText(news.getContent());
    }
    public void deleteNews (New news){
        newdatabaseadapter.delete(news);

        newAdapter.clearNew();
        newAdapter.addNewses(newdatabaseadapter.findNewsAll());
    }

}
