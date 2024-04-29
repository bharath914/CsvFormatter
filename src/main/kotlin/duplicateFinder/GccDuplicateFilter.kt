package duplicateFinder

import com.github.doyaaaaaken.kotlincsv.dsl.csvReader
import com.github.doyaaaaaken.kotlincsv.dsl.csvWriter

data class GccData(
    val roll_no: String,
    val name: String,
    val branch: String,
    val batch: String,
    val college: String,
    val isCoordinator: Boolean = false,
    val email: String
)


class GccDuplicateFilter {
    private val resultList = arrayListOf<GccData>()
    fun format() {
        println("Starting work!.................")
        csvReader().open("C:\\Users\\Bharath\\Downloads\\gcc_data_main[1].csv") {
            readAllAsSequence().forEachIndexed { index, rawList ->
                println("current index is $index")
                if (index != 0) {
                    println("Roll No : ${rawList[0]} and is coordinator : ${rawList[5]}")
                    val data = GccData(
                        roll_no = rawList[0],
                        name = rawList[1],
                        branch = rawList[2],
                        batch = rawList[3],
                        college = rawList[4],
                        isCoordinator = rawList[5].toLowerCase().toBooleanStrict(),
                        email = rawList[6]
                    )
                    resultList.add(data)
                }
            }

        }


        val hashSet = hashSetOf<String>();
        val output = "C:\\Users\\Bharath\\Downloads\\gcc_data_main_output.csv"
        csvWriter().open(output) {
            writeRow("roll_no", "name", "branch", "batch", "college", "isCoordinator", "email")
            resultList.forEach {
                if (!hashSet.contains(it.roll_no)) {
                    it.apply {
                        writeRow(
                            roll_no,
                            name,
                            branch,
                            batch,
                            college,
                            isCoordinator,
                            email
                        )
                    }
                    hashSet.add(it.roll_no)
                }else{
                    println("The duplicate is ${it.roll_no}")
                }
            }
        }
    }
}

fun main() {
    GccDuplicateFilter().format()
}