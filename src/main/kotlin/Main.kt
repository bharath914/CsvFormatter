import com.github.doyaaaaaken.kotlincsv.dsl.csvReader
import com.github.doyaaaaaken.kotlincsv.dsl.csvWriter
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put
import kotlin.math.min


suspend fun main() {
    val format = FormatCsv()
    format.main()


}

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
    val totalCredits: Float = 0.0f,
    val totalSgpa: String,
    val totalPercentage: String,
    val backlogs: Int = 0
)

@Serializable
data class StudentResult(
    val rollNo: String,
    val totalPercentage: Float,
    val totalCgpa: Float,
    val totalBacklogs: Int,
    val semResult: List<SemResult>
)

class FormatCsv {


    suspend fun main() {

        println("Starting work")

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

                        val subjectSet = hashMapOf<String, Result>()
                        val gradeSet = hashMapOf<String, Result>()
                        var totalCreditsXGrade = 0.0f
                        var totalCredits = 0.0f
                        var backlogs = 0
                        semRes.forEachIndexed { index, result ->

                            if (subjectSet.containsKey(result.subjectCode)) {
                                val prev = subjectSet[result.subjectCode]!!
                                val prevGrade = prev.grade
                                val currentGrade = result.grade
                                val bestGrade = compareGrades(prevGrade, currentGrade)


                                if ((prev.grade == "F" && result.grade != "F") || (prev.grade == "ABSENT" && result.grade != "F")) {
                                    backlogs--
                                    subjectSet.replace(result.subjectCode, result)
                                }
                                if (prev.grade != "F" || prev.grade != "COMPLE") {
                                    if (result.grade != "F") {
                                        val prevCredit = prev.credits.toFloat() * getCreditsByGrade(prevGrade).toFloat()
                                        if (bestGrade == result.grade) {
                                            totalCreditsXGrade -= prevCredit
                                            totalCredits -= prev.credits.toFloat()
                                            val curCredit =
                                                result.credits.toFloat() * getCreditsByGrade(grade = result.grade).toFloat()
                                            totalCreditsXGrade += curCredit
                                            totalCredits += result.credits.toFloat()
                                        }
                                    }
                                }

                            } else {
                                subjectSet[result.subjectCode] = result
                                if (result.grade == "F" || result.grade == "ABSENT") backlogs++

                                if (!gradeSet.containsKey(result.subjectCode + result.grade)) {
                                    totalCredits += result.credits.toFloat()
                                    totalCreditsXGrade += (result.credits.toFloat() * getCreditsByGrade(result.grade).toFloat())
                                }
                                gradeSet[result.subjectCode + result.grade] = result
                            }
                        }


                        val sgpa: Float =
                            if (totalCreditsXGrade != 0f && totalCredits != 0f) (totalCreditsXGrade / totalCredits) else 0f
                        var percent = (sgpa - 0.75f) * 10f
                        if (sgpa == 0f) percent = 0f
                        val semRes = SemResult(
                            sem = sem,
                            result = semRes,
                            totalCredits = totalCredits,
                            totalSgpa = "$sgpa",
                            totalPercentage = "$percent",
                            backlogs = backlogs
                        )
                        persemList.add(
                            semRes
                        )

                    }
                    var totalCgpaUpperSum = 0f
                    var totalCreditsOfBtech = 0f
                    var totalBacklogs = 0
                    persemList.forEach {
                        totalCgpaUpperSum += (it.totalSgpa.toFloat() * it.totalCredits)
                        totalCreditsOfBtech += it.totalCredits
                        totalBacklogs += it.backlogs
                    }
                    val cgpa = totalCgpaUpperSum / totalCreditsOfBtech
                    val percentage = (cgpa - 0.75f) * 10f
                    finalResult.add(
                        StudentResult(
                            rollNo,
                            percentage,
                            totalCgpa = cgpa,
                            totalBacklogs,
                            persemList.sortedBy {
                                it.sem
                            }
                        )
                    )
                    println("The roll No is $rollNo and total backlogs is $totalBacklogs")


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
                        println(jsonSemResult)
                    }
                }
            }
        }


//
//    println(grouped.keys.toList())

        // Try adding program arguments via Run/Debug configuration.
        // Learn more about running applications: https://www.jetbrains.com/help/idea/running-applications.html.

    }

    fun getCreditsByGrade(grade: String): Int {
        return when (grade) {
            "A+" -> 10
            "A" -> 9
            "B" -> 8
            "C" -> 7
            "D" -> 6
            "E" -> 5
            else -> 0
        }
    }

    fun compareGrades(grade1: String, grade2: String): String {
        val min = min((grade1.first() - 65).toInt(), (grade2.first() - 65).toInt())
        return "${(min + 65).toChar()}"

    }

    fun sam() {
//
//        val res = SemResult(
//            sem = 12,
//            result = [Result(
//                sem = 12,
//                date = 2022 Aug,
//                rollNo = 216 Q1A4612,
//                subjectCode = R201242,
//                subject = PROBLEM SOLVING USING PYTHON LABORATORY,
//                grade = A +,
//                credits = 1.5,
//                internalMarks = 14
//            ), Result(
//                sem = 12,
//                date = 2022 Aug,
//                rollNo = 216 Q1A4612,
//                subjectCode = R201239,
//                subject = APPLIED CHEMISTRY LABORATORY,
//                grade = A +,
//                credits = 1.5,
//                internalMarks = 14
//            ), Result(
//                sem = 12,
//                date = 2022 Aug,
//                rollNo = 216Q1A4612,
//                subjectCode = R201229,
//                subject = CONSTITUTION OF INDIA,
//                grade = COMPLE,
//                credits = 0,
//                internalMarks = 0
//            ), Result(
//                sem = 12,
//                date = 2022 Aug,
//                rollNo = 216Q1A4612,
//                subjectCode = R201221,
//                subject = DIGITAL LOGIC DESIGN,
//                grade = B,
//                credits = 3,
//                internalMarks = 24
//            ), Result(
//                sem = 12,
//                date = 2022 Aug,
//                rollNo = 216 Q1A4612,
//                subjectCode = R201220,
//                subject = BASIC ELECTRICAL & ELECTRONICS ENGINEERI,
//                grade = D,
//                credits = 3,
//                internalMarks = 23
//            ), Result(
//                sem = 12,
//                date = 2022 Aug,
//                rollNo = 216 Q1A4612,
//                subjectCode = R201201,
//                subject = MATHEMATICS - II,
//                grade = D,
//                credits = 3,
//                internalMarks = 22
//            ), Result(
//                sem = 12,
//                date = 2022 Aug,
//                rollNo = 216 Q1A4612,
//                subjectCode = R201215,
//                subject = APPLIED CHEMISTRY,
//                grade = F,
//                credits = 0,
//                internalMarks = 24
//            ), Result(
//                sem = 12,
//                date = 2022 Aug,
//                rollNo = 216 Q1A4612,
//                subjectCode = R201243,
//                subject = DIGITAL LOGIC DESIGN LABORATORY,
//                grade = A +,
//                credits = 1.5,
//                internalMarks = 15
//            ), Result(
//                sem = 12,
//                date = 2022 Aug,
//                rollNo = 216 Q1A4612,
//                subjectCode = R201219,
//                subject = PROBLEM SOLVING USING PYTHON,
//                grade = D,
//                credits = 3,
//                internalMarks = 22
//            ), Result(
//                sem = 12,
//                date = 2022 Aug,
//                rollNo = 216 Q1A4612,
//                subjectCode = R201215,
//                subject = APPLIED CHEMISTRY,
//                grade = D,
//                credits = 3,
//                internalMarks = 24
//            ), Result(
//                sem = 12,
//                date = 2022 Aug,
//                rollNo = 216 Q1A4612,
//                subjectCode = R201215,
//                subject = APPLIED CHEMISTRY,
//                grade = D,
//                credits = 3,
//                internalMarks = 24
//            )],
//            totalCredits = 25.5,
//            totalSgpa = 6.9411764,
//            totalPercentage = 61.911766,
//            backlogs = -1
//        )
    }

}