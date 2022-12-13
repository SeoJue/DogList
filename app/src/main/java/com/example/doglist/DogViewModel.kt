package com.example.doglist

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.ImageLoader
import com.android.volley.toolbox.JsonArrayRequest
import org.json.JSONArray
import org.json.JSONObject
import java.net.URLEncoder

class DogViewModel(application:Application) : AndroidViewModel(application) {
    data class Dog (var id: Int, var breed: String, var size: String, var coat:String, var image: String)

    companion object {
        const val QUEUE_TAG = "VolleyRequestDog"
        const val SERVER_URL = "https://dogdb-ylaoo.run.goorm.io"
    }

    private val dogs = ArrayList<Dog>()
    private val _list = MutableLiveData<ArrayList<Dog>>()
    val list: LiveData<ArrayList<Dog>>
        get() = _list

    private val queue : RequestQueue
    val imageLoader: ImageLoader

    init {
        _list.value = dogs
        queue = MySingleton.getInstance(getApplication()).requestQueue
        imageLoader = MySingleton.getInstance(getApplication()).imageLoader
    }

    fun getImageUrl(i: Int): String = "$SERVER_URL/image/" + URLEncoder.encode(dogs[i].image, "utf-8")

    fun requestDog() {
        val url = "$SERVER_URL"
        val request = JsonArrayRequest(
            Request.Method.GET,
            url,
            null,
            {
                dogs.clear()
                parseJson(it)
                _list.value=dogs
            },
            {
                Toast.makeText(getApplication(), it.toString(), Toast.LENGTH_LONG).show()
            }
        )
        request.tag = QUEUE_TAG
        queue.add(request)
    }

    private fun parseJson(items: JSONArray) {
        for (i in 0 until items.length()) {
            val item: JSONObject = items[i] as JSONObject
            val id = item.getInt("id")
            val breed = item.getString("breed")
            val size = item.getString("size")
            val coat = item.getString("coat")
            val image = item.getString("image")
            dogs.add(Dog(id, breed, size, coat, image))
        }
    }

    override fun onCleared() {
        super.onCleared()
        queue.cancelAll(QUEUE_TAG)
    }
}