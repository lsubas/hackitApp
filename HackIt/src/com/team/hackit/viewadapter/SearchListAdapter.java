package com.team.hackit.viewadapter;

import java.net.URI;
import java.util.List;

import com.team.hackit.ImageLoadTask;
import com.team.hackit.R;
import com.team.hackit.model.SearchResult;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class SearchListAdapter extends ArrayAdapter<SearchResult>{
	 
	private final Activity context;
	private final List<SearchResult> searchResults;
		
	public SearchListAdapter(Activity context, List<SearchResult> searchResults) {
		super(context, R.layout.list_single, searchResults);
		this.context = context;
		this.searchResults = searchResults;
	}
	@Override
	public View getView(int position, View view, ViewGroup parent) {
		
		
		LayoutInflater inflater = context.getLayoutInflater();
		View rowView= inflater.inflate(R.layout.list_single, null, true);
		TextView txtTitle = (TextView) rowView.findViewById(R.id.txt);
		ImageView imageView = (ImageView) rowView.findViewById(R.id.img);
		txtTitle.setText(searchResults.get(position).getTitle());
		new ImageLoadTask(searchResults.get(position).getThumbnail(), imageView).execute();
		return rowView;
	}
	}