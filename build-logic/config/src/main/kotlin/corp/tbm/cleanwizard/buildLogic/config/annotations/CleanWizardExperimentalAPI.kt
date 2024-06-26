package corp.tbm.cleanwizard.buildLogic.config.annotations

/**
 * API marked with this annotation is experimental and is not guaranteed to be stable.
 */
@RequiresOptIn(
    level = RequiresOptIn.Level.ERROR,
    message = "This API is experimental. " +
            "Use it at your own risk because it can lead to unexpected results."
)
@Target(
    AnnotationTarget.CLASS,
    AnnotationTarget.TYPEALIAS,
    AnnotationTarget.FUNCTION,
    AnnotationTarget.PROPERTY,
    AnnotationTarget.FIELD,
    AnnotationTarget.CONSTRUCTOR
)
annotation class CleanWizardExperimentalAPI