package com.fp.data

import java.io.IOException

class ApiException(
    val statusCode: Int,
    val errorMessage: String
) : IOException("Error $statusCode: $errorMessage")