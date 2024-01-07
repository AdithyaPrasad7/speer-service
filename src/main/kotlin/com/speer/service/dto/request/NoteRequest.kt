package com.speer.service.dto.request

import com.speer.service.model.Notes
import com.speer.service.model.User

data class NoteRequest(
    val note: String
) {
    fun toEntity(users: MutableList<User>): Notes {
        return Notes(note = note, users = users)
    }
}
