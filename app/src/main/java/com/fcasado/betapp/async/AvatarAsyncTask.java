package com.fcasado.betapp.async;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by fcasado on 9/16/16.
 */
public class AvatarAsyncTask extends AsyncTask<String, Void, Bitmap> {
	private ImageView imageView;

	public AvatarAsyncTask(ImageView imageView) {
		this.imageView = imageView;
	}

	@Override
	protected Bitmap doInBackground(String... params) {
		URL fbAvatarUrl = null;
		Bitmap fbAvatarBitmap = null;

		try {
			fbAvatarUrl = new URL("https://graph.facebook.com/" + params[0] + "/picture");
			fbAvatarBitmap = BitmapFactory.decodeStream(fbAvatarUrl.openConnection().getInputStream());
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return fbAvatarBitmap;
	}

	@Override
	protected void onPostExecute(Bitmap bitmap) {
		super.onPostExecute(bitmap);

		if (imageView != null && bitmap != null) {
			imageView.setImageBitmap(bitmap);
		}
	}
}
