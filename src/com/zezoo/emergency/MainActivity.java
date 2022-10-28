package com.zezoo.emergency;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.Time;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.GeofenceStatusCodes;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStates;
import com.google.android.gms.location.LocationStatusCodes;
import com.google.android.gms.location.SettingsApi;
import com.zezoo.emergency.SingleShotLocationProvider.GPSCoordinates;

import android.Manifest;
import android.accounts.Account;
import android.accounts.AccountManager;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.BatteryManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.provider.ContactsContract;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import javax.mail.MessagingException;

public class MainActivity extends Activity implements LocationListener {

	public static final String ARCHIVES_NAME = "an";
	public static final int RequestPermissionCode = 1;

	TextView e, t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17, t18;
	ImageButton Click;
	ToggleButton toggleRecorder, toggleTrucker;
	ProgressBar mprogressBar;

	int getGpsState = 1;
	int abc = 0;
	int levelBattary;
	int countNumber, action;
	int SL_UP = 1;
	long startTime = 0L;
	long timeInMilliseconds = 0L;
	long timeSwapBuff = 0L;
	long updatedTime = 0L;
	double Longitude;
	double Latitude;
	boolean pref_send_sms, pref_send_email, pref_call, pref_record_voice, pref_gps_trucker, pref_gps_trucker_send_email,
			pref_gps_trucker_send_sms, pref_record_voice_send_email, pref_truck_every_second;
	boolean isPause = false;
	boolean messageState = true;
	boolean abcd = true;
	boolean abcde = true;
	boolean abcdef = true;
	boolean re = true;
	boolean GpsStatus = false;
	boolean InternetStatus = false;
	boolean helpMeans[] = { true, true, true };
	String pref_help_number;
	String pref_current_email;
	String pref_current_backup_email;
	String pref_help_email;
	String Holder;
	String locationName = "";
	String audioFileName;
	String AudioSavePathInDevice = null;
	String listTime[];
	String listLongitude[];
	String listLatitude[];
	String listLocations[];
	String listCurrentCoordinates[] = { "", "", "", "" };
	String ab = "";

	ObjectAnimator anim;
	NetworkInfo[] info;
	Location location;
	LocationManager locationManager;
	Criteria criteria;
	List<String> providerList;
	GMailSender sender;
	SharedPreferences prefs, archives;
	MediaRecorder mediaRecorder;
	MediaPlayer mediaPlayer;
	Handler customHandler = new Handler();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		setPreferences();
		EnableRuntimePermission();
		e = (TextView) findViewById(R.id.e);
		t1 = (TextView) findViewById(R.id.t1);
		t2 = (TextView) findViewById(R.id.t2);
		t3 = (TextView) findViewById(R.id.t3);
		t4 = (TextView) findViewById(R.id.t4);
		t5 = (TextView) findViewById(R.id.t5);
		t6 = (TextView) findViewById(R.id.t6);
		t7 = (TextView) findViewById(R.id.t7);
		t8 = (TextView) findViewById(R.id.t8);
		t9 = (TextView) findViewById(R.id.t9);
		t10 = (TextView) findViewById(R.id.t10);
		t11 = (TextView) findViewById(R.id.t11);
		t12 = (TextView) findViewById(R.id.t12);
		t13 = (TextView) findViewById(R.id.t13);
		t14 = (TextView) findViewById(R.id.t14);
		t15 = (TextView) findViewById(R.id.t15);
		t16 = (TextView) findViewById(R.id.t16);
		t17 = (TextView) findViewById(R.id.t17);
		t18 = (TextView) findViewById(R.id.t18);
		toggleRecorder = (ToggleButton) findViewById(R.id.toggleRecorder);
		toggleTrucker = (ToggleButton) findViewById(R.id.toggleTrucker);
		Click = (ImageButton) findViewById(R.id.Click);
		mprogressBar = (ProgressBar) findViewById(R.id.circular_progress_bar);

		anim = ObjectAnimator.ofInt(mprogressBar, "progress", 0, 100);
		anim.setDuration(11000);
		anim.setInterpolator(new DecelerateInterpolator());

		WifiManager wifiManager = (WifiManager)this.getSystemService(Context.WIFI_SERVICE);
		wifiManager.setWifiEnabled(true);
		
		locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		criteria = new Criteria();
		Holder = locationManager.getBestProvider(criteria, false);
		providerList = locationManager.getAllProviders();

		toggleRecorder.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				recordVoice(isChecked);
				if (!isChecked) {
					toggleRecorder.setChecked(false);
					new AlertDialog.Builder(MainActivity.this).setTitle("≈—”«· «· ”ÃÌ· «·’Ê Ì ⁄»— «·»—Ìœ «·≈·ﬂ —Ê‰Ì")
							.setMessage("ﬁœ Ì — » ’—› —’Ìœ ≈–« ﬂ«‰  «·»Ì«‰«  „›⁄·…").setCancelable(false)
							.setPositiveButton("≈—”«·", new OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog, int which) {
									try {
										email(3);
										if (checkInternet()) {
											Toast.makeText(getApplicationContext(), " „  ”·Ì„ «·—”«·…", 500).show();
										} else {
											Toast.makeText(getApplicationContext(),
													"·„ Ì „  ”·Ì„ «·—”«·…...·« ÌÊÃœ ≈ ’«· »«·≈‰ —‰ ", 600).show();
										}
									} catch (Exception e) {
										Toast.makeText(getApplicationContext(), "ÕœÀ Œÿ√ ·„ Ì „  ”·Ì„ «·—”«·…", 600)
												.show();
									}

								}
							}).setNegativeButton("≈·€«¡", new OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog, int which) {
									// Whatever...
								}
							}).show();
				}
			}
		});
		toggleTrucker.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if (isChecked) {
					getGpsState = 2;
					gps();
				} else {
					restartActivity();
				}
			}
		});
		Click.setOnTouchListener(new View.OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {

				action = event.getAction();
				if (action == MotionEvent.ACTION_DOWN) {
					startTime = SystemClock.uptimeMillis();
					customHandler.postDelayed(updateTimerThread, 0);
					startProgress();
				} else if (action == MotionEvent.ACTION_UP) {
					if (countNumber >= 3) {
						timeSwapBuff = timeInMilliseconds;
						customHandler.removeCallbacks(updateTimerThread);
						slide(SL_UP);
						Click.setClickable(false);
						Click.setActivated(false);
						isPause = true;
						if (pref_record_voice) {
							if (!toggleRecorder.isChecked()) {
								toggleRecorder.setChecked(true);
								recordVoice(true);
							}
						}
						getGpsState = 1;
						gps();
					}
					anim.start();
					anim.cancel();
				}
				return false;
			}
		});

	}

	void startProgress() {
		if (!isPause) {
			anim.start();
		}
	}

	void gps() {
		EnableRuntimePermission();
		if (getGpsState == 1)
			t1.setText("Checking GPS...");
		if (checkGps()) {
			if (getGpsState == 1) {
				t2.setText("GPS Is Enabled");
				t3.setText("Getting Current Location, Please Wait...");
			}
			runGettingLocationMethod();

		} else {
			if (getGpsState == 1)
				t2.setText("GPS Is Disabled...Turning GPS...");
			enableGps();
		}

	}

	void data() {
		final Handler handler3 = new Handler();
		handler3.postDelayed(new Runnable() {
			@Override
			public void run() {
				viewTexts("Checking Internet Connection...");
				if (checkInternet()) {
					final Handler handler1 = new Handler();
					handler1.postDelayed(new Runnable() {
						@Override
						public void run() {
							viewTexts("Connected...");
							email(1);
						}
					}, 1500);
				} else {
					final Handler handler3 = new Handler();
					handler3.postDelayed(new Runnable() {
						@Override
						public void run() {
							viewTexts("Not Connected...Turning On Mobile Data...");
							enableMobileData();
							final Handler handler1 = new Handler();
							handler1.postDelayed(new Runnable() {
								@Override
								public void run() {
									if (checkInternet()) {
										Toast.makeText(getApplicationContext(), " „  ‘€Ì· «·»Ì«‰« ", 500).show();
										email(1);
									} else {
										helpMeans[1] = false;
										if (pref_call)
											call();
										Toast.makeText(getApplicationContext(),
												"€Ì— „ ’· »«·≈‰ —‰ , ·« Ì„ﬂ‰ ≈—”«· —”«·… «·»—Ìœ «·≈·ﬂ —Ê‰Ì", 1500)
												.show();
										Toast.makeText(getApplicationContext(),
												"ÌÃ»  ‘€Ì· »Ì«‰«  «·Â« › ÌœÊÌ« ﬁ»· «·»œ¡", 1000).show();

									}
								}
							}, 2500);
						}
					}, 1200);
				}

			}
		}, 2300);

	}

	void email(int isThereRecord) {
		if (isThereRecord == 1) {
			viewTexts("Sending Email Message...");
		} else if (isThereRecord == 2) {
			viewTexts("Sending Email Message...");
		} else if (isThereRecord == 3) {

		}
		if (isThereRecord == 2 || isThereRecord == 3) {
			try {
				new Thread(new Runnable() {
					public void run() {
						String message = "X : " + String.valueOf(Longitude) + " --- Y : " + String.valueOf(Latitude)
								+ " --- Area : " + locationName;
						try {
							GMailSender sender = new GMailSender(pref_current_email, "my.lover.joudy");
							sender.sendMail("Emergency Call", message, pref_current_email, "zezoocvi.77@gmail.com",
									AudioSavePathInDevice);
							messageState = true;
						} catch (Exception e) {
							try {
								GMailSender sender = new GMailSender(pref_current_backup_email, "my.lover.joudy");
								sender.sendMail("Emergency Call", message, pref_current_backup_email,
										"zezoocvi.77@gmail.com", AudioSavePathInDevice);
								messageState = true;
							} catch (Exception e1) {
								messageState = false;
							}
						}
					}
				}).start();
				if (isThereRecord == 2) {
					final Handler handler3 = new Handler();
					handler3.postDelayed(new Runnable() {
						@Override
						public void run() {
							if (messageState) {
								viewTexts("Message Delivered");
							} else {
								helpMeans[1] = false;
								viewTexts("Message Not Delivered");
							}
							if (pref_call) {
								call();
							} else {
								helpMeans[2] = false;
								viewTexts("Asking For Help Is Finished");
								showResult();
							}
						}
					}, 2500);
				}
			} catch (Exception e) {
				if (isThereRecord == 2) {
					final Handler handler4 = new Handler();
					handler4.postDelayed(new Runnable() {

						@Override
						public void run() {
							helpMeans[1] = false;
							viewTexts("Message Not Delivered");
							if (pref_call) {
								call();
							} else {
								helpMeans[2] = false;
								viewTexts("Asking For Help Is Finished");
								showResult();
							}
						}

					}, 2500);
				}
				Toast.makeText(getApplicationContext(), "·« Ì„ﬂ‰ ≈—”«· —”«·… »—Ìœ ≈·ﬂ —Ê‰Ì", 700).show();
			}
		} else if (isThereRecord == 1) {
			try {
				new Thread(new Runnable() {
					public void run() {
						String message = "X : " + String.valueOf(Longitude) + " --- Y : " + String.valueOf(Latitude)
								+ " --- Area : " + locationName;
						try {
							GMailSender sender = new GMailSender(pref_current_email, "my.lover.joudy");
							sender.sendMail("Emergency Call", message, pref_current_email, "zezoocvi.77@gmail.com");
							messageState = true;
						} catch (Exception e) {
							try {
								GMailSender sender = new GMailSender(pref_current_backup_email, "my.lover.joudy");
								sender.sendMail("Emergency Call", message, pref_current_backup_email,
										"zezoocvi.77@gmail.com");
								messageState = true;
							} catch (Exception e1) {
								messageState = false;
							}
						}
					}
				}).start();
				final Handler handler3 = new Handler();
				handler3.postDelayed(new Runnable() {
					@Override
					public void run() {
						if (messageState) {
							viewTexts("Message Delivered");
						} else {
							helpMeans[1] = false;
							viewTexts("Message Not Delivered");
						}
						if (pref_call) {
							call();
						} else {
							helpMeans[2] = false;
							viewTexts("Asking For Help Is Finished");
							showResult();
						}
					}
				}, 2500);
			} catch (

			Exception e) {
				final Handler handler4 = new Handler();
				handler4.postDelayed(new Runnable() {

					@Override
					public void run() {
						helpMeans[1] = false;
						viewTexts("Message Not Delivered");
						if (pref_call) {
							call();
						} else {
							helpMeans[2] = false;
							viewTexts("Asking For Help Is Finished");
							showResult();
						}
					}

				}, 2500);
				Toast.makeText(getApplicationContext(), "·« Ì„ﬂ‰ ≈—”«· —”«·… »—Ìœ ≈·ﬂ —Ê‰Ì", 700).show();
			}
		}
	}

	void slide(int type) {

		// Load animation
		Animation slide_up = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_up);
		Animation slide_down = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_down);
		if (type == SL_UP) {
			Click.startAnimation(slide_up);
			mprogressBar.startAnimation(slide_up);
			e.startAnimation(slide_up);
		} else {
			Click.startAnimation(slide_down);
			mprogressBar.startAnimation(slide_down);
		}

	}

	void runGettingLocationMethod() {
		getLocation(getApplicationContext(), false);
		final Handler handler3 = new Handler();
		handler3.postDelayed(new Runnable() {
			@Override
			public void run() {
				if (t4.getText().toString() == "" && Double.valueOf(Longitude) == 0 || locationName == "") {
					getLocationNow(getApplicationContext());
					final Handler handler2 = new Handler();
					handler2.postDelayed(new Runnable() {
						@Override
						public void run() {
							if (t4.getText().toString() == "" && Double.valueOf(Longitude) == 0
									|| locationName == "") {
								getLocation(getApplicationContext(), true);
								final Handler handler1 = new Handler();
								handler1.postDelayed(new Runnable() {
									@Override
									public void run() {
										if (t4.getText().toString() == "" && Double.valueOf(Longitude) == 0
												|| locationName == "") {
											getLocationNow(getApplicationContext());
											final Handler handler = new Handler();
											handler.postDelayed(new Runnable() {
												@Override
												public void run() {
													if (t4.getText().toString() == "" && Double.valueOf(Longitude) == 0
															|| locationName == "") {
														Toast.makeText(getApplicationContext(),
																"Method Of Getting GPS N3, By Mobile Data", 500).show();
														enableMobileData();
														final Handler h = new Handler();
														h.postDelayed(new Runnable() {
															@Override
															public void run() {
																if (t4.getText().toString() == ""
																		&& Double.valueOf(Longitude) == 0
																		|| locationName == "") {
																	if (checkInternet()) {
																		getLocation(getApplicationContext(), false);
																		final Handler h1 = new Handler();
																		h1.postDelayed(new Runnable() {
																			@Override
																			public void run() {
																				try {
																					if (t4.getText().toString() != ""
																							&& Double.valueOf(
																									Longitude) != 0
																							|| locationName != "") {
																						disableMobileData();
																					} else {
																						disableMobileData();
																						Toast.makeText(
																								getApplicationContext(),
																								"ÕœÀ Œÿ√ ·«Ì„ﬂ‰ «·Õ’Ê· ⁄·Ï «·„Êﬁ⁄",
																								700).show();
																						if (getGpsState == 1) {
																							helpMeans[0] = false;
																							helpMeans[1] = false;
																							if (pref_call) {
																								call();
																							} else {
																								restartActivity();
																							}
																						} else {
																							getGpsState = 1;
																							toggleTrucker
																									.setChecked(false);
																						}
																					}
																				} catch (Exception e) {
																					Toast.makeText(
																							getApplicationContext(),
																							"ÕœÀ Œÿ√ ·«Ì„ﬂ‰ «·Õ’Ê· ⁄·Ï «·„Êﬁ⁄",
																							700).show();
																					if (getGpsState == 1) {
																						helpMeans[0] = false;
																						helpMeans[1] = false;
																						if (pref_call) {
																							call();
																						} else {
																							restartActivity();
																						}
																					} else {
																						getGpsState = 1;
																						toggleTrucker.setChecked(false);
																					}
																				}
																			}
																		}, 8000);
																	} else {
																		Toast.makeText(getApplicationContext(),
																				"ÌÃ»  ‘€Ì· »Ì«‰«  «·Â« › ÌœÊÌ« ﬁ»· «·»œ¡",
																				1000).show();
																		if (getGpsState == 1) {
																			helpMeans[0] = false;
																			helpMeans[1] = false;
																			if (pref_call) {
																				call();
																			} else {
																				restartActivity();
																			}
																		} else {
																			getGpsState = 1;
																			toggleTrucker.setChecked(false);
																		}
																	}
																}
															}
														}, 3000);
													}
												}
											}, 7000);
										}
									}
								}, 6000);
							}
						}
					}, 9000);
				}
			}
		}, 11000);
		

	}

	boolean checkGps() {
		try {
			EnableRuntimePermission();
		} catch (Exception e) {
			Toast.makeText(getApplicationContext(), "ÌÃ» ≈⁄«œ…  ‘€Ì· «·»—‰«„Ã ⁄‰œ ÕœÊÀ Œÿ√", 500).show();
		}
		try {
			locationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
			GpsStatus = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
		} catch (Exception e) {
			GpsStatus = false;
		}
		return GpsStatus;
	}

	void enableGps() {
		final Handler handler1 = new Handler();
		handler1.postDelayed(new Runnable() {
			@Override
			public void run() {
				try {
					Toast.makeText(getApplicationContext(), "ﬁ„ » ‘€Ì· GPS À„ «÷€ÿ “— «·—ÃÊ⁄", 1000).show();
					startActivityForResult(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS), 1);
				} catch (Exception e) {
					Toast.makeText(getApplicationContext(), "Ì—ÃÏ  ›⁄Ì· Œœ„«  «·„Êﬁ⁄ ÌœÊÌ« ﬁ»· «·»œ¡", 600).show();
					restartApp();
				}
			}
		}, 2000);
	}

	void getLocationNow(Context context) {
		if (checkGps()) {
			Toast.makeText(getApplicationContext(), "Method Of Getting GPS N2", 500).show();
			SingleShotLocationProvider.requestSingleUpdate(context, new SingleShotLocationProvider.LocationCallback() {
				@Override
				public void onNewLocationAvailable(GPSCoordinates locatio) {
					abc = abc + 1;
					if (getGpsState == 2 || pref_gps_trucker) {
						showIfGetLocationIsDone();
					}
					Calendar calendar = Calendar.getInstance(Locale.getDefault());
					int hour = calendar.get(Calendar.HOUR_OF_DAY);
					int minute = calendar.get(Calendar.MINUTE);
					int second = calendar.get(Calendar.SECOND);
					int day = calendar.get(Calendar.DAY_OF_MONTH);
					int month = calendar.get(Calendar.MONTH);
					int year = calendar.get(Calendar.YEAR);
					String time = String.valueOf(hour) + ":" + String.valueOf(minute) + ":" + String.valueOf(second);
					Longitude = locatio.longitude;
					Latitude = locatio.latitude;
					Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
					try {
						List<Address> listAddresses = geocoder.getFromLocation(Latitude, Longitude, 1);
						if (null != listAddresses && listAddresses.size() > 0) {
							locationName = listAddresses.get(0).getAddressLine(0);
							if (getGpsState == 1) {
								t4.setText("Longitude : " + String.valueOf(Longitude));
								final Handler handler1 = new Handler();
								handler1.postDelayed(new Runnable() {
									@Override
									public void run() {
										t5.setText("Latitude  : " + String.valueOf(Latitude));
										final Handler handler2 = new Handler();
										handler2.postDelayed(new Runnable() {
											@Override
											public void run() {
												t6.setText("Area : " + locationName);
											}
										}, 1000);
									}
								}, 1000);
							}
							if (t4.getText().toString() != "") {
								if (getGpsState == 1)
									sendSms();
								if (pref_gps_trucker) {
									if (!toggleTrucker.isChecked()) {
										toggleTrucker.setChecked(true);
									}
									truckLocations(locatio.longitude, locatio.latitude,
											listAddresses.get(0).getAddressLine(0), time);
								}
							}
							if (getGpsState == 2) {
								if (Double.valueOf(Longitude) != 0) {
									truckLocations(locatio.longitude, locatio.latitude,
											listAddresses.get(0).getAddressLine(0), time);
								}
							}
						}
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			});
		}
	}

	void getLocation(Context context, boolean tryGpsOnly) {
		Holder = locationManager.getBestProvider(criteria, true);
		if (checkGps()) {
			if (Holder != null) {
				Toast.makeText(getApplicationContext(), "Method Of Getting GPS N1", 500).show();
				if (ActivityCompat.checkSelfPermission(context,
						Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
						&& ActivityCompat.checkSelfPermission(context,
								Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
					return;
				}
				location = locationManager.getLastKnownLocation(Holder);
				if (tryGpsOnly) {
					locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, MainActivity.this);
				} else {
					locationManager.requestLocationUpdates(Holder, 0, 0, MainActivity.this);
				}

			}
		} else {
			Toast.makeText(getApplicationContext(), "Ì—ÃÏ  ›⁄Ì· Œœ„«  «·„Êﬁ⁄ ÌœÊÌ« ﬁ»· «·»œ¡", 600).show();
			restartApp();
		}
	}

	void getLastLocation() {
		Holder = locationManager.getBestProvider(criteria, true);
		Calendar calendar = Calendar.getInstance(Locale.getDefault());
		int hour = calendar.get(Calendar.HOUR_OF_DAY);
		int minute = calendar.get(Calendar.MINUTE);
		int second = calendar.get(Calendar.SECOND);
		int day = calendar.get(Calendar.DAY_OF_MONTH);
		int month = calendar.get(Calendar.MONTH);
		int year = calendar.get(Calendar.YEAR);
		String time = String.valueOf(hour) + ":" + String.valueOf(minute) + ":" + String.valueOf(second);
		location = locationManager.getLastKnownLocation(Holder);
		Longitude = location.getLongitude();
		Latitude = location.getLatitude();
		Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
		try {
			List<Address> listAddresses = geocoder.getFromLocation(Latitude, Longitude, 1);
			if (null != listAddresses && listAddresses.size() > 0) {
				locationName = listAddresses.get(0).getAddressLine(0);
				if (getGpsState == 1) {
					t4.setText("Longitude : " + String.valueOf(Longitude));
					final Handler handler1 = new Handler();
					handler1.postDelayed(new Runnable() {
						@Override
						public void run() {
							t5.setText("Latitude  : " + String.valueOf(Latitude));
							final Handler handler2 = new Handler();
							handler2.postDelayed(new Runnable() {
								@Override
								public void run() {
									t6.setText("Area : " + locationName);
								}
							}, 1000);
						}
					}, 1000);
				}
				if (t4.getText().toString() != "") {
					if (getGpsState == 1)
						sendSms();
					if (pref_gps_trucker) {
						if (!toggleTrucker.isChecked()) {
							toggleTrucker.setChecked(true);
						}
						truckLocations(location.getLongitude(), location.getLatitude(),
								listAddresses.get(0).getAddressLine(0), time);
					}
				}
				if (getGpsState == 2) {
					if (Double.valueOf(Longitude) != 0) {
						truckLocations(location.getLongitude(), location.getLatitude(),
								listAddresses.get(0).getAddressLine(0), time);
					}
				}
			}
			Toast.makeText(getApplicationContext(), String.valueOf(Longitude), 500).show();
			Toast.makeText(getApplicationContext(), String.valueOf(Latitude), 500).show();
			Toast.makeText(getApplicationContext(), listAddresses.get(0).getAddressLine(0), 500).show();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	void sendSms() {
		if (abcd) {
			if (t4.getText().toString() != "") {
				abcd = false;
				if (pref_send_sms) {
					final Handler handler3 = new Handler();
					handler3.postDelayed(new Runnable() {
						@Override
						public void run() {
							viewTexts("Sending SMS Message...");
							final Handler handler2 = new Handler();
							handler2.postDelayed(new Runnable() {
								@Override
								public void run() {
									String phoneNumber = "+963" + pref_help_number;
									String message = "X : " + String.valueOf(Longitude) + " --- Y : "
											+ String.valueOf(Latitude) + " --- Area : " + locationName;
									String SENT = "SMS_SENT";
									String DELIVERED = "SMS_DELIVERED";
									PendingIntent sentPI = PendingIntent.getBroadcast(getApplicationContext(), 0,
											new Intent(SENT), 0);
									PendingIntent deliveredPI = PendingIntent.getBroadcast(getApplicationContext(), 0,
											new Intent(DELIVERED), 0);
									registerReceiver(new BroadcastReceiver() {
										@Override
										public void onReceive(Context arg0, Intent arg1) {
											if (getResultCode() == Activity.RESULT_OK) {
												viewTexts("SMS Sent");
											} else {
												switch (getResultCode()) {
												case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
													viewTexts("SMS Not Sent, Generic Failure");
													break;
												case SmsManager.RESULT_ERROR_NO_SERVICE:
													viewTexts("SMS Not Sent, No Service");
													break;
												case SmsManager.RESULT_ERROR_NULL_PDU:
													viewTexts("SMS Not Sent, Null PDU");
													break;
												case SmsManager.RESULT_ERROR_RADIO_OFF:
													viewTexts("SMS Not Sent, Radio Off");
													break;
												}
												helpMeans[0] = false;
												if (pref_send_email) {
													data();
												} else {
													call();
												}
											}

										}
									}, new IntentFilter(SENT));
									registerReceiver(new BroadcastReceiver() {
										@Override
										public void onReceive(Context arg0, Intent arg1) {
											viewTexts("Sending SMS Message...");
											final Handler handler1 = new Handler();
											handler1.postDelayed(new Runnable() {
												@Override
												public void run() {
													switch (getResultCode()) {
													case Activity.RESULT_OK:
														viewTexts("SMS Delivered");
														break;
													case Activity.RESULT_CANCELED:
														helpMeans[0] = false;
														viewTexts("SMS Not Delivered");
														break;
													}
													if (pref_send_email) {
														data();
													} else {
														call();
													}
												}
											}, 1500);
										}
									}, new IntentFilter(DELIVERED));
									SmsManager sms = SmsManager.getDefault();
									sms.sendTextMessage(phoneNumber, null, message, sentPI, deliveredPI);

								}
							}, 1700);
						}
					}, 2700);
				} else {
					helpMeans[0] = false;
					if (pref_send_email) {
						data();
					} else {
						call();
					}
				}
			} else {

			}
		}
	}

	boolean checkInternet() {
		ConnectivityManager cm = (ConnectivityManager) getApplicationContext()
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).isConnectedOrConnecting()
				|| cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI).isConnectedOrConnecting()) {
			return true;
		} else {
			return false;
		}
	}

	void enableMobileData() {
		try {
			// create instance of connectivity manager and get system service

			final ConnectivityManager conman = (ConnectivityManager) getApplicationContext()
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			// define instance of class and get name of connectivity manager
			// system service class
			final Class conmanClass = Class.forName(conman.getClass().getName());
			// create instance of field and get mService Declared field
			final Field iConnectivityManagerField = conmanClass.getDeclaredField("mService");
			// Attempt to set the value of the accessible flag to true
			iConnectivityManagerField.setAccessible(true);
			// create instance of object and get the value of field conman
			final Object iConnectivityManager = iConnectivityManagerField.get(conman);
			// create instance of class and get the name of iConnectivityManager
			// field
			final Class iConnectivityManagerClass = Class.forName(iConnectivityManager.getClass().getName());
			// create instance of method and get declared method and type
			final Method setMobileDataEnabledMethod = iConnectivityManagerClass
					.getDeclaredMethod("setMobileDataEnabled", Boolean.TYPE);
			// Attempt to set the value of the accessible flag to true
			setMobileDataEnabledMethod.setAccessible(true);
			// dynamically invoke the iConnectivityManager object according to
			// your need (true/false)
			setMobileDataEnabledMethod.invoke(iConnectivityManager, true);
		} catch (Exception e) {
		}
	}

	void disableMobileData() {
		try {
			// create instance of connectivity manager and get system service

			final ConnectivityManager conman = (ConnectivityManager) getApplicationContext()
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			// define instance of class and get name of connectivity manager
			// system service class
			final Class conmanClass = Class.forName(conman.getClass().getName());
			// create instance of field and get mService Declared field
			final Field iConnectivityManagerField = conmanClass.getDeclaredField("mService");
			// Attempt to set the value of the accessible flag to true
			iConnectivityManagerField.setAccessible(false);
			// create instance of object and get the value of field conman
			final Object iConnectivityManager = iConnectivityManagerField.get(conman);
			// create instance of class and get the name of iConnectivityManager
			// field
			final Class iConnectivityManagerClass = Class.forName(iConnectivityManager.getClass().getName());
			// create instance of method and get declared method and type
			final Method setMobileDataEnabledMethod = iConnectivityManagerClass
					.getDeclaredMethod("setMobileDataEnabled", Boolean.TYPE);
			// Attempt to set the value of the accessible flag to true
			setMobileDataEnabledMethod.setAccessible(false);
			// dynamically invoke the iConnectivityManager object according to
			// your need (true/false)
			setMobileDataEnabledMethod.invoke(iConnectivityManager, false);
		} catch (Exception e) {
		}
	}

	void call() {
		if (pref_call) {
			viewTexts("Calling The Savior...");
			final Handler handler1 = new Handler();
			handler1.postDelayed(new Runnable() {
				@Override
				public void run() {
					try {
						helpMeans[2] = true;
						Intent callIntent = new Intent(Intent.ACTION_CALL);
						callIntent.setData(Uri.parse("tel:" + pref_help_number));
						startActivity(callIntent);
						viewTexts("Called");
					} catch (Exception e) {
						helpMeans[2] = false;
						viewTexts("Not Called");
						Toast.makeText(getApplicationContext(), "·« Ì„ﬂ‰ «·≈ ’«·", 600).show();
					}
					final Handler handler2 = new Handler();
					handler2.postDelayed(new Runnable() {
						@Override
						public void run() {
							viewTexts("Asking For Help Is Finished");
							showResult();
						}
					}, 1500);
				}
			}, 1500);
		} else {
			helpMeans[2] = false;
			final Handler handler = new Handler();
			handler.postDelayed(new Runnable() {

				@Override
				public void run() {
					viewTexts("Asking For Help Is Not");
					showResult();
				}

			}, 3500);
		}
	}

	void truckLocations(double longitude, double latitude, String locationN, String time) {
		if (toggleTrucker.isChecked()) {
			archives = getSharedPreferences(ARCHIVES_NAME, MODE_PRIVATE);
			SharedPreferences.Editor editor = archives.edit();
			if (listCurrentCoordinates[0] == "" && listCurrentCoordinates[1] == "" && listCurrentCoordinates[2] == ""
					&& listCurrentCoordinates[3] == "") {
				listCurrentCoordinates[0] = String.valueOf(longitude);
				listCurrentCoordinates[1] = String.valueOf(latitude);
				listCurrentCoordinates[2] = locationN;
				listCurrentCoordinates[3] = time;

				editor.putString("X_" + String.valueOf(abc - 1), String.valueOf(longitude));
				editor.putString("Y_" + String.valueOf(abc - 1), String.valueOf(latitude));
				editor.putString("A_" + String.valueOf(abc - 1), locationN);
				editor.putString("T_" + String.valueOf(abc - 1), time);
				editor.putInt("itemsNumber", abc - 1);
				editor.commit();
			} else if (listCurrentCoordinates[0] != String.valueOf(longitude)
					&& listCurrentCoordinates[1] != String.valueOf(latitude)) {
				listCurrentCoordinates[0] = String.valueOf(longitude);
				listCurrentCoordinates[1] = String.valueOf(latitude);
				listCurrentCoordinates[2] = locationN;
				listCurrentCoordinates[3] = time;

				editor.putString("X_" + String.valueOf(abc - 1), String.valueOf(longitude));
				editor.putString("Y_" + String.valueOf(abc - 1), String.valueOf(latitude));
				editor.putString("A_" + String.valueOf(abc - 1), locationN);
				editor.putString("T_" + String.valueOf(abc - 1), time);
				editor.putInt("itemsNumber", abc - 1);
				editor.commit();

			}

		}

	}

	Handler handl;
	Runnable ra;

	

	void recordVoice(boolean record) {
		if (record) {
			File f = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/Emergency Recorder/");
			f.mkdir();
			Calendar calendar = Calendar.getInstance(Locale.getDefault());
			int hour = calendar.get(Calendar.HOUR_OF_DAY);
			int minute = calendar.get(Calendar.MINUTE);
			int second = calendar.get(Calendar.SECOND);
			int day = calendar.get(Calendar.DAY_OF_MONTH);
			int month = calendar.get(Calendar.MONTH);
			int year = calendar.get(Calendar.YEAR);

			audioFileName = String.valueOf(year) + "-" + String.valueOf(month) + "-" + String.valueOf(day) + "-"
					+ String.valueOf(hour) + "-" + String.valueOf(minute) + "-" + String.valueOf(second);
			AudioSavePathInDevice = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Emergency Recorder/"
					+ audioFileName + ".3gp";
			MediaRecorderReady();
			try {
				mediaRecorder.prepare();
				mediaRecorder.start();
			} catch (IllegalStateException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			Toast.makeText(getApplicationContext(), "Ã«—Ì  ”ÃÌ· «·’Ê ", 700).show();
		} else {
			try {
				mediaRecorder.stop();
				Toast.makeText(getApplicationContext(), " „ Õ›Ÿ  ”ÃÌ· «·’Ê  ›Ì «·„·›« ", 700).show();
			} catch (Exception e) {
				toggleRecorder.setChecked(true);
			}
		}
	}

	void showResult() {
		if (helpMeans[0]) {
			Toast.makeText(getApplicationContext(), " „ ≈—”«· —”«·… ⁄»— «·—ﬁ„", 700).show();
		}
		if (helpMeans[1]) {
			Toast.makeText(getApplicationContext(), " „ ≈—”«· —”«·… ⁄»— «·≈Ì„Ì·", 700).show();
		}
		if (helpMeans[2]) {
			Toast.makeText(getApplicationContext(), " „ «·≈ ’«· ⁄»— «·—ﬁ„", 700).show();
		}
		if (!helpMeans[0] && !helpMeans[1] && !helpMeans[2]) {
			Toast.makeText(getApplicationContext(), "„‰ÿﬁ ﬂ «·Õ«·Ì… : " + locationName, 1400).show();
			Toast.makeText(getApplicationContext(), "Ì—ÃÏ  ›⁄Ì· Ê”«∆· «·„”«⁄œ…", 700).show();
		}
	}

	public void EnableRuntimePermission() {

		if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,
				Manifest.permission.ACCESS_FINE_LOCATION)) {

			Toast.makeText(MainActivity.this, "ACCESS_FINE_LOCATION permission allows us to Access GPS in app",
					Toast.LENGTH_LONG).show();

		} else {

			ActivityCompat.requestPermissions(MainActivity.this,
					new String[] { Manifest.permission.ACCESS_FINE_LOCATION }, RequestPermissionCode);

		}
	}

	public void MediaRecorderReady() {
		mediaRecorder = new MediaRecorder();
		mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
		mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
		mediaRecorder.setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB);
		mediaRecorder.setOutputFile(AudioSavePathInDevice);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_trucker:
			Intent i = new Intent(MainActivity.this, TruckerActivity.class);
			startActivity(i);
			break;
		case R.id.action_settings:
			Intent i1 = new Intent(MainActivity.this, SettingsActivity.class);
			startActivity(i1);
			break;
		case R.id.action_restart:
			restartApp();
			break;
		case R.id.action_exit:

			finish();
			onDestroy();
			break;
		}
		return true;
	}

	void setPreferences() {
		archives = getSharedPreferences(ARCHIVES_NAME, MODE_PRIVATE);
		prefs = PreferenceManager.getDefaultSharedPreferences(this);

		pref_send_sms = prefs.getBoolean("cb_send_sms", false);
		pref_send_email = prefs.getBoolean("cb_send_email", true);
		pref_call = prefs.getBoolean("cb_call", true);

		pref_record_voice = prefs.getBoolean("cb_record_voice", false);
		pref_record_voice_send_email = prefs.getBoolean("cb_record_voice_send_email", true);
		pref_gps_trucker = prefs.getBoolean("cb_gps_trucker", true);
		pref_gps_trucker_send_sms = prefs.getBoolean("cb_gps_trucker_send_sms", false);
		pref_gps_trucker_send_email = prefs.getBoolean("cb_gps_trucker_send_email", false);
		pref_truck_every_second = prefs.getBoolean("cb_truck_every_second", false);

		pref_help_number = prefs.getString("et_help_number", "0968969114");
		pref_current_email = prefs.getString("et_current_email", "emergency.s.209@gmail.com");
		pref_current_backup_email = prefs.getString("et_current_backup_email", "emergency.s.210@gmail.com");
		pref_help_email = prefs.getString("et_help_email", "zezoocvi.77@gmail.com");

	}

	private Runnable updateTimerThread = new Runnable() {

		@Override
		public void run() {
			timeInMilliseconds = SystemClock.uptimeMillis() - startTime;
			updatedTime = timeInMilliseconds;
			countNumber = (int) (updatedTime / 1000);
			customHandler.postDelayed(this, 0);
		}

	};

	void restartApp() {

		Intent i = getBaseContext().getPackageManager().getLaunchIntentForPackage(getBaseContext().getPackageName());
		i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(i);
		onDestroy();
	}

	void restartActivity() {
		Intent i = new Intent(MainActivity.this, MainActivity.class);
		MainActivity.this.startActivity(i);
		MainActivity.this.finish();

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case 1:
			gps();
			break;
		}

	}

	@Override
	protected void onDestroy() {

		super.onDestroy();
	}

	@Override
	public void onLocationChanged(Location location) {
		abc = abc + 1;
		if (getGpsState == 2 || pref_gps_trucker) {
			showIfGetLocationIsDone();
		}
		Calendar calendar = Calendar.getInstance(Locale.getDefault());
		int hour = calendar.get(Calendar.HOUR_OF_DAY);
		int minute = calendar.get(Calendar.MINUTE);
		int second = calendar.get(Calendar.SECOND);
		int day = calendar.get(Calendar.DAY_OF_MONTH);
		int month = calendar.get(Calendar.MONTH);
		int year = calendar.get(Calendar.YEAR);
		String time = String.valueOf(hour) + ":" + String.valueOf(minute) + ":" + String.valueOf(second);
		List<String> providerList = locationManager.getAllProviders();
		if (null != location && null != providerList && providerList.size() > 0) {
			Longitude = location.getLongitude();
			Latitude = location.getLatitude();
			Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
			try {
				List<Address> listAddresses = geocoder.getFromLocation(Latitude, Longitude, 1);
				if (null != listAddresses && listAddresses.size() > 0) {
					locationName = listAddresses.get(0).getAddressLine(0);
					if (getGpsState == 1) {
						t4.setText("Longitude : " + String.valueOf(Longitude));
						final Handler handler1 = new Handler();
						handler1.postDelayed(new Runnable() {
							@Override
							public void run() {
								t5.setText("Latitude  : " + String.valueOf(Latitude));
								final Handler handler2 = new Handler();
								handler2.postDelayed(new Runnable() {
									@Override
									public void run() {
										t6.setText("Area : " + locationName);
									}
								}, 1000);
							}
						}, 1000);
					}
					if (t4.getText().toString() != "") {
						if (getGpsState == 1)
							sendSms();
						if (pref_gps_trucker) {
							if (!toggleTrucker.isChecked()) {
								toggleTrucker.setChecked(true);
							}
							truckLocations(location.getLongitude(), location.getLatitude(),
									listAddresses.get(0).getAddressLine(0), time);
						}
					}
					if (getGpsState == 2) {
						if (Double.valueOf(Longitude) != 0) {
							truckLocations(location.getLongitude(), location.getLatitude(),
									listAddresses.get(0).getAddressLine(0), time);
						}
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

	private void showIfGetLocationIsDone() {
		if (abcde) {
			if (Double.valueOf(Longitude) != 0 && Double.valueOf(Latitude) != 0) {
				if (locationName != "") {
					abcde = false;
					Toast.makeText(getApplicationContext(), "Ã«—Ì   »⁄ «·„Êﬁ⁄", 800).show();
				}
			}
		}

	}

	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onProviderEnabled(String provider) {

	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {

	}

	void viewTexts(String text) {
		if (t1.getText().toString() != "") {
			if (t2.getText().toString() != "") {
				if (t3.getText().toString() != "") {
					if (t4.getText().toString() != "") {
						if (t5.getText().toString() != "") {
							if (t6.getText().toString() != "") {
								if (t7.getText().toString() != "") {
									if (t8.getText().toString() != "") {
										if (t9.getText().toString() != "") {
											if (t10.getText().toString() != "") {
												if (t11.getText().toString() != "") {
													if (t12.getText().toString() != "") {
														if (t13.getText().toString() != "") {
															if (t14.getText().toString() != "") {
																if (t15.getText().toString() != "") {
																	if (t16.getText().toString() != "") {
																		if (t17.getText().toString() != "") {
																			if (t18.getText().toString() == "")
																				t18.setText(text);
																		} else
																			t17.setText(text);
																	} else
																		t16.setText(text);
																} else
																	t15.setText(text);
															} else
																t14.setText(text);
														} else
															t13.setText(text);
													} else
														t12.setText(text);
												} else
													t11.setText(text);
											} else
												t10.setText(text);
										} else
											t9.setText(text);
									} else
										t8.setText(text);
								} else
									t7.setText(text);
							} else
								t6.setText(text);
						} else
							t5.setText(text);
					} else
						t4.setText(text);
				} else
					t3.setText(text);
			} else
				t2.setText(text);
		} else
			t1.setText(text);
	}


	@Override
	public void onBackPressed() {
		new AlertDialog.Builder(this).setIcon(android.R.drawable.ic_dialog_alert).setTitle("Exit")
				.setMessage("Â· √‰  „ √ﬂœ „‰ «·Œ—ÊÃø ﬁœ   Êﬁ› »⁄’ «·⁄„·Ì« ")
				.setPositiveButton("‰⁄„", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						finish();
						onDestroy();
					}
				}).setNegativeButton("≈·€«¡", null).show();
	}

}
