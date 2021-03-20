/*
 * Look4Sat. Amateur radio satellite tracker and pass predictor.
 * Copyright (C) 2019-2021 Arty Bishop (bishop.arty@gmail.com)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package com.rtbishop.look4sat.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.liveData
import com.rtbishop.look4sat.data.model.Result
import com.rtbishop.look4sat.data.model.SatPass
import com.rtbishop.look4sat.data.repository.PrefsRepo
import com.rtbishop.look4sat.data.repository.SatelliteRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

@FlowPreview
@HiltViewModel
class SharedViewModel @Inject constructor(
    prefsRepo: PrefsRepo,
    private val satelliteRepo: SatelliteRepo,
) : ViewModel() {

    private val _passes = MutableStateFlow<Result<MutableList<SatPass>>>(Result.InProgress)
    val passes: LiveData<Result<MutableList<SatPass>>> = _passes.asLiveData()

    init {
        if (prefsRepo.isFirstLaunch()) {
            prefsRepo.setFirstLaunchDone()
        }
    }

    fun getAppTimer() = liveData {
        while (true) {
            emit(System.currentTimeMillis())
            delay(1000)
        }
    }

    fun getTransmittersForSat(satId: Int) = satelliteRepo.getSatTransmitters(satId).asLiveData()
}
