package com.fcasado.betapp.custom;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.fcasado.betapp.R;

/**
 * Created by fcasado on 9/20/16.
 */

public class LineDividerItemDecoration extends RecyclerView.ItemDecoration {
	private Drawable mDivider;
	private int extraHorizontalPadding;

	public LineDividerItemDecoration(Context context, int extraHorizontalPadding) {
		mDivider = ContextCompat.getDrawable(context, R.drawable.line_divider);
		this.extraHorizontalPadding = extraHorizontalPadding;
	}

	@Override
	public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
		int left = parent.getPaddingLeft() + extraHorizontalPadding;
		int right = parent.getWidth() - parent.getPaddingRight() - extraHorizontalPadding;

		int childCount = parent.getChildCount();
		for (int i = 0; i < childCount; i++) {
			View child = parent.getChildAt(i);

			RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();

			int top = child.getBottom() + params.bottomMargin;
			int bottom = top + mDivider.getIntrinsicHeight();

			mDivider.setBounds(left, top, right, bottom);
			mDivider.draw(c);
		}
	}
}
