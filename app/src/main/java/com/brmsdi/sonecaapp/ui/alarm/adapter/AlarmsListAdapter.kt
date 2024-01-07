package com.brmsdi.sonecaapp.ui.alarm.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import com.brmsdi.sonecaapp.data.listener.LocationCalcListener
import com.brmsdi.sonecaapp.databinding.CardAlarmsBinding
import com.brmsdi.sonecaapp.model.AlarmWithDaysOfWeek

/**
 *
 * @author Wisley Bruno Marques França
 * @since 1
 */
class AlarmsListAdapter(val context: Context, private val locationCalcListener: LocationCalcListener, private val lastLocation: Location?): Adapter<AlarmsViewHolder>() {
    private val list = mutableListOf<AlarmWithDaysOfWeek>()
    companion object {
        var drawable = 0
    }

    init {
        drawable = 0
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlarmsViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = CardAlarmsBinding.inflate(layoutInflater, parent, false)
        return AlarmsViewHolder(context, binding, locationCalcListener, lastLocation)
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: AlarmsViewHolder, position: Int) {
        holder.bind(list[position])
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateItems(newList: MutableList<AlarmWithDaysOfWeek>) {
        list.clear()
        list.addAll(newList)
        this.notifyDataSetChanged()
    }
}