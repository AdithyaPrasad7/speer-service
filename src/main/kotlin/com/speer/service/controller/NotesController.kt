package com.speer.service.controller

import com.speer.service.dto.request.EmailRequest
import com.speer.service.dto.request.NoteRequest
import com.speer.service.dto.response.NotesResponse
import com.speer.service.dto.response.Response
import com.speer.service.dto.response.UserResponse
import com.speer.service.model.Notes
import com.speer.service.service.NotesService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/notes")
class NotesController(private val `notesService`: NotesService) {

    @GetMapping()
    fun getAllNotes(): ResponseEntity<Response<List<Notes>?>> {
        val response = notesService.getAll()
        return ResponseEntity.ok(Response(message = "Notes:-", data = response))

    }

    @GetMapping("/{id}")
    fun getNoteById(@PathVariable id: Long): ResponseEntity<Response<NotesResponse?>> {
        val response = notesService.getNoteById(id)
        return ResponseEntity.ok(Response(message = "Note:-", data = response))

    }

    @PostMapping()
    fun addNote(@RequestBody request: NoteRequest): ResponseEntity<Response<String>> {
        var response = notesService.addNote(request)
        return ResponseEntity.ok(Response(message = response))
    }

    @PutMapping("/{id}")
    fun editNote(@PathVariable id: Long, @RequestBody request: NoteRequest): ResponseEntity<Response<Notes>?> {
        var response = notesService.editNote(id, request)
        return ResponseEntity.ok(Response(message = "Note:-", data = response))
    }

    @DeleteMapping("/{id}")
    fun editNote(@PathVariable id: Long): ResponseEntity<Response<String>> {
        var response = notesService.deleteNote(id)
        return ResponseEntity.ok(Response(message = response))
    }

    @PostMapping("/{id}/share")
    fun shareNote(@PathVariable id: Long, @RequestBody request: EmailRequest): ResponseEntity<Response<String>> {
        var response = notesService.shareNote(id, request)
        return ResponseEntity.ok(Response(message = response))
    }
}