package com.example.clientes.view

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
fun EditarCliente(navController: NavController, viewModel: RegistroCliente, id: String){
    var cliente by remember { mutableStateOf<Cliente?>(null) }

    LaunchedEffect(id) {
        // viewModel.obtenerAlumno(id)
        viewModel.obtenerCliente(id) { alumnoObtenido ->
            cliente = alumnoObtenido
        }
    }

    if(cliente == null){
        Text("Cargando datos del cliente...")
        return
    }

    var nombre by remember { mutableStateOf(cliente?.nombre ?: "") }
    var fechaRegistro by remember { mutableStateOf(cliente?.fechaRegistro ?: "") }
    var correo by remember { mutableStateOf(cliente?.correo ?: "") }
    var telefono by remember { mutableStateOf(cliente?.telefono ?: "") }


    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { TitleView(name = "Editar datos") },
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


            OutlinedTextField(
                value = nombre,
                onValueChange = { newValue ->
                    cliente = cliente?.copy(nombre = newValue)
                    nombre = newValue
                },
                label = { Text( text= "Nombre") },
                // keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
                    .padding(horizontal = 30.dp)
            )
            Space()
            DateFieldEdit(cliente = cliente!!){ nuevaFecha ->
                fechaRegistro = nuevaFecha
                cliente = cliente?.copy(fechaRegistro = nuevaFecha)
            }
            Space()
            OutlinedTextField(
                value = telefono.toString(),
                onValueChange = { newValue ->
                    if(newValue.all { it.isDigit()}){
                        cliente = cliente?.copy(telefono = newValue)
                        telefono = newValue
                    }
                },
                label = { Text( text= "Teléfono") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
                    .padding(horizontal = 30.dp)
            )
            Space()
            OutlinedTextField(
                value = correo,
                onValueChange = { newValue ->
                    cliente = cliente?.copy(correo = newValue)
                    correo = newValue
                },
                label = { Text( text= "Grupo") },
                // keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
                    .padding(horizontal = 30.dp)
            )
            Space()
            Button(onClick = {
                if (nombre.isNotEmpty() && fechaRegistro.isNotEmpty() && telefono != null && correo.isNotEmpty()){
                    cliente?.let {
                        it.id?.let { it1 ->
                            viewModel.editarDatos(
                                id = it1,
                                nombre = nombre,
                                fechaRegistro = fechaRegistro,
                                correo = correo,
                                telefono = telefono
                            )
                        }
                    }

                    //viewModel.editarDatos(matricula, nombre, fechaNacimiento, grupo, telefono)
                    navController.navigate("Home")
                    Toast.makeText(context, "Cambios realizados exitosamente.", Toast.LENGTH_LONG).show()

                }else{
                    Toast.makeText(context, "Completa todos los datos", Toast.LENGTH_SHORT).show()
                }
            }, colors = ButtonDefaults.buttonColors(
                contentColor = Color.White,
                containerColor = Color(0xFF24476c)
            )) {
                Text(text = "Guardar cambios", fontSize = 19.sp)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DateFieldEdit(cliente: Cliente, onDateChange: (String) -> Unit){
    var showDatePicker by remember { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState(initialSelectedDateMillis = parseDateToMillis(cliente.fechaRegistro))
    val selectedDate = datePickerState.selectedDateMillis?.let {
        convertMillisToDateEdit(it)
    } ?: ""

    val formatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

    var fechaRegistro by remember { mutableStateOf(cliente.fechaRegistro) }


    OutlinedTextField(
        value = fechaRegistro,
        onValueChange = { },
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

    if (showDatePicker) {
        Popup(
            onDismissRequest = { showDatePicker = false },
            alignment = Alignment.TopStart
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .offset(y = 64.dp)
                    .shadow(elevation = 4.dp)
                    .background(MaterialTheme.colorScheme.surface)
                    .padding(16.dp)
            ) {
                DatePicker(
                    state = datePickerState,
                    showModeToggle = false
                )

            }
        }
    }

    // Actualiza el valor de la fecha cuando el usuario selecciona una fecha
    LaunchedEffect(datePickerState.selectedDateMillis) {
        datePickerState.selectedDateMillis?.let {
            val formattedDate = convertMillisToDate(it)
            fechaRegistro = formattedDate
            onDateChange(formattedDate)
            //viewModel.onInputChange(alumno!!.copy(fechaNacimiento = formattedDate))
        }
    }
}


fun parseDateToMillis(date: String): Long? {
    return try {
        val formatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        formatter.parse(date)?.time
    } catch (e: Exception) {
        null
    }
}

fun convertMillisToDateEdit(millis: Long): String {
    val formatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    return formatter.format(Date(millis))
}
