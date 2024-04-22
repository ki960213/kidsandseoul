package com.ki960213.kidsandseoul.presentation.common.extension

import androidx.navigation.NavController
import androidx.navigation.NavDirections

fun NavController.navigateSafely(directions: NavDirections) = runCatching { navigate(directions) }
