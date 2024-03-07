package entity.attendance

data class AttendanceITe(
    val attendance: List<Attendance>,
    val batch: String,
    val branch: String,
    val name: String,
    val roll_no: String
)