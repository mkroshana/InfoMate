package com.uob.infomate.Recommendation;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.uob.infomate.R;

public class RecommendationHolder extends RecyclerView.ViewHolder implements View.OnClickListener
{
    ImageView mImageView;
    TextView mTitle;
    ItemClickListenerRecommendation itemClickListenerRecommendation;

    RecommendationHolder(@NonNull View itemView){
        super(itemView);

        this.mImageView = itemView.findViewById(R.id.cardImage);
        this.mTitle = itemView.findViewById(R.id.cardTitle);

        itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v)
    {
        this.itemClickListenerRecommendation.onItemClickListener(v, getLayoutPosition());
    }

    public void setItemClickListenerRecommendation (ItemClickListenerRecommendation icr)
    {
        this.itemClickListenerRecommendation = icr;
    }
}
