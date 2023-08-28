package be.ehb.gdt.nutrisearch.util

import be.ehb.gdt.nutrisearch.domain.exceptions.ForbiddenOperationException

fun dietitianCheck(isTreatingPatient: Boolean) {
    if(!isTreatingPatient) {
        throw ForbiddenOperationException("You are not allowed to see this patient's data")
    }
}