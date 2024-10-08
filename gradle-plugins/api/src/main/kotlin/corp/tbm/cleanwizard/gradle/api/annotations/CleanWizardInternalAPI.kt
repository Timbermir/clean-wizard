package corp.tbm.cleanwizard.gradle.api.annotations

/**
 * API marked with this annotation is experimental and is not intended to be used
 * outside the Clean Wizard.
 */
@RequiresOptIn(
    level = RequiresOptIn.Level.ERROR,
    message = "This API is internal. " +
            "It is not intended to use used outside Clean Wizard."
)
@Target(
    AnnotationTarget.CLASS,
    AnnotationTarget.TYPEALIAS,
    AnnotationTarget.FUNCTION,
    AnnotationTarget.PROPERTY,
    AnnotationTarget.FIELD,
    AnnotationTarget.CONSTRUCTOR
)
annotation class CleanWizardInternalAPI