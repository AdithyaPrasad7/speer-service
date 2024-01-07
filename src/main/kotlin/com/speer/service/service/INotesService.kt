package com.speer.service.service

import com.speer.service.dto.request.EmailRequest
import com.speer.service.dto.request.NoteRequest
import com.speer.service.dto.response.NotesResponse
import com.speer.service.dto.response.UserResponse
import com.speer.service.model.Notes

interface INotesService {
    fun getAll(): List<Notes>?
    fun getNoteById(id: Long): NotesResponse?
    fun addNote(request: NoteRequest): String
    fun editNote(id: Long, request: NoteRequest): Notes?
    fun deleteNote(id: Long): String
    fun shareNote(id: Long, request: EmailRequest): String
}