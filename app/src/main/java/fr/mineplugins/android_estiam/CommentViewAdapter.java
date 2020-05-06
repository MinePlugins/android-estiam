package fr.mineplugins.android_estiam;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.bumptech.glide.Glide;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;

import androidx.recyclerview.widget.RecyclerView;
import de.hdodenhof.circleimageview.CircleImageView;

public class CommentViewAdapter extends RecyclerView.Adapter<CommentViewAdapter.ViewHolder> {

    private static final String TAG = "CommentViewAdapter";

    private ArrayList<Comment> comments;
    private Context context;
    public CommentViewAdapter(ArrayList<Comment> comments, Context context) {
        this.comments = comments;
        Log.e(TAG, "CommentViewAdapter: " + comments.toString());
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_comments, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public int getItemCount() {
        return comments.size();
    }

    private int getRandomMaterialColor(String typeColor) {
        int returnColor = Color.GRAY;
        int arrayId = context.getResources().getIdentifier("mdcolor_" + typeColor, "array", context.getPackageName());

        if (arrayId != 0) {
            TypedArray colors = context.getResources().obtainTypedArray(arrayId);
            int index = (int) (Math.random() * colors.length());
            returnColor = colors.getColor(index, Color.GRAY);
            colors.recycle();
        }
        return returnColor;
    }
    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        Log.d(TAG, "onBindViewHolder: RECYCYCYCHICNKIJN." + comments.get(position).content);
        holder.content.setText(comments.get(position).content);
        TextDrawable drawable = TextDrawable.builder()
                .beginConfig()
                .width(80)  // width in px
                .height(80) // height in px
                .endConfig()
                .buildRound(comments.get(position).email.substring(0,2).toUpperCase(), getRandomMaterialColor("400"));
        holder.avatar.setImageDrawable(drawable);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView avatar;
        TextView content;
        RelativeLayout parentLayout;
        public ViewHolder(View itemView) {
            super(itemView);
            content = itemView.findViewById(R.id.list_comments);
            avatar = itemView.findViewById(R.id.image_comments);
        }

    }

}
