package attendance

import com.github.doyaaaaaken.kotlincsv.dsl.csvReader
import com.github.doyaaaaaken.kotlincsv.dsl.csvWriter
import com.google.gson.Gson
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable

data class AttendanceItem(
    val rollNo: String,
    val name: String,
    val batch: String,
    val branch: String,

    val attendance: DayAttendance,
    val college: String
)

@Serializable
data class DayAttendance(
    val date: String,
    val present: String,
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
                    readAllAsSequence().forEachIndexed { index, data ->
                        if (index > 0) {
                            val rollNo = data[0]
                            val name = data[1]
                            val batch = data[6]
                            val branch = data[3]
                            val college = data[4]
                            finalList.add(
                                AttendanceItem(
                                    rollNo = rollNo,
                                    name = name,
                                    batch = batch,
                                    branch = branch,
                                    attendance = DayAttendance(
                                        date = "01-02-2024",
                                        present = "P"
                                    ),
                                    college = college

                                )
                            )
                        }
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
                    writeRow("roll_no", "name", "batch", "branch", "attendance","college")
                    finalList.forEach {

                        it.apply {

                            val json = Gson().toJson(arrayOf(attendance))
                            writeRow(
                                rollNo, name, batch, branch, json.toString(),college
                            )
                        }

                    }
                }
            }
        }
    }
}