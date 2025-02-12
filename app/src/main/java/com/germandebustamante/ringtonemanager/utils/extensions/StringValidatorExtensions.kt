package com.germandebustamante.ringtonemanager.utils.extensions


private const val EMAIL_REGEX = "^[A-Za-z](.*)([@]{1})(.{1,})(\\.)(.{1,})"
private const val PASSWORD_MIN_LENGTH = 8

fun String.isValidEmail(): Boolean = EMAIL_REGEX.toRegex().matches(this)
fun String.isValidPassword(): Boolean = length >= PASSWORD_MIN_LENGTH