package com.example.apitareas.error.exception

class UserNotFoundException (message: String) : Exception("User Not Found (404). $message") {
}