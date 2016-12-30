package com.yyy.xxx.mygalacticon;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.io.IOException;
import java.util.ArrayList;


/**
 * Created by len on 2016. 12. 28..
 */

public class CardFragment extends Fragment {

    private static String TAG = CardFragment.class.getName();

    ContentAdapter contentAdapter;
    PhotoRequester mphotoRequester;
    LinearLayoutManager mLinearLayoutManager;
    ArrayList<Photo> photoArrayList = new ArrayList<>();


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        RecyclerView recyclerView = (RecyclerView) inflater.inflate(
                R.layout.recyclerview, container, false);

        contentAdapter = new ContentAdapter(recyclerView.getContext(), photoArrayList);
        mLinearLayoutManager = new LinearLayoutManager(getActivity());

        recyclerView.setLayoutManager(mLinearLayoutManager);
        recyclerView.setAdapter(contentAdapter);
//        recyclerView.setHasFixedSize(true);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                int totalItemCount = recyclerView.getLayoutManager().getItemCount();
                if (!mphotoRequester.isLoadingData() && totalItemCount == getLastVisibleItemPosition() + 1)
                {
                    try {
                        mphotoRequester.getPhoto(getActivity());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        return recyclerView;
    }

    private int getLastVisibleItemPosition() {
        return mLinearLayoutManager.findLastVisibleItemPosition();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mphotoRequester = new PhotoRequester(getActivity(),photoRequesterResponse);

//        Log.d(TAG, String.valueOf(photoArrayList.size()));

        try {
            // 초기화면에 가득차게 만들고싶음.
            mphotoRequester.getPhoto(getActivity());
            mphotoRequester.getPhoto(getActivity());
            mphotoRequester.getPhoto(getActivity());
            mphotoRequester.getPhoto(getActivity());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private PhotoRequester.PhotoRequesterResponse  photoRequesterResponse = new PhotoRequester.PhotoRequesterResponse() {
        @Override
        public void receivedNewPhoto(Photo newPhoto) {
            if (contentAdapter != null){

                photoArrayList.add(newPhoto);
                contentAdapter.notifyDataSetChanged();
            }
        }
    };



    private class ContentAdapter extends RecyclerView.Adapter<ViewHolder>{

        private Context mContext;
        private ArrayList<Photo> photos;


        public ContentAdapter(Context context, ArrayList<Photo> photoArrayList) {
            mContext = context;
            photos = photoArrayList;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ViewHolder(LayoutInflater.from(parent.getContext()), parent);

        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {

            Photo photo = photos.get(position);

            Glide.with(CardFragment.this)
                    .load(photo.getmUrl())
                    .into(holder.card_imageView);

            holder.name.setText(photo.getmHumanDate());
            holder.desciption.setText(photo.getmExplanation());
        }

        @Override
        public int getItemCount() {
            return photos.size();
        }
    }

    private static class ViewHolder extends RecyclerView.ViewHolder{

        public ImageView card_imageView;
        public TextView name;
        public TextView desciption;


        public ViewHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.item_card, parent,false));

            card_imageView = (ImageView) itemView.findViewById(R.id.card_Imageview);
            name = (TextView) itemView.findViewById(R.id.card_name);
            desciption = (TextView) itemView.findViewById(R.id.card_text);
        }
    }

}
