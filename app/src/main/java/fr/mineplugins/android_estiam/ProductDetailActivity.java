package fr.mineplugins.android_estiam;

import android.content.res.TypedArray;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;
import com.google.gson.JsonParseException;

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
    private Session session;
    private int id;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_product_detail);
        session = new Session(this);
        TextInputLayout field_comments = findViewById(R.id.field_comments);

        Button send_comment = findViewById(R.id.button_comment);
        send_comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String commentaires = String.valueOf(field_comments.getEditText().getText());
                if(commentaires != null){
                    sendComments(commentaires);
                }
            }
        });

        getIncomingIntent();

    }
    private void sendComments(String comment){
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        String URL = "http://www.vasedhonneurofficiel.com/ws/addComment.php";
        StringRequest objectRequest = new StringRequest(Request.Method.POST, URL,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        Gson g = new Gson();
                        try {
                            CommentResponse comment_response = g.fromJson(response, CommentResponse.class);
                            if (comment_response.success == 200) {
                                resetTextMessage(id);
                            } else {
                                setTextMessage("Une erreur est survenue");
                            }
                        } catch (JsonParseException e) {
                            setTextMessage("Une erreur interne est survenue");
                        }
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("Request", "Error: "+ error);
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("productId", Integer.toString(id));
                params.put("email", session.getEmail());
                params.put("content", comment);
                return params;
            }
        };
        requestQueue.add(objectRequest);
    }
    private void setTextMessage(String text){
        TextView title_comments = findViewById(R.id.title_comments);
        title_comments.setText(text);
        title_comments.setTextColor(Color.RED);
    }
    private void resetTextMessage(int id){
        TextView title_comments = findViewById(R.id.title_comments);
        TextInputEditText comment_input = findViewById(R.id.field_comments_text);
        comment_input.getText().clear();
        title_comments.setTextColor(Color.GREEN);
        title_comments.setText("Commentaire envoye !");
        getComments(id);
    }
    private void getIncomingIntent(){
        if(getIntent().hasExtra("image_url") && getIntent().hasExtra("titre") && getIntent().hasExtra("id") && getIntent().hasExtra("liked")){
            String imageUrl = getIntent().getStringExtra("image_url");
            String titre = getIntent().getStringExtra("titre");
            id = getIntent().getIntExtra("id", 0);
            boolean liked = getIntent().getBooleanExtra("liked", false);
            getComments(id);
            setImage(imageUrl, titre, liked, id);

//            initRecyclerView(getComments(id));
        }
    }
    private ArrayList<Comment> getComments(int id){
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        String URL = "http://www.vasedhonneurofficiel.com/ws/commentsList.php";
        comments.clear();
        StringRequest objectRequest = new StringRequest(Request.Method.POST, URL,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        Gson g = new Gson();
                        Comment[] commentArr = g.fromJson(response, Comment[].class);
                        Collections.addAll(comments, commentArr);
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
        Button send_comment = findViewById(R.id.button_comment);
        TextInputLayout comment = findViewById(R.id.field_comments);
        send_comment.setVisibility(View.INVISIBLE);
        comment.setVisibility(View.INVISIBLE);
        if(session.getEmail() != null){
            Log.e(TAG, "setImage: YYESYSYS" + session.getEmail());
            send_comment.setVisibility(View.VISIBLE);
            comment.setVisibility(View.VISIBLE);
        }
    }
}
