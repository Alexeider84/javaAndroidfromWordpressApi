package com.chen2dev.recetascubanas;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.chen2dev.recetascubanas.model.Category;
import com.chen2dev.recetascubanas.model.MySingletonVolley;
import com.chen2dev.recetascubanas.model.Post;
import com.google.android.material.navigation.NavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    DrawerLayout drawer;
    ActionBarDrawerToggle toggle;
    NavigationView navigationView;

    RecyclerView post_list;
    public static final String TAGC = "TAGC";
    public static final String TAG = "TAG";

    List<Post> posts;
    List<Category> cat_list;
    PostsAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        posts = new ArrayList<>();
        cat_list = new ArrayList<>();

        drawer = findViewById(R.id.drawer);
        post_list = findViewById(R.id.post_list_);
        toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.open, R.string.close);
        navigationView = findViewById(R.id.nav_view);
        toggle.setDrawerIndicatorEnabled(true);//enable hamburger sign
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        //extrae las categorias
        extractCategories(getResources().getString(R.string.categories_url));
       /* if (this.cat_list.size()>0)
            extractPosts();
*/
        GridLayoutManager manager = new GridLayoutManager(this, 2);
        post_list.setLayoutManager(manager);
        adapter = new PostsAdapter(posts);
        post_list.setAdapter(adapter);


    }

    //@SuppressLint("SuspiciousIndentation")
    public void extractCategories(String URL) {
        // use volley to extract the data

       // RequestQueue queue = Volley.newRequestQueue(this);
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, URL, null,
                new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                Log.d(TAGC, "onResponse: " + response.toString());
                for (int i = 0; i < response.length(); i++) {

                    try {
                        Category c = new Category();

                        JSONObject jsonObjectData = response.getJSONObject(i);

                        // extract the id
                        c.setId(jsonObjectData.getInt("id"));

                        // extract the name
                        c.setName(jsonObjectData.getString("name"));

                        //extract the parent
                        c.setParent(jsonObjectData.getInt("parent"));

                        //extract the parent
                        c.setCount(jsonObjectData.getInt("count"));

                        cat_list.add(c);
                       // adapter.notifyDataSetChanged();


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }

            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        MySingletonVolley.getInstance(getBaseContext()).addToRequestQueue(request);
        //queue.add(request);

       /* if (cat_list.size()>0)
        extractPosts(cat_list, queue);
*/

    }

    public void extractPosts(List<Category> cat_list) {//@NonNull  cat_list,RequestQueue queue
        // use volley to extract the data
        String URL = getResources().getString(R.string.posts_url);
       //RequestQueue queue = Volley.newRequestQueue(this);

  for (final int[] i = {0}; i[0] < this.cat_list.size(); i[0]++) {
            final int[] cont_post = {10};
            int cant_post_real = this.cat_list.get(i[0]).getCount(); //cat_list.get(i[0]).getCount();
            int page = 0;
            while(cont_post[0] == 10 && cant_post_real>0) {
                page ++ ;
                String complete_url = URL+page+"&categories=" + this.cat_list.get(i[0]).getId(); //cat_list.get(i[0]).getId();
                JsonArrayRequest request1 = new JsonArrayRequest(Request.Method.GET, complete_url, null, new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d(TAG, "onResponse: " + response.toString());
                        for (int k = 0; k < response.length(); k++) {
                           try {
                                Post p = new Post();

                                JSONObject jsonObjectData = response.getJSONObject(k);

                                // extract the title
                                JSONObject titleObject = jsonObjectData.getJSONObject("title");
                                p.setTitle(titleObject.getString("rendered"));

                                // extract the content
                                JSONObject contentObject = jsonObjectData.getJSONObject("content");
                                p.setContent(contentObject.getString("rendered"));

                                //extract the excerpt
                                JSONObject excerptObject = jsonObjectData.getJSONObject("excerpt");
                                p.setExcerpt(excerptObject.getString("rendered"));

                                // extract feature image
                                JSONObject embeddedObject = jsonObjectData.getJSONObject("_embedded");
                                JSONArray wpfeaturemediaArray = embeddedObject.getJSONArray("wp:featuredmedia");
                                JSONObject firstElement = wpfeaturemediaArray.getJSONObject(0);
                                p.setFeature_image(firstElement.getString("source_url"));

                                posts.add(p);
                                adapter.notifyDataSetChanged();

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }


                        }
                        cont_post[0] =response.length();

                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(MainActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
                MySingletonVolley.getInstance(getBaseContext()).addToRequestQueue(request1);
                //queue.add(request1);
            }
        }
    }
}