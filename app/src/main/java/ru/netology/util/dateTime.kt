package ru.netology.util

import java.text.SimpleDateFormat
import java.util.Date


fun getDateTime(): String {
    val sdf = SimpleDateFormat("dd/M/yyyy hh:mm:ss")
    return sdf.format(Date())
}