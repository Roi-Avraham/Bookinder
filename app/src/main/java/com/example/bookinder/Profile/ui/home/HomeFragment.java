package com.example.bookinder.Profile.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.example.bookinder.CurrentUser;
import com.example.bookinder.OtherUser;
import com.example.bookinder.R;
import com.example.bookinder.ServerAddress;
import com.example.bookinder.databinding.FragmentHomeBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    View view;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        view = inflater.inflate(R.layout.fragment_home, container, false);

        // Retrieve the data from the Bundle
        String ownerId = OtherUser.otherUser;

        JSONObject myProfileForm = new JSONObject();
        try {
            myProfileForm.put("current_user", ownerId);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), myProfileForm.toString());

        postRequest(ServerAddress.serverAddress+"/profile/" + ownerId, body);

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
                    JSONObject jsonObject = new JSONObject(responseString);
                    String person_name = jsonObject.getString("name");
                    String age = jsonObject.getString("age");
                    String phone_number = jsonObject.getString("phone");
                    String mail = jsonObject.getString("mail");
                    String genres = jsonObject.getString("genres");
                    String profile_image = jsonObject.getString("image_address");
                    System.out.println("the pic issss " + profile_image);
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            View includedLayout1 = getActivity().findViewById(R.id.item_image);
                            TextView name_of_the_user = includedLayout1.findViewById(R.id.name_of_the_user);
                            name_of_the_user.setText(person_name);

                            String s = "age: " + age;
                            TextView ageView = includedLayout1.findViewById(R.id.age);
                            ageView.setText(s);

                            View includedLayout2 = getActivity().findViewById(R.id.item_phone);
                            TextView phoneView = includedLayout2.findViewById(R.id.phone);
                            phoneView.setText(phone_number);

                            View includedLayout3 = getActivity().findViewById(R.id.item_mail);
                            TextView mailView = includedLayout3.findViewById(R.id.mail);
                            mailView.setText(mail);

                            View includedLayout4 = getActivity().findViewById(R.id.item_genres);
                            TextView genreView = includedLayout4.findViewById(R.id.favorite_genres);
                            if (!genres.equals("")) {
                                String s2 = genres.substring(0, genres.length() - 1);
                                String s3 = s2.concat(".");
                                genreView.setText(s3);
                            }else {
                                genreView.setText(genres);
                            }

//                            Uri uri = Uri.parse(profile_image);
                            ImageView imageView =  includedLayout1.findViewById(R.id.user_image);
//                            imageView.setImageURI(uri);
                            //Glide.with(getActivity()).load(profile_image).into(imageView);
                            Picasso.get().load(profile_image).into(imageView);


                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}