package attendance

import com.github.doyaaaaaken.kotlincsv.dsl.csvReader
import com.github.doyaaaaaken.kotlincsv.dsl.csvWriter
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

data class AttendanceItem(
    val rollNo: String,
    val name: String,
    val batch: String,
    val branch: String,
    val attendance: String
)


suspend fun main() {
    val formatter = AttendanceFormatter()
    formatter.format()
}

class AttendanceFormatter {
    private val finalList = arrayListOf<AttendanceItem>()
    suspend fun format() {


        coroutineScope {
            launch {
                csvReader().open("C:\\Users\\Bharath\\Desktop\\CsvFormatter\\src\\main\\resources\\output\\formattedStudentsList.csv") {
                    readAllAsSequence().forEach { data ->
                        val rollNo = data[0]
                        val name = data[1]
                        val batch = data[6]
                        val branch = data[3]
                        val attendance = "[]"
                        finalList.add(
                            AttendanceItem(
                                rollNo = rollNo, name = name, batch = batch, branch = branch, attendance = attendance
                            )
                        )
                    }
                }
            }
        }
        write()
    }

    private suspend fun write() {

        coroutineScope {
            launch {
                csvWriter().open("C:\\Users\\Bharath\\Desktop\\CsvFormatter\\src\\main\\resources\\output\\Attendance21_25Data.csv") {
                    writeRow("roll_no", "name", "batch", "branch", "attendance")
                    finalList.forEach {
                        it.apply {
                            writeRow(
                                rollNo, name, batch, branch, attendance
                            )
                        }

                    }
                }
            }
        }
    }
}