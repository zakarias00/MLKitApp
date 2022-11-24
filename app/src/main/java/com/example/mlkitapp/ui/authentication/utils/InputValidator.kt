package com.example.mlkitapp.ui.authentication.utils

private const val EMAIL_MESSAGE = "Please enter valid email address"
private const val EMPTY_EMAIL_MESSAGE = "Email address is required"
private const val PASSWORD_MESSAGE = "Password must contain at least 8 characters"
private const val EMPTY_PASSWORD_MESSAGE = "Password is required"

sealed interface InputValidator
open class Email(var message: String = EMAIL_MESSAGE): InputValidator
open class EmptyEmail(var message: String = EMPTY_EMAIL_MESSAGE): InputValidator
open class Password(var message: String = PASSWORD_MESSAGE): InputValidator
open class EmptyPassword(var message: String = EMPTY_PASSWORD_MESSAGE): InputValidator