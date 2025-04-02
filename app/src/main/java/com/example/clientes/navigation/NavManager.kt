package com.example.clientes.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.clientes.view.ClienteForm
import com.example.clientes.view.EditarCliente
import com.example.clientes.view.HomeView
import com.example.clientes.viewModel.RegistroCliente

@Composable
fun NavManager(){
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "Home"){
        composable("Home"){
            val clientevm : RegistroCliente = viewModel()
            HomeView(navController, clientevm)
        }
        composable("nuevoCliente"){
           val cliente : RegistroCliente = viewModel()
            ClienteForm(navController, cliente)
        }
        composable("EditarCliente/{id}", arguments = listOf(
            navArgument("id") { type = NavType.StringType}
        )){
            val id = it.arguments?.getString("id")?: ""
            val ec : RegistroCliente = viewModel()
            EditarCliente(navController, id = id, viewModel = viewModel())
        }

    }

}