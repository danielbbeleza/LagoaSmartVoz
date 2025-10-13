package com.lagoasmartvoz.app

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform