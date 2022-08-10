package com.garibyan.armen.tbc_classwork_5.nodel.network

import com.squareup.moshi.Json

data class ViewItem(
    @Json(name = "field_id")
    val fieldId: Int? = null,
    val hint: String? = null,
    @Json(name = "field_type")
    val fieldType: String? = null,
    val keyboard: String? = null,
    val required: Any? = null,
    @Json(name = "is_active")
    val isActive: Any? = null,
    val icon: String? = null,
)
