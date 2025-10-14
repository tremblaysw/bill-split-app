package ca.tremblay95.billsplit.presentation.create_split

import androidx.annotation.StringRes
import ca.tremblay95.billsplit.domain.model.Split

data class CreateSplitState(
    val split : Split = Split(),
    val isEntryValid : Boolean = false,
    @StringRes val errorResource : Int? = null
)
