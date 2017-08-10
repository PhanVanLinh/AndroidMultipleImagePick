package com.toong.multipleimagepick.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import com.squareup.picasso.Picasso;
import com.toong.multipleimagepick.GalleryActivity;
import com.toong.multipleimagepick.GalleryImage;
import com.toong.multipleimagepick.R;
import java.util.ArrayList;

public class MyRecyclerViewAdapter extends RecyclerView.Adapter<MyRecyclerViewAdapter.ViewHolder> {
    @GalleryActivity.Action
    private String mAction;
    private Context mContext;
    private ArrayList<GalleryImage> mData = new ArrayList<>();
    private LayoutInflater mInflater;
    private ItemClickListener mItemClickListener;

    public MyRecyclerViewAdapter(Context context, String action) {
        mInflater = LayoutInflater.from(context);
        mContext = context;
        mAction = action;
    }

    public void addAll(ArrayList<GalleryImage> files) {
        mData.clear();
        mData.addAll(files);
        notifyDataSetChanged();
    }

    public ArrayList<GalleryImage> getSelected() {
        ArrayList<GalleryImage> dataT = new ArrayList<GalleryImage>();
        for (int i = 0; i < mData.size(); i++) {
            if (mData.get(i).isSelected()) {
                dataT.add(mData.get(i));
            }
        }
        return dataT;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.gallery_item, parent, false);
        return new ViewHolder(view, mItemClickListener);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        GalleryImage data = mData.get(position);
        holder.bind(mContext, data);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ItemClickListener mItemClickListener;
        private ImageView ivImage;
        private View viewDim;
        private CheckBox checkBox;
        private GalleryImage mData;

        private ViewHolder(View itemView, ItemClickListener itemClickListener) {
            super(itemView);
            ivImage = (ImageView) itemView.findViewById(R.id.image_source);
            viewDim = itemView.findViewById(R.id.view_dim);
            checkBox = (CheckBox) itemView.findViewById(R.id.checkbox_check);
            mItemClickListener = itemClickListener;

            ivImage.setOnClickListener(this);
            checkBox.setOnClickListener(this);
        }

        void bind(Context context, GalleryImage data) {
            mData = data;
            Picasso.with(context)
                    .load("file://" + data.sdcardPath)
                    .noFade()
                    .centerCrop()
                    .fit()
                    .into(ivImage);
            updateView();
        }

        private void updateView() {
            viewDim.setVisibility(mData.isSelected() ? View.VISIBLE : View.GONE);
            checkBox.setChecked(mData.isSelected());
        }

        @Override
        public void onClick(View view) {
            if (mItemClickListener != null) {
                mItemClickListener.onItemClick(view, getAdapterPosition());
            }
            mData.setSelected(!mData.isSelected());
            updateView();
        }
    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.mItemClickListener = itemClickListener;
    }

    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}