package com.duducat.astrology;

import com.duducat.activeconfig.ActiveConfig;
import com.duducat.activeconfig.ActiveConfig.AsyncGetImageHandler;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class HoroscopeFragment extends Fragment {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		int i = getArguments().getInt("i") + 1;
		final View view = inflater.inflate(R.layout.fragment_main, container, false);

		ActiveConfig.setTextViewWithKey("K" + i, null, ((TextView) view.findViewById(R.id.content)));
		ActiveConfig.setImageViewWithKey("K" + i, null, ((ImageView) view.findViewById(R.id.cover)));
		
		ActiveConfig.getImageAsync("K"+i, new AsyncGetImageHandler() {
			
			@Override
			public void OnSuccess(Drawable image) {
				((ImageView) view.findViewById(R.id.cover)).setImageDrawable(image);
				view.findViewById(R.id.loading).setVisibility(View.INVISIBLE);
				
			}
			
			@Override
			public void OnFailed() {
				
			}
		});
		
		view.findViewById(R.id.content).setOnLongClickListener(new OnLongClickListener() {
			
			@Override
			public boolean onLongClick(View arg0) {
				ActiveConfig.clearCache();
				return true;
			}
		});
		
		return view;
	}
}
