package be.ehb.gdt.nutrisearch.domain.dietitians.exceptions

class DietitianRegistrationException(val errorCode: ErrorCode) : RuntimeException() {
    override val message: String
        get() = when(errorCode) {
            ErrorCode.INVALID_RIZIV -> "Invalid riziv number provided"
            ErrorCode.RIZIV_ALREADY_REGISTERED -> "Dietitian with provided riziv number is already registered"
            ErrorCode.EMAIL_ALREADY_USED -> "Email address is already used"
        }
    enum class ErrorCode {
        INVALID_RIZIV,
        RIZIV_ALREADY_REGISTERED,
        EMAIL_ALREADY_USED,
    }
}