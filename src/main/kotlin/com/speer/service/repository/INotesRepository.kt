package com.speer.service.repository

import com.speer.service.model.Notes
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface INotesRepository : JpaRepository<Notes, Long> {
    fun findByNoteContaining(keywords: String): List<Notes>?
}