package edu.skku.jonadan.hangangmongttang;

import android.app.Activity;
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
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import org.aviran.cookiebar2.CookieBar;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ReviewFragment extends Fragment {

    ListView reviewListView;
    Button reviewBtn;

    static ConstraintLayout noResultLayout;

    public static ArrayList<ReviewListItem> reviewList = new ArrayList<>();
    public static ReviewListAdapter reviewListAdapter;

    public static int facilityId;

    final private static int REVIEW_ADD = 100;
    final private static int REVIEW_ADD_NOT = 200;

    static Activity activity;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_review, container, false);

        reviewBtn = rootView.findViewById(R.id.btn_review);
        reviewListView = rootView.findViewById(R.id.review_list);
        noResultLayout = rootView.findViewById(R.id.no_result);

        reviewBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddReviewFragment addReviewFragment = new AddReviewFragment();
                Bundle bundle = new Bundle();
                bundle.putInt("request_code", AddReviewFragment.REVIEW_CREATE);
                addReviewFragment.setArguments(bundle);
                addReviewFragment.setStyle(DialogFragment.STYLE_NO_TITLE, android.R.style.Theme_Holo_Light );
                addReviewFragment.show(getActivity().getSupportFragmentManager(),"tag");
            }
        });
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        activity = getActivity();

        facilityId = InfoActivity.facilityId;
        reviewListAdapter = new ReviewListAdapter(reviewList, new ReviewListAdapter.ReviewInterface() {
            @Override
            public void deleteReview(int index) {
                int target_rid = reviewList.get(index).getRid();
                //delete from database
                JSONObject delete_review = new SQLSender().sendSQL(
                        "DELETE from review where rid="+new Integer(target_rid).toString()+";"
                );
                try{
                    if(!delete_review.getBoolean("isError")){
                        Log.d("db_conn",delete_review.toString());
                    }
                }catch (JSONException e){
                    e.printStackTrace();
                }
                reviewList.remove(index);
                reviewListAdapter.notifyDataSetChanged();
                if(reviewList.size()==0){
                    noResultLayout.setVisibility(View.VISIBLE);
                }else{
                    noResultLayout.setVisibility(View.GONE);
                }
            }

            @Override
            public void updateReview(int index) {
                int target_rid = reviewList.get(index).getRid();
                AddReviewFragment addReviewFragment = new AddReviewFragment();
                Bundle bundle = new Bundle();
                bundle.putInt("request_code", AddReviewFragment.REVIEW_UPDATE);
                bundle.putInt("rid", target_rid);
                addReviewFragment.setArguments(bundle);
                addReviewFragment.setStyle(DialogFragment.STYLE_NO_TITLE, android.R.style.Theme_Holo_Light );
                addReviewFragment.show(getActivity().getSupportFragmentManager(),"tag");
            }
        });
        reviewListView.setAdapter(reviewListAdapter);

        getReview(REVIEW_ADD_NOT);
    }

    public static void getReview(int is_review_added){
        reviewList.clear();

        int review_num;

        int rid;
        String user;
        String password;
        String content;
        String date;
        float rate;

        if(is_review_added == REVIEW_ADD){
            CookieBar.build(activity)
                    .setIcon(R.drawable.ic_thanks)
                    .setMessage("리뷰가 등록되었습니다")
                    .setMessageColor(R.color.colorBlack)
                    .setBackgroundColor(R.color.colorAccent)
                    .setCookiePosition(CookieBar.TOP)
                    .setDuration(2000)
                    .show();
        }

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

        if(reviewList.size()==0){
            noResultLayout.setVisibility(View.VISIBLE);
        }else{
            noResultLayout.setVisibility(View.GONE);
        }
    }
}
