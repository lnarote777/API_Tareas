package com.example.apitareas.error.exception

class UserExistException (message: String) : Exception("User Exist (400). $message") {
}