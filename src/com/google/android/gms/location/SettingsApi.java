package com.google.android.gms.location;


import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;

public interface SettingsApi {

	  /**
	   * Checks if the relevant system settings are enabled on the device to carry out the desired
	   * location request.
	   *
	   * @param apiClient Client which does not need to be connected at time of request.
	   * @param request Location requirements.
	   * @return a PendingResult which can be used to check status of request and optionally resolve
	   * resolutions.
	   */
	  PendingResult<LocationSettingsResult> checkLocationSettings(GoogleApiClient apiClient,
	      LocationSettingsRequest request);
	}
