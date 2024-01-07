package com.speer.service.service

import com.speer.service.auth.AuthDetails
import com.speer.service.model.Notes
import org.springframework.stereotype.Service

@Service
class SearchService(
    private val authDetails: AuthDetails,
    private val noteService: INotesService,

    ) : ISearchService {
    override fun searchNotes(query: String): List<Notes>? {
        var user = authDetails.getLoggedInUser()
        return noteService.searchNote(query)
    }
}