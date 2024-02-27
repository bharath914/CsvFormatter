package entity

data class MaterialsDto(
    val college: String,
    val regulation: String,
    val semesters: List<Semester>,
    val university: String
)