package com.brmsdi.sonecaapp.ui.alarm

import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.brmsdi.sonecaapp.data.listener.LocationCalcListener
import com.brmsdi.sonecaapp.databinding.FragmentAlarmsBinding
import com.brmsdi.sonecaapp.ui.alarm.adapter.AlarmsListAdapter
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.maps.model.LatLng
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class AlarmsFragment : Fragment(), LocationCalcListener {
    private lateinit var binding: FragmentAlarmsBinding
    private val viewModel by viewModel<AlarmViewModel>()
    private val fusedLocationProviderClient by inject<FusedLocationProviderClient>()
    private lateinit var alarmsListAdapter: AlarmsListAdapter
    private var lastLocation: Location? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAlarmsBinding.inflate(layoutInflater)
        binding.recyclerAlarms.layoutManager = LinearLayoutManager(this.requireContext())
        observe()
        return binding.root
    }

    private fun observe() {
        viewModel.data.observe(this.viewLifecycleOwner) {
            if (it.isEmpty()) binding.textInfo.visibility = View.VISIBLE
            else binding.textInfo.visibility = View.GONE
            alarmsListAdapter.updateItems(it.toMutableList())
        }
    }

    override fun onResume() {
        super.onResume()
        initAdapter()
    }

    override fun calculateDistance(location: Location, alarmLocation: LatLng): Float {
        val alarm = Location("")
        alarm.latitude = alarmLocation.latitude
        alarm.longitude = alarmLocation.longitude
        return location.distanceTo(alarm)
    }

    private fun initAdapter() {
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
        getLocation()
    }

    @SuppressLint("MissingPermission")
    private fun getLocation() {
        fusedLocationProviderClient.lastLocation.addOnCompleteListener {
            if (it.isSuccessful) {
                lastLocation = it.result
            }
            alarmsListAdapter = AlarmsListAdapter(this.requireContext(), this, lastLocation)
            binding.recyclerAlarms.adapter = alarmsListAdapter
            getData()
        }
    }

    private fun getData() {
        viewModel.updateData()
    }

    private val requestPermission =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) {
            if (it[android.Manifest.permission.ACCESS_COARSE_LOCATION] == true || it[android.Manifest.permission.ACCESS_FINE_LOCATION] == true) {
                getLocation()
            }
        }
}