package org.covid19india.android.safepassageindia.passscanner.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.covid19india.android.safepassageindia.R;
import org.covid19india.android.safepassageindia.activity.PassViewActivity;
import org.covid19india.android.safepassageindia.model.UserPassList;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

public class PassAdapter extends RecyclerView.Adapter<PassAdapter.PassHolder> {
    UserPassList userPassList;
    Context context;

    public PassAdapter(UserPassList userPassList) {
        this.userPassList = userPassList;
    }

    @NonNull
    @Override
    public PassAdapter.PassHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_pass_list, null);
        return new PassHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PassAdapter.PassHolder holder, int position) {
        final int pos = position;
        String passType = "Type: " + userPassList.getPasses().get(position).getPass_type();
        String issuer = "Issuer: " + userPassList.getPasses().get(position).getPass_issuedBy();
        holder.imageView.setImageResource(R.drawable.img);
        holder.title1.setText(passType);
        holder.title2.setText(issuer);
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((CardView) view).setEnabled(false);
                Intent intent = new Intent(context, PassViewActivity.class);
                intent.putExtra("user", userPassList.getUsers().get(0));
                intent.putExtra("pass", userPassList.getPasses().get(pos));
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return userPassList.getPasses().size();
    }

    public class PassHolder extends RecyclerView.ViewHolder {
        private ImageView imageView;
        private TextView title1, title2;
        private CardView cardView;

        public PassHolder(@NonNull final View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.user_pic);
            title1 = itemView.findViewById(R.id.title1);
            title2 = itemView.findViewById(R.id.title2);
            cardView = itemView.findViewById(R.id.card_view);
        }
    }
}
