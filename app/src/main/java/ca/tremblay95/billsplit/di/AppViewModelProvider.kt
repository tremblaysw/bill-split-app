package ca.tremblay95.billsplit.di

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import ca.tremblay95.billsplit.BillSplitApplication
import ca.tremblay95.billsplit.presentation.create_split.CreateSplitViewModel
import ca.tremblay95.billsplit.presentation.split_list.SplitListViewModel

object AppViewModelProvider {
    val Factory = viewModelFactory {
        // Initializer for SplitListViewModel
        initializer {
            SplitListViewModel(application().appModule.splitUseCases.getSplitList)
        }

        // Initializer for CreateSplitViewModel
        initializer {
            CreateSplitViewModel(application().appModule.splitUseCases.addSplit)
        }
    }
}

fun CreationExtras.application() : BillSplitApplication =
    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as BillSplitApplication)
