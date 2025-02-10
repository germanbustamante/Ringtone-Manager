package com.germandebustamante.ringtonemanager.utils.extensions

fun Boolean?.isTrue(): Boolean = this == true
fun String?.isFalse(): Boolean = this.isNullOrBlank() || this != "true"
fun String?.isTrue(): Boolean = this == "true"
fun String.capitalizeFirstLetter(): String = replaceFirstChar { it.uppercase() }