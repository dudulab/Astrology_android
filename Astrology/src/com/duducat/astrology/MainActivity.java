package com.duducat.astrology;

import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.Vector;

import com.duducat.activeconfig.ActiveConfig;

import android.content.Context;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TabHost.TabContentFactory;


public class MainActivity extends FragmentActivity implements TabHost.OnTabChangeListener, ViewPager.OnPageChangeListener {

	private TabHost mTabHost;
	private ViewPager mViewPager;
	private HorizontalScrollView scrollView;
	private HashMap<String, TabInfo> mapTabInfo = new HashMap<String, MainActivity.TabInfo>();
	private PagerAdapter mPagerAdapter;

	private class TabInfo {
		private String tag;
		public View On;
		public View Off;
		public View Tab;

		TabInfo(String tag, Class<?> clazz, Bundle args) {
			this.tag = tag;
		}

	}

	class TabFactory implements TabContentFactory {

		private final Context mContext;

		/**
		 * @param context
		 */
		public TabFactory(Context context) {
			mContext = context;
		}

		public View createTabContent(String tag) {
			View v = new View(mContext);
			v.setMinimumWidth(0);
			v.setMinimumHeight(0);
			return v;
		}

	}

	protected void onCreate(Bundle savedInstanceState) {

		ActiveConfig.register(getApplicationContext(), "5lRViOaV", "qrmLeHBt2fSLOv5Q");

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		scrollView = (HorizontalScrollView) findViewById(R.id.scroll);
		this.intialiseViewPager();
		this.initialiseTabHost(savedInstanceState);
	}

	protected void onSaveInstanceState(Bundle outState) {
		outState.putString("tab", mTabHost.getCurrentTabTag()); // save the tab
																// selected
		super.onSaveInstanceState(outState);
	}

	private void intialiseViewPager() {

		List<Fragment> fragments = new Vector<Fragment>();

		for (int i = 0; i < 12; i++) {
			Bundle bundle = new Bundle();
			bundle.putInt("i", i);
			Fragment f = Fragment.instantiate(this, HoroscopeFragment.class.getName(), bundle);
			fragments.add(f);
		}

		this.mPagerAdapter = new PagerAdapter(super.getSupportFragmentManager(), fragments);
		//
		this.mViewPager = (ViewPager) super.findViewById(R.id.viewpager);
		this.mViewPager.setAdapter(this.mPagerAdapter);
		this.mViewPager.setOnPageChangeListener(this);
	}

	private void initialiseTabHost(Bundle args) {
		mTabHost = (TabHost) findViewById(android.R.id.tabhost);
		mTabHost.setup();
		TabInfo tabInfo = null;
		for (int i = 0; i < 12; i++) {

			TabHost.TabSpec spec = this.mTabHost.newTabSpec("Tab" + i);
			spec.setContent(new TabFactory(this));
			View tab = LayoutInflater.from(mTabHost.getContext()).inflate(R.layout.tab_layout, null);

			int resID = getResources().getIdentifier("a" + (i + 1) + "_0", "drawable", getPackageName());
			((ImageView) tab.findViewById(R.id.on)).setImageDrawable(getResources().getDrawable(resID));
			resID = getResources().getIdentifier("a" + (i + 1) + "_1", "drawable", getPackageName());
			((ImageView) tab.findViewById(R.id.off)).setImageDrawable(getResources().getDrawable(resID));

			spec.setIndicator(tab);
			mTabHost.addTab(spec);

			tabInfo = new TabInfo("Tab" + i, HoroscopeFragment.class, args);
			tabInfo.On = (ImageView) tab.findViewById(R.id.on);
			tabInfo.Off = (ImageView) tab.findViewById(R.id.off);
			tabInfo.Tab = tab;
			this.mapTabInfo.put(tabInfo.tag, tabInfo);
		}

		// Default to first tab
		this.onTabChanged("Tab0");

		mTabHost.setOnTabChangedListener(this);
	}

	public void onTabChanged(String tag) {
		TabInfo newTab = this.mapTabInfo.get(tag);
		for (Entry<String, TabInfo> t : mapTabInfo.entrySet()) {
			if (t.getKey().equals(tag)) {
				t.getValue().On.setVisibility(View.VISIBLE);
				t.getValue().Off.setVisibility(View.INVISIBLE);
			} else {
				t.getValue().On.setVisibility(View.INVISIBLE);
				t.getValue().Off.setVisibility(View.VISIBLE);
			}
		}

		scrollView.requestChildRectangleOnScreen(newTab.Tab, new Rect(0, 0, newTab.Tab.getWidth(), 1), false);

		int pos = this.mTabHost.getCurrentTab();
		this.mViewPager.setCurrentItem(pos, true);
	}

	@Override
	public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onPageSelected(int position) {
		// TODO Auto-generated method stub
		this.mTabHost.setCurrentTab(position);
	}

	@Override
	public void onPageScrollStateChanged(int state) {
		// TODO Auto-generated method stub

	}

	public class PagerAdapter extends FragmentPagerAdapter {

		private List<Fragment> fragments;

		public PagerAdapter(FragmentManager fm, List<Fragment> fragments) {
			super(fm);
			this.fragments = fragments;
		}

		@Override
		public Fragment getItem(int position) {
			return this.fragments.get(position);
		}

		@Override
		public int getCount() {
			return this.fragments.size();
		}
	}
}