package com.brmsdi.sonecaapp.ui.map

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.content.res.Resources
import android.graphics.Color
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
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
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
import com.brmsdi.sonecaapp.utils.ZoomImpl
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class MapFragment : Fragment(), OnMapReadyCallback, OnMapClickListener, OnMarkerClickListener,
    OnCircleClickListener {
    private val tag = MapFragment::class.java.simpleName
    private lateinit var binding: FragmentMapBinding
    private val mapViewModel by viewModel<MapViewModel>()
    private lateinit var supportMapFragment: SupportMapFragment
    private lateinit var googleMap: GoogleMap
    private val fusedLocationClient by inject<FusedLocationProviderClient> ()
    private val placeFields: List<Place.Field> = listOf(Place.Field.NAME)
    private lateinit var placeRequest: FindCurrentPlaceRequest
    private lateinit var placesClient: PlacesClient
    private val listCircleWithMarkerDTO = mutableListOf<CircleWithMarkerDTO>()
    private lateinit var dialog: AlertDialog
    private var clickedLatLng: LatLng? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMapBinding.inflate(inflater, container, false)
        binding.buttonLocalization.setOnClickListener {
            requestPermission.launch(
                arrayOf(
                    android.Manifest.permission.ACCESS_COARSE_LOCATION,
                    android.Manifest.permission.ACCESS_FINE_LOCATION
                )
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
        styleMap(this.googleMap)
        if (!(ActivityCompat.checkSelfPermission(
                this.requireContext(),
                android.Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
                    || ActivityCompat.checkSelfPermission(
                this.requireContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED)
        ) {
            requestPermission.launch(
                arrayOf(
                    android.Manifest.permission.ACCESS_COARSE_LOCATION,
                    android.Manifest.permission.ACCESS_FINE_LOCATION
                )
            )
            return
        }
        permissionGranted()
        //findCurrentPlace()
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
                if (removed.isNotEmpty())
                {
                    removed.forEach {itemRemoved ->
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
        val circle = addCircle(googleMap, LatLng(alarm.latitude, alarm.longitude), alarmWithDaysOfWeek)
        val marker = addMarker(latLng, alarm)
        if (marker != null) {
            alarm.alarmId.let { alarmId ->
                listCircleWithMarkerDTO.add(CircleWithMarkerDTO(alarmId, circle, marker))
                return@fill
            }
        }
        circle.remove()
    }

    private fun moveCamera(latLng: LatLng, distance: Distance) {
        val zoom = ZoomImpl().generate(latLng, distance, resources)
        val cameraPosition = CameraPosition.builder()
            .target(latLng)
            .zoom(zoom)
            .build()
        googleMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))
    }

    @SuppressLint("MissingPermission")
    private fun permissionGranted() {
        observer()
        googleMap.setOnMapClickListener(this)
        googleMap.setOnMarkerClickListener(this)
        googleMap.setOnCircleClickListener(this)
        googleMap.isMyLocationEnabled = true
        lastLocation(fusedLocationClient, googleMap)
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
                if (card.id == R.id.day_of_week_sunday || card.id == R.id.day_of_week_saturday)
                    card.setTextColor(
                        ContextCompat.getColor(
                            this.requireContext(),
                            R.color.crimson
                        )
                    )
                else card.setTextColor(Color.WHITE)
            }
        }
    }

    private fun addMarker(latLng: LatLng, alarm: Alarm): Marker? {
        return addInfoWindow(googleMap, latLng, alarm.title, "snippet")
    }

    private fun lastLocation(
        fusedLocationProviderClient: FusedLocationProviderClient,
        googleMap: GoogleMap
    ) {
        if (!(ActivityCompat.checkSelfPermission(
                this.requireContext(),
                android.Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
                    || ActivityCompat.checkSelfPermission(
                this.requireContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED)
        ) {
            requestPermission.launch(
                arrayOf(
                    android.Manifest.permission.ACCESS_COARSE_LOCATION,
                    android.Manifest.permission.ACCESS_FINE_LOCATION
                )
            )
            return
        }
        fusedLocationProviderClient.lastLocation.addOnCompleteListener {
            val location = it.result
            if (location != null) {
                val cameraPosition = CameraPosition
                    .builder()
                    .zoom(ZOOM_CAMERA)
                    .target(LatLng(location.latitude, location.longitude))
                    .build()
                googleMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))
            }
        }
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

    private fun findCurrentPlace() {
        if (!(ActivityCompat.checkSelfPermission(
                this.requireContext(),
                android.Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
                    || ActivityCompat.checkSelfPermission(
                this.requireContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED)
        ) {
            requestPermission.launch(
                arrayOf(
                    android.Manifest.permission.ACCESS_COARSE_LOCATION,
                    android.Manifest.permission.ACCESS_FINE_LOCATION
                )
            )
            return
        }
        placesClient.findCurrentPlace(placeRequest)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val place = task.result.placeLikelihoods[0].place
                    binding.editName.setText(place.name)
                } else {
                    val exception = task.exception
                    binding.editName.setText("")
                    if (exception is ApiException) {
                        Log.e(tag, "Place not found: ${exception.statusCode}")
                    }
                }
            }
    }

    @SuppressLint("MissingPermission")
    private val requestPermission =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) {
            if (it[android.Manifest.permission.ACCESS_COARSE_LOCATION] == true || it[android.Manifest.permission.ACCESS_FINE_LOCATION] == true) {
                permissionGranted()
                if (binding.info.visibility == View.VISIBLE) {
                    binding.info.visibility = View.GONE
                    binding.buttonLocalization.visibility = View.GONE
                    binding.map.visibility = View.VISIBLE
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