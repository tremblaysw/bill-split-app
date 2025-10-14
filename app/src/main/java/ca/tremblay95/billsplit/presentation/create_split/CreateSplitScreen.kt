package ca.tremblay95.billsplit.presentation.create_split

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import ca.tremblay95.billsplit.R
import ca.tremblay95.billsplit.domain.model.Split
import ca.tremblay95.billsplit.presentation.common.BillSplitTopBar
import ca.tremblay95.billsplit.di.AppViewModelProvider
import ca.tremblay95.billsplit.presentation.common.NavigationDestination
import ca.tremblay95.billsplit.ui.theme.BillSplitAppTheme
import kotlinx.coroutines.launch

object CreateSplitDestination : NavigationDestination {
    override val route : String = "create_split_screen"
    override val titleResource : Int = R.string.create_split_title
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateSplitScreen(
    navigateBack : () -> Unit,
    onNavigateUp : () -> Unit,
    canNavigateBack : Boolean = true,
    viewModel : CreateSplitViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            BillSplitTopBar(
                title = stringResource(CreateSplitDestination.titleResource),
                canNavigateBack = canNavigateBack,
                navigateUp = onNavigateUp
            )
        }
    ) { innerPadding ->
        CreateSplitBody(
            uiState = viewModel.uiState,
            onSplitValueChange = viewModel::updateUiState,
            onSaveClick = {
                coroutineScope.launch {
                    if (viewModel.saveSplitMethod()) {
                        navigateBack()
                    }
                }
            },
            modifier = Modifier
                .padding(
                    start = innerPadding.calculateStartPadding(LocalLayoutDirection.current),
                    end = innerPadding.calculateEndPadding(LocalLayoutDirection.current),
                    top = innerPadding.calculateTopPadding()
                )
                .verticalScroll(rememberScrollState())
                .fillMaxWidth()
        )
    }
}

@Composable
fun CreateSplitBody(
    uiState : CreateSplitState,
    onSplitValueChange : (Split) -> Unit,
    onSaveClick : () -> Unit,
    modifier : Modifier = Modifier
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.padding_large)),
        modifier = modifier.padding(dimensionResource(R.dimen.padding_medium))
    ) {
        CreateSplitInputForm(
            split = uiState.split,
            onValueChange = onSplitValueChange,
            modifier = Modifier.fillMaxWidth()
        )
        Button(
            onClick = onSaveClick,
            enabled = uiState.isEntryValid,
            shape = MaterialTheme.shapes.small,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(stringResource(R.string.save_action))
        }
    }
}

@Composable
fun CreateSplitInputForm(
    split : Split,
    onValueChange : (Split) -> Unit = {},
    @StringRes errorResource : Int? = null,
    enabled : Boolean = true,
    modifier : Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.padding_small))
    ) {
        OutlinedTextField(
            value = split.name,
            onValueChange = {
                onValueChange(split.copy(name = it))
            },
            label = {
                Text(stringResource(R.string.name_required))
            },
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                unfocusedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                disabledContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                errorBorderColor = MaterialTheme.colorScheme.error,
                errorContainerColor = MaterialTheme.colorScheme.errorContainer,
                errorTextColor = MaterialTheme.colorScheme.onErrorContainer
            ),
            modifier = Modifier.fillMaxWidth(),
            isError = errorResource != null,
            enabled = enabled,
            singleLine = true
        )
        if (errorResource != null) {
            Text(
                text = stringResource(errorResource),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.padding(
                    start = dimensionResource(R.dimen.padding_medium),
                )
            )
        }
        else {
            Spacer(modifier = Modifier.height(dimensionResource(R.dimen.padding_medium)))
        }
        OutlinedTextField(
            value = split.description,
            onValueChange = {
                onValueChange(split.copy(description = it))
            },
            label = {
                Text(stringResource(R.string.description))
            },
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                unfocusedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                disabledContainerColor = MaterialTheme.colorScheme.secondaryContainer,
            ),
            modifier = Modifier.fillMaxWidth(),
            enabled = enabled,
            maxLines = 5
        )
        if (enabled) {
            Text(
                text = stringResource(R.string.required_fields),
                modifier = Modifier.padding(start = dimensionResource(R.dimen.padding_medium))
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CreateSplitPreview() {
    BillSplitAppTheme {
        CreateSplitBody(
            uiState = CreateSplitState(
                Split(
                    name = "Split Preview",
                    description = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Morbi porta dignissim quam vitae imperdiet. Mauris vestibulum quam ut neque venenatis, sed mattis odio gravida. Vivamus iaculis dictum tortor, accumsan mattis mauris fermentum sodales. Curabitur feugiat est id venenatis posuere. Cras eget hendrerit mauris, tempus semper quam. Fusce iaculis vehicula ex sit amet placerat."
                )
            ),
            onSplitValueChange = {},
            onSaveClick = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
fun CreateSplitInputFormPreview()
{
    BillSplitAppTheme {
        CreateSplitInputForm(
            Split(name = "Split Preview",
                description = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Morbi porta dignissim quam vitae imperdiet. Mauris vestibulum quam ut neque venenatis, sed mattis odio gravida. Vivamus iaculis dictum tortor, accumsan mattis mauris fermentum sodales. Curabitur feugiat est id venenatis posuere. Cras eget hendrerit mauris, tempus semper quam. Fusce iaculis vehicula ex sit amet placerat."
            ),
            {},
            null,
//            R.string.duplicate_split_name,
            true,
            Modifier.padding(10.dp)
        )
    }
}
