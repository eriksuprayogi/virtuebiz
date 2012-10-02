package com.virtuebiz.aplication;


import com.coverflow.widget.CoverFlow;
import com.virtuebiz.utils.LayoutUtil;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuffXfermode;
import android.graphics.Bitmap.Config;
import android.graphics.PorterDuff.Mode;
import android.graphics.Shader.TileMode;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewPager.LayoutParams;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ImageView.ScaleType;

public class TopPage extends Activity {
	CoverFlow coverFlow;
	TextView tv;
	TextView title1;
	TextView title2;
	int screenHeight, screenWidth;
	
	Integer[] mImageIds = { R.drawable.c1_01, R.drawable.c1_02,
			R.drawable.c1_04, R.drawable.c1_05, R.drawable.c1_06,
			R.drawable.c1_07, R.drawable.c1_08, R.drawable.c1_09 };
	
	String[] brand = { "Cover 1", "Cover 2 ", "Cover 3", "Cover 4", "Cover 5",
			"Cover 6", "Cover 7", "Cover 8" };

	String[] ket = { "keterangan 1", "keterangan 2 ", "keterangan Rienda",
			" keterangan jeans", "keterangan Tessa", "keterangan LG",
			" keterangan Samsung", "keterangan Sony" };
	Handler handler = new Handler();

	Changefling ch;
	ImageButton menu;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

//		screenHeight = LayoutUtil.getScreenHeight(getApplicationContext());
//		screenWidth = LayoutUtil.getScreenWidth(getApplicationContext());
		
		setContentView(R.layout.top_page);
		coverFlow = (CoverFlow) findViewById(R.id.coverFlow);
		coverFlow.setAdapter(new ImageAdapter(this));
		ImageAdapter coverImageAdapter = new ImageAdapter(this);
		coverImageAdapter.createReflectedImages();
		coverFlow.setAdapter(coverImageAdapter);
		coverFlow.setSpacing(-100);
		tv = (TextView) findViewById(R.id.textView1);
		title1 = (TextView) findViewById(R.id.title1);
		title2 = (TextView) findViewById(R.id.title2);
		menu = (ImageButton) findViewById(R.id.menu_btn);
		menu.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// finish();
				// startActivity(new Intent(TopPage.this,MenuPage.class));
			}
		});

		tv.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				// coverFlow.onFling(null, null, 480, 0);
				coverFlow.setAnimationDuration(1000);
				coverFlow.onKeyDown(KeyEvent.KEYCODE_DPAD_RIGHT, new KeyEvent(0, 0));

			}
		});

		coverFlow.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

					@Override
					public void onItemSelected(AdapterView<?> arg0, View arg1,
							int arg2, long arg3) {
						// TODO Auto-generated method stub
						title1.setText(brand[arg2]);
						title2.setText(ket[arg2]);
					}

					@Override
					public void onNothingSelected(AdapterView<?> arg0) {
						// TODO Auto-generated method stub

					}
				});
		
		ch = new Changefling();
		coverFlow.setOnTouchListener(new View.OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				handler.removeCallbacks(ch);
				return false;
			}
		});
		coverFlow.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> item, View v, int pos,
					long id) {
				// TODO Auto-generated method stub
				Log.i("TAG", pos + "");
				// Intent intent = new Intent(TopPage.this, CatalogPage.class);
				// intent.putExtra("id", pos);
				// startActivity(intent);
			}
		});

	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		handler.removeCallbacks(ch);
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onResume();
		for (int i = 1; i <= mImageIds.length; i++) {

			handler.postDelayed(ch, 2000 * i);

		}

	}

	class Changefling implements Runnable {

		@Override
		public void run() {
			// TODO Auto-generated method stub
			coverFlow.onFling(null, null, -800, 0);
		}

	}

	public class ImageAdapter extends BaseAdapter {
		int mGalleryItemBackground;
		private Context mContext;

		private ImageView[] mImages;

		public ImageAdapter(Context c) {
			mContext = c;
			mImages = new ImageView[mImageIds.length];
		}

		public boolean createReflectedImages() {
			// The gap we want between the reflection and the original image
			final int reflectionGap = 4;

			int index = 0;
			for (int imageId : mImageIds) {
				Bitmap originalImage = BitmapFactory.decodeResource(
						getResources(), imageId);
				int width = originalImage.getWidth();
				int height = originalImage.getHeight();

				// This will not scale but will flip on the Y axis
				Matrix matrix = new Matrix();
				matrix.preScale(1, -1);
				
				// Create a Bitmap with the flip matrix applied to it.
				// We only want the bottom half of the image
				Bitmap reflectionImage = Bitmap.createBitmap(originalImage, 0,
						height / 2, width, height / 2, matrix, false);

				// Create a new bitmap with same width but taller to fit
				// reflection
				Bitmap bitmapWithReflection = Bitmap.createBitmap(width, (height + height / 2), Config.ARGB_8888);

				// Create a new Canvas with the bitmap that's big enough for
				// the image plus gap plus reflection
				Canvas canvas = new Canvas(bitmapWithReflection);
				// Draw in the original image
				canvas.drawBitmap(originalImage, 0, 0, null);
				// Draw in the gap
				Paint deafaultPaint = new Paint();
				canvas.drawRect(0, height, width, height + reflectionGap,
						deafaultPaint);
				// Draw in the reflection
				canvas.drawBitmap(reflectionImage, 0, height + reflectionGap,
						null);

				// Create a shader that is a linear gradient that covers the
				// reflection
				Paint paint = new Paint();
				LinearGradient shader = new LinearGradient(0,
						originalImage.getHeight(), 0,
						bitmapWithReflection.getHeight() + reflectionGap,
						0x70ffffff, 0x00ffffff, TileMode.CLAMP);
				// Set the paint to use this shader (linear gradient)
				paint.setShader(shader);
				// Set the Transfer mode to be porter duff and destination in
				paint.setXfermode(new PorterDuffXfermode(Mode.DST_IN));
				// Draw a rectangle using the paint with our linear gradient
				canvas.drawRect(0, height, width, bitmapWithReflection.getHeight() + reflectionGap, paint);

				ImageView imageView = new ImageView(mContext);
				imageView.setImageBitmap(bitmapWithReflection);
				Log.d("width", width + " height " + height);
				imageView.setLayoutParams(new CoverFlow.LayoutParams(width, height + (height / 2)));
				imageView.setScaleType(ScaleType.FIT_XY);
				mImages[index++] = imageView;

			}
			return true;
		}

		public int getCount() {
			return mImageIds.length;
		}

		public Object getItem(int position) {
			return position;
		}

		public long getItemId(int position) {
			return position;
		}

		public View getView(int position, View convertView, ViewGroup parent) {

			return mImages[position];
		}

		/**
		 * Returns the size (0.0f to 1.0f) of the views depending on the
		 * 'offset' to the center.
		 */
		public float getScale(boolean focused, int offset) {
			/* Formula: 1 / (2 ^ offset) */
			return Math.max(0, 1.0f / (float) Math.pow(2, Math.abs(offset)));
		}

	}

}
