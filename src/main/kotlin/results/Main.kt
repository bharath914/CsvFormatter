@file:Suppress("KDocUnresolvedReference")

package results

import com.github.doyaaaaaken.kotlincsv.dsl.csvReader
import com.github.doyaaaaaken.kotlincsv.dsl.csvWriter
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable
import kotlin.math.min


suspend fun main() {
    val format = FormatCsv()
    format.main()


}


/**
 * @property Result - Wraps the result of each subject
 * @param sem : Semester
 * @param date : Date of the exam
 * @param rollNo : Roll no of the student
 * @param subjectCode : Subject code
 * @param subject : Name of the subject
 * @param credits : Total Credits per subject
 * @param internalMarks : Total Internal Marks per subject
 */

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

/**
 * @property SemResult : Result of Each Semester
 * @param result : List of results in each sem
 * @param totalCredits : Total Credits In Semester
 * @param totalSgpa : Total SGPA Per sem
 * @param totalPercentage : Total Percentage
 * @param backlogs : no of backlogs at each semester.
 */
@Serializable
data class SemResult(

    val sem: String,
    val result: List<Result>,
    val totalCredits: Float = 0.0f,
    val totalSgpa: String,
    val totalPercentage: String,
    val backlogs: Int = 0
)

/**
 * @property StudentResult : Wraps The Total Result in this data class
 * @param rollNo : Roll No of the student
 * @param totalPercentage : Total Semester Percentage
 * @param totalBacklogs : Total Backlogs
 * @param semResult : List of each semester result.
 */

@Serializable
data class StudentResult(
    val rollNo: String,
    val totalPercentage: Float,
    val totalCgpa: Float,
    val totalBacklogs: Int,
    val semResult: List<SemResult>
)


/**
 * @property FormatCsv : CSV Formatter
 * @property main() : main method
 */
class FormatCsv {
    /**
     * @param list : stores the data from the csv file to this list
     * @param finalResult : Final Result that will be written to new csv file
     */

    private val list = arrayListOf<Result>()
    private val finalResult = arrayListOf<StudentResult>()


   suspend fun main() {

        println("Starting work")

        /**
         * @param csvReader().open() : Opens the CSV Files from the resources
         */
        csvReader().open("C:\\Users\\Bharath\\Desktop\\CsvFormatter\\src\\main\\resources\\assests\\ResultsUpto22.csv") {
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
        /**
         * @param grouped : Map of RollNo's and students Results
         */
        val grouped = list.groupBy {
            it.rollNo
        }


        /**
         * Launch a Coroutine Scope for faster execution of this code.
         */
        coroutineScope {
            this.launch {


        /**
         * Iterate Through Each Sem Result of Every student
         */

        grouped.forEach { (rollNo, result) ->

            /**
             * @param group : results are stored normally so convert them as grouped list
             *  for each sem
             */

            val group = result.groupBy {
                it.sem
            }
            val perSemList = arrayListOf<SemResult>()

            /**
             * @param perSemList : Stores Result of Each Sem of Single Student
             */


//                  Iterate through the grouped List

            group.forEach { (sem, semRes) ->

                /**
                 * @param sem         : Each Sem
                 * @param semRes      : Each Sem Result
                 * @param subjectSet  : Set for Fixing the subjectSet
                 * @param gradeSet    : Set for Fixing the Revaluation
                 */


                val subjectSet = hashMapOf<String, Result>()
                val gradeSet = hashMapOf<String, Result>()
                var totalCreditsXGrade = 0.0f
                var totalCredits = 0.0f
                var backlogs = 0



                semRes.forEachIndexed { _, result ->

                    if (subjectSet.containsKey(result.subjectCode)) {
                        val prev = subjectSet[result.subjectCode]!!
                        val prevGrade = prev.grade
                        val currentGrade = result.grade
                        val bestGrade = compareGrades(prevGrade, currentGrade)


                        if ((prev.grade == "F" && result.grade != "F") || (prev.grade == "ABSENT" && result.grade != "F") || (result.grade == "AB" && prev.grade != "AB")) {
                            backlogs--
                            subjectSet.replace(result.subjectCode, result)
                        }
                        if (prev.grade != "F" && prev.grade != "COMPLE" && prev.grade != "ABSENT" && prev.grade != "AB") {
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
                        if (result.grade == "F" || result.grade == "ABSENT" || result.grade == "AB") backlogs++

                        if (!gradeSet.containsKey(result.subjectCode + result.grade)) {
                            totalCredits += result.credits.toFloat()
                            totalCreditsXGrade += (result.credits.toFloat() * getCreditsByGrade(result.grade).toFloat())
                        }
                        gradeSet[result.subjectCode + result.grade] = result
                    }
                }


                val sgpa: Float =
                    if (totalCreditsXGrade != 0f && totalCredits != 0f) (totalCreditsXGrade / totalCredits) else 0.0f
                var percent = (sgpa - 0.75f) * 10f
                if (sgpa == 0f) percent = 0.0f
                val currSemRes = SemResult(
                    sem = sem,
                    result = semRes,
                    totalCredits = totalCredits,
                    totalSgpa = "$sgpa",
                    totalPercentage = "$percent",
                    backlogs = backlogs
                )
                perSemList.add(
                    currSemRes
                )

            }
            var totalCgpaUpperSum = 0f
            var totalCreditsOfBtech = 0f
            var totalBacklogs = 0
            perSemList.forEach {
                totalCgpaUpperSum += (it.totalSgpa.toFloat() * it.totalCredits)
                totalCreditsOfBtech += it.totalCredits
                totalBacklogs += it.backlogs
            }
            val cgpa = totalCgpaUpperSum / totalCreditsOfBtech
            val percentage = (cgpa - 0.75f) * 10f
            finalResult.add(
                StudentResult(
                    rollNo,
                    if (percentage > 0f) percentage else 0.0f,
                    totalCgpa = if (cgpa > 0f) cgpa else 0.0f,
                    totalBacklogs,
                    perSemList.sortedBy {
                        it.sem
                    }
                )
            )
//                    println("The rollNo is $rollNo and total backlogs is $totalBacklogs")


        }
        writeToCSV()
            }
        }


    }

    /**
     * Writes The Calculated Results to given .csv file.
     *
     */

    private suspend fun writeToCSV() {
        coroutineScope { launch(IO) {


        csvWriter().open("C:\\Users\\Bharath\\Desktop\\CsvFormatter\\src\\main\\resources\\output\\FormattedResults21_25.csv") {
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
                println("Completed Up to $index / ${finalResult.size - 1} ")
//                        println(jsonSemResult)
            }
        }
            }
        }

    }

    /**
     * As per JNTUK Grading System
     * @param grade : Grade of the Student
     * @return Appropriates Points for the grade.
     */

    private fun getCreditsByGrade(grade: String): Int {
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

    /**
     * When The student Applies for Revaluation then the grades will be compared.
     */

    private fun compareGrades(grade1: String, grade2: String): String {
        val min = min((grade1.first() - 65).code, (grade2.first() - 65).code)
        return "${(min + 65).toChar()}"

    }


}