package fr.mineplugins.android_estiam;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class FavoriteFragment extends Fragment implements ProductsViewAdapter.OnProductListener {
    private DatabaseHelper productDB;
    private final ArrayList<Product> listProducts = new ArrayList<Product>();
    private Context context;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_product_list, container, false);
        context = view.getContext();
        productDB = new DatabaseHelper(context);
        ArrayList<Product> listProductsrecycler = loadData();
        if(listProductsrecycler.size() == 0) {
            TextView nothing_message = view.findViewById(R.id.text_if_nothing);
            nothing_message.setTextSize(20);
        }
        initRecyclerView(listProductsrecycler, view);
        return view;
    }
    public ArrayList<Product> loadData(){
        Cursor data = productDB.getAllFav();

        if(data.getCount() == 0){
            // Show message

            return listProducts;
        }
        while (data.moveToNext()){
            Product product = new Product();
            product.id = data.getInt(0);
            product.titre = data.getString(1);
            product.url = data.getString(2);
            product.liked = data.getInt(3) > 0;
            Log.d("TAG", "onCreateView: " + product.liked);
            listProducts.add(product);

        }
        return listProducts;
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
        productDB.onLiked(listProducts.get(position).id);

    }
}
