package uk.co.davideandreazzini.jarealestate;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.google.gson.Gson;
import java.util.ArrayList;
import Objects.IntentExtras;
import Objects.Property;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public ArrayList<Property> propertiesArray;
    private final PropertiesActivity mActivity;

    public RecyclerViewAdapter (ArrayList<Property> propertiesArray, PropertiesActivity mActivity){
        this.propertiesArray= propertiesArray;
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
            return new CardHolder(listItem);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        try {
            if (holder instanceof CardHolder) {
                // if the holder is of type CardHolder render the card UI
                CardHolder vh = (CardHolder) holder;
                Property prop = propertiesArray.get(position);
                String propTitle = prop.propertyTypeFullDescription + " " + prop.displayAmount;
                vh.myTextView.setText(propTitle);
                // onClick on the button redirect to the PropertyDetail
                vh.propDetailsBtn.setOnClickListener(e->{
                    ArrayList<IntentExtras> extras = new ArrayList<IntentExtras>();
                    extras.add(new IntentExtras("property", prop.key));
                    extras.add(new IntentExtras("collection", prop.type));
                    mActivity.goTo(new PropertyActivity(), extras);
                });
                if(prop.bmp!=null){
                    vh.propertyImage.setImageBitmap(prop.bmp);
                }
            } else if (holder instanceof FooterViewHolder) {
                // if the holder is an instance of FooterViewHolder
                // render the Footer UI, button for loadMore
                FooterViewHolder vh = (FooterViewHolder) holder;
                vh.loadMoreBtn.setOnClickListener(e->mActivity.loadMore());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    @Override
    public int getItemCount() {
        return propertiesArray.size()+1;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == propertiesArray.size()) {
            // if the position is the end of the array return the value
            // of the FOOTER_VIEW
            return FOOTER_VIEW;
        }

        return super.getItemViewType(position);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public MyViewHolder(View itemView) {
            super(itemView);
        }
    }

    public class CardHolder extends MyViewHolder {
        private TextView myTextView;
        private ImageView propertyImage;
        private Button propDetailsBtn;
        public CardHolder(View itemView) {
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