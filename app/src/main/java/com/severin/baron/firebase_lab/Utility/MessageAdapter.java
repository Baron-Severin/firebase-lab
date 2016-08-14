package com.severin.baron.firebase_lab.Utility;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.severin.baron.firebase_lab.Model.Message;
import com.severin.baron.firebase_lab.R;

import java.util.List;

/**
 * Created by erikrudie on 8/14/16.
 */
public class MessageAdapter extends 
RecyclerView.Adapter<MessageAdapter.ViewHolder> {

public static class ViewHolder extends RecyclerView.ViewHolder {
    public TextView tvTimestamp, tvUsername, tvMessageBody;

    public ViewHolder(View itemView) {
        super(itemView);

        tvTimestamp = (TextView) itemView.findViewById(R.id.recycler_item_timeStamp);
        tvUsername = (TextView) itemView.findViewById(R.id.recycler_item_userName);
        tvMessageBody = (TextView) itemView.findViewById(R.id.recycler_item_textBody);
    }
}

    private List<Message> mModel;
    private Context mContext;

public MessageAdapter(Context context, List<Message> model) {
mModel = model;
mContext = context;
        }

private Context getContext() {
        return mContext;
        }

@Override
public MessageAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(R.layout.recycler_item_chat, parent, false);

        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
        }

@Override
public void onBindViewHolder(MessageAdapter.ViewHolder viewHolder, int position) {
    Message model = mModel.get(position);

    TextView tvTimestamp = viewHolder.tvTimestamp;
    tvTimestamp.setText(model.getTimeStamp().toString());
    TextView tvUsername = viewHolder.tvUsername;
    tvUsername.setText(model.getUserId());
    TextView tvTextBody = viewHolder.tvMessageBody;
    tvTextBody.setText(model.getBody());
}

// Returns the total count of items in the list
@Override
public int getItemCount() {
        return mModel.size();
        }

}