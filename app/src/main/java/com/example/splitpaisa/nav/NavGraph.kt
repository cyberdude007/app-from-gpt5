
package com.example.splitpaisa.nav

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.splitpaisa.ui.screens.home.HomeScreen
import com.example.splitpaisa.ui.screens.add.AddExpenseScreen
import com.example.splitpaisa.ui.screens.people.PeopleScreen
import com.example.splitpaisa.ui.screens.accounts.AccountsScreen
import com.example.splitpaisa.ui.screens.debts.DebtsScreen

enum class Dest(val route: String) {
    Home("home"), Add("add"), People("people"), Accounts("accounts"), Debts("debts")
}

@Composable
fun AppNav(navController: NavHostController = rememberNavController()) {
    NavHost(navController, startDestination = Dest.Home.route) {
        composable(Dest.Home.route) { HomeScreen(navController) }
        composable(Dest.Add.route) { AddExpenseScreen(navController) }
        composable(Dest.People.route) { PeopleScreen(navController) }
        composable(Dest.Accounts.route) { AccountsScreen(navController) }
        composable(Dest.Debts.route) { DebtsScreen(navController) }
    }
}
