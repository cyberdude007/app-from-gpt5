
package com.example.splitpaisa.ui.screens.accounts

import androidx.lifecycle.ViewModel
import com.example.splitpaisa.data.repo.Repo
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AccountsVm @Inject constructor(repo: Repo): ViewModel() {
    val balances = repo.observeAccountBalances()
}
