package com.example.clientes.model

data class Cliente(
    val nombre: String,
    val fechaRegistro: String,
    val telefono: String,
    val correo: String,
    val id: String
) {
    constructor(): this(
        "",
        "",
        "",
        "",
        ""
    )
}
