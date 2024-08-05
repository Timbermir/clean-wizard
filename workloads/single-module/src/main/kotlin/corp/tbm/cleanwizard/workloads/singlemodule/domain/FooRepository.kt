package corp.tbm.cleanwizard.workloads.singlemodule.domain

import corp.tbm.cleanwizard.foundation.annotations.Repository

@Repository
interface FooRepository {
    fun debik(lox: List<String>): List<Int>
}