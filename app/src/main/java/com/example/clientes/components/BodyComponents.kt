package com.example.clientes.components


import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


@Composable
fun TitleView(name: String){
    Text(text = name, fontSize = 40.sp, fontWeight = FontWeight.Bold, color = Color.White )
}

@Composable
fun Space(){
    Spacer(modifier = Modifier.height(10.dp))
}

@Composable
fun MainButton(name: String, backColor: Color, color: Color, onClick: () -> Unit){
    Button(onClick = onClick, colors = ButtonDefaults.buttonColors(
        contentColor = color,
        containerColor = backColor
    )) {
        Text(text = name)
    }
}

@Composable
fun MainIconButton(icon: ImageVector, onClick: () -> Unit){
    IconButton(onClick= onClick) {
        Icon(imageVector = icon, contentDescription = null, tint = Color.White)
    }
}

@Composable
fun ActionButton(){
    FloatingActionButton(
        onClick = {/*TODO*/},
        containerColor = Color.Red,
        contentColor = Color.White)
    {
        Icon(imageVector = Icons.Default.Add, contentDescription = "Agregar")
    }
}

@Composable
fun MainTextField(value: String, onValue: (String) -> Unit, label: String){
    OutlinedTextField(
        value = value,
        onValueChange = onValue,
        label = { Text( text= label) },
        // keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        modifier = Modifier.fillMaxWidth()
            .padding(horizontal = 30.dp)
    )
}