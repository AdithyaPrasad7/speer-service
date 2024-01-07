package com.speer.service.service

import com.speer.service.auth.AuthDetails
import com.speer.service.dto.request.EmailRequest
import com.speer.service.dto.request.NoteRequest
import com.speer.service.dto.response.NotesResponse
import com.speer.service.model.Notes
import com.speer.service.model.User
import com.speer.service.repository.INotesRepository
import org.springframework.stereotype.Service

@Service
class NotesService(
    private val iNotesRepository: INotesRepository,
    private val authDetails: AuthDetails,
    private val userService: IUserService,

    ) : INotesService {

    override fun getAll(): List<Notes>? {
        var user = authDetails.getLoggedInUser()
        return user.userNotes ?: return null

    }

    override fun getNoteById(id: Long): NotesResponse? {
        var user = authDetails.getLoggedInUser()
        try {
            return iNotesRepository.getReferenceById(id).toResponse()
        } catch (e: Exception) {
            return null
        }
    }

    override fun addNote(request: NoteRequest): String {
        var user = authDetails.getLoggedInUser()
        val note = request.toEntity(mutableListOf(user))
        iNotesRepository.save(note)
        return "Note added to user ${user.email}"

    }

    override fun editNote(id: Long, request: NoteRequest): Notes? {
        var user = authDetails.getLoggedInUser()
        try {
            var selectedNote: Notes = iNotesRepository.getReferenceById(id)
            selectedNote.note = request.note
            return iNotesRepository.save(selectedNote)
        } catch (e: Exception) {
            return null
        }
    }

    override fun deleteNote(id: Long): String {
        var user = authDetails.getLoggedInUser()
        if (getNoteById(id) != null) {
            iNotesRepository.deleteById(id)
            return "Note deleted successfully"
        } else {
            return "Note does not exists"
        }
    }

    override fun shareNote(id: Long, request: EmailRequest): String {
        var user = authDetails.getLoggedInUser()
        if (user.email == request.email) {
            return "Sharing Note to Yourself is not possible as Note already exist"
        }
        var anotherUser = userService.getUserByEmail(request.email)
        if (anotherUser.name == "Invalid User")
            return "Invalid User"
        val note = getNoteById(id)?.let { NoteRequest(it.note) }
        if (note != null) {
            var sharedUser = anotherUser
            iNotesRepository.save(note.toEntity(mutableListOf(anotherUser)))
            return "Note added to user ${user.email}"
        } else
            return "Note does not exists for the user ${user.email}"

    }
}

