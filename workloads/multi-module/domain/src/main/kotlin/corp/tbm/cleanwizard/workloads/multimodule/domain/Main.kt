package corp.tbm.cleanwizard.workloads.multimodule.domain

import corp.tbm.cleanwizard.foundation.annotations.Repository


fun main() {
}

@Repository
interface FooRepository {
    fun debik(lox : List<String>): List<Int>
}