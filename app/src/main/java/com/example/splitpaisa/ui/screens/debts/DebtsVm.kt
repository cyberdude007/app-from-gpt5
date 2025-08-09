
package com.example.splitpaisa.ui.screens.debts

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.splitpaisa.data.repo.Repo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DebtsVm @Inject constructor(private val repo: Repo): ViewModel() {
    private val _nets = MutableStateFlow<Map<Long, Long>>(emptyMap())
    val nets: StateFlow<Map<Long, Long>> = _nets
    private var names: Map<Long, String> = emptyMap()

    init {
        viewModelScope.launch {
            val list = repo.peopleWithNet()
            _nets.value = list.associate { it.person.id to it.netPaise }
            names = list.associate { it.person.id to it.person.nickname }
        }
    }

    fun nameFor(id: Long) = names[id] ?: "P$id"
}
