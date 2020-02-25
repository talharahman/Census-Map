package com.example.censusmap.view;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;

import com.example.censusmap.R;
import com.example.censusmap.repositiory.OnQuerySubmitListener;

public class MainActivity extends AppCompatActivity  {

    private OnQuerySubmitListener barQueryListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MainDataFragment mainFragment = MainDataFragment.newInstance();
        barQueryListener = mainFragment;

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.main_activity_container, mainFragment)
                .commit();

        setToolBar();
    }


    private void setToolBar() {
        Toolbar toolBar = findViewById(R.id.main_toolbar);
        setSupportActionBar(toolBar);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeAsUpIndicator(R.drawable.ic_menu_info);
        setSearchView();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_info, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_button:
                    // set menu function
                break;
        }
        return true;
    }


    private void setSearchView() {
        SearchView searchView = findViewById(R.id.map_search_view);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                barQueryListener.onQuerySubmit(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String filter) {
                return false;
            }
        });

    }

}
