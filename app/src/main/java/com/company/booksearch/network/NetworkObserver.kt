package com.company.booksearch.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.State
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class NetworkObserver(context: Context) {

    private val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    private val _isConnected = mutableStateOf(true)
    val isConnected: State<Boolean> = _isConnected

    init {
        val networkRequest = NetworkRequest.Builder()
            .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            .build()

        connectivityManager.registerNetworkCallback(networkRequest, object :
            ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) {
                CoroutineScope(Dispatchers.Main).launch {
                    _isConnected.value = true
                }
            }
            override fun onLost(network: Network) {
                CoroutineScope(Dispatchers.Main).launch {
                    _isConnected.value = false
                }
            }
        })

        // Initial network check
        CoroutineScope(Dispatchers.Main).launch {
            _isConnected.value = isNetworkAvailable()
        }
    }

    private suspend fun isNetworkAvailable(): Boolean {
        return withContext(Dispatchers.IO) {
            val activeNetwork = connectivityManager.activeNetwork ?: return@withContext false
            val capabilities =
                connectivityManager.getNetworkCapabilities(activeNetwork) ?: return@withContext false
            return@withContext capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
        }
    }
}
