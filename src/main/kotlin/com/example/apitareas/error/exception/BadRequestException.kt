package com.example.apitareas.error.exception

class BadRequestException(message: String) : Exception("Bad Request Exception (400). $message") {
}