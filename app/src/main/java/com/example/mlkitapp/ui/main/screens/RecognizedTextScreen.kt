package com.example.mlkitapp.ui.main.screens

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.example.mlkitapp.ui.main.textrecognition.TextRecognViewModel

@Composable
fun RecognizedTextScreen(
    textRecognViewModel: TextRecognViewModel,
    listener: BackToCameraInterface
){
    ConstraintLayout(
        modifier = Modifier.fillMaxSize()
    ) {
        val (title, text, backToCameraButton) = createRefs()
        val textValue = textRecognViewModel.recognizedTextFlow.collectAsState()

        val scrollState = rememberScrollState(0)

        Text(
            modifier = Modifier
                .constrainAs(title) {
                    top.linkTo(parent.top)
                    linkTo(start = parent.start, end = parent.end)
                    width = Dimension.wrapContent
                    height = Dimension.wrapContent
                }
                .then(Modifier.padding(16.dp)),
            text = "TEXT RECOGNITION RESULT",
            style = TextStyle(fontStyle = FontStyle.Italic),
            fontWeight = FontWeight.Bold
        )

        SelectionContainer(modifier = Modifier
            .constrainAs(text) {
                linkTo(top = title.bottom, bottom = backToCameraButton.top, 16.dp, 16.dp)
                linkTo(start = parent.start, end = parent.end)
                width = Dimension.fillToConstraints
                height = Dimension.fillToConstraints
            }
            .then(
                Modifier.padding(16.dp)
            )
            .then(
                Modifier.border(
                    width = 4.dp,
                    color = Color.Black,
                    shape = RectangleShape
                )
            )) {
            Text(
                text = textValue.value.toString() ?: "",
                modifier = Modifier
                    .verticalScroll(scrollState)
                    .then(Modifier.padding(8.dp))
            )
        }

        Button(modifier = Modifier.constrainAs(backToCameraButton) {
            linkTo(start = parent.start, end = parent.end)
            bottom.linkTo(parent.bottom, 16.dp)
            width = Dimension.preferredWrapContent
            height = Dimension.preferredWrapContent
        }, onClick = {
            listener.onCameraBackButtonClick()
        })
        {
            Text("GO BACK TO CAMERA")
        }
    }
}

interface BackToCameraInterface {
    fun onCameraBackButtonClick()
}
/*
@Preview
@Composable
fun RecognizedTextScreenPreview(){
    RecognizedTextScreen(

    )
}  */