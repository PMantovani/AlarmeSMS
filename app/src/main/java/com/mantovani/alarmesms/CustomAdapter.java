package com.mantovani.alarmesms;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.List;

/**
 * Class created for usage on a list view with info of senders and patterns
 * Created by Pedro on 27-Jan-17.
 */

class CustomAdapter extends ArrayAdapter<String> {

    private final Activity context;
    private final List<String> item;
    private final String type; // "sender" or "pattern". Specifies which adapter the instance is.

    CustomAdapter(Activity context, List<String> item, String type) {
        super(context, R.layout.rule_item, item);

        this.context = context;
        this.item = item;
        this.type = type;
    }

    @Override @NonNull
    public View getView(final int position, View view, @NonNull ViewGroup parent) {
        LayoutInflater inflater=context.getLayoutInflater();
        View rowView = view;

        if (rowView == null) {
            rowView = inflater.inflate(R.layout.rule_item, parent, false);
            ViewHolder holder = new ViewHolder();
            holder.delete = (ImageView) rowView.findViewById(R.id.delete_img);
            holder.text = (TextView) rowView.findViewById(R.id.text_view);
            rowView.setTag(holder);
        }

        ViewHolder holder = (ViewHolder) rowView.getTag();
        holder.text.setText(item.get(position));
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Removes item from list
                item.remove(position);

                // Cast to MainActivity to find rule object
                MainActivity activity = (MainActivity) context;
                // If this adapter is of type sender, delete sender at rule
                if (type.equals("sender")) {
                    activity.rule.deleteSender(position);
                }
                else {
                    activity.rule.deletePattern(position);
                }


                // Refreshes adapter UI and saves changes in rule
                notifyDataSetChanged();
                activity.saveRuleChange();
            }
        });

        return rowView;
    }

    static class ViewHolder {
        public ImageView delete;
        public TextView text;
    }
}
