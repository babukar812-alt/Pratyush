package com.example.model

/**
 * Clean data models representing the official NCERT curriculum hierarchy.
 */
data class Chapter(
    val id: String,
    val chapterNumber: Int,
    val name: String,
    val classLevel: String,
    val subject: String,
    val syllabusStatus: String = "Official NCERT",
    val keyConcepts: List<String> = emptyList()
)

data class SubjectCurriculum(
    val id: String,
    val name: String,
    val iconKey: String,
    val chapters: List<Chapter>
)

data class ClassCurriculum(
    val classLevel: String,
    val displayName: String,
    val subjects: List<SubjectCurriculum>
)
