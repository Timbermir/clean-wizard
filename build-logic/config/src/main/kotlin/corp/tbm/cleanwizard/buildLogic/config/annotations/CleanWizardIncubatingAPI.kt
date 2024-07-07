package corp.tbm.cleanwizard.buildLogic.config.annotations

import org.gradle.api.Incubating

/**
 * API marked with this annotation is incubating,
 * this means that the feature is currently a work-in-progress and may change at any time.
 */
@RequiresOptIn(
    level = RequiresOptIn.Level.WARNING,
    message = "This API is incubating and not guaranteed to be stable. " +
            "Use it at your own risk because as it can lead to unexpected results."
)
@Target(
    AnnotationTarget.CLASS,
    AnnotationTarget.TYPEALIAS,
    AnnotationTarget.FUNCTION,
    AnnotationTarget.PROPERTY,
    AnnotationTarget.FIELD,
    AnnotationTarget.CONSTRUCTOR
)
@Incubating
annotation class CleanWizardIncubatingAPI