
package com.example.splitpaisa.ui.screens.people

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.splitpaisa.data.repo.Repo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PeopleVm @Inject constructor(private val repo: Repo): ViewModel() {
    private val _people = MutableStateFlow<List<Repo.PersonWithNet>>(emptyList())
    val people: StateFlow<List<Repo.PersonWithNet>> = _people

    init { refresh() }

    private fun refresh() = viewModelScope.launch { _people.value = repo.peopleWithNet() }

    suspend fun settleTheyPaidMe(personId: Long, amountPaise: Long) {
        repo.settle(personId, "THEY_PAID_ME", amountPaise, accountId = 1L); refresh()
    }
    suspend fun settleIPaidThem(personId: Long, amountPaise: Long) {
        repo.settle(personId, "I_PAID_THEM", amountPaise, accountId = 1L); refresh()
    }
}
