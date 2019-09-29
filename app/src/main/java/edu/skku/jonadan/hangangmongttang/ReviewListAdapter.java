package edu.skku.jonadan.hangangmongttang;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.RatingBar;
import android.widget.TextView;

import java.util.ArrayList;

public class ReviewListAdapter extends BaseAdapter {

    private ArrayList<ReviewListItem> reviewList = new ArrayList<>();
    private int rid;
    private String user;
    private String password;
    private String content;
    private String date;
    private float rate;

    private TextView userTv;
    private TextView contentTv;
    private TextView dateTv;
    private RatingBar ratingBar;
    private ImageButton deleteBtn;
    private ImageButton modifyBtn;
    private ReviewInterface reviewInterface;

    public interface ReviewInterface {
        void deleteReview(int index);
        void updateReview(int index);
    }

    public ReviewListAdapter() {
    }

    public ReviewListAdapter(ArrayList<ReviewListItem> reviewList, ReviewInterface reviewInterface) {
        this.reviewList = reviewList;
        this.reviewInterface = reviewInterface;
    }

    @Override
    public int getCount() {
        return reviewList.size();
    }

    @Override
    public Object getItem(int position) {
        return reviewList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        final Context context = viewGroup.getContext();

        if(view == null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.item_review, viewGroup, false);
        }

        ReviewListItem reviewItem = reviewList.get(i);
        rid = reviewItem.getRid();
        user = reviewItem.getUser();
        password = reviewItem.getPassword();
        date = reviewItem.getDate();
        content = reviewItem.getContent();
        rate = reviewItem.getRate();

        userTv = view.findViewById(R.id.user);
        dateTv = view.findViewById(R.id.date);
        contentTv = view.findViewById(R.id.review);
        ratingBar = view.findViewById(R.id.ratingBar);
        deleteBtn = view.findViewById(R.id.delete_btn);
        modifyBtn = view.findViewById(R.id.modify_btn);

        userTv.setText(user);
        dateTv.setText(date);
        contentTv.setText(content);
        ratingBar.setRating(rate);

        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reviewInterface.deleteReview(i);
            }
        });

        modifyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reviewInterface.updateReview(i);
            }
        });
        return view;
    }
}
