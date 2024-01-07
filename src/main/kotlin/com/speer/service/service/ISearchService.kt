package com.speer.service.service

import com.speer.service.model.Notes

interface ISearchService {

    fun searchNotes(query: String): List<Notes>?
}