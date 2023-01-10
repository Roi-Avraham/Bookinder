package com.example.bookinder.MyProfile.ui.BooksForSale;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.bookinder.BookDetails;
import com.example.bookinder.BookDetailsExchange;
import com.example.bookinder.R;
import com.example.bookinder.models.ItemBookData;
import com.squareup.picasso.Picasso;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import retrofit2.http.Url;

public class ItemBookAdapter extends RecyclerView.Adapter<ItemBookAdapter.ViewHolder>{

    ArrayList<ItemBookData> itemBookData;
    Context context;

    public ItemBookAdapter(ArrayList<ItemBookData> itemBookData, View view){
        this.itemBookData = itemBookData;
        this.context = view.getContext();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view =layoutInflater.inflate(R.layout.item_book,parent,false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final ItemBookData itemBookDataList = itemBookData.get(position);
        if(itemBookDataList.getBookMethod().equals("sale")) {
            holder.textViewName.setText(itemBookDataList.getBookName());
            String s = "for sale | " + itemBookDataList.getPrice() +"$";
            holder.textViewMethod.setText(s);
            Picasso.get().load(itemBookDataList.getBookImage()).into(holder.bookImage);
            //holder.bookImage.setImageResource(itemBookDataList.getBookImage());
        } else {
            holder.textViewName.setText(itemBookDataList.getBookName());
            String s = "for exchange";
            holder.textViewMethod.setText(s);
            Picasso.get().load(itemBookDataList.getBookImage()).into(holder.bookImage);
//            Glide.with(this.context).load(itemBookDataList.getBookImage()).into(holder.bookImage);
        }


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(itemBookDataList.getBookMethod().equals("sale")) {
                    Intent intent = new Intent(context, BookDetails.class);
                    intent.putExtra("book_name",itemBookDataList.getBookName());
                    intent.putExtra("book_image",itemBookDataList.getBookImage());
                    intent.putExtra("authorName",itemBookDataList.getAuthorName());
                    intent.putExtra("price",itemBookDataList.getPrice());
                    intent.putExtra("genre",itemBookDataList.getGenre());
                    intent.putExtra("ownerName",itemBookDataList.getOwnerName());
                    intent.putExtra("ownerId",itemBookDataList.getOwnerId());
                    intent.putExtra("ownerImage",itemBookDataList.getOwnerImage());
                    intent.putExtra("card_id",itemBookDataList.getCard_id());
                    intent.putExtra("isLiked",itemBookDataList.getIsLiked());
                    context.startActivity(intent);

                } else {
                    Intent intent = new Intent(context, BookDetailsExchange.class);
                    intent.putExtra("book_name",itemBookDataList.getBookName());
                    intent.putExtra("book_image",itemBookDataList.getBookImage());
                    intent.putExtra("authorName",itemBookDataList.getAuthorName());
                    intent.putExtra("genre",itemBookDataList.getGenre());
                    intent.putExtra("ownerId",itemBookDataList.getOwnerId());
                    intent.putExtra("ownerImage",itemBookDataList.getOwnerImage());
                    intent.putExtra("ownerName",itemBookDataList.getOwnerName());
                    intent.putExtra("card_id",itemBookDataList.getCard_id());
                    intent.putExtra("isLiked",itemBookDataList.getIsLiked());
                    context.startActivity(intent);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return itemBookData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView bookImage;
        TextView textViewName;
        TextView textViewMethod;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            bookImage = itemView.findViewById(R.id.bookImage);
            textViewName = itemView.findViewById(R.id.bookName);
            textViewMethod = itemView.findViewById(R.id.bookMethod);
        }
    }
}
