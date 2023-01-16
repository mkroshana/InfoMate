package com.uob.infomate.Recommendation;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.uob.infomate.Locations.NABridgeActivity;
import com.uob.infomate.R;
import com.uob.infomate.Locations.SigiriyaActivity;
import com.uob.infomate.Locations.TempleOfToothActivity;

import java.util.ArrayList;

public class RecommendationAdapter extends RecyclerView.Adapter<RecommendationHolder> {

    Context c;
    ArrayList<RecommendationModel> models;

    public RecommendationAdapter(Context c, ArrayList<RecommendationModel> models) {
        this.c = c;
        this.models = models;
    }

    @NonNull
    @Override
    public RecommendationHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType)
    {

        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.recommendation_row, null);
        return new RecommendationHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecommendationHolder recommendationHolder, int i)
    {
        recommendationHolder.mTitle.setText(models.get(i).getTitle());
        recommendationHolder.mImageView.setImageResource(models.get(i).getImg());

        recommendationHolder.setItemClickListenerRecommendation(new ItemClickListenerRecommendation()
        {
            @Override
            public void onItemClickListener(View v, int position)
            {
                String UITitle = models.get(position).getTitle();

            }
        });

        recommendationHolder.setItemClickListenerRecommendation(new ItemClickListenerRecommendation()
        {
            @Override
            public void onItemClickListener(View v, int position)
            {
                if (models.get(position).getTitle().equals("Sigiriya Rock"))
                {
                    Intent intent = new Intent(c, SigiriyaActivity.class);
                    c.startActivity(intent);
                }
                if (models.get(position).getTitle().equals("Nine Arches Bridge"))
                {
                    Intent intent = new Intent(c, NABridgeActivity.class);
                    c.startActivity(intent);
                }
                if (models.get(position).getTitle().equals("Temple of Tooth"))
                {
                    Intent intent = new Intent(c, TempleOfToothActivity.class);
                    c.startActivity(intent);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return models.size();
    }
}
