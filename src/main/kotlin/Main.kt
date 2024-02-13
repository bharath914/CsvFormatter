import com.github.doyaaaaaken.kotlincsv.dsl.csvReader
import com.github.doyaaaaaken.kotlincsv.dsl.csvWriter
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable
import java.io.File

@Serializable
data class Result(

    val sem: String,
    val date: String,
    val rollNo: String,
    val subjectCode: String,
    val subject: String,
    val grade: String,
    val credits: String,
    val internalMarks: String,


    )

@Serializable
data class SemResult(

    val sem: String,
    val result: List<Result>,

    )

@Serializable
data class StudentResult(
    val rollNo: String,
    val totalPercentage: String,
    val totalCgpa: String,
    val totalBacklogs: String,
    val semResult: List<SemResult>
)

suspend fun main(args: Array<String>) {


    val list = arrayListOf<Result>()
    val finalResult = arrayListOf<StudentResult>()
    csvReader().open("C:\\Users\\Bharath\\Desktop\\CsvFormatter\\src\\main\\resources\\results_upto22Csc.csv") {
        readAllAsSequence().forEachIndexed { ind: Int, row: List<String> ->
            if (ind != 0) {
                list.add(
                    Result(
                        row[0],
                        row[1],
                        row[2],
                        row[3],
                        row[4],
                        row[5],
                        row[6],
                        row[7]
                    )
                )
            }
        }
    }
    val grouped = list.groupBy {
        it.rollNo
    }

    val jsonList = arrayListOf<String>()
    val semResultList = arrayListOf<SemResult>()
    var index = 0;
    coroutineScope {
        this.launch {
            grouped.forEach { (rollNo, result) ->
                val group = result.groupBy {
                    it.sem
                }
                val persemList = arrayListOf<SemResult>()
                group.forEach { (sem, semRes) ->
                    persemList.add(SemResult(sem, semRes))
                }

                finalResult.add(
                    StudentResult(
                        rollNo,
                        "60%",
                        totalCgpa = "6.46",
                        "2",
                        persemList.sortedBy {
                            it.sem
                        }
                    )
                )


            }
        }
    }
    coroutineScope {
        this.launch(IO) {
            csvWriter().open("C:\\Users\\Bharath\\Desktop\\CsvFormatter\\src\\main\\resources\\SemResultCSC.csv") {
                writeRow("rollNo", "totalPercentage", "totalCgpa", "totalBacklogs", "semResult")
                finalResult.forEachIndexed { index, studentResult ->
                    val jsonSemResult = Gson().toJson(studentResult.semResult)
                    writeRow(
                        studentResult.rollNo,
                        studentResult.totalPercentage,
                        studentResult.totalCgpa,
                        studentResult.totalBacklogs,
                        jsonSemResult
                    )
                }
            }
        }
    }

    coroutineScope {
        this.launch {

        }
    }


//
//    println(grouped.keys.toList())

    // Try adding program arguments via Run/Debug configuration.
    // Learn more about running applications: https://www.jetbrains.com/help/idea/running-applications.html.

}