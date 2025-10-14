package ca.tremblay95.billsplit.presentation.create_split

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import ca.tremblay95.billsplit.R
import ca.tremblay95.billsplit.common.BillSplitError
import ca.tremblay95.billsplit.common.Result
import ca.tremblay95.billsplit.domain.model.Split
import ca.tremblay95.billsplit.domain.use_cases.AddSplit

// TODO: Pass in GetMethodsListUseCase
class CreateSplitViewModel(
    private val addSplit : AddSplit
) : ViewModel() {
    var uiState by mutableStateOf(CreateSplitState())
        private set

    fun updateUiState(split : Split) {
        uiState = CreateSplitState(
            split = split,
            isEntryValid = validateInput(split)
        )
    }

    suspend fun saveSplitMethod() : Boolean {
        if (validateInput()) {
            val result = addSplit(uiState.split)
            when (result) {
                is Result.Success -> {
                    return true
                }
                is Result.Error -> {
                    when (result.reason) {
                        is BillSplitError.Unknown -> {
                            uiState.copy(errorResource = R.string.create_split_failed_error)
                        }
                        is BillSplitError.DuplicateName -> {
                            uiState.copy(errorResource = R.string.duplicate_split_name_error)
                        }
                    }
                }
                is Result.Loading -> {}
                is Result.NotFound -> {}
            }
        }
        return false
    }

    private fun validateInput(state : Split = uiState.split) : Boolean {
        return with(state) {
            name.isNotBlank() // do we need to ensure there's always a description?
        }
    }
}

