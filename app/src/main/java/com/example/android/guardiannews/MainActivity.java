package com.example.android.guardiannews;

import android.app.LoaderManager;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements LoaderCallbacks<List<News>> {

    private static final String LOG_TAG = MainActivity.class.getName();

    /**
     * API key for URL requests.
     **/
    private static final String API_KEY = "45a4d399-cc54-424c-a5d4-8fb9ed9330ec";

    /**
     * TextView that is displayed when the list is empty
     */
    private TextView mEmptyView;
    private ProgressBar bar;

    /**
     * URL for news data from Guardian API URL
     */
    private static final String GUARDIAN_REQUEST_URL =
            "https://content.guardianapis.com/search?";

    /**
     * Adapter for the list of news
     */
    private NewsAdapter mAdapter;

    /**
     * Constant value for the news loader ID. We can choose any integer.
     * This really only comes into play if you're using multiple loaders.
     */
    private static final int NEWS_LOADER_ID = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Find a reference to the {@link ListView} in the layout
        ListView newsListView = (ListView) findViewById(R.id.list);
        mEmptyView = (TextView) findViewById(R.id.empty_view);
        newsListView.setEmptyView(mEmptyView);
        bar = (ProgressBar) findViewById(R.id.loading_view);

        //Create a new adapter that takes an empty list of news as input
        mAdapter = new NewsAdapter(this, new ArrayList<News>());

        // Set the adapter on the {@link ListView}
        // so the list can be populated in the user interface
        newsListView.setAdapter(mAdapter);

        // Set an item click listener on the ListView, which sends an intent to a web browser
        // to open a website with more information about the selected news.
        newsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                // Find the current news that was clicked on
                News currentNews = mAdapter.getItem(position);

                // Convert the String URL into a URI object (to pass into the Intent constructor)
                Uri newsUri = Uri.parse(currentNews.getWebUrl());

                // Create a new intent to view the news URI
                Intent websiteIntent = new Intent(Intent.ACTION_VIEW, newsUri);

                // Send the intent to launch a new activity
                startActivity(websiteIntent);
            }
        });

        // Check internet connection and handle it.
        ConnectivityManager cm =
                (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
        if (isConnected) {
            // Get a reference to the LoaderManager, in order to interact with loaders.
            LoaderManager loaderManager = getLoaderManager();

            // Initialize the loader. Pass in the int ID constant defined above and pass in null for
            // the bundle. Pass in this activity for the LoaderCallbacks parameter (which is valid
            // because this activity implements the LoaderCallbacks interface).
            loaderManager.initLoader(NEWS_LOADER_ID, null, this);
        } else {
            bar.setVisibility(View.GONE);
            mEmptyView.setText(R.string.no_connection);
        }
    }

    @Override
    public Loader<List<News>> onCreateLoader(int i, Bundle bundle) {
        Uri baseUri = Uri.parse(GUARDIAN_REQUEST_URL);
        Uri.Builder uriBuilder = baseUri.buildUpon();
        uriBuilder.appendQueryParameter("api-key", API_KEY);

        return new NewsLoader(this, uriBuilder.toString());
    }

    @Override
    public void onLoadFinished(Loader<List<News>> loader, List<News> newsList) {
        // Handling fluid user experience by setting empty_view and loading indicator.
        bar.setVisibility(View.GONE);

        mEmptyView.setText(R.string.empty_view);
        // Clear the adapter of previous news data
        mAdapter.clear();

        // If there is a valid list of {@link News}, then add them to the adapter's
        // data set. This will trigger the ListView to update.
        if (newsList != null && !newsList.isEmpty()) {
            mAdapter.addAll(newsList);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<News>> loader) {
        // Loader reset, so we can clear out our existing data.
        mAdapter.clear();
    }

}
