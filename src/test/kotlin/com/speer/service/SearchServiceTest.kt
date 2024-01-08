package com.speer.service

import com.nhaarman.mockitokotlin2.whenever
import com.speer.service.auth.AuthDetails
import com.speer.service.model.User
import com.speer.service.model.Notes
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.junit.jupiter.MockitoExtension
import com.speer.service.service.NotesService
import com.speer.service.service.SearchService
import org.mockito.junit.jupiter.MockitoSettings
import org.mockito.quality.Strictness


@ExtendWith(MockitoExtension::class)
@MockitoSettings(strictness = Strictness.LENIENT)
class SearchServiceTest {

    @Mock
    private lateinit var notesService: NotesService

    @Mock
    private lateinit var searchService: SearchService

    @Mock
    private lateinit var authDetails: AuthDetails


    @BeforeEach
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        searchService = SearchService(authDetails, notesService)

    }

    @Test
    fun `test searchNote SearchService method when Notes exists based on query search`() {
        val loggedInUser =
            User(id = 1, name = "Test User", email = "test@gmail.com", password = "password", phone = "9876543210")
        val query = "t"
        val notesList = listOf(Notes(noteId = 1, note = "Note 1"), Notes(noteId = 2, note = "Note 2"))

        whenever(authDetails.getLoggedInUser()).thenReturn(loggedInUser)
        whenever(notesService.searchNote(query)).thenReturn(notesList)

        val result = searchService.searchNotes(query)

        assert(result != null)
        assert(result!!.size == 2)
    }

    @Test
    fun `test searchNote NotesService method when Notes does'nt exists based on query search`() {
        val loggedInUser =
            User(id = 1, name = "Test User", email = "test@gmail.com", password = "password", phone = "9876543210")
        val query = "NP"

        whenever(authDetails.getLoggedInUser()).thenReturn(loggedInUser)
        whenever(notesService.searchNote(query)).thenReturn(null)

        val result = searchService.searchNotes(query)

        assert(result == null)
    }
}