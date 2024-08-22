package corp.tbm.cleanwizard.foundation.annotations

import corp.tbm.cleanwizard.gradle.api.annotations.CleanWizardInternalAPI

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.SOURCE)
annotation class Repository

@CleanWizardInternalAPI
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.SOURCE)
annotation class UseCase