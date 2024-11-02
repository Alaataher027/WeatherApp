package com.example.data.location

import android.Manifest
import android.app.Application
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import androidx.core.content.ContextCompat
import com.example.domain.location.LocationTracker
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import kotlinx.coroutines.CancellableContinuation
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.suspendCancellableCoroutine
import javax.inject.Inject
import kotlin.coroutines.resume

@ExperimentalCoroutinesApi
class DefaultLocationTracker @Inject constructor(
    private val locationClient: FusedLocationProviderClient,
    private val application: Application
) : LocationTracker {

    override suspend fun getCurrentLocation(): Location? {
        val hasAccessFineLocationPermission = ContextCompat.checkSelfPermission(
            application,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
        val hasAccessCoarseLocationPermission = ContextCompat.checkSelfPermission(
            application,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        val locationManager =
            application.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        val isGpsEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER) ||
                locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)

        if (!hasAccessCoarseLocationPermission || !hasAccessFineLocationPermission || !isGpsEnabled) {
            return null
        }

        // suspends getting the location until
        // 1>> The last known location is retrieved successfully
        // 2 >> If the lastLocation isn't complete yet, it sets listeners to wait for the completion of the request.

        return try {
            suspendCancellableCoroutine { cont ->
                locationClient.lastLocation.apply {
                    if (isComplete) {
                        if (isSuccessful) {
                            cont.resume(result)
                        } else {
                            cont.resume(null)
                        }
                        return@suspendCancellableCoroutine
                    }
                    addOnSuccessListener {
                        if (it != null) {
                            cont.resume(it)
                        } else {
                            // If `lastLocation` is null, request a new location update
                            requestNewLocationUpdate(cont)
                        }
                    }
                    addOnFailureListener {
                        cont.resume(null)
                    }
                    addOnCanceledListener {
                        cont.cancel()
                    }
                }
            }
        } catch (e: SecurityException) {
            null // Return null if a SecurityException occurs due to permission issues
        }
    }

    private fun requestNewLocationUpdate(cont: CancellableContinuation<Location?>) {
        val locationRequest = LocationRequest.create().apply {
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
            numUpdates = 1
            interval = 1000L
        }

        val locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                cont.resume(locationResult.lastLocation)
                locationClient.removeLocationUpdates(this)
            }
        }

        try {
            locationClient.requestLocationUpdates(
                locationRequest,
                locationCallback,
                null
            )
        } catch (e: SecurityException) {
            cont.resume(null) // Handle potential SecurityException gracefully
        }

        cont.invokeOnCancellation {
            locationClient.removeLocationUpdates(locationCallback)
        }
    }
}
