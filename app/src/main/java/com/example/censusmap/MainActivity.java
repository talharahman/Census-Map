package com.example.censusmap;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.example.censusmap.fragments.DataFragment;
import com.example.censusmap.fragments.MainFragment;
import com.example.censusmap.fragments.OnBarQueryListener;
import com.example.censusmap.repositiory.FragmentInterface;

public class MainActivity extends AppCompatActivity implements FragmentInterface {

    android.support.v7.widget.Toolbar toolBar;
    private OnBarQueryListener barQueryListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MainFragment mainFragment = MainFragment.newInstance();
        barQueryListener = (OnBarQueryListener) mainFragment;

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.main_activity_container, mainFragment)
                .commit();

        toolBar = findViewById(R.id.main_toolbar);
        setSupportActionBar(toolBar);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeAsUpIndicator(R.drawable.ic_developer_info);
        setSearchView();
    }


    @Override
    public void moveToDetailsScreen(String zipCode) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.main_activity_container, DataFragment.newInstance(zipCode))
                .setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.pop_enter, R.anim.pop_exit)
                .addToBackStack(null)
                .commit();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.developer_info, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.developer_contact:
                developerInfo().show();
                break;
        }
        return true;
    }

    public AlertDialog developerInfo() {
        AlertDialog.Builder devInfo = new AlertDialog.Builder(MainActivity.this);
        devInfo.setTitle(R.string.developer_info_text)
                .setItems(R.array.developer_contact_text, (dialog, which) -> {
                    switch (which) {
                        case 0:
                            Uri emailUri = Uri.parse(String.valueOf(R.string.e_mail));
                            Intent emailIntent = new Intent(Intent.ACTION_VIEW, emailUri);
                            startActivity(emailIntent);
                            break;
                        case 1:
                            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(String.valueOf(R.string.github_repo))));
                            break;
                        case 2:
                            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(String.valueOf(R.string.linkedin_profile))));
                            break;
                    }
                });
        return devInfo.create();
    }

    private void setSearchView() {
        SearchView searchView = findViewById(R.id.map_search_view);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                barQueryListener.onQuery(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });

    }

}
