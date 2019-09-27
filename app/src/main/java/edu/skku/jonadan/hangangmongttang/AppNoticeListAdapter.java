package edu.skku.jonadan.hangangmongttang;

import android.content.Context;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class AppNoticeListAdapter extends RecyclerView.Adapter {

    public static final int HEADER = 0;
    public static final int CHILD = 1;

    private ArrayList<Notice> noticeList;
    private ArrayList<NoticeItem> noticeItemList;
    private int openItem = -1;

    public AppNoticeListAdapter(ArrayList<Notice> noticeList) {
        this.noticeList = noticeList;
        this. noticeItemList = new ArrayList<>();
        for (Notice notice: noticeList) {
            this.noticeItemList.add(new NoticeItem(HEADER, notice));
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = null;
        Context context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        RecyclerView.LayoutParams lp =
                new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        switch (viewType) {
            case HEADER:
                view = inflater.inflate(R.layout.item_notice_header, parent, false);
                ListHeaderViewHolder header = new ListHeaderViewHolder(view);
                return header;
            case CHILD:
                view = inflater.inflate(R.layout.item_notice_child, parent, false);
                ListChildViewHolder child = new ListChildViewHolder(view);
                return child;
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        final NoticeItem item = noticeItemList.get(position);
        switch (item.type) {
            case HEADER:
                final ListHeaderViewHolder headerHolder = (ListHeaderViewHolder) holder;
                headerHolder.noticeTitle.setText(item.getNotice().getTitle());
                headerHolder.noticeDate.setText(item.getNotice().getDate());
                headerHolder.noticeHeaderContainer.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (openItem == -1) {
                            openItem = position;
                            noticeItemList.clear();
                            for (Notice notice: noticeList) {
                                noticeItemList.add(new NoticeItem(HEADER, notice));
                            }
                            noticeItemList.add(position+1, new NoticeItem(CHILD, item.getNotice()));
                            notifyDataSetChanged();
                        } else {
                            if (openItem == position) {
                                openItem = -1;
                                noticeItemList.remove(position+1);
                                notifyDataSetChanged();
                                // Open content
                            } else {
                                if (position > openItem) {
                                    openItem = position - 1;
                                } else {
                                    openItem = position;
                                }
                                noticeItemList.clear();
                                for (Notice notice: noticeList) {
                                    noticeItemList.add(new NoticeItem(HEADER, notice));
                                }
                                noticeItemList.add(openItem+1, new NoticeItem(CHILD, item.getNotice()));
                                notifyDataSetChanged();
                            }
                        }
                    }
                });
                break;
            case CHILD:
                final ListChildViewHolder childHolder = (ListChildViewHolder) holder;
                childHolder.noticeContent.setText(item.getNotice().getContent());
                childHolder.noticeContent.setMovementMethod(new ScrollingMovementMethod());
                childHolder.noticeContent.scrollTo(0, 0);
                childHolder.noticeContent.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View view, MotionEvent motionEvent) {
                        boolean isLarger;

                        isLarger = ((TextView) view).getLineCount()
                                * ((TextView) view).getLineHeight() > view.getHeight();
                        if (motionEvent.getAction() == MotionEvent.ACTION_MOVE
                                && isLarger) {
                            view.getParent().requestDisallowInterceptTouchEvent(true);

                        } else {
                            view.getParent().requestDisallowInterceptTouchEvent(false);
                        }
                        return false;
                    }
                });
                break;
        }
    }

    @Override
    public int getItemViewType(int position) {
        return noticeItemList.get(position).getType();
    }

    @Override
    public int getItemCount() {
        return noticeItemList.size();
    }

    public static class ListHeaderViewHolder extends RecyclerView.ViewHolder {

        public LinearLayout noticeHeaderContainer;
        public TextView noticeTitle;
        public TextView noticeDate;

        public ListHeaderViewHolder(View itemView) {
            super(itemView);
            noticeHeaderContainer = itemView.findViewById(R.id.notice_header_container);
            noticeTitle = itemView.findViewById(R.id.notice_title);
            noticeDate = itemView.findViewById(R.id.notice_date);
        }
    }

    public static class ListChildViewHolder extends RecyclerView.ViewHolder {

        public TextView noticeContent;

        public ListChildViewHolder(View itemView) {
            super(itemView);
            noticeContent = itemView.findViewById(R.id.notice_content);
        }
    }

    private class NoticeItem {

        private int type;
        private Notice notice;

        public NoticeItem(int type, Notice notice) {
            this.type = type;
            this.notice = notice;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public Notice getNotice() {
            return notice;
        }

        public void setNotice(Notice notice) {
            this.notice = notice;
        }
    }
}
