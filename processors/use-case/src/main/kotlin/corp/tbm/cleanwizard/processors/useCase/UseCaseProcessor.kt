package corp.tbm.cleanwizard.processors.useCase

import com.google.devtools.ksp.processing.*
import com.google.devtools.ksp.symbol.KSAnnotated

class UseCaseProcessor(
    private val codeGenerator: CodeGenerator,
    processorOptions: Map<String, String>,
    private val logger: KSPLogger
) : SymbolProcessor {
    override fun process(resolver: Resolver): List<KSAnnotated> {
        return emptyList()
    }
}

internal class UseCaseProcessorProvider : SymbolProcessorProvider {
    override fun create(environment: SymbolProcessorEnvironment): SymbolProcessor {
        return UseCaseProcessor(environment.codeGenerator, environment.options, environment.logger)
    }
}