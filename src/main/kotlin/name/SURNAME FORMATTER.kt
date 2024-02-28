package name

import com.github.doyaaaaaken.kotlincsv.dsl.csvReader
import com.github.doyaaaaaken.kotlincsv.dsl.csvWriter
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class NameWrapper(
    @SerialName("rollNo")
    val rollNo: String,
    @SerialName("name")
    val name: String,
    @SerialName("surname")
    val surname: String,
    @SerialName("address")
    val address:String


)

suspend fun main() {
    val format = SURNAMEFORMATTER()

    format.format()

}

/**
 * @property SURNAMEFORMATTER : Class for functions
 * @property format() : Function to start formatting.
 * Read everything from the raw csv file
 * @property namesList : for writing in new csv File
 */

class SURNAMEFORMATTER {

    private val namesList = arrayListOf<NameWrapper>()
    suspend fun format() {
        coroutineScope {
            this.launch {

                csvReader().open("C:\\Users\\Bharath\\Desktop\\CsvFormatter\\src\\main\\resources\\assests\\STUDENT_DETAILS_BATCH2021.csv") {
                    readAllAsSequence().forEachIndexed { index, strings ->
                        if (index > 0) {
                            val rollNo = strings[0]
                            val fullName = strings[1]
                            val address = strings[7]+strings[8]+strings[9]+strings[10]
                            if (fullName.isNotEmpty()) {
                                val fullNameList = fullName.split(" ")
                                val surname = fullNameList.first()
                                println(fullName)
                                val name = fullNameList.subList(1, fullNameList.size).joinToString(" ")
                                namesList.add(
                                    NameWrapper(
                                        rollNo, name, surname,address
                                    )
                                )
                            } else {
                                namesList.add(
                                    NameWrapper(
                                        rollNo, "N/A", "N/A",address
                                    )
                                )
                            }
                        }
                    }
                }


            }.invokeOnCompletion {

                launch {

                    write()
                }

            }
        }

    }

    private fun write() {
        csvWriter().open("C:\\Users\\Bharath\\Desktop\\CsvFormatter\\src\\main\\resources\\output\\name.csv") {
            writeRow("name", "surname","image_url","batch","address")
            namesList.forEach {
                writeRow(it.name, it.surname,"http://61.1.171.141/kiet/05photos/${it.rollNo}.jpg","2021-2025",it.address)
                println("Writing name ${it.name}")
            }
        }
    }

}