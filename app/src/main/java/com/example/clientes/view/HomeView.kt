package com.example.clientes.view

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.clientes.R
import com.example.clientes.components.TitleView
import com.example.clientes.model.Cliente
import com.example.clientes.viewModel.RegistroCliente

@SuppressLint("UnusedBoxWithConstraintsScope")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeView(navController: NavController, viewModel: RegistroCliente){
    //val clientes by viewModel.clientes.observeAsState(emptyList())
    val clientes by viewModel.clientes.observeAsState(emptyList())
    var expandedMenuItems by remember { mutableStateOf<Map<String, Boolean>>(emptyMap()) }
    val context = LocalContext.current
    var showEliminarCliente by rememberSaveable { mutableStateOf(false) }
    var clienteSeleccionado by remember { mutableStateOf<Cliente?>(null) }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { TitleView(name = "Clientes") },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color(0xFF24476c)
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { navController.navigate("nuevoCliente")
                } ,
                containerColor = Color(0xFF24476c),
                contentColor = Color.White
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Agregar")
            }
        }
    ) { innerPadding ->
        BoxWithConstraints(
            modifier = Modifier.fillMaxSize().padding(innerPadding)
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(16.dp))
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 26.dp)
                        .padding(bottom = 120.dp)
                ) {
                    items(clientes) { cliente ->
                        Card(
                            modifier = Modifier
                                .padding(6.dp)
                                .fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier.fillMaxSize()
                                    .background(Color(0x8024476c)),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Image(
                                    modifier = Modifier.size(70.dp)
                                        .padding(8.dp)
                                        .clip(CircleShape),
                                    painter = painterResource(R.drawable.ic_launcher_foreground),
                                    contentDescription = "Foto del usuario",
                                    contentScale = ContentScale.Crop,
                                    colorFilter = ColorFilter.tint(Color.LightGray)
                                )
                                Spacer(modifier = Modifier.height(5.dp).width(7.dp))
                                Column(
                                    verticalArrangement = Arrangement.Center,
                                    modifier = Modifier.padding(end = 8.dp)
                                ) {
                                    Spacer(modifier = Modifier.height(9.dp))
                                    Row {
                                        Text(
                                            text = "${cliente.nombre}",
                                            color = Color.White,
                                            fontSize = 21.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }

                                    Row {
                                        Text(
                                            text = " ${cliente.fechaRegistro}",
                                            color = Color.White,
                                            fontSize = 19.sp
                                        )
                                    }
                                    Row {
                                        Text(
                                            text = " ${cliente.correo}",
                                            color = Color.White,
                                            fontSize = 19.sp
                                        )
                                    }
                                    Row {
                                        Text(
                                            text = " ${cliente.telefono}",
                                            color = Color.White,
                                            fontSize = 19.sp
                                        )
                                    }
                                    Spacer(modifier = Modifier.padding(bottom = 9.dp))
                                }

                                // Ícono del dropdown menu
                                IconButton(
                                    onClick = {
                                        val key = cliente.id // Obtén el key del alumno aquí
                                        expandedMenuItems = expandedMenuItems.toMutableMap().apply {
                                            put(key ?: "", expandedMenuItems[key] != true)
                                        }
                                    }
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.MoreVert, // Tres puntos
                                        contentDescription = "Opciones"
                                    )
                                }

                                // DropdownMenu
                                DropdownMenu(
                                    expanded = expandedMenuItems[cliente.id ?:""] == true,
                                    onDismissRequest = {
                                        expandedMenuItems = expandedMenuItems.toMutableMap().apply {
                                            //put(cliente.id ?: "", false)
                                            put(cliente.id ?: "", expandedMenuItems[cliente.id ?: ""] != true)
                                        }
                                    }
                                ) {
                                    DropdownMenuItem(
                                        text = { Text("Editar") },
                                        onClick = {
                                            navController.navigate("EditarCliente/${cliente.id}")
                                            expandedMenuItems = expandedMenuItems.toMutableMap().apply {
                                                put(cliente.id ?: "", false)
                                            }
                                        }
                                    )

                                    DropdownMenuItem(
                                        text = { Text("Eliminar") },
                                        onClick = {
                                             clienteSeleccionado = cliente
                                             showEliminarCliente = true
                                             expandedMenuItems = expandedMenuItems.toMutableMap().apply {
                                                 put(cliente.id ?: "", false)
                                             }
                                        }
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
        if (showEliminarCliente && clienteSeleccionado != null) {
            EliminarCliente(
                onAccept = {
                      viewModel.eliminarDatos(clienteSeleccionado?.id ?: "")
                    Toast.makeText(context, "Cliente eliminado", Toast.LENGTH_SHORT).show()
                    showEliminarCliente = false
                },
                onDismiss = {
                    showEliminarCliente = false
                }
            )
        }
    }
}

@Composable
fun EliminarCliente(onDismiss: () -> Unit, onAccept: () -> Unit){
    val openDialog = remember { mutableStateOf(true) }

    AlertDialog(
        onDismissRequest = {
            openDialog.value = false
            onDismiss()
        },
        title = { Text(text = "Eliminar cliente") },
        text = { Text(text = "Los datos serán eliminados de manera permanente, ¿Desea continuar?") },
        confirmButton = {
            TextButton(onClick = {
                openDialog.value = false
                onAccept()
            }, modifier = Modifier
                .clip(RoundedCornerShape(50))
                .background(Color(0xFF24476c))
            )
            { Text("Aceptar", color = Color.White) }
        },
        dismissButton = {
            TextButton(onClick = {
                openDialog.value = false
                onDismiss()
            }, modifier = Modifier
                .clip(RoundedCornerShape(50))
                .background(Color(0xFFA42C2C))
            )
            { Text("Cancelar", color = Color.White) }
        }
    )
}


