package com.yyy.xxx.mygalacticon;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
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

public class TilFragment extends Fragment {


    ContentAdapter adapter;
    PhotoRequester mPhotoRequester;
    ArrayList<Photo> photoArrayList = new ArrayList<>();
    GridLayoutManager gridLayoutManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        RecyclerView recyclerView = (RecyclerView) inflater.inflate(
                R.layout.recyclerview, container, false);

        adapter = new ContentAdapter(recyclerView.getContext(), photoArrayList);

        gridLayoutManager = new GridLayoutManager(getActivity(), 2);
        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(true);

        // Set padding for Tiles (not needed for Cards/Lists!)
        int tilePadding = getResources().getDimensionPixelSize(R.dimen.tile_padding);
        recyclerView.setPadding(tilePadding, tilePadding, tilePadding, tilePadding);
        recyclerView.setLayoutManager(gridLayoutManager);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                int totalItemCount = recyclerView.getLayoutManager().getItemCount();
                if (!mPhotoRequester.isLoadingData() && totalItemCount == getLastVisibleItemPosition() + 1)
                {
                    try {
                        mPhotoRequester.getPhoto(getActivity());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        return recyclerView;
    }

    private int getLastVisibleItemPosition() {
        return gridLayoutManager.findLastVisibleItemPosition();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mPhotoRequester = new PhotoRequester(getActivity(), photoRequesterResponse);
        try {
            mPhotoRequester.getPhoto(getActivity());
            mPhotoRequester.getPhoto(getActivity());
            mPhotoRequester.getPhoto(getActivity());
            mPhotoRequester.getPhoto(getActivity());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private PhotoRequester.PhotoRequesterResponse photoRequesterResponse = new PhotoRequester.PhotoRequesterResponse() {
        @Override
        public void receivedNewPhoto(Photo newPhoto) {

            if (adapter != null){

//                if (photoArrayList.size() != 10) {
                    photoArrayList.add(newPhoto);
//                }
                adapter.notifyDataSetChanged();
            }
        }
    };

    public class ContentAdapter extends RecyclerView.Adapter<ViewHolder> {
        // Set numbers of List in RecyclerView.
//        private static final int LENGTH = 18;
//        private final String[] mPlaces;
//        private final Drawable[] mPlacePictures;
        private Context mContext;
        private ArrayList<Photo> photos;

        public ContentAdapter(Context context, ArrayList<Photo> photoArrayList) {
            mContext = context;
            photos = photoArrayList;

//            Resources resources = context.getResources();
//            mPlaces = resources.getStringArray(R.array.places);
//            TypedArray a = resources.obtainTypedArray(R.array.places_picture);
//            mPlacePictures = new Drawable[a.length()];
//            for (int i = 0; i < mPlacePictures.length; i++) {
//                mPlacePictures[i] = a.getDrawable(i);
//            }
//            a.recycle();
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ViewHolder(LayoutInflater.from(parent.getContext()), parent);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {

            Photo photo = photos.get(position);

            Glide.with(TilFragment.this)
                    .load(photo.getmUrl())
                    .into(holder.picture);

            holder.name.setText(photo.getmExplanation());

//            holder.picture.setImageDrawable(mPlacePictures[position % mPlacePictures.length]);
//            holder.name.setText(mPlaces[position % mPlaces.length]);
        }

        @Override
        public int getItemCount() {
            return photos.size();
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView picture;
        public TextView name;
        public ViewHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.item_tile, parent, false));

            picture = (ImageView) itemView.findViewById(R.id.tile_picture);
            name = (TextView) itemView.findViewById(R.id.tile_title);

//            itemView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Context context = v.getContext();
//                    Intent intent = new Intent(context, DetailActivity.class);
//                    intent.putExtra(DetailActivity.EXTRA_POSITION,getAdapterPosition());
//                    context.startActivity(intent);
//                }
//            });
        }
    }

}

