package com.example.android.guardiannews;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Maffey on 2017-07-07.
 */

public class NewsAdapter extends ArrayAdapter<News> {

    public NewsAdapter(Context context, List<News> news) {
        super(context, 0, news);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Check if there is an existing list item view (called convertView) that we can reuse,
        // otherwise, if convertView is null, then inflate a new list item layout.
        View listItemView = convertView;
        ViewHolder holder;

        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.list_item, parent, false);
            // Initialize holder and find IDs of the corresponding views
            holder = new ViewHolder();
            holder.titleView = listItemView.findViewById(R.id.title);
            holder.sectionView = listItemView.findViewById(R.id.section);
            listItemView.setTag(holder);
        } else {
            holder = (ViewHolder) listItemView.getTag();
        }
        News currentNews = getItem(position);
        holder.titleView.setText(currentNews.getTitle());
        holder.sectionView.setText(currentNews.getSection());

        return convertView;
    }

    private static class ViewHolder {
        private TextView titleView;
        private TextView sectionView;
    }
}
