package com.speer.service

import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.whenever
import com.speer.service.auth.AuthDetails
import com.speer.service.auth.JwtTokenUtil
import com.speer.service.dto.request.NoteRequest
import com.speer.service.dto.request.EmailRequest
import com.speer.service.model.User
import com.speer.service.model.Notes
import com.speer.service.service.UserService
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.junit.jupiter.MockitoExtension
import com.speer.service.repository.INotesRepository
import com.speer.service.service.NotesService
import org.mockito.Mockito.*
import org.mockito.junit.jupiter.MockitoSettings
import org.mockito.quality.Strictness
import org.springframework.security.authentication.AuthenticationManager


@ExtendWith(MockitoExtension::class)
@MockitoSettings(strictness = Strictness.LENIENT)
class NotesServiceTest {
    @Mock
    private lateinit var iNotesRepository: INotesRepository

    @Mock
    private lateinit var authManager: AuthenticationManager

    @Mock
    private lateinit var jwtUtil: JwtTokenUtil

    @Mock
    private lateinit var notesService: NotesService

    @Mock
    private lateinit var userService: UserService

    @Mock
    private lateinit var authDetails: AuthDetails


    @BeforeEach
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        notesService = NotesService(iNotesRepository, authDetails, userService)

    }

    @Test
    fun `test getAll NotesService method when userNotes are not null`() {
        val loggedInUser = User(
            id = 1,
            name = "Test User",
            email = "test@gmail.com",
            password = "password",
            phone = "9876543210",
            userNotes = mutableListOf(Notes(noteId = 1, note = "Note 1"), Notes(noteId = 2, note = "Note 2"))
        )
        whenever(authDetails.getLoggedInUser()).thenReturn(loggedInUser)

        val result = notesService.getAll()

        assert(result != null)
        assert(result!!.size == 2)
    }

    @Test
    fun `test getAll NotesService method when userNotes are null`() {
        val loggedInUser = User(
            id = 1,
            name = "Test User",
            email = "test@gmail.com",
            password = "password",
            phone = "9876543210",
            userNotes = null
        )
        whenever(authDetails.getLoggedInUser()).thenReturn(loggedInUser)

        val result = notesService.getAll()

        assert(result == null)
    }

    @Test
    fun `test getNoteById NotesService method when note with requested id is present`() {
        val loggedInUser = User(
            id = 1,
            name = "Test User",
            email = "test@gmail.com",
            password = "password",
            phone = "9876543210",
            userNotes = mutableListOf(Notes(noteId = 1, note = "Note 1"), Notes(noteId = 2, note = "Note 2"))
        )
        whenever(authDetails.getLoggedInUser()).thenReturn(loggedInUser)
        whenever(iNotesRepository.getReferenceById(1)).thenReturn(Notes(noteId = 1, note = "Note 1"))

        val result = notesService.getNoteById(1)

        assert(result != null)
        if (result != null) {
            assert(result.note == "Note 1")
        }
    }

    @Test
    fun `test getNoteById NotesService method when note with requested id is not present`() {
        val loggedInUser = User(
            id = 1,
            name = "Test User",
            email = "test@gmail.com",
            password = "password",
            phone = "9876543210",
            userNotes = mutableListOf(Notes(noteId = 1, note = "Note 1"), Notes(noteId = 2, note = "Note 2"))
        )
        whenever(authDetails.getLoggedInUser()).thenReturn(loggedInUser)
        whenever(iNotesRepository.getReferenceById(3)).thenThrow(RuntimeException("Note not found"))

        val result = notesService.getNoteById(3)

        assert(result == null)
    }

    @Test
    fun `test addNote NotesService method`() {
        val loggedInUser = User(
            id = 1,
            name = "Test User",
            email = "test@gmail.com",
            password = "password",
            phone = "9876543210",
            userNotes = mutableListOf()
        )
        val noteRequest = NoteRequest(note = "Add Test Note")
        whenever(authDetails.getLoggedInUser()).thenReturn(loggedInUser)

        val result = notesService.addNote(noteRequest)


        assert(result == "Note added to user ${loggedInUser.email}")
        verify(iNotesRepository).save(any(Notes::class.java))
    }

    @Test
    fun `test editNote NotesService method when note is present and with proper request`() {
        // Arrange
        val loggedInUser = User(
            id = 1,
            name = "Test User",
            email = "test@gmail.com",
            password = "password",
            phone = "9876543210",
            userNotes = mutableListOf(Notes(noteId = 1, note = "Note 1"), Notes(noteId = 2, note = "Note 2"))
        )
        val noteId: Long = 1
        val request = NoteRequest(note = "Updated Note 1")
        whenever(authDetails.getLoggedInUser()).thenReturn(loggedInUser)
        whenever(iNotesRepository.getReferenceById(noteId)).thenReturn(Notes(noteId = noteId, note = "Note 1"))
        whenever(iNotesRepository.save(loggedInUser.userNotes!![0])).thenReturn(
            Notes(
                noteId = noteId,
                note = "Updated Note 1"
            )
        )


        val result = notesService.editNote(noteId, request)


        if (result != null) {
            assert(result.note != null)
            assert(result.note == "Updated Note 1")
        }

    }

    @Test
    fun `test editNote NotesService method when note is not present`() {
        val loggedInUser = User(
            id = 1,
            name = "Test User",
            email = "test@gmail.com",
            password = "password",
            phone = "9876543210",
            userNotes = mutableListOf(Notes(noteId = 1, note = "Note 1"))
        )
        val nonExsistingNoteId: Long = 2
        val request = NoteRequest(note = "Updated Note")

        whenever(authDetails.getLoggedInUser()).thenReturn(loggedInUser)
        whenever(iNotesRepository.getReferenceById(nonExsistingNoteId)).thenReturn(null)

        val result = notesService.editNote(nonExsistingNoteId, request)

        assert(result == null)
    }

    @Test
    fun `test deleteNote NotesService method when note is present for a loggedUser`() {
        val loggedInUser = User(
            id = 1,
            name = "Test User",
            email = "test@gmail.com",
            password = "password",
            phone = "9876543210",
            userNotes = mutableListOf(Notes(noteId = 1, note = "Note 1"))
        )
        val note: Notes = loggedInUser.userNotes?.get(0)!!
        whenever(authDetails.getLoggedInUser()).thenReturn(loggedInUser)
        whenever(iNotesRepository.deleteById(note.noteId!!)).then { }
        whenever(iNotesRepository.getReferenceById(1)).thenReturn(note)
        val result = notesService.deleteNote(note.noteId!!)

        assert(result == "Note deleted successfully")
    }

    @Test
    fun `test deleteNote NotesService method when note is not present for a loggedUser`() {
        val loggedInUser = User(
            id = 1,
            name = "Test User",
            email = "test@gmail.com",
            password = "password",
            phone = "9876543210",
            userNotes = mutableListOf(Notes(noteId = 1, note = "Note 1"))
        )
        val noteId: Long = 100
        whenever(iNotesRepository.getReferenceById(1)).thenReturn(null)
        whenever(authDetails.getLoggedInUser()).thenReturn(loggedInUser)
        whenever(iNotesRepository.deleteById(noteId)).then { }

        val result = notesService.deleteNote(noteId)

        assert(result == "Note does not exists")
    }

    @Test
    fun `test shareNote NotesService method when there exists valid sharing`() {
        val loggedInUser =
            User(
                id = 1,
                name = "Test User",
                email = "test@gmail.com",
                phone = "9876543210",
                password = "password",
                userNotes = mutableListOf(Notes(noteId = 1, note = "Note 1"), Notes(noteId = 2, note = "Note 2"))
            )
        val anotherUser = User(
            id = 2,
            name = "Another User",
            email = "another@gmail.com",
            phone = "9876543211",
            password = "password"
        )
        val note: Notes = loggedInUser.userNotes?.get(0)!!
        val noteRequest = NoteRequest(note = note.note)
        val emailRequest = EmailRequest(email = anotherUser.email)
        whenever(iNotesRepository.getReferenceById(1)).thenReturn(note)
        whenever(authDetails.getLoggedInUser()).thenReturn(loggedInUser)
        whenever(userService.getUserByEmail(emailRequest.email)).thenReturn(anotherUser)
        whenever(iNotesRepository.save(noteRequest.toEntity(mutableListOf(anotherUser)))).thenReturn(
            Notes(
                noteId = note.noteId!!,
                note = "Shared Note by loggeduser"
            )
        )

        val result = notesService.shareNote(note.noteId!!, emailRequest)
        println("$result")
        assert(result == "Note added to user ${loggedInUser.email}")
    }

    @Test
    fun `test shareNote NotesService method when User sharing to theirself`() {
        val loggedInUser =
            User(id = 1, name = "Test User", email = "test@gmail.com", phone = "9876543210", password = "password")
        val noteId: Long = 1
        val emailRequest = EmailRequest(email = loggedInUser.email)

        whenever(authDetails.getLoggedInUser()).thenReturn(loggedInUser)

        val result = notesService.shareNote(noteId, emailRequest)

        assert(result == "Sharing Note to Yourself is not possible as Note already exist")
    }

    @Test
    fun `test shareNote NotesService method when User does't exists`() {
        val loggedInUser =
            User(id = 1, name = "Test User", email = "test@gmail.com", phone = "9876543210", password = "password")
        val invalidUser = User(
            id = null,
            name = "Invalid User",
            email = "invalid@gmail.com",
            phone = "9876543211",
            password = "password"
        )
        val noteId: Long = 1
        val emailRequest = EmailRequest(email = invalidUser.email)

        whenever(authDetails.getLoggedInUser()).thenReturn(loggedInUser)
        whenever(userService.getUserByEmail(emailRequest.email)).thenReturn(invalidUser)

        val result = notesService.shareNote(noteId, emailRequest)

        assert(result == "Invalid User") { "Expected error message, but got $result" }
    }

    @Test
    fun `test shareNote NotesService method while non-existent note`() {
        val loggedInUser =
            User(id = 1, name = "Test User", email = "test@gmail.com", phone = "9876543210", password = "password")
        val anotherUser = User(
            id = 2,
            name = "Another User",
            email = "another@gmail.com",
            phone = "9876543211",
            password = "password"
        )
        val loggedInUserNoteId: Long = 100
        val emailRequest = EmailRequest(email = anotherUser.email)

        whenever(authDetails.getLoggedInUser()).thenReturn(loggedInUser)
        whenever(userService.getUserByEmail(emailRequest.email)).thenReturn(anotherUser)
        whenever(notesService.getNoteById(loggedInUserNoteId)).thenReturn(null)

        val result = notesService.shareNote(loggedInUserNoteId, emailRequest)
        println("$result")

        assert(result == "Note does not exists for the user ${loggedInUser.email}")
    }

}