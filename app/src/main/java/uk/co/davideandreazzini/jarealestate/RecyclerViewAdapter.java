package uk.co.davideandreazzini.jarealestate;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;

import Models.IntentExtras;
import Models.Property;

/**
 * Created by davide on 04/11/16.
 */

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public ArrayList<Property> myValues;
    private final PropertiesActivity mActivity;

    public RecyclerViewAdapter (ArrayList<Property> myValues, PropertiesActivity mActivity){
        this.myValues= myValues;
        this.mActivity = mActivity;
    }
    private static final int FOOTER_VIEW = 1;

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View listItem;
        if(viewType==FOOTER_VIEW){
            listItem = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_footer, parent, false);
            return new FooterViewHolder(listItem);
        }else{
            listItem = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview, parent, false);
            return new CardHoler(listItem);
        }


    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        try {
            if (holder instanceof CardHoler) {
                Gson gson = new Gson();
                CardHoler vh = (CardHoler) holder;
                Property prop = myValues.get(position);
                String propTitle = prop.propertyTypeFullDescription + " " + prop.displayAmount;
                vh.myTextView.setText(propTitle);
                vh.propDetailsBtn.setOnClickListener(e->{
                    ArrayList<IntentExtras> extras = new ArrayList<IntentExtras>();
                    extras.add(new IntentExtras("property", prop.key));
                    extras.add(new IntentExtras("collection", prop.type));
                    mActivity.goTo(new PropertyActivity(), extras);
                });
                if(prop.bmp!=null){
                    vh.propertyImage.setImageBitmap(prop.bmp);
                }
//                holder.itemView.setOnClickListener(e-> Log.d("d", myValues.get(position).propertyTypeFullDescription));
            } else if (holder instanceof FooterViewHolder) {
                FooterViewHolder vh = (FooterViewHolder) holder;
                vh.loadMoreBtn.setOnClickListener(e->mActivity.loadMore());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    @Override
    public int getItemCount() {
        return myValues.size()+1;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == myValues.size()) {
            // This is where we'll add footer.
            return FOOTER_VIEW;
        }

        return super.getItemViewType(position);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public MyViewHolder(View itemView) {
            super(itemView);
        }
    }

    public class CardHoler extends MyViewHolder {
        private TextView myTextView;
        private ImageView propertyImage;
        private Button propDetailsBtn;
        public CardHoler(View itemView) {
            super(itemView);
            myTextView = (TextView)itemView.findViewById(R.id.text_cardview);
            propertyImage = (ImageView) itemView.findViewById(R.id.property_image);
            propDetailsBtn = (Button) itemView.findViewById(R.id.propDetailsBtn);
        }
    }


    public class FooterViewHolder extends MyViewHolder {
        private Button loadMoreBtn;
        public FooterViewHolder(View itemView) {
            super(itemView);
            loadMoreBtn = (Button)itemView.findViewById(R.id.loadMoreBtn);
        }
    }


}