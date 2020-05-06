package fr.mineplugins.android_estiam;

import android.content.res.TypedArray;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.google.android.material.button.MaterialButton;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class ProductDetailActivity extends AppCompatActivity {
    private static final String TAG = "ProductDetailActivity";
    private final ArrayList<Comment> comments = new ArrayList<Comment>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_product_detail);
        getIncomingIntent();

    }

    private void getIncomingIntent(){
        if(getIntent().hasExtra("image_url") && getIntent().hasExtra("titre") && getIntent().hasExtra("id") && getIntent().hasExtra("liked")){
            String imageUrl = getIntent().getStringExtra("image_url");
            String titre = getIntent().getStringExtra("titre");
            int id = getIntent().getIntExtra("id", 0);
            boolean liked = getIntent().getBooleanExtra("liked", false);
            getComments(id);
            setImage(imageUrl, titre, liked, id);

//            initRecyclerView(getComments(id));
        }
    }
    private ArrayList<Comment> getComments(int id){
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        String URL = "http://www.vasedhonneurofficiel.com/ws/commentsList.php";
        StringRequest objectRequest = new StringRequest(Request.Method.POST, URL,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        Gson g = new Gson();
                        Comment[] commentArr = g.fromJson(response, Comment[].class);

                        Collections.addAll(comments, commentArr);
                        Log.d(TAG, "onErrorResponse: OK - " );
                        for(int i = 0; i < comments.size(); i++) {
                            Log.d(TAG, "onResponse: " + comments.get(i).email);
                            Log.d(TAG, "onResponse: " + comments.get(i).id);
                            Log.d(TAG, "onResponse: " + comments.get(i).content);
                        }
                        initRecyclerView(comments);
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d(TAG, "onErrorResponse: "+ error);
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("productId", Integer.toString(id));
                return params;
            }
        };
        requestQueue.add(objectRequest);
        return comments;

    }
    public void initRecyclerView(ArrayList<Comment> comments){
        RecyclerView recyclerView = findViewById(R.id.comments);
        RecyclerView.LayoutManager manager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(manager);
        CommentViewAdapter adapter = new CommentViewAdapter(comments, this);
        recyclerView.setAdapter(adapter);
    }
    private void setImage(String imageUrl, String titre, boolean liked, int id){
        MaterialButton mLiked = findViewById(R.id.product_detail_like);
        if(liked) {
            mLiked.setIconTintResource(R.color.colorAccent);
            mLiked.setIconResource(R.drawable.ic_like);
        }
        TextView name = findViewById(R.id.product_detail_name);
        name.setText(titre);
        ImageView image = findViewById(R.id.product_detail_image);
        Glide.with(this)
                .asBitmap()
                .load(imageUrl)
                .into(image);
    }
}
