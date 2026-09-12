package com.example.data

/**
 * NCERT Data API gateway delegating directly to structured CurriculumData.
 */
object NCERTData {
    val classes: List<String> get() = CurriculumData.getClasses()

    fun getSubjects(classLevel: String): List<String> {
        return CurriculumData.getSubjects(classLevel)
    }

    // Default subjects for fallback
    val subjects: List<String>
        get() = CurriculumData.getSubjects("10")

    fun getChapters(classLevel: String, subject: String): List<String> {
        return CurriculumData.getChapterNames(classLevel, subject, includeAll = true)
    }

    fun getChapterCount(classLevel: String, subject: String): Int {
        return CurriculumData.getChapterCount(classLevel, subject)
    }
}
