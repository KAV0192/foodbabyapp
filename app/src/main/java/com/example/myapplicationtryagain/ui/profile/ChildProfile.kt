package com.example.myapplicationtryagain.ui.profile

import kotlinx.serialization.Serializable

@Serializable
data class ChildProfile(
    val id: String = "",
    val name: String = "",
    val birthDate: String = "",
    val gender: String = "",
    val weight: String = "",
    val height: String = "",
    val avatarUrl: String = "",
    val allergies: List<String> = emptyList(),
    val notes: String = ""
)
