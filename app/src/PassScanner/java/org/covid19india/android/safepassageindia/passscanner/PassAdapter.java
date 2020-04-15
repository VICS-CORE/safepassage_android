package org.covid19india.android.safepassageindia.passscanner;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.covid19india.android.safepassageindia.Pass;
import org.covid19india.android.safepassageindia.R;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class PassAdapter extends RecyclerView.Adapter<PassAdapter.PassHolder> {
    List<Pass> passes;

    public PassAdapter(List<Pass> passes) {
        this.passes = passes;
    }

    @NonNull
    @Override
    public PassAdapter.PassHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_pass_list, null);
        return new PassHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PassAdapter.PassHolder holder, int position) {
        holder.imageView.setImageResource(R.mipmap.ic_launcher);
        holder.title1.setText(passes.get(position).getPass_type());
        holder.title2.setText(passes.get(position).getPass_issuedBy() + "");
    }

    @Override
    public int getItemCount() {
        return passes.size();
    }

    public class PassHolder extends RecyclerView.ViewHolder {
        private ImageView imageView;
        private TextView title1, title2;

        public PassHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.user_pic);
            title1 = itemView.findViewById(R.id.title1);
            title2 = itemView.findViewById(R.id.title2);
        }
    }
}
