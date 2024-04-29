package StudentsListMaker

import com.github.doyaaaaaken.kotlincsv.dsl.csvReader
import com.github.doyaaaaaken.kotlincsv.dsl.csvWriter
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.serialization.SerialName

suspend fun main() {
    val maker = StudentsListMaker()
    maker.format()
}

data class Student(
    val roll_no: String,

    @SerialName("name")
    val name: String,

    @SerialName("password")
    val password: String,

    @SerialName("branch")
    val branch: String,


    @SerialName("campus_code")
    val campus_code: String,


    @SerialName("profile_image")
    val profile_image: String,

    @SerialName("batch")
    val batch: String,


    @SerialName("phone_num")
    val phone_num: String,

    @SerialName("email")
    val email: String,
    @SerialName("regulation")
    val regulation: String,
)

class StudentsListMaker {
    private val studentsList = arrayListOf<Student>()
    suspend fun format() {
        csvReader().open("C:\\Users\\Bharath\\Desktop\\CsvFormatter\\src\\main\\resources\\output\\formatted21_25Data.csv") {
            readAllAsSequence().forEachIndexed { index, strings ->
                if (index > 0) {
                    studentsList.add(
                        Student(
                            roll_no = strings[1],
                            name = strings[5] + " " + strings[6],
                            password = strings[1],
                            branch = strings[3],
                            campus_code = if (strings[2] == "KIET+") "KIEK" else if (strings[2] == "KIEW") "KIEW" else "KIET",
                            profile_image = strings[7],
                            batch = strings[9],
                            phone_num = strings[4],
                            email = strings[11],
                            regulation = "R20"
                        )
                    )
                }
            }
        }
        coroutineScope {
            launch {
                write()
            }
        }
    }

    fun write() {
        studentsList.forEach {
            csvWriter().open("C:\\Users\\Bharath\\Desktop\\CsvFormatter\\src\\main\\resources\\output\\formattedStudentsList.csv") {
                writeRow(
                    "roll_no",
                    "name",
                    "password",
                    "branch",
                    "campus_code",
                    "profile_image",
                    "batch",
                    "phone_num",
                    "email",
                    "regulation"
                )
                studentsList.forEach {
                    it.apply {
                        writeRow(
                            roll_no,
                            name,
                            password,
                            branch,
                            campus_code,
                            profile_image,
                            batch,
                            phone_num,
                            email,
                            regulation
                        )
                    }
                }
            }

        }
    }

}