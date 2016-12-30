package com.yyy.xxx.mygalacticon;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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
 *
 */

public class ListContentFragment extends Fragment {

    private static String TAG = ListContentFragment.class.getName();

    ContentAdapter adapter;
    PhotoRequester mPhotoRequester;
    LinearLayoutManager mLinearLayoutManager;
    ArrayList<Photo> photoArrayList = new ArrayList<>();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
//        recyclerView = (RecyclerView) inflater.inflate(
//                R.layout.recyclerview, container, false);
        View rootView = inflater.inflate(R.layout.recyclerview, container, false);
        RecyclerView recyclerView = (RecyclerView) rootView.findViewById(R.id.my_recycler_view);


        adapter = new ContentAdapter(getContext(), photoArrayList);

        mLinearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(mLinearLayoutManager);
        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(true);
        //진우형이 캐스트를 하지않고 인터페이스를 객체생성하여서 넘겨주었음

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
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mPhotoRequester = new PhotoRequester(getActivity(), photoRequesterResponse);


        Log.d(TAG, String.valueOf(photoArrayList.size()));

            try {
                // 초기화면에 가득차게 만들고싶음.
                    mPhotoRequester.getPhoto(getActivity());
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
/**
 *  진우님이 코딩한 부분
 * */
    private PhotoRequester.PhotoRequesterResponse  photoRequesterResponse = new PhotoRequester.PhotoRequesterResponse() {
        @Override
        public void receivedNewPhoto(Photo newPhoto) {
            if (adapter != null){

                photoArrayList.add(newPhoto);
                adapter.notifyDataSetChanged();
            }
        }
    };

    private int getLastVisibleItemPosition() {
        return mLinearLayoutManager.findLastVisibleItemPosition();
    }


    //private class ContentAdapter extends RecyclerView.Adapter<ViewHolder> implements PhotoRequester.PhotoRequesterResponse{
    private class ContentAdapter extends RecyclerView.Adapter<ViewHolder> {

        private Context mContext;
        private ArrayList<Photo> photos;
        //private Photo mPhoto;


        public ContentAdapter(Context context, ArrayList<Photo> photoArrayList) {
            mContext = context;
            photos = photoArrayList;
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {

            Photo photo = photos.get(position);

            Glide.with(ListContentFragment.this)
                    .load(photo.getmUrl())
                    .into(holder.avator);

            holder.name.setText(photo.getmHumanDate());
            holder.description.setText(photo.getmExplanation());
        }

        public void addPhoto(Photo photo){
            if (photos != null){
                photos.add(photo);
            }
        }


        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ViewHolder(LayoutInflater.from(parent.getContext()), parent);
        }

        @Override
        public int getItemCount() {
            return photos.size();
        }

        //@Override
        //public void receivedNewPhoto(final Photo newPhoto) {
        //
        //    getActivity().runOnUiThread(new Runnable() {
        //        @Override
        //        public void run() {
        //            photos.add(newPhoto);
        //            adapter.notifyItemInserted(photos.size());
        //        }
        //    });
        //}
    }

    private class ViewHolder extends RecyclerView.ViewHolder{

        public ImageView avator;
        public TextView name;
        public TextView description;

        public ViewHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.item_list, parent, false));

            avator = (ImageView) itemView.findViewById(R.id.list_avator);
            name = (TextView) itemView.findViewById(R.id.list_title);
            description = (TextView) itemView.findViewById(R.id.list_decs);

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