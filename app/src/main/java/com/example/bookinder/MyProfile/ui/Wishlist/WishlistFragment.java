package com.example.bookinder.MyProfile.ui.Wishlist;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bookinder.CurrentUser;
import com.example.bookinder.MyProfile.ui.BooksForSale.GalleryViewModel;
import com.example.bookinder.MyProfile.ui.BooksForSale.ItemBookAdapter;
import com.example.bookinder.R;
import com.example.bookinder.ServerAddress;
import com.example.bookinder.databinding.FragmentBooksForSaleBinding;
import com.example.bookinder.databinding.FragmentWishlistBinding;
import com.example.bookinder.models.ItemBookData;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class WishlistFragment extends Fragment {

    private FragmentWishlistBinding binding;
    String current_user = CurrentUser.getCurrentUser();
    ArrayList<ItemBookData> itemBooksData = new ArrayList<ItemBookData>();
    RecyclerView recyclerView;
    View view;
    ItemBookAdapter itemBookAdapter;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentWishlistBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        view = inflater.inflate(R.layout.fragment_wishlist, container, false);
        recyclerView = binding.recyclerView;
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        JSONObject bookForSaleForm = new JSONObject();
        try {
            bookForSaleForm.put("current_user", current_user);
            bookForSaleForm.put("method", "wishlist");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), bookForSaleForm.toString());

        postRequest(ServerAddress.serverAddress+"/wishlist/" + current_user, body);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    public void postRequest(String postUrl, RequestBody postBody) {
        OkHttpClient client = new OkHttpClient();

        final Request request = new Request.Builder()
                .url(postUrl)
                .post(postBody)
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                call.cancel();
            }

            @Override
            public void onResponse(Call call, final Response response) {
                try {
                    final String responseString = response.body().string().trim();
                    System.out.println("res is: " + responseString);
                    Gson gson = new Gson();
                    String[] all_cards = gson.fromJson(responseString, String[].class);
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            for(int i = 0; i < all_cards.length; i++) {
                                JSONObject bookForSaleForm = new JSONObject();
                                try {
                                    bookForSaleForm.put("card_id", all_cards[i]);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                                RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), bookForSaleForm.toString());

                                postRequestCard(ServerAddress.serverAddress+"/items/" + all_cards[i] + "/" + CurrentUser.currentUser, body);
                            }
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }


    public void postRequestCard(String postUrl, RequestBody postBody) {
        OkHttpClient client = new OkHttpClient();

        final Request request = new Request.Builder()
                .url(postUrl)
                .post(postBody)
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                call.cancel();
            }
            @Override
            public void onResponse(Call call, final Response response) {
                try {
                    final String responseString = response.body().string().trim();
                    JSONObject jsonObject = new JSONObject(responseString);
                    String title = jsonObject.getString("title");
                    String method = jsonObject.getString("method");
                    String book_image = jsonObject.getString("book_image");
                    String author = jsonObject.getString("author");
                    String price = jsonObject.getString("price");
                    String genre = jsonObject.getString("genre");
                    String seller_id = jsonObject.getString("seller_id");
                    String seller_name = jsonObject.getString("seller_name");
                    String seller_image = jsonObject.getString("seller_image");
                    String isLiked = jsonObject.getString("isLiked");
                    String card_id = jsonObject.getString("id");
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ItemBookData itemBookData = new ItemBookData(card_id,title,method,book_image,
                                    author,price,genre,seller_id,seller_name,seller_image,isLiked);
                            itemBooksData.add(itemBookData);
                            itemBookAdapter = new ItemBookAdapter(itemBooksData, view);
                            recyclerView.setAdapter(itemBookAdapter);
                            itemBookAdapter.notifyDataSetChanged();
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
