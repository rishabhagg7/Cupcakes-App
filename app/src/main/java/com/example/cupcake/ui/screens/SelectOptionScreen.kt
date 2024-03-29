package com.example.cupcake.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.cupcake.R
import com.example.cupcake.ui.components.FormattedPriceLabel

@Composable
fun SelectOptionScreen(
    subtotal: String,
    options: List<String>,
    modifier: Modifier = Modifier,
    onSelectionChanged: (String) -> Unit = {},
    onCancelButtonClicked: () -> Unit = {},
    onNextButtonClicked: () -> Unit = {},
) {
    var selectedValue by rememberSaveable { mutableStateOf("") }
    Column(
       modifier = modifier
           .verticalScroll(rememberScrollState()),
       verticalArrangement = Arrangement.SpaceBetween
   ) {
       Column(
           modifier = Modifier
               .padding(dimensionResource(id = R.dimen.padding_medium))
       ){
           options.forEach{item->
               Row(
                   verticalAlignment = Alignment.CenterVertically,
                   modifier = Modifier
                       .selectable(
                           selected = selectedValue == item,
                            onClick = {
                                 selectedValue = item
                                 onSelectionChanged(item)
                            }
                       )
               ) {
                    RadioButton(
                        selected = selectedValue == item,
                        onClick = {
                            selectedValue = item
                            onSelectionChanged(item)
                        }
                    )
                   Text(
                       text = item
                   )
               }
           }
           HorizontalDivider(
               thickness = dimensionResource(id = R.dimen.thickness_divider),
               modifier = Modifier.padding(bottom = dimensionResource(R.dimen.padding_medium))
           )
           FormattedPriceLabel(
               subtotal = subtotal,
               modifier = Modifier
                   .align(Alignment.End)
                   .padding(vertical = dimensionResource(id = R.dimen.padding_medium))
           )
       }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(dimensionResource(id = R.dimen.padding_medium)),
            horizontalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.padding_medium)),
            verticalAlignment = Alignment.Bottom
        ) {
            OutlinedButton(
                onClick = onCancelButtonClicked,
                modifier = Modifier
                    .weight(1f)
            ) {
                Text(text = stringResource(id = R.string.cancel))
            }
            Button(
                onClick = onNextButtonClicked,
                modifier = Modifier
                    .weight(1f),
                enabled = selectedValue.isNotEmpty(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.tertiary,
                    contentColor = MaterialTheme.colorScheme.onTertiary,
                    disabledContentColor = MaterialTheme.colorScheme.onBackground
                )
            ) {
                Text(text = stringResource(id = R.string.next))
            }
        }
   }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun SelectionOptionScreenPreview() {
    SelectOptionScreen(
        subtotal = "299.99",
        options = listOf("Option 1", "Option 2", "Option 3", "Option 4"),
        modifier = Modifier.fillMaxHeight()
    )
}