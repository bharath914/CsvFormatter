package results

import com.github.doyaaaaaken.kotlincsv.client.CsvReader
import com.github.doyaaaaaken.kotlincsv.client.CsvWriter
import com.google.gson.Gson

class ResultsDecoder {
    val list = arrayListOf<Result>()
    fun decode() {
        CsvReader().open("D:\\KIET\\Results Data\\Results_rows.csv") {
            var index =0
            readAllAsSequence().forEach { rawStr ->
                if (index>=1) {
                    val resultJson = rawStr[4]
                    println(resultJson)
                    val semResult = Gson().fromJson(resultJson, Array<SemResult>::class.java).toList()
                    semResult.forEach {
                        list.addAll(it.result)
                    }
                }
                index++
            }

        }
        writeToCsv(list)
    }

    private fun writeToCsv(results: List<Result>) {
        CsvWriter().open("C:\\Users\\Bharath\\Desktop\\CsvFormatter\\src\\main\\resources\\assests\\result\\allyrresults.csv") {
            list.forEach { res ->
                writeRow(
                    res.sem,
                    res.date,
                    res.rollNo,
                    res.subjectCode,
                    res.subject,
                    res.grade,
                    res.credits,
                    res.internalMarks
                )
            }
        }
    }
}

fun main() {
    val decoder = ResultsDecoder()
    decoder.decode()
}