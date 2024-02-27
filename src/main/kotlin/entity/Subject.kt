package entity

data class Subject(
    val commonFor: String,
    val name: String,
    val pyqs: List<Pyq>,
    val subjectCode: String,
    val units: List<Unit>
)