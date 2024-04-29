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
    formatter.duplicateRemover()
}

class AttendanceFormatter {
    private val finalList = arrayListOf<AttendanceItem>()
    suspend fun format() {


        coroutineScope {
            launch {
                csvReader().open("C:\\Users\\Bharath\\Downloads\\gcc_data_main_output.csv") {
                    readAllAsSequence().forEachIndexed { index, data ->
                        if (index > 0) {
                            val rollNo = data[0]
                            val name = data[1]
                            val batch = data[3]
                            val branch = data[2]
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
        write(finalList)
    }


    suspend fun duplicateRemover() {
        val dupList = arrayListOf<AttendanceItem>()
        coroutineScope {


            launch {
                csvReader().open("C:\\Users\\Bharath\\Desktop\\CsvFormatter\\src\\main\\resources\\output\\gcc_attendance.csv") {
                    readAllAsSequence().forEachIndexed { index, data ->
                        if (index > 0) {
                            val rollNo = data[0]
                            val name = data[1]
                            val batch = data[3]
                            val branch = data[2]
                            val college = data[4]
                            dupList.add(
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
                val distinct = dupList.distinctBy {
                    it.rollNo
                }
                write(distinct)

            }
        }
    }

    private suspend fun write(list: List<AttendanceItem>) {

        coroutineScope {
            launch {
                csvWriter().open(
                    "C:\\Users\\Bharath\\Desktop\\CsvFormatter\\src\\main\\resources\\output\\gcc_attendance.csv",
                ) {
                    writeRow("roll_no", "name", "batch", "branch", "attendance", "college")
                    list.forEach {

                        it.apply {

                            val json = Gson().toJson(arrayOf(attendance))
                            writeRow(
                                rollNo, name, batch, branch, json.toString(), college
                            )
                        }

                    }
                }
            }
        }
    }
}