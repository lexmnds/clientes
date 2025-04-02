package com.example.clientes.viewModel


import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.clientes.model.Cliente
import com.google.firebase.Firebase
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database

class RegistroCliente: ViewModel() {
    private val database = Firebase.database.reference
    private val _cliente = mutableStateOf(Cliente())
    val cliente : State<Cliente> = _cliente
    private val _clientes = MutableLiveData<List<Cliente>>(emptyList())
    val clientes: LiveData<List<Cliente>> get() = _clientes

    init {
        mostrarDatos()
    }

    fun onInputChange(newCliente: Cliente) {
        _cliente.value = newCliente
    }

    fun enviarDatos() {
        val clienteData = _cliente.value
        if (clienteData != null) {
            val telefonoStr = clienteData.telefono.toString()

            val updateCliente = clienteData.copy(
                telefono = telefonoStr
            )

            val newClienteRef = database.child("clientes").push()
            Log.d("RegistroCliente", "Referencia generada: ${newClienteRef.key}")
            val clienteConId = newClienteRef.key?.let { updateCliente.copy(id = it) }

            newClienteRef.setValue(clienteConId)
                .addOnSuccessListener {
                    Log.d("RegistroCliente", "Datos enviados correctamente con ID: ${newClienteRef.key}")
                }
                .addOnFailureListener { error ->
                    Log.e("RegistroCliente", "Error al enviar datos: ${error.message}")
                }
        }

    }

    fun eliminarDatos(id: String) {
        // val matricula = _alumno.value.matricula
        //Log.d("MATRICULA", "MATRICULA A ELIMINAR: $matricula")
        if (id.isNotEmpty()) {
            database.child("clientes").child(id).removeValue()
                .addOnSuccessListener {
                    Log.d("Registro", "Datos eliminados")
                }
                .addOnFailureListener { error ->
                    Log.e("Error", "Error al eliminar datos ${error.message}")
                }
        } else {
            Log.e("Error", "La matrícula está vacía")
        }
    }

    fun mostrarDatos() {
        val dataRef = database.child("clientes")
        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val clientesList = mutableListOf<Cliente>()
                    for (childSnapshot in snapshot.children) {
                        val cliente = childSnapshot.getValue(Cliente::class.java)
                        if (cliente != null) {
                            val key = childSnapshot.key
                            val clienteConKey = key?.let { cliente.copy(id = it) }
                            if (clienteConKey != null) {
                                clientesList.add(clienteConKey)
                            }
                        }
                    }
                    _clientes.value = clientesList
                } else {
                    Log.e("Error", "No existen datos en la base de datos")
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("Error", "Error al mostrar los datos")
            }
        }
        dataRef.addValueEventListener(listener)
    }

    fun obtenerCliente(id: String, onAlumnoObtenido: (Cliente) -> Unit) {
        database.child("clientes").child(id).get().addOnSuccessListener { snapshot ->
            if (snapshot.exists()) {
                val cliente = snapshot.getValue(Cliente::class.java)
                cliente?.let { onAlumnoObtenido(it) }
            } else {
                Log.e("Firebase", "Alumno no encontrado")
            }
        }
    }

    fun editarDatos(id: String, nombre: String, fechaRegistro: String, telefono: String, correo: String
    ){
        // database.child("alumnos").child(matricula)

        val updateCliente = mapOf(
            "nombre" to nombre,
            "fechaRegistro" to fechaRegistro,
            "telefono" to telefono,
            "correo" to correo
        )

        database.child("clientes").child(id)
            .updateChildren(updateCliente)
            .addOnCompleteListener{ task ->
                if (task.isSuccessful){
                    Log.d("Editar Alumno", "Datos actualizados correctamente")
                }else {
                    Log.e("Error editar datos", "Error al actualizar los datos ${task.exception}")
                }
            }

    }


}
