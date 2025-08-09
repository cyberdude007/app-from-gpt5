
package com.example.splitpaisa.ui.screens.home

import androidx.lifecycle.ViewModel
import com.example.splitpaisa.data.repo.Repo
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeVm @Inject constructor(repo: Repo): ViewModel() {
    val balances = repo.observeAccountBalances()
    val recent = repo.observeRecentTransactions()
}
