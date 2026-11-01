package com.example.moviereviews.utils

import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonToken
import com.google.gson.stream.JsonWriter

object NumericStringTypeAdapter : TypeAdapter<String>() {
    override fun write(out: JsonWriter, value: String?) {
        out.value(value)
    }

    override fun read(input: JsonReader): String? = when (input.peek()) {
        JsonToken.NULL -> {
            input.nextNull()
            null
        }

        else -> input.nextString()
    }
}
