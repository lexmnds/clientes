package com.example.clientes.view

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DatePicker
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Popup
import androidx.navigation.NavController
import com.example.clientes.R
import com.example.clientes.components.Space
import com.example.clientes.components.TitleView
import com.example.clientes.model.Cliente
import com.example.clientes.viewModel.RegistroCliente
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ClienteForm(navController: NavController, viewModel: RegistroCliente){
    val cliente by viewModel.cliente
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { TitleView(name = "Nuevo cliente") },
                navigationIcon = {
                    IconButton(
                        onClick = {
                            navController.navigate("Home")
                        },
                    ) {
                        Image(
                            modifier = Modifier.size(30.dp),
                            painter = painterResource(R.drawable.arrow_back),
                            contentDescription = "Flecha regreso",
                            colorFilter = ColorFilter.tint(Color.White),
                        )
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color(0xFF24476c)
                )
            )
        }
    ) {
        val context = LocalContext.current

        Column(
            modifier = Modifier.fillMaxWidth().padding(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Spacer(modifier = Modifier.height(130.dp))
            Space()
            OutlinedTextField(
                value = cliente.nombre,
                onValueChange = { viewModel.onInputChange(cliente.copy(nombre = it)) },
                label = { Text( text= "Nombre") },
                // keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
                    .padding(horizontal = 30.dp)
            )
            Space()
            DateField(cliente = cliente, viewModel = viewModel)
            Space()
            OutlinedTextField(
                value = cliente.telefono.toString(),
                onValueChange = { newValue ->
                    if(newValue.all { it.isDigit()}){
                        viewModel.onInputChange(cliente.copy(telefono = newValue))
                    }
                },
                label = { Text( text= "Teléfono") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
                    .padding(horizontal = 30.dp)
            )
            Space()
            OutlinedTextField(
                value = cliente.correo,
                onValueChange = { viewModel.onInputChange(cliente.copy(correo = it)) },
                label = { Text( text= "Correo") },
                // keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
                    .padding(horizontal = 30.dp)
            )
            Space()
            Button(onClick = {
                if ( cliente.nombre.isNotEmpty() && cliente.fechaRegistro.isNotEmpty() && cliente.telefono != null && cliente.correo.isNotEmpty()){
                    viewModel.enviarDatos()
                    navController.navigate("Home")
                    Toast.makeText(context, "Alumno agregado exitosamente", Toast.LENGTH_LONG).show()
                }
            }, colors = ButtonDefaults.buttonColors(
                contentColor = Color.White,
                containerColor = Color(0xFF24476c)
            )) {
                Text(text = "Enviar", fontSize = 19.sp)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DateField(cliente: Cliente, viewModel: RegistroCliente){
    var showDatePicker by remember { mutableStateOf(false) }

    // Usamos un solo state para el datePicker
    val datePickerState = rememberDatePickerState(initialSelectedDateMillis = convertDateToMillis(cliente.fechaRegistro))

    // Estado para manejar la fecha seleccionada
    var selectedDate by remember { mutableStateOf(cliente.fechaRegistro) }

    // Fecha que se mostrará en el TextField
    val displayDate = selectedDate

    OutlinedTextField(
        value = displayDate,
        onValueChange = { }, // No actualizamos el valor aquí porque el campo es solo lectura
        label = { Text("Fecha registro") },
        readOnly = true,
        trailingIcon = {
            IconButton(onClick = { showDatePicker = !showDatePicker }) {
                Icon(
                    imageVector = Icons.Default.DateRange,
                    contentDescription = "Select date"
                )
            }
        },
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 30.dp)
            .height(64.dp)
    )

    // Mostrar el DatePicker cuando showDatePicker sea true
    if (showDatePicker) {
        Popup(
            onDismissRequest = { showDatePicker = false },
            alignment = Alignment.TopStart
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(elevation = 4.dp)
                    .background(MaterialTheme.colorScheme.surface)
                    .padding(16.dp)
            ) {
                Column {
                    DatePicker(
                        state = datePickerState,
                        showModeToggle = false
                    )

                    Row(
                        horizontalArrangement = Arrangement.End,
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Button(
                            onClick = {
                                datePickerState.selectedDateMillis?.let {
                                    val formattedDate = convertMillisToDate(it)
                                    selectedDate = formattedDate // Actualizamos la fecha seleccionada
                                    viewModel.onInputChange(cliente.copy(fechaRegistro = selectedDate)) // Enviamos el nuevo valor al viewModel
                                    showDatePicker = false
                                }
                            },
                            modifier = Modifier.padding(8.dp)
                        ) {
                            Text("Aceptar")
                        }

                        Button(
                            onClick = {
                                selectedDate = cliente.fechaRegistro // Cancelamos y restauramos la fecha original
                                showDatePicker = false
                            }
                        ) {
                            Text("Cancelar")
                        }
                    }
                }
            }
        }
    }
}

// Función para convertir una fecha a milisegundos
fun convertDateToMillis(dateString: String): Long {
    val formatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    return try {
        val date = formatter.parse(dateString)
        date?.time ?: 0
    } catch (e: Exception) {
        0
    }
}

fun convertMillisToDate(millis: Long): String {
    val formatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    return formatter.format(Date(millis))
}