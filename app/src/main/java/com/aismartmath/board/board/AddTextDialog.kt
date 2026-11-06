package com.aismartmath.board.board

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/** Add your own text (a question, a note). After adding, move it, resize it, change its colour and size. */
@Composable
fun AddTextDialog(state: BoardState) {
    var text by remember { mutableStateOf("") }
    AlertDialog(
        onDismissRequest = { state.textDialog = false },
        containerColor = Color(0xFF2A1B12),
        title = { Text("Add text", color = Color(0xFFF1D3A5), fontSize = 15.sp) },
        text = {
            BasicTextField(
                value = text,
                onValueChange = { text = it },
                textStyle = TextStyle(color = Color.White, fontSize = 14.sp),
                cursorBrush = SolidColor(Color(0xFFD08A4E)),
                decorationBox = { inner ->
                    if (text.isEmpty()) Text("Type or paste your question…", color = Color(0xFF9C8470), fontSize = 14.sp)
                    inner()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 90.dp, max = 200.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color(0xFF160E0A))
                    .border(BorderStroke(1.dp, Color(0xFF6B4630)), RoundedCornerShape(8.dp))
                    .padding(8.dp),
            )
        },
        confirmButton = {
            TextButton(onClick = {
                if (text.isNotBlank()) {
                    state.textDialog = false
                    state.addText(text)
                }
            }) { Text("Add", color = Color(0xFFF1D3A5)) }
        },
        dismissButton = {
            TextButton(onClick = { state.textDialog = false }) { Text("Cancel", color = Color(0xFFB59A83)) }
        },
    )
}
