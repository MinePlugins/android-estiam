package fr.mineplugins.android_estiam;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class ProductFragment extends Fragment implements ProductsViewAdapter.OnProductListener {
    private DatabaseHelper productDB;
    private final ArrayList<Product> listProducts = new ArrayList<Product>();
    private Context context;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_product_list, container, false);
        context = view.getContext();
        productDB = new DatabaseHelper(context);
        initRecyclerView(loadData(), view);
        return view;
    }
    public ArrayList<Product> loadData(){
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        String URL = "http://www.vasedhonneurofficiel.com/ws/productsList.php";
        JsonArrayRequest objectRequest = new JsonArrayRequest(
                Request.Method.GET,
                URL,
                null,
                response -> {
                    Log.d("Response", response.toString());
                    for(int i = 0; i < response.length(); i++){
                        JSONObject item = null;
                        try {
                            item = response.getJSONObject(i);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        Product product = new Product();
                        product.readFromJson(item);
                        boolean isInserted = productDB.insertData(product.titre, product.url, false);
                        if(isInserted){
                            Log.d("INSERTED", "INSERTED");
                        }
                    }
                },
                error -> {
                    return;
                }
        );
        requestQueue.add(objectRequest);
        Cursor data = productDB.getAllData();
        ArrayList<Product> listProductsloc = new ArrayList<Product>();

        if(data.getCount() == 0){
            // Show message
            return listProductsloc;
        }
        while (data.moveToNext()){
            Product product = new Product();
            product.id = data.getInt(0);
            product.titre = data.getString(1);
            product.url = data.getString(2);
            product.liked = data.getInt(3) > 0;
            Log.d("TAG", "onCreateView: " + product.liked);
            listProductsloc.add(product);

        }
        return listProductsloc;
    }

    public void initRecyclerView(ArrayList<Product> listProducts, View view){
        RecyclerView recyclerView = view.findViewById(R.id.recyclerview);
        RecyclerView.LayoutManager manager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(manager);
        ProductsViewAdapter adapter = new ProductsViewAdapter(listProducts, context, this);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onProductLike(int position) {
        Log.e("TAG", "onProductLike: CLICKED - " + position);
        productDB.onLiked(position);


//        listProducts.get(position);
//        Intent intent = new Intent(this, NewActivity.java);
//        startActivity(intent);
    }
}
