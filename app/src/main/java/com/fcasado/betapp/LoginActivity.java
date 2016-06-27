package com.fcasado.betapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.facebook.share.model.AppInviteContent;
import com.facebook.share.widget.AppInviteDialog;
import com.fcasado.betapp.bets.BetsListActivity;
import com.fcasado.betapp.create.CreateBetActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.json.JSONArray;
import org.json.JSONException;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

	private static final String TAG = "FacebookLogin";
	private FirebaseAuth mAuth;
	private FirebaseAuth.AuthStateListener mAuthListener;
	private CallbackManager mCallbackManager;
	private AccessTokenTracker mTracker;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);

		// UI
		findViewById(R.id.button_invite_friends).setOnClickListener(this);
		findViewById(R.id.test_create_bet).setOnClickListener(this);
		findViewById(R.id.test_list_bets).setOnClickListener(this);

		mTracker = new AccessTokenTracker() {
			@Override
			protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken currentAccessToken) {
				if (currentAccessToken == null) {
					mAuth.signOut();
				}
			}
		};
		mTracker.startTracking();

		mAuth = FirebaseAuth.getInstance();
		mAuthListener = new FirebaseAuth.AuthStateListener() {
			@Override
			public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
				FirebaseUser user = firebaseAuth.getCurrentUser();
				if (user != null) {
					// User is signed in
					LogUtils.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
				} else {
					// User is signed out
					LogUtils.d(TAG, "onAuthStateChanged:signed_out");
				}
				updateUI(user);
			}
		};

		// Initialize Facebook Login button
		mCallbackManager = CallbackManager.Factory.create();
		LoginButton loginButton = (LoginButton) findViewById(R.id.button_facebook_login);
		loginButton.setReadPermissions("email", "public_profile", "user_friends");
		loginButton.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
			@Override
			public void onSuccess(LoginResult loginResult) {
				LogUtils.d(TAG, "facebook:onSuccess:" + loginResult);
				handleFacebookAccessToken(loginResult.getAccessToken());
			}

			@Override
			public void onCancel() {
				LogUtils.d(TAG, "facebook:onCancel");
				updateUI(null);
			}

			@Override
			public void onError(FacebookException error) {
				LogUtils.d(TAG, "facebook:onError", error);
				updateUI(null);
			}
		});


	}

	@Override
	public void onStart() {
		super.onStart();
		mAuth.addAuthStateListener(mAuthListener);
	}

	@Override
	public void onStop() {
		super.onStop();
		if (mAuthListener != null) {
			mAuth.removeAuthStateListener(mAuthListener);
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();

		if (mTracker != null) {
			mTracker.stopTracking();
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		mCallbackManager.onActivityResult(requestCode, resultCode, data);
	}

	private void handleFacebookAccessToken(AccessToken token) {
		AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
		mAuth.signInWithCredential(credential)
				.addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
					@Override
					public void onComplete(@NonNull Task<AuthResult> task) {
						LogUtils.d(TAG, "signInWithCredential:onComplete:" + task.isSuccessful());

						// If sign in fails, display a message to the user. If sign in succeeds
						// the auth state listener will be notified and logic to handle the
						// signed in user can be handled in the listener.
						if (!task.isSuccessful()) {
							LogUtils.w(TAG, "signInWithCredential", task.getException());
							Toast.makeText(LoginActivity.this, "Authentication failed.",
									Toast.LENGTH_SHORT).show();
						} else {
							findExistingFriends();
						}
					}
				});
	}

	private void updateUI(FirebaseUser user) {
		findViewById(R.id.button_invite_friends).setVisibility(user != null ? View.VISIBLE : View.GONE);
	}

	private void handleInviteFriends() {
		String appLinkUrl, previewImageUrl;

		appLinkUrl = "https://fb.me/196364884091856";

		if (AppInviteDialog.canShow()) {
			AppInviteContent content = new AppInviteContent.Builder()
					.setApplinkUrl(appLinkUrl)
					.build();
			AppInviteDialog.show(this, content);
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.button_invite_friends:
				handleInviteFriends();
				break;
			case R.id.test_create_bet:
				startActivity(new Intent(LoginActivity.this, CreateBetActivity.class));
				break;
			case R.id.test_list_bets:
				startActivity(new Intent(LoginActivity.this, BetsListActivity.class));
				break;

		}
	}

	private void findExistingFriends() {
		new GraphRequest(
				AccessToken.getCurrentAccessToken(),
				"/me/friends",
				null,
				HttpMethod.GET,
				new GraphRequest.Callback() {
					public void onCompleted(GraphResponse response) {
						LogUtils.d(TAG, response.toString());
											/* handle the result */
						try {
							JSONArray data = response.getJSONObject().getJSONArray("data");
							for (int i = 0; i < data.length(); i++) {
								LogUtils.d(TAG, "Data[" + i + "]: " + data.get(i).toString());
							}
						} catch (JSONException e) {
							e.printStackTrace();
						}

					}
				}
		).executeAsync();
	}
}
