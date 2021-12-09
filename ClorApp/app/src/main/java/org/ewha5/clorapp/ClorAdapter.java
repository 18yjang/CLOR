package org.ewha5.clorapp;

import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ClorAdapter extends RecyclerView.Adapter<ClorAdapter.ViewHolder>
        implements OnNoteItemClickListener {
    ArrayList<Clor> items = new ArrayList<Clor>();

    OnNoteItemClickListener listener;

    int layoutType = 0;

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View itemView = inflater.inflate(R.layout.note_item, viewGroup, false);

        return new ViewHolder(itemView, this, layoutType);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        Clor item = items.get(position);
        viewHolder.setItem(item);
        viewHolder.setLayoutType(layoutType);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void addItem(Clor item) {
        items.add(item);
    }

    public void setItems(ArrayList<Clor> items) {
        this.items = items;
    }

    public Clor getItem(int position) {
        return items.get(position);
    }


    public void setOnItemClickListener(OnNoteItemClickListener listener) {
        this.listener = listener;
    }


    @Override
    public void onItemClick(ViewHolder holder, View view, int position) {
        if (listener != null) {
            listener.onItemClick(holder, view, position);
        }
    }

    public void switchLayout(int position) {
        layoutType = position;
    }


    static class ViewHolder extends RecyclerView.ViewHolder {


        LinearLayout layout1;
        LinearLayout layout2;

        ImageView moodImageView;
        ImageView moodImageView2;

        ImageView pictureExistsImageView;
        ImageView pictureImageView;

        TextView combTextView;
        TextView combTextView2;

        TextView categoryTextView;
        TextView categoryTextView2;

        TextView dateTextView;
        TextView dateTextView2;

        public ViewHolder(View itemView, final OnNoteItemClickListener listener, int layoutType) {
            super(itemView);

            layout1 = itemView.findViewById(R.id.layout1);
            categoryTextView = itemView.findViewById(R.id.categoryTextView);
            combTextView = itemView.findViewById(R.id.combTextView);
            moodImageView = itemView.findViewById(R.id.moodImageView);

            layout2 = itemView.findViewById(R.id.layout2);
            categoryTextView2 = itemView.findViewById(R.id.categoryTextView2);
            combTextView2 = itemView.findViewById(R.id.combTextView2);
            moodImageView2 = itemView.findViewById(R.id.moodImageView2);

            pictureExistsImageView = itemView.findViewById(R.id.pictureExistsImageView);
            pictureImageView = itemView.findViewById(R.id.pictureImageView);
            //pictureExistsImageView2 = itemView.findViewById(R.id.pictureExistsImageView2);

            dateTextView = itemView.findViewById(R.id.dateTextView);
            dateTextView2 = itemView.findViewById(R.id.dateTextView2);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();

                    if (listener != null) {
                        listener.onItemClick(ViewHolder.this, view, position);
                    }
                }
            });

            setLayoutType(layoutType);
        }

        public void setItem(Clor item) {

            // set mood
            String mood = item.getMood();
            int moodIndex = Integer.parseInt(mood);
            setMoodImage(moodIndex);

            // set picture exists
            String picturePath = item.getPicture();
            Log.d("ClorAdapter", "picturePath -> " + picturePath);

            if (picturePath != null && !picturePath.equals("")) {
                pictureExistsImageView.setVisibility(View.VISIBLE);
                pictureExistsImageView.setImageURI(Uri.parse("file://" + picturePath));
                pictureImageView.setVisibility(View.VISIBLE);
                pictureImageView.setImageURI(Uri.parse("file://" + picturePath));

            } else {
                pictureExistsImageView.setVisibility(View.GONE);
                pictureImageView.setVisibility(View.GONE);
                pictureImageView.setImageResource(R.drawable.noimagefound);
                //pictureExistsImageView.setImageResource(R.drawable.noimagefound);
            }

            //set combination
            String combination = item.getComb();
            if(combination.equals("0")) {
                combTextView.setText("보색 조합");
                combTextView2.setText("보색 조합");
            } else if (combination.equals("1")) {
                combTextView.setText("유사색 조합");
                combTextView2.setText("유사색 조합");
            }

            //set category
            categoryTextView.setText(item.getCategory());
            categoryTextView2.setText(item.getCategory());

            //set date
            dateTextView.setText(item.getCreateDateStr());
            dateTextView2.setText(item.getCreateDateStr());

        }

        public void setMoodImage(int moodIndex) {
            switch(moodIndex) {
                case 0:
                    moodImageView.setImageResource(R.drawable.face1_33);
                    moodImageView2.setImageResource(R.drawable.face1_33);
                    break;
                case 1:
                    moodImageView.setImageResource(R.drawable.face2_33);
                    moodImageView2.setImageResource(R.drawable.face2_33);
                    break;
                case 2:
                    moodImageView.setImageResource(R.drawable.face3_33);
                    moodImageView2.setImageResource(R.drawable.face3_33);
                    break;
                case 3:
                    moodImageView.setImageResource(R.drawable.face4_33);
                    moodImageView2.setImageResource(R.drawable.face4_33);
                    break;
                case 4:
                    moodImageView.setImageResource(R.drawable.face5_33);
                    moodImageView2.setImageResource(R.drawable.face5_33);
                    break;
                default:
                    moodImageView.setImageResource(R.drawable.face3_33);
                    moodImageView2.setImageResource(R.drawable.face3_33);
                    break;
            }
        }

        public void setLayoutType(int layoutType) {
            if (layoutType == 0) {
                layout1.setVisibility(View.VISIBLE);
                layout2.setVisibility(View.GONE);
            } else if (layoutType == 1) {
                layout1.setVisibility(View.GONE);
                layout2.setVisibility(View.VISIBLE);
            }
        }

    }

}
