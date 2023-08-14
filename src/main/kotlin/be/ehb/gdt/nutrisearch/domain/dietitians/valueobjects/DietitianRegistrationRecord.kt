package be.ehb.gdt.nutrisearch.domain.dietitians.valueobjects

class DietitianRegistrationRecord(
    val email: String,
    val firstname: String,
    val lastname: String,
    val riziv: String,
) {
    val fullName: String
        get() = "$firstname $lastname"
}