package com.example.apitareas.error.exception

class NotFoundException  (message: String) : Exception("Not Found (404). $message") {
}