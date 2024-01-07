package com.speer.service.model

import com.fasterxml.jackson.annotation.JsonIgnore
import com.speer.service.dto.response.NotesResponse
import jakarta.persistence.*


@Entity
@Table(name = "notes")
data class Notes(
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val noteId: Long? = null,

    @Column(name = "note")
    var note: String,

    @ManyToMany(fetch = FetchType.LAZY)
    @JsonIgnore
    @JoinTable(
        name = "user_notes",
        joinColumns = [JoinColumn(name = "note_id")],
        inverseJoinColumns = [JoinColumn(name = "user_id")]
    )
    val users: MutableList<User> = mutableListOf<User>()
) {
    fun toResponse(): NotesResponse {
        return NotesResponse(note = note)
    }
}
