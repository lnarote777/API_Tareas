package com.example.apitareas.error.exception

class BadRequestException(message: String) : Exception("Not authorized exception (401). $message") {
}