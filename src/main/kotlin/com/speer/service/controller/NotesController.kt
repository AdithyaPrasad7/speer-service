package com.speer.service.controller

import com.speer.service.dto.request.EmailRequest
import com.speer.service.dto.request.NoteRequest
import com.speer.service.dto.response.NotesResponse
import com.speer.service.dto.response.Response
import com.speer.service.dto.response.UserResponse
import com.speer.service.model.Notes
import com.speer.service.service.NotesService
import io.github.resilience4j.ratelimiter.annotation.RateLimiter
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/notes")
class NotesController(private val `notesService`: NotesService) {

    @GetMapping()
    @RateLimiter(name = "Controller", fallbackMethod = "rateLimitFallback")
    fun getAllNotes(): ResponseEntity<Response<List<Notes>?>> {
        val response = notesService.getAll()
        return ResponseEntity.ok(Response(message = "Notes:-", data = response))

    }

    @GetMapping("/{id}")
    @RateLimiter(name = "Controller", fallbackMethod = "rateLimitFallback")
    fun getNoteById(@PathVariable id: Long): ResponseEntity<Response<NotesResponse?>> {
        val response = notesService.getNoteById(id)
        return ResponseEntity.ok(Response(message = "Note:-", data = response))

    }

    @PostMapping()
    @RateLimiter(name = "Controller", fallbackMethod = "rateLimitFallback")
    fun addNote(@RequestBody request: NoteRequest): ResponseEntity<Response<String>> {
        var response = notesService.addNote(request)
        return ResponseEntity.ok(Response(message = response))
    }

    @PutMapping("/{id}")
    @RateLimiter(name = "Controller", fallbackMethod = "rateLimitFallback")
    fun editNote(@PathVariable id: Long, @RequestBody request: NoteRequest): ResponseEntity<Response<Notes>?> {
        var response = notesService.editNote(id, request)
        return ResponseEntity.ok(Response(message = "Note:-", data = response))
    }

    @DeleteMapping("/{id}")
    @RateLimiter(name = "Controller", fallbackMethod = "rateLimitFallback")
    fun editNote(@PathVariable id: Long): ResponseEntity<Response<String>> {
        var response = notesService.deleteNote(id)
        return ResponseEntity.ok(Response(message = response))
    }

    @PostMapping("/{id}/share")
    @RateLimiter(name = "Controller", fallbackMethod = "rateLimitFallback")
    fun shareNote(@PathVariable id: Long, @RequestBody request: EmailRequest): ResponseEntity<Response<String>> {
        var response = notesService.shareNote(id, request)
        return ResponseEntity.ok(Response(message = response))
    }

    fun rateLimitFallback(e: Exception?): ResponseEntity<String> {
        return ResponseEntity("Notes Controller Rate Exceeded", HttpStatus.TOO_MANY_REQUESTS)
    }
}