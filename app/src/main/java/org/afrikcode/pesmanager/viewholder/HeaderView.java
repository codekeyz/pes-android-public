package org.afrikcode.pesmanager.viewholder;

import android.view.View;
import android.widget.TextView;

import org.afrikcode.pesmanager.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HeaderView {

    @BindView(R.id.username)
    public TextView username;
    @BindView(R.id.email)
    public TextView email;

    public HeaderView(View view) {
        ButterKnife.bind(this, view);
    }
}
