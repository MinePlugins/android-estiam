package fr.mineplugins.android_estiam;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;

import androidx.recyclerview.widget.RecyclerView;
import de.hdodenhof.circleimageview.CircleImageView;

public class ProductsViewAdapter extends RecyclerView.Adapter<ProductsViewAdapter.ViewHolder> {

    private static final String TAG = "ProductsViewAdapter";

    private ArrayList<Product> productList;
    private Context context;
    private OnProductListener mOnProductListener;
    public ProductsViewAdapter(ArrayList<Product> productList, Context context, OnProductListener onProductListener) {
        this.productList = productList;
        this.context = context;
        this.mOnProductListener = onProductListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_products, parent, false);
        ViewHolder holder = new ViewHolder(view, mOnProductListener);
        return holder;
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        Log.d(TAG, "onBindViewHolder: oki.");
        Glide.with(context)
                .asBitmap()
                .load(productList.get(position).url)
                .into(holder.productImage);
        if(productList.get(position).liked) {
            holder.productLiked.setIconTintResource(R.color.colorAccent);
            holder.productLiked.setIconResource(R.drawable.ic_like);
            Log.d(TAG, "onBindViewHolder: COLLOOR.");

        }
        holder.productName.setText(productList.get(position).titre);
        holder.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: clicked !!");
                Toast.makeText(context, productList.get(position).titre, Toast.LENGTH_SHORT).show();
            }
        });
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        CircleImageView productImage;
        TextView productName;
        MaterialButton productLiked;
        OnProductListener onProductListener;
        RelativeLayout parentLayout;
        public ViewHolder(View itemView, OnProductListener onProductListener) {
            super(itemView);
            productImage = itemView.findViewById(R.id.image);
            productName = itemView.findViewById(R.id.product_name);
            parentLayout = itemView.findViewById(R.id.parent_layout);
            productLiked = itemView.findViewById(R.id.product_like);
            this.onProductListener = onProductListener;
            productLiked.setOnClickListener(this);
        }

        @SuppressLint("ResourceType")
        @Override
        public void onClick(View view) {
            Log.e(TAG, "onClick: " + productLiked.getIconTint().toString());
            Log.e(TAG, "onClick: " + context.getColorStateList(R.color.colorAccent).toString());
            if(productLiked.getIconTint().toString().equals(context.getColorStateList(R.color.colorAccent).toString())){
                productLiked.setIconTintResource(R.color.colorPrimary);
                productLiked.setIconResource(R.drawable.ic_unlike);
            } else {
                productLiked.setIconTintResource(R.color.colorAccent);
                productLiked.setIconResource(R.drawable.ic_like);
            }

            onProductListener.onProductLike(getAdapterPosition());
        }
    }
    public interface OnProductListener{
        void onProductLike(int position);
    }
}
