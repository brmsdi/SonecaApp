package com.brmsdi.sonecaapp.ui.map

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.content.res.Resources
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.brmsdi.sonecaapp.BuildConfig
import com.brmsdi.sonecaapp.R
import com.brmsdi.sonecaapp.data.listener.DialogConfirmAndCancelListener
import com.brmsdi.sonecaapp.databinding.FragmentMapBinding
import com.brmsdi.sonecaapp.utils.DialogUtil.Companion.closeDialog
import com.brmsdi.sonecaapp.utils.DialogUtil.Companion.createDialog
import com.brmsdi.sonecaapp.utils.MapUtils.Companion.addCircle
import com.brmsdi.sonecaapp.utils.MapUtils.Companion.addInfoWindow
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.GoogleMap.OnCircleClickListener
import com.google.android.gms.maps.GoogleMap.OnMapClickListener
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.Circle
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.gms.maps.model.Marker
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.net.FindCurrentPlaceRequest
import com.google.android.libraries.places.api.net.PlacesClient
import android.widget.CompoundButton.OnCheckedChangeListener
import androidx.core.content.ContextCompat
import com.brmsdi.sonecaapp.data.dto.CircleWithMarkerDTO
import com.brmsdi.sonecaapp.model.Alarm
import com.brmsdi.sonecaapp.model.AlarmWithDaysOfWeek
import com.brmsdi.sonecaapp.model.Distance
import com.brmsdi.sonecaapp.utils.ColorUtil.Companion.isDarkMode
import com.brmsdi.sonecaapp.utils.MapUtils.Companion.addMyLocationCircle
import com.brmsdi.sonecaapp.utils.MapUtils.Companion.getRatio
import com.brmsdi.sonecaapp.utils.ZoomImpl
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.Priority
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class MapFragment : Fragment(), OnMapReadyCallback, OnMapClickListener, OnMarkerClickListener,
    OnCircleClickListener {
    private val tag = MapFragment::class.java.simpleName
    private lateinit var binding: FragmentMapBinding
    private val mapViewModel by viewModel<MapViewModel>()
    private lateinit var supportMapFragment: SupportMapFragment
    private lateinit var googleMap: GoogleMap
    private val fusedLocationClient by inject<FusedLocationProviderClient>()
    private val placeFields: List<Place.Field> = listOf(Place.Field.NAME)
    private lateinit var placeRequest: FindCurrentPlaceRequest
    private lateinit var placesClient: PlacesClient
    private val listCircleWithMarkerDTO = mutableListOf<CircleWithMarkerDTO>()
    private lateinit var dialog: AlertDialog
    private var clickedLatLng: LatLng? = null
    private var circleMyLocation: Circle? = null
    private val request = mutableListOf(
        android.Manifest.permission.ACCESS_COARSE_LOCATION,
        android.Manifest.permission.ACCESS_FINE_LOCATION
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMapBinding.inflate(inflater, container, false)
        binding.buttonLocalization.setOnClickListener {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                request.add(android.Manifest.permission.ACCESS_BACKGROUND_LOCATION)
            }
            requestPermission.launch (
                request.toTypedArray()
            )
        }
        placeRequest = FindCurrentPlaceRequest.newInstance(placeFields)
        Places.initialize(this.requireContext(), BuildConfig.MAPS_API_KEY)
        placesClient = Places.createClient(this.requireContext())
        supportMapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        supportMapFragment.getMapAsync(this)
        return binding.root
    }

    override fun onMapReady(googleMap: GoogleMap) {
        this.googleMap = googleMap
        this.googleMap.setMinZoomPreference(1.0F)
        this.googleMap.setMaxZoomPreference(21.0F)
        styleMap(this.googleMap)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            request.add(android.Manifest.permission.ACCESS_BACKGROUND_LOCATION)
        }
        val count = request.count {
            (ActivityCompat.checkSelfPermission(
                this.requireContext(),
                it
            ) == PackageManager.PERMISSION_GRANTED)
        }
        if (request.size == count) {
            permissionGranted()
            return
        }
        requestPermission.launch (
            request.toTypedArray()
        )
    }

    override fun onMapClick(latLng: LatLng) {
        clickedLatLng = latLng
        mapViewModel.getDataDialog()
    }

    override fun onMarkerClick(marker: Marker): Boolean {
        listCircleWithMarkerDTO
            .firstOrNull { it.circle.center == marker.position }
            ?.let {
                mapViewModel.delete(it.alarmId)
                return true
            }
        return false
    }

    override fun onCircleClick(circle: Circle) {
        listCircleWithMarkerDTO
            .firstOrNull { it.circle.center == circle.center }
            ?.let {
                mapViewModel.delete(it.alarmId)
            }
    }

    private fun observer() {
        mapViewModel.alarmWithDaysOfWeek.observe(this.viewLifecycleOwner) {
            if (listCircleWithMarkerDTO.isEmpty()) {
                it.forEach { alarmWithDaysOfWeek ->
                    fill(alarmWithDaysOfWeek)
                }
                return@observe
            }

            if (it.size < listCircleWithMarkerDTO.size) {
                val removed =
                    listCircleWithMarkerDTO.filter { circleWithMarkerDTO -> circleWithMarkerDTO.alarmId !in it.map { alarmWithDaysOfWeek -> alarmWithDaysOfWeek.alarm.alarmId } }
                if (removed.isNotEmpty()) {
                    removed.forEach { itemRemoved ->
                        itemRemoved.circle.remove()
                        itemRemoved.marker.remove()
                    }
                    listCircleWithMarkerDTO.removeIf { itemRemove -> itemRemove.alarmId in removed.map { itemRemoved -> itemRemoved.alarmId } }
                    return@observe
                }
            }

            it.last { alarmWithDaysOfWeek ->
                fill(alarmWithDaysOfWeek)
                val alarm = alarmWithDaysOfWeek.alarm
                moveCamera(LatLng(alarm.latitude, alarm.longitude), alarmWithDaysOfWeek.distance)
                true
            }
        }

        mapViewModel.alarmDialogData.observe(this.viewLifecycleOwner) { alarmDialogData ->
            clickedLatLng?.let {
                dialog = createDialog(
                    this.requireContext(),
                    it,
                    eventDialogConfirmAndCancelListener(),
                    eventOnCheckedChangeListener(),
                    alarmDialogData
                )
                dialog.show()
                clickedLatLng = null
            }
        }

        mapViewModel.alarmWithDaysOfWeekSaved.observe(this.viewLifecycleOwner) {
            if (it != null) {
                mapViewModel.getAllAlarms()
            }
        }
    }

    private fun fill(alarmWithDaysOfWeek: AlarmWithDaysOfWeek) {
        val alarm = alarmWithDaysOfWeek.alarm
        val latLng = LatLng(alarm.latitude, alarm.longitude)
        val circle =
            addCircle(googleMap, LatLng(alarm.latitude, alarm.longitude), alarmWithDaysOfWeek)
        val marker = addMarker(latLng, alarm)
        if (marker != null) {
            alarm.alarmId.let { alarmId ->
                listCircleWithMarkerDTO.add(CircleWithMarkerDTO(alarmId, circle, marker))
                return@fill
            }
        }
        circle.remove()
    }

    @SuppressLint("MissingPermission")
    private fun initializeCameraMove() {
        fusedLocationClient.lastLocation.addOnCompleteListener { task ->
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                request.add(android.Manifest.permission.ACCESS_BACKGROUND_LOCATION)
            }
            val count = request.count {
                (ActivityCompat.checkSelfPermission(
                    this.requireContext(),
                    it
                ) == PackageManager.PERMISSION_GRANTED)
            }
            if (request.size == count) {
                if (task.isSuccessful) {
                    val location = task.result
                    val cameraPosition = CameraPosition
                        .builder()
                        .zoom(ZOOM_CAMERA)
                        .target(LatLng(location.latitude, location.longitude))
                    googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition.build()))
                }
                return@addOnCompleteListener
            }
            requestPermission.launch (
                request.toTypedArray()
            )
        }
    }

    private fun moveCamera(latLng: LatLng, distance: Distance) {
        val zoom = ZoomImpl().generate(latLng, distance, resources)
        val cameraPosition = CameraPosition.builder()
            .target(latLng)
            .zoom(zoom)
            .build()
        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))
    }

    @SuppressLint("MissingPermission")
    private fun permissionGranted() {
        observer()
        googleMap.setOnMapClickListener(this)
        googleMap.setOnMarkerClickListener(this)
        googleMap.setOnCircleClickListener(this)
        fusedLocationClient.requestLocationUpdates(
            createLocationRequest(), createLocationCallback(), null
        )
        initializeCameraMove()
        mapViewModel.getAllAlarms()
    }

    private fun eventDialogConfirmAndCancelListener(): DialogConfirmAndCancelListener {
        return object : DialogConfirmAndCancelListener {
            override fun confirm(alarmWithDaysOfWeek: AlarmWithDaysOfWeek) {
                mapViewModel.newAlarm(alarmWithDaysOfWeek)
                closeDialog(dialog)
            }

            override fun cancel() {
                closeDialog(dialog)
            }
        }
    }

    private fun eventOnCheckedChangeListener(): OnCheckedChangeListener {
        return OnCheckedChangeListener { card, isChecked ->
            if (isChecked) {
                card.setTextColor(ContextCompat.getColor(this.requireContext(), R.color.blue))
            } else {
                if (card.id == R.id.day_of_week_sunday || card.id == R.id.day_of_week_saturday) {
                    card.setTextColor(
                        ContextCompat.getColor(
                            this.requireContext(),
                            R.color.crimson
                        )
                    )
                    return@OnCheckedChangeListener
                } else if (isDarkMode(this.requireContext())) {
                    card.setTextColor(Color.WHITE)
                    return@OnCheckedChangeListener
                }
                card.setTextColor(
                    ContextCompat.getColor(
                        this.requireContext(),
                        R.color.dark_background
                    )
                )
            }
        }
    }

    private fun addMarker(latLng: LatLng, alarm: Alarm): Marker? {
        return addInfoWindow(googleMap, latLng, alarm.title)
    }

    private fun styleMap(googleMap: GoogleMap) {
        val nightModeFlags = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
        val isNightMode = nightModeFlags == Configuration.UI_MODE_NIGHT_YES
        if (isNightMode) {
            try {
                googleMap.setMapStyle(
                    MapStyleOptions.loadRawResourceStyle(
                        this.requireContext(),
                        R.raw.style_json
                    )
                )
            } catch (e: Resources.NotFoundException) {
                Log.e(tag, e.message.toString())
            }
        }
    }

    private fun createLocationRequest(): LocationRequest {
        return LocationRequest.Builder(1000)
            .setPriority(Priority.PRIORITY_HIGH_ACCURACY).build()
    }

    private fun createLocationCallback(): LocationCallback {
        return object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                locationResult.lastLocation?.let { location ->
                    if (circleMyLocation == null) {
                        circleMyLocation = addMyLocationCircle(googleMap, location)
                        return@let
                    }
                    val zoomValue = googleMap.cameraPosition.zoom
                    val ratio =
                        getRatio(currentZoom = zoomValue, maxZoomLevel = googleMap.maxZoomLevel)
                    val finalValue = +(ratio - zoomValue)
                    circleMyLocation?.radius = finalValue.toDouble()
                    circleMyLocation?.center = LatLng(location.latitude, location.longitude)
                }
            }
        }
    }


    @SuppressLint("MissingPermission")
    private val requestPermission =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) {
            if (it[android.Manifest.permission.ACCESS_COARSE_LOCATION] == true
                || it[android.Manifest.permission.ACCESS_FINE_LOCATION] == true
            ) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    if (it[android.Manifest.permission.ACCESS_BACKGROUND_LOCATION] != null && it[android.Manifest.permission.ACCESS_BACKGROUND_LOCATION] == true) {
                        permissionGranted()
                        if (binding.info.visibility == View.VISIBLE) {
                            binding.info.visibility = View.GONE
                            binding.buttonLocalization.visibility = View.GONE
                            binding.map.visibility = View.VISIBLE
                        }
                    }
                } else {
                    permissionGranted()
                    if (binding.info.visibility == View.VISIBLE) {
                        binding.info.visibility = View.GONE
                        binding.buttonLocalization.visibility = View.GONE
                        binding.map.visibility = View.VISIBLE
                    }
                }

            } else {
                googleMap.isMyLocationEnabled = false
                binding.info.text = getString(R.string.info_location_permission)
                binding.info.visibility = View.VISIBLE
                binding.buttonLocalization.visibility = View.VISIBLE
                binding.map.visibility = View.GONE
            }
        }

    companion object {
        const val ZOOM_CAMERA = 17.0f
    }
}