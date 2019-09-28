package edu.skku.jonadan.hangangmongttang;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ReviewFragment extends Fragment {
    ListView reviewListView;
    Button reviewBtn;

    public static ArrayList<ReviewListItem> reviewList = new ArrayList<>();
    public static ReviewListAdapter reviewListAdapter;

    public static int facilityId;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_review, container, false);

        reviewBtn = rootView.findViewById(R.id.btn_review);
        reviewListView = rootView.findViewById(R.id.review_list);

        reviewBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddReviewFragment addReviewFragment = new AddReviewFragment();
                addReviewFragment.setStyle( DialogFragment.STYLE_NO_TITLE, android.R.style.Theme_Holo_Light );
                addReviewFragment.show(getActivity().getSupportFragmentManager(),"tag");
            }
        });
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        facilityId = InfoActivity.facilityId;
        reviewListAdapter = new ReviewListAdapter(reviewList);
        reviewListView.setAdapter(reviewListAdapter);

        getReview();
    }

    public static void getReview(){
        reviewList.clear();

        int review_num;

        int rid;
        String user;
        String password;
        String content;
        String date;
        float rate;

        JSONObject get_review = new SQLSender().sendSQL("SELECT * from review where fid="+new Integer(facilityId).toString()+";");
        try{
            if(!get_review.getBoolean("isError")){
                Log.d("db_conn",get_review.toString());

                JSONArray reviews = get_review.getJSONArray("result");
                JSONObject review;
                ReviewListItem reviewItem;
                review_num = reviews.length();

                for(int i=0;i<review_num;i++){
                    review = reviews.getJSONObject(i);
                    rid = review.getInt("rid");
                    user = review.getString("user");
                    password = review.getString("password");
                    date = review.getString("date");
                    rate = review.getInt("rate");
                    content = review.getString("content");

                    reviewItem = new ReviewListItem(rid, user, password, date, rate, content);
                    reviewList.add(reviewItem);
                }
            }
        }catch (JSONException e){
            e.printStackTrace();
        }

        reviewListAdapter.notifyDataSetChanged();
    }

    public static void deleteReview(int index){
        int target_rid = reviewList.get(index).getRid();
        //delete from database
        JSONObject delete_review = new SQLSender().sendSQL("DELETE from review where rid="+new Integer(target_rid).toString()+";");
        try{
            if(!delete_review.getBoolean("isError")){
                Log.d("db_conn",delete_review.toString());
            }
        }catch (JSONException e){
            e.printStackTrace();
        }
        //reload
        getReview();
    }
}
