package org.afrikcode.pesmanager.viewholder;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;


import org.afrikcode.pesmanager.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TimelineVH extends RecyclerView.ViewHolder {

    @BindView(R.id.img_status)
    ImageView statusImage;
    @BindView(R.id.tv_timelineName)
    TextView name;
    @BindView(R.id.tv_total_monthly_trans)
    TextView totalAmount;
    @BindView(R.id.parentCard)
    CardView parent;

    public TimelineVH(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public ImageView getStatusImage() {
        return statusImage;
    }

    public TextView getName() {
        return name;
    }

//    public TextView getStatusMsg() {
//        return statusMsg;
//    }

    public TextView getTotalAmount() {
        return totalAmount;
    }

    public CardView getParent() {
        return parent;
    }
}
