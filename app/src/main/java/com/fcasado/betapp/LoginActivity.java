package com.fcasado.betapp;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.ContentProvider;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.Profile;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.facebook.share.model.AppInviteContent;
import com.facebook.share.widget.AppInviteDialog;
import com.fcasado.betapp.bets.BetsListActivity;
import com.fcasado.betapp.create.CreateBetActivity;
import com.fcasado.betapp.favorites.FavoriteBetContract;
import com.fcasado.betapp.utils.Constants;
import com.fcasado.betapp.data.User;
import com.fcasado.betapp.friends.FriendsActivity;
import com.fcasado.betapp.utils.FirebaseUtils;
import com.fcasado.betapp.utils.LogUtils;
import com.fcasado.betapp.widget.FavoriteBetsWidgetProvider;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
	private static final String TAG = "LoginActivity";

	private FirebaseAuth mAuth;
	private FirebaseAuth.AuthStateListener mAuthListener;
	private CallbackManager mCallbackManager;
	private AccessTokenTracker mTracker;

	@BindView(R.id.button_invite_friends)
	Button inviteFriendsButton;
	@BindView(R.id.test_create_bet)
	Button createBetButton;
	@BindView(R.id.test_list_bets)
	Button betListButton;
	@BindView(R.id.test_list_friends)
	Button friendsListButton;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		ButterKnife.bind(this);

		FirebaseUtils.logEvent(this, FirebaseUtils.LOGIN_ACTIVITY, null);

		// UI
		inviteFriendsButton.setOnClickListener(this);
		createBetButton.setOnClickListener(this);
		betListButton.setOnClickListener(this);
		friendsListButton.setOnClickListener(this);

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
							Toast.makeText(LoginActivity.this, R.string.auth_failed,
									Toast.LENGTH_SHORT).show();
						} else {
							userLoggedIn();
						}
					}
				});
	}

	private void updateUI(FirebaseUser user) {
		if (user == null) {
			// User logged out
			getContentResolver().delete(FavoriteBetContract.BetEntry.CONTENT_URI, null, null);

			// Update app widget data
			int ids[] = AppWidgetManager.getInstance(this).getAppWidgetIds(new ComponentName(getApplication(), FavoriteBetsWidgetProvider.class));
			AppWidgetManager.getInstance(this).notifyAppWidgetViewDataChanged(ids, R.id.listView_favorite_bets);
		}
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
			case R.id.test_list_friends:
				startActivity(new Intent(LoginActivity.this, FriendsActivity.class));
				break;

		}
	}

	private void userLoggedIn() {
		FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
		DatabaseReference database = FirebaseDatabase.getInstance().getReference().child(Constants.CHILD_USERS).child(currentUser.getUid());
		database.addListenerForSingleValueEvent(new ValueEventListener() {
			@Override
			public void onDataChange(DataSnapshot dataSnapshot) {
				// Register user if it doesn't exist
				if (dataSnapshot == null || !dataSnapshot.exists()) {
					LogUtils.d(TAG, "Registering user");
					saveUserDataToDatabase();
				}
			}

			@Override
			public void onCancelled(DatabaseError databaseError) {
				// Do nothing.
			}
		});
	}

	private void saveUserDataToDatabase() {
		FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
		Profile fbProfile = Profile.getCurrentProfile();
		if (currentUser == null || fbProfile == null) {
			return;
		}

		User user = new User(currentUser.getUid(), fbProfile.getId(), fbProfile.getName());

		Map<String, Object> userValues = user.toMap();
		Map<String, Object> childUpdates = new HashMap<>();
		childUpdates.put(Constants.SEPARATOR + user.getUid(), userValues);

		DatabaseReference database = FirebaseDatabase.getInstance().getReference().child(Constants.CHILD_USERS);
		database.addListenerForSingleValueEvent(new ValueEventListener() {
			@Override
			public void onDataChange(DataSnapshot dataSnapshot) {
				LogUtils.d(TAG, "Created user profile in db");
			}

			@Override
			public void onCancelled(DatabaseError databaseError) {

			}
		});
		database.updateChildren(childUpdates);

		DatabaseReference fbDatabase = FirebaseDatabase.getInstance().getReference().child(Constants.CHILD_FACEBOOK).child(user.getFacebookId());
		fbDatabase.child(Constants.CHILD_USER_ID).setValue(user.getUid());
		fbDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
			@Override
			public void onDataChange(DataSnapshot dataSnapshot) {
				LogUtils.d(TAG, "Data was added: " + dataSnapshot.toString());
			}

			@Override
			public void onCancelled(DatabaseError databaseError) {
				LogUtils.d(TAG, "Data was canceled");
			}
		});
	}
}
