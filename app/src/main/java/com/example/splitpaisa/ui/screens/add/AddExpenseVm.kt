
package com.example.splitpaisa.ui.screens.add

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.splitpaisa.data.repo.Repo
import com.example.splitpaisa.data.entity.PersonEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class AddUi(val people: List<PersonEntity> = emptyList())

@HiltViewModel
class AddExpenseVm @Inject constructor(private val repo: Repo) : ViewModel() {
    private val _ui = MutableStateFlow(AddUi())
    val ui: StateFlow<AddUi> = _ui

    init {
        viewModelScope.launch { _ui.value = AddUi(repo.allPeople()) }
    }

    suspend fun saveYouPaid(totalPaise: Long) {
        val people = repo.allPeople()
        val selfIndex = 0 // treat first as "you"
        val category = repo.categoryByName("Food", "EXPENSE") ?: return
        repo.addSplitExpenseYouPaid(1L, category.id, totalPaise, people.map { it.id }, selfIndex)
    }

    suspend fun saveFriendPaid(totalPaise: Long, friendIdx: Int) {
        val people = repo.allPeople()
        val selfIndex = 0 // treat first as "you"
        val friend = people.getOrNull(friendIdx) ?: return
        val category = repo.categoryByName("Food", "EXPENSE") ?: return
        repo.addSplitExpenseFriendPaid(friend.id, category.id, totalPaise, people.map { it.id }, selfIndex)
    }
}
