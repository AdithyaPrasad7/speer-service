package com.speer.service.controller

import com.speer.service.dto.response.Response
import com.speer.service.model.Notes
import com.speer.service.service.NotesService
import com.speer.service.service.SearchService
import io.github.resilience4j.ratelimiter.annotation.RateLimiter
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/search")
class SearchController(private val `searchService`: SearchService) {
    @GetMapping()
    @RateLimiter(name = "Controller", fallbackMethod = "rateLimitFallback")
    fun searchNotes(@RequestParam("q") query: String): ResponseEntity<Response<List<Notes>?>> {
        val response = searchService.searchNotes(query)
        return ResponseEntity.ok(Response(message = "Note:-", data = response))
    }

    fun rateLimitFallback(e: Exception?): ResponseEntity<String> {
        return ResponseEntity("Search Controller Rate Exceeded", HttpStatus.TOO_MANY_REQUESTS)
    }
}